package com.mybank.module4_creditcard.dao.impl;

import com.mybank.module4_creditcard.dao.BillDao;
import com.mybank.module4_creditcard.dao.mapper.BillRowMapper;
import com.mybank.module4_creditcard.entity.Bill;
import com.mybank.module4_creditcard.entity.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Transactional
@Repository
public class BillDaoImpl implements BillDao {
    JdbcTemplate jdbc;

    @Autowired
    public BillDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public boolean payBill(Bill bill) {

        Objects.requireNonNull(bill);

        if (bill.getConsumption().compareTo(BigDecimal.ZERO) < 0) return false;

        String verifySql = "update creditcards set consumption = consumption + ? " +
                "where card_id = ? and customer_id = ? and status = ? and password = ? " +
                "and consumption + ? <= credit_limit";
        int num = jdbc.update(verifySql,
                bill.getConsumption(),
                bill.getCreditCardId(),
                Integer.parseInt(bill.getCustomerId()),
                CreditCard.CardStatus.ACTIVE.getIntValue(),
                bill.getPassword(),
                bill.getConsumption());
        if (num == 0) {
            return false;
        }

        /*String sql = "insert into bills (customer_id, credit_card_id, consumption, type, time, comment) " +
                "values (?, ?, ?, ?, ?, ?)";*/
        String sql = "insert into transaction" +
                "(card_id, card_type, transaction_time, transaction_amount, transaction_type, transaction_channel, " +
                "money_goes) " +
                "values " +
                "(?, 'credit', ?, ?, 'transfer', 'internet', ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            var ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, bill.getCreditCardId());
            ps.setTimestamp(2, Timestamp.valueOf(bill.getTime()));
            ps.setBigDecimal(3, bill.getConsumption());
            ps.setString(4, bill.getReceiptId());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        Objects.requireNonNull(key);
        bill.setBillId(String.valueOf(key.intValue()));
        return true;
    }

    @Override
    public List<Bill> queryCardBills(String cardId) {
        Objects.requireNonNull(cardId);
        String sql = "select * from transaction where card_id = ? order by transaction_time desc";
        return jdbc.query(sql, new BillRowMapper(), cardId);
    }
}
