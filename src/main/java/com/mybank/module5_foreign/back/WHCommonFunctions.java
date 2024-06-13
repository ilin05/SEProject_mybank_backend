package com.mybank.module5_foreign.back;

import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class WHCommonFunctions {

    private WHDBUtil dbUtil;

    public WHCommonFunctions(JdbcTemplate jdbc){
        this.dbUtil = new WHDBUtil(jdbc);
    }

    public WHUser findUserDetails(String username, String password) {
        List<Map<String, Object>> user = dbUtil.findUser_users(username, password);
        if (user != null && !user.isEmpty()) {
            Map<String, Object> userDetails = user.get(0);
            WHUser WHUserObj = new WHUser();
            WHUserObj.setId((int) userDetails.get("user_id"));
            WHUserObj.setName((String) userDetails.get("user_name"));
            WHUserObj.setPassword((String) userDetails.get("password"));
            return WHUserObj;
        }
        return null;
    }
    public Double getUserRMBalance(int userId) {
        return dbUtil.getUserRMBalance(userId);
    }


    public int findUser(String username, String password){
        List<Map<String, Object>>  user = dbUtil.findUser_adm(username, password);
        if (user != null && user.size() > 0){
            return 3;
        }
        user = dbUtil.findUser_opt(username, password);
        if (user != null && user.size() > 0){
            return 2;
        }
        user = dbUtil.findUser_users(username, password);
        if (user != null && user.size() > 0){
            return 1;
        }
        return 0;
    }

    public int findUser(String username){
        List<Map<String, Object>>  user = dbUtil.findUser_adm(username);
        if (user != null && user.size() > 0){
            return 3;
        }
        user = dbUtil.findUser_opt(username);
        if (user != null && user.size() > 0){
            return 2;
        }
        user = dbUtil.findUser_users(username);
        if (user != null && user.size() > 0){
            return 1;
        }
        return 0;
    }

    public List<String> getAllCurrencyName(){
        List<Map<String, Object>> items = this.dbUtil.selectAllCurrency();
        List<String> result = new ArrayList<>();
        for (Map<String, Object> item : items){
            result.add((String) item.get("currency_name"));
        }
        return result;
    }

    public int findCurrencyID(String currency_name){
        List<Map<String, Object>> items = this.dbUtil.selectCurrencyName(currency_name);
        if (items != null && items.size() == 1) {
            return (int) items.get(0).get("currency_id");
        }
        else {
            return -1;
        }
    }

    public String findCurrencyName(int currency_id){
        List<Map<String, Object>> items = this.dbUtil.selectCurrencyID(currency_id);
        if (items.size() == 1) {
            return (String) items.get(0).get("currency_name");
        }
        else {
            return null;
        }
    }

    public double getExchangeRate(int currency_from, int currency_to){
        LocalDateTime currentTime = LocalDateTime.now();
        int now_minu = currentTime.getHour() * 60 + currentTime.getMinute();
        double noise = Math.sin((double)now_minu / (double)(24 * 60) * 2 * Math.PI);
        double base_rate = calBaseRate(currency_from, currency_to);
        return base_rate + base_rate * 0.01 * noise;
    }

    public int deal(String username, String paypassword, int currency_from, int currency_to, double amount){
        int user_id = -1;
        List<Map<String, Object>> user = this.dbUtil.findUserByName(username);
        if(user.size() == 0){
            System.out.println("用户不存在: " + username);
            return 0;
        } else {
            user_id = (int) this.dbUtil.findUserByName(username).get(0).get("user_id");
        }
        if (this.dbUtil.findUserPayPassword(user_id, paypassword).size() != 1){
            System.out.println("支付密码错误: " + username);
            return 0;
        }

        double rate = getExchangeRate(currency_from, currency_to);
        double amount_from = amount;
        double amount_to = amount_from / rate;
        double currency_from_amount = 0;
        double currency_to_amount = 0;

        if (this.dbUtil.findUserCurrency(user_id, currency_from).size() == 0){
            System.out.println("用户没有持有该货币 (from): " + currency_from);
            this.dbUtil.insertUserCurrency(user_id, currency_from);
        } else {
            currency_from_amount = ((BigDecimal) this.dbUtil.findUserCurrency(user_id, currency_from).get(0).get("amount")).doubleValue();
        }

        if (this.dbUtil.findUserCurrency(user_id, currency_to).size() == 0){
            System.out.println("用户没有持有该货币 (to): " + currency_to);
            this.dbUtil.insertUserCurrency(user_id, currency_to);
        } else {
            currency_to_amount = ((BigDecimal) this.dbUtil.findUserCurrency(user_id, currency_to).get(0).get("amount")).doubleValue();
        }

        if (currency_from_amount - amount < 0){
            System.out.println("余额不足: " + username);
            return -1;
        }

        this.dbUtil.updateUserCurrency(user_id, currency_from, currency_from_amount - amount_from);
        this.dbUtil.updateUserCurrency(user_id, currency_to, currency_to_amount + amount_to);
        this.dbUtil.insertBalance(user_id, currency_from, currency_to, amount_from, amount_to, LocalDateTime.now().toString());
        return 1;
    }

    public Map<String, Double> getExchangeRateByCurrency(int currency_id){
        List<String> all_currency_name = this.getAllCurrencyName();
        Map<String, Double> result = new HashMap<>();
        for (String currency_name : all_currency_name){
            System.out.println(currency_name);
            result.put(currency_name, getExchangeRate(currency_id, findCurrencyID(currency_name)));
        }
        return result;
    }


    public List<WHTransaction> getAllBalanceByUser(String username, int threshold) {
        int userId = -1;
        List<Map<String, Object>> user = this.dbUtil.findUserByName(username);
        if (user.size() == 0) {
            return new ArrayList<>();
        } else {
            userId = (int) this.dbUtil.findUserByName(username).get(0).get("user_id");
        }

        List<Map<String, Object>> data = this.dbUtil.findBalanceByUser(userId, threshold);
        List<WHTransaction> result = new ArrayList<>();
        for (Map<String, Object> item : data) {
            WHTransaction transaction = new WHTransaction();
            transaction.setCurrencyFromId((int) item.get("currency_from_id"));
            transaction.setCurrencyToId((int) item.get("currency_to_id"));
            transaction.setAmountFrom(((BigDecimal) item.get("amount_from")).doubleValue());
            transaction.setAmountTo(((BigDecimal) item.get("amount_to")).doubleValue());
            transaction.setTransactionTime(((Timestamp) item.get("transaction_time")).toLocalDateTime());
            transaction.setCurrencyFromName(getCurrencyNameById(transaction.getCurrencyFromId()));
            transaction.setCurrencyToName(getCurrencyNameById(transaction.getCurrencyToId()));
            result.add(transaction);
        }
        result.sort(Comparator.comparing(WHTransaction::getTransactionTime).reversed());
        return result;
    }

    public String getCurrencyNameById(int currencyId) {
        List<Map<String, Object>> currency = dbUtil.selectCurrencyID(currencyId);
        if (currency != null && !currency.isEmpty()) {
            return (String) currency.get(0).get("currency_name");
        }
        return null;
    }

    public List<Map<String, Object>> getAllCurrencies() {
        return dbUtil.selectAllCurrency();
    }

    public int addCurrency(String name, double base_exchange_rate){
        if (base_exchange_rate <= 0) return -1;
        if (this.dbUtil.selectCurrencyName(name).size() != 0) return 0;

        int id = this.dbUtil.selectAllCurrency().size();
        this.dbUtil.insertCurrency(id, name, base_exchange_rate);
        return 1;
    }

    public boolean deleteCurrency(String name){
        List<Map<String, Object>> currency = this.dbUtil.selectCurrencyName(name);
        if (currency.size() == 0) return false;
        if (checkCurrencyUse(name)) return false;
        this.dbUtil.deleteCurrency(findCurrencyID(name));
        return true;
    }

    public boolean changeRate(int currency, double rate){
        if (this.dbUtil.selectRateByCurrency(currency).size() == 0) return false;
        if (rate <= 0) return false;
        this.dbUtil.updateExchangeRate(currency, rate);
        return true;

    }

    public List<Map<String, Object>> getAllOperators() {
        return dbUtil.findAllOpt();
    }

    public WHOperator findOperator(String username, String password) {
        List<Map<String, Object>> operators = dbUtil.findUser_opt(username, password);
        if (operators != null && operators.size() > 0) {
            Map<String, Object> operatorData = operators.get(0);
            WHOperator WHOperator = new WHOperator();
            WHOperator.setOptId((int) operatorData.get("opt_id"));
            WHOperator.setOptName((String) operatorData.get("opt_name"));
            WHOperator.setOptPassword((String) operatorData.get("opt_password"));
            WHOperator.setControlCurrency((boolean) operatorData.get("control_currency"));
            WHOperator.setControlRate((boolean) operatorData.get("control_rate"));
            return WHOperator;
        }
        return null;
    }

    public boolean addOP(String op_name, String op_password){
        if (this.findUser(op_name) != 0) return false;
        this.dbUtil.insertOP(op_name, op_password);
        return true;
    }

    public boolean deleteOP(String op_name){
        if (this.dbUtil.findOptByName(op_name).size() == 0) return false;
        this.dbUtil.deleteOP(op_name);
        return true;
    }

    public boolean changeAuthority(String op_name, Boolean control_currency, Boolean control_rate){
        if (this.dbUtil.findOptByName(op_name).size() == 0) return false;
        int currency = 0;
        int rate = 0;
        if (control_currency) currency = 1;
        if (control_rate) rate = 1;
        System.out.println(currency + "" + rate );
        this.dbUtil.changeOPAuthority(op_name, currency, rate);
        return true;
    }


    public boolean addUser(String name, String password, String pay_password){
        int id = this.dbUtil.findUserAllNum();
        if (this.dbUtil.findUser_users(name, password).size() == 0){
            this.dbUtil.addUser(id, name, password, pay_password);
            this.dbUtil.insertUserCurrency(id, 0);
            return true;
        }
        else {
            return false;
        }
    }


    public boolean addWHModule(String username, int currency, double amount){
        List<Map<String, Object>> user = this.dbUtil.findUserByName(username);
        if (user.size() == 0){
            return false;
        } else {
            int user_id = (int) this.dbUtil.findUserByName(username).get(0).get("user_id");
            List<Map<String, Object>> userCurrency = this.dbUtil.findUserCurrency(user_id, currency);
            if (userCurrency.size() == 0) {
                // 如果用户没有该外币记录，插入初始记录
                this.dbUtil.insertUserCurrency(user_id, currency);
                userCurrency = this.dbUtil.findUserCurrency(user_id, currency);
            }
            double ori_currency = ((BigDecimal) userCurrency.get(0).get("amount")).doubleValue();
            this.dbUtil.updateUserCurrency(user_id, currency, ori_currency + amount);
            return true;
        }
    }

    public boolean delWHModule(String username, int currency, double amount){
        List<Map<String, Object>> user = this.dbUtil.findUserByName(username);
        if (user.size() == 0){
            return false;
        } else {
            int user_id = (int) this.dbUtil.findUserByName(username).get(0).get("user_id");
            List<Map<String, Object>> userCurrency = this.dbUtil.findUserCurrency(user_id, currency);
            if (userCurrency.size() == 0) {
                // 如果用户没有该外币记录，则不能删除
                return false;
            }
            double ori_currency = ((BigDecimal) userCurrency.get(0).get("amount")).doubleValue();
            if (ori_currency - amount < 0){
                return false;
            } else {
                double new_amount = ori_currency - amount;
                if (new_amount == 0) {
                    this.dbUtil.deleteUserCurrency(user_id, currency);
                } else {
                    this.dbUtil.updateUserCurrency(user_id, currency, new_amount);
                }
                return true;
            }
        }
    }




    public List<Map<String, Object>> getUserHoldings(int userId) {
        List<Map<String, Object>> allHoldings = dbUtil.findUserHoldings(userId);
        List<Map<String, Object>> filteredHoldings = new ArrayList<>();

        for (Map<String, Object> holding : allHoldings) {
            int currencyId = (int) holding.get("currency_id");
            if (currencyId != 0) {
                holding.put("currency_name", findCurrencyName(currencyId)); // 添加货币名称
                filteredHoldings.add(holding);
            }
        }

        return filteredHoldings;
    }




    //private function
    private double calBaseRate(int currency_from, int currency_to){
        System.out.println(currency_from);
        System.out.println(currency_to);
        double rate_from = ((BigDecimal) this.dbUtil.selectRateByCurrency(currency_from).get(0).get("currency_rate")).doubleValue();
        double rate_to = ((BigDecimal) this.dbUtil.selectRateByCurrency(currency_to).get(0).get("currency_rate")).doubleValue();
        return rate_to/rate_from;
    }


    private boolean checkCurrencyUse(String name){
        if (this.dbUtil.findUserCurrencyByCurrency(findCurrencyID(name)).size() != 0) return true;
        if (this.dbUtil.findUserCurrencyByCurrency(findCurrencyID(name)).size() != 0) return true;
        return false;
    }

}