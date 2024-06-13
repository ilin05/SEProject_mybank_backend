package com.mybank.module4_creditcard.dao.mapper;

import com.mybank.module4_creditcard.entity.CreditCard;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditCardRowMapper implements RowMapper<CreditCard> {
    @Override
    public CreditCard mapRow(ResultSet rs, int rowNum) throws SQLException {
        CreditCard card = new CreditCard();
        card.setCardId(rs.getString("card_id"));
        card.setCustomerId(String.valueOf(rs.getInt("customer_id")));
        card.setDepositCardId(rs.getString("deposit_card_id"));
        card.setStatus(CreditCard.CardStatus.getStatus(rs.getInt("status")));
        card.setCreditLimit(rs.getBigDecimal("credit_limit"));
        card.setConsumption(rs.getBigDecimal("consumption"));
        return card;
    }
}
