package com.mybank.module5_foreign.back;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class WHDBUtil {

    JdbcTemplate jdbc;

    String table_currency = "wh_currency(currency_id, currency_name, currency_rate)";
    String table_account = "wh_account(user_id, user_name, password, pay_password)";
    String table_users_pay_password = "users_pay_password(user_id, pay_password)";
    String table_user_currency = "wh_user_currency(user_id, currency_id, amount)";
    String table_exchange_rate = "exchange_rate(currency_to_id, currency_from_id, rate)";
    String table_transaction = "wh_transaction(user_id, currency_to_id, currency_from_id, amount_to, amount_from, transaction_time)";
    String table_administrator = "administrator (adm_id, super_authority, adm_name, adm_password, control_currency, control_rate)";
    String table_operator = "wh_operator(opt_id, opt_name, opt_password, control_currency, control_rate)";
    String table_out_transaction = "transaction(card_id, card_type, transaction_time, transaction_amount, transaction_type, money_source, money_goes)";

    public WHDBUtil(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }




    //transaction
    public int insertBalance(int user_id, int currency_from_id, int currency_to_id, double amount_from, double amount_to, String date){
        Object[] objects = new Object[]{user_id, currency_to_id, currency_from_id, amount_to, amount_from, date};
        return jdbc.update("insert into " + table_transaction + " values (?, ?, ?, ?, ?, ?)", objects);
    }
    public List<Map<String, Object>> findBalanceByUser(int user_id, int threadhold){
        Object[] objects = new Object[]{user_id, threadhold};
        return jdbc.queryForList("select * from wh_transaction where user_id = ? limit ?", objects);
    }
    public List<Map<String, Object>> findBalanceByCurrency(int currency_id){
        Object[] objects = new Object[]{currency_id, currency_id};
        return jdbc.queryForList("select * from wh_transaction where currency_to_id = ? or  currency_from_id = ?", objects);
    }




    public List<Map<String, Object>> findUserHoldings(int userId) {
        Object[] params = new Object[]{userId};
        return jdbc.queryForList("SELECT * FROM wh_user_currency WHERE user_id = ?", params);
    }

    public Double getUserRMBalance(int userId) {
        Object[] params = new Object[]{userId, 0}; // 0 is the currency_id for RMB
        List<Map<String, Object>> result = jdbc.queryForList(
                "SELECT amount FROM wh_user_currency WHERE user_id = ? AND currency_id = ?",
                params
        );
        if (result != null && !result.isEmpty()) {
            return ((BigDecimal) result.get(0).get("amount")).doubleValue();
        }
        return 0.0;
    }



    //user_currency
    public List<Map<String, Object>> findUserCurrency(int user_id, int currency_id){
        Object[] objects = new Object[]{user_id, currency_id};
        return jdbc.queryForList("select * from wh_user_currency where user_id = ? and currency_id = ?", objects);
    }
    public int updateUserCurrency(int user_id, int currency_id, double amount){
        Object[] objects = new Object[]{amount, user_id, currency_id};
        return jdbc.update("update wh_user_currency set amount = ? where user_id = ? and currency_id = ?", objects);
    }
    public int insertUserCurrency(int user_id, int currency_id){
        Object[] objects = new Object[]{user_id, currency_id};
        return jdbc.update("insert into " + table_user_currency + " values(?,?,0)" , objects);
    }
    public List<Map<String, Object>> findUserCurrencyByCurrency(int currency_id){
        Object[] objects = new Object[]{currency_id};
        return jdbc.queryForList("select * from wh_user_currency where currency_id = ?", objects);
    }





    //users
    public List<Map<String, Object>> findUser_users(String name, String password){
        Object[] objects = new Object[]{name, password};
        return jdbc.queryForList("select * from wh_account where user_name = ? and password = ?", objects);
    }
    public List<Map<String, Object>> findUser_users(String name){
        Object[] objects = new Object[]{name};
        return jdbc.queryForList("select * from wh_account where user_name = ?", objects);
    }
    public List<Map<String, Object>> findUserByName(String name){
        Object[] objects = new Object[]{name};
        return jdbc.queryForList("select * from wh_account where user_name = ?", objects);
    }
    public List<Map<String, Object>> findUserPayPassword(int user_id, String pay_password){
        Object[] objects = new Object[]{user_id, pay_password};
        return jdbc.queryForList("select * from wh_account where user_id = ? and pay_password = ?", objects);
    }
    public int findUserAllNum(){
        return jdbc.queryForList("select * from wh_account").size();
    }
    public int addUser(int id, String name, String password, String pay_password){
        Object[] objects = new Object[]{id, name, password, pay_password};
        return jdbc.update("insert into " + table_account + " values(?,?,?,?)" , objects);
    }





    //administrator
    public List<Map<String, Object>> findUser_adm(String name, String password){
        List<Map<String, Object>> result = new ArrayList<>();
        if (name.equals("root") && password.equals("password")){
            Map<String, Object> item = new HashMap<>();
            item.put("name", "root");
            item.put("password", "password");
            result.add(item);
            return result;
        }
        else{
            return null;
        }
    }
    public List<Map<String, Object>> findUser_adm(String name){
        List<Map<String, Object>> result = new ArrayList<>();
        if (name.equals("root")){
            Map<String, Object> item = new HashMap<>();
            item.put("name", "root");
            item.put("password", "password");
            result.add(item);
            return result;
        }
        else{
            return null;
        }
    }



    //operator
    public List<Map<String, Object>> findUser_opt(String name, String password){
        Object[] objects = new Object[]{name, password};
        return jdbc.queryForList("select * from wh_operator where opt_name = ? and opt_password = ?", objects);
    }
    public List<Map<String, Object>> findUser_opt(String name){
        Object[] objects = new Object[]{name};
        return jdbc.queryForList("select * from wh_operator where opt_name = ?", objects);
    }
    public List<Map<String, Object>> findOptByName(String name){
        Object[] objects = new Object[]{name};
        return jdbc.queryForList("select * from wh_operator where opt_name = ?", objects);
    }
    public List<Map<String, Object>> findAllOpt(){
        return jdbc.queryForList("select * from wh_operator");
    }
    public int insertOP(String name, String password){
        int id = findAllOpt().size();
        Object[] objects = new Object[]{id, name, password};
        return jdbc.update("insert into " + table_operator + " values(?, ?, ?, 0, 0)", objects);
    }
    public int deleteOP(String name){
        Object[] objects = new Object[]{name};
        return jdbc.update("delete from wh_operator where opt_name = ?", objects);
    }
    public int changeOPAuthority(String name, int currency, int rate){
        Object[] objects = new Object[]{currency, rate, name};
        return jdbc.update("update wh_operator set control_currency = ?, control_rate = ? where opt_name = ?", objects);
    }





    //currency
    public List<Map<String, Object>> selectAllCurrency(){
        return jdbc.queryForList("select * from wh_currency");
    }

    public List<Map<String, Object>> selectCurrencyID(int ID){
        Object[] objects = new Object[]{ID};
        return jdbc.queryForList("select * from wh_currency where currency_id = ?", objects);
    }
    public List<Map<String, Object>> selectCurrencyName(String name){
        Object[] objects = new Object[]{name};
        return jdbc.queryForList("select * from wh_currency where currency_name = ?", objects);
    }
    public int insertCurrency(int id, String name, double rate){
        Object[] objects = new Object[]{id, name, rate};
        return jdbc.update("insert into " + table_currency + " values (?, ?, ?)", objects);
    }

    public int deleteCurrency(int id){
        Object[] objects = new Object[]{id};
        return jdbc.update("delete from wh_currency where currency_id = ?", objects);
    }

    public List<Map<String, Object>> selectRateByCurrency(int currency){
        Object[] objects = new Object[]{currency};
        return jdbc.queryForList("select * from wh_currency where currency_id = ?", objects);
    }

    public int updateExchangeRate(int currency, double rate){
        Object[] objects = new Object[]{rate, currency};
        return jdbc.update("update wh_currency set currency_rate = ? where currency_id = ?", objects);
    }


    public int deleteUserCurrency(int user_id, int currency_id) {
        Object[] objects = new Object[]{user_id, currency_id};
        return jdbc.update("delete from wh_user_currency where user_id = ? and currency_id = ?", objects);
    }



    //saving_account
    public List<Map<String, Object>> selectAccount(String account_name){
        Object[] objects = new Object[]{account_name};
        return jdbc.queryForList("select * from saving_account where account_id = ?", objects);
    }
    public List<Map<String, Object>> selectAccount(String account_name, String password){
        Object[] objects = new Object[]{account_name, password};
        return jdbc.queryForList("select * from saving_account where account_id = ? and password = ?", objects);
    }
    public List<Map<String, Object>> selectAccountBalance(String account_name){
        Object[] objects = new Object[]{account_name};
        return jdbc.queryForList("select * from saving_account where account_id = ?", objects);
    }
    public List<Map<String, Object>> selectAccountBalance(String account_name, String password){
        Object[] objects = new Object[]{account_name, password};
        return jdbc.queryForList("select * from saving_account where account_id = ? and password = ?", objects);
    }
    public int updateAccountBalance(String account_name, double account){
        Object[] objects = new Object[]{account, account_name};
        return jdbc.update("update saving_account set balance = ? where account_id = ?", objects);
    }

    //transaction
    public int insertOutTransaction(String card_id, String user_name, double amount, String time, boolean to_WH){
        if (to_WH){
            Object[] objects = new Object[]{card_id, time, amount, card_id, user_name + "_CNY"};
            return jdbc.update("insert into " + table_out_transaction + " values (?, 'save', ?, ?, 'to_foreign', ?, ?)", objects);
        }
        else {
            Object[] objects = new Object[]{card_id, time, amount, user_name + "_CNY", card_id};
            return jdbc.update("insert into " + table_out_transaction + " values (?, 'save', ?, ?, 'from_foreign', ?, ?)", objects);
        }
    }

}
