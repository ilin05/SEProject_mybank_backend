package com.mybank.module4_creditcard.dao.impl;

import com.mybank.module4_creditcard.dao.AuditorDao;
import com.mybank.module4_creditcard.dao.mapper.AuditorRowMapper;
import com.mybank.module4_creditcard.entity.role.Auditor;
import com.mybank.module4_creditcard.entity.role.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Transactional
@Repository
public class AuditorDaoImpl implements AuditorDao {
    JdbcTemplate jdbc;

    @Autowired
    public AuditorDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void createAuditor(Auditor auditor) throws NullPointerException, NumberFormatException,
            DataAccessException {

        Objects.requireNonNull(auditor);

        String sql = "insert into auditors (name, password, phone, can_review)" +
                " values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            var ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, Objects.requireNonNull(auditor.getName()));
            ps.setString(2, Objects.requireNonNull(auditor.getPassword()));
            ps.setString(3, Objects.requireNonNull(auditor.getPhone()));
            ps.setBoolean(4, auditor.getAuth() == Authority.REVIEW_APPLICATIONS);
            return ps;
        }, keyHolder);

        // Get the generated auditor ID
        Number key = keyHolder.getKey();
        Objects.requireNonNull(key);
        auditor.setAuditorId(String.valueOf(key.intValue()));
    }

    @Override
    public List<Auditor> queryAuditors() {
        String sql = "select * from auditors";
        return jdbc.query(sql, new AuditorRowMapper());
    }

    @Override
    public void grantAuditorPermission(String auditorId, Authority auth) {
        Objects.requireNonNull(auditorId);
        Objects.requireNonNull(auth);
        String sql = "update auditors set can_review = ? where auditor_id = ?";
        jdbc.update(sql, auth == Authority.REVIEW_APPLICATIONS, Integer.parseInt(auditorId));
    }

    @Override
    public void revokeAuditorPermission(String auditorId, Authority auth) {
        Objects.requireNonNull(auditorId);
        Objects.requireNonNull(auth);
        String sql = "update auditors set can_review = ? where auditor_id = ?";
        jdbc.update(sql, auth != Authority.REVIEW_APPLICATIONS, Integer.parseInt(auditorId));
    }

    @Override
    public void deleteAuditor(String auditorId) {
        Objects.requireNonNull(auditorId);
        String sql = "delete from auditors where auditor_id = ?";
        jdbc.update(sql, Integer.parseInt(auditorId));
    }
}
