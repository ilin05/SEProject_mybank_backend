package com.mybank.module4_creditcard.dao.mapper;

import com.mybank.module4_creditcard.entity.Application;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicationRowMapper implements RowMapper<Application> {
    @Override
    public Application mapRow(ResultSet rs, int rowNum) throws SQLException {
        Application application = new Application();
        application.setApplId(rs.getString("appl_id"));
        application.setCustomerId(rs.getString("customer_id"));
        application.setCardId(rs.getString("credit_card_id"));
        application.setDepositId(rs.getString("deposit_card_id"));
        application.setLimit(rs.getBigDecimal("credit_limit"));
        application.setPassword(rs.getString("password"));
        application.setComment(rs.getString("comment"));
        application.setTime(rs.getTimestamp("time").toLocalDateTime());
        application.setStatus(Application.ApplStatus.getStatus(rs.getInt("status")));
        return application;
    }
}
