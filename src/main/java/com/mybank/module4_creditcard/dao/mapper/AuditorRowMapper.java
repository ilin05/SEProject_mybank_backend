package com.mybank.module4_creditcard.dao.mapper;

import com.mybank.module4_creditcard.entity.role.Auditor;
import com.mybank.module4_creditcard.entity.role.Authority;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuditorRowMapper implements RowMapper<Auditor> {
    @Override
    public Auditor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Auditor auditor = new Auditor();
        auditor.setAuditorId(rs.getString("auditor_id"));
        auditor.setName(rs.getString("name"));
        auditor.setPhone(rs.getString("phone"));

        boolean can_review = rs.getBoolean("can_review");
        if (can_review) {
            auditor.setAuth(Authority.REVIEW_APPLICATIONS);
        }
        return auditor;
    }
}
