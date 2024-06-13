package com.mybank.module4_creditcard.dao.mapper;

import com.mybank.module4_creditcard.entity.Bill;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BillRowMapper implements RowMapper<Bill> {

    @Override
    public Bill mapRow(ResultSet rs, int rowNum) throws SQLException {
        Bill bill = new Bill();
        bill.setBillId(rs.getString("transaction_id"));
        bill.setCreditCardId(rs.getString("card_id"));
        bill.setConsumption(rs.getBigDecimal("transaction_amount"));
        bill.setType(Bill.PaymentType.getType(rs.getInt("transaction_type")));
        bill.setTime(rs.getObject("transaction_time", java.time.LocalDateTime.class));
        return bill;
    }
}
