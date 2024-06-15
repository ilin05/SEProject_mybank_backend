package com.mybank.module4_creditcard.dao.impl;

import com.mybank.module4_creditcard.dao.ApplicationDao;
import com.mybank.module4_creditcard.dao.mapper.ApplicationRowMapper;
import com.mybank.module4_creditcard.entity.Application;
import com.mybank.module4_creditcard.entity.CreditCard;
import com.mybank.module4_creditcard.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Transactional
@Repository
public class ApplicationDaoImpl implements ApplicationDao {
    private final JdbcTemplate jdbc;

    @Autowired
    public ApplicationDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void addApplication(Application appl) throws NullPointerException, NumberFormatException,
            DataAccessException {

        Objects.requireNonNull(appl);

        if (appl.getLimit().compareTo(BigDecimal.ZERO) < 0) throw new NumberFormatException();

        String sql = "insert into applications " +
                "(status, customer_id, deposit_card_id, credit_limit, password, time, comment) " +
                "values (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, Application.ApplStatus.REVIEWING.getIntValue());
            ps.setInt(2, Integer.parseInt(appl.getCustomerId()));
            ps.setString(3, appl.getDepositId());
            ps.setBigDecimal(4, Objects.requireNonNull(appl.getLimit()));
            ps.setString(5, Objects.requireNonNull(appl.getPassword()));
            ps.setTimestamp(6, Timestamp.valueOf(appl.getTime()));
            ps.setString(7, Objects.requireNonNull(appl.getComment()));
            return ps;
        }, keyHolder);

        // Get the generated application ID
        Number key = keyHolder.getKey();
        Objects.requireNonNull(key);
        appl.setApplId(String.valueOf(keyHolder.getKey().intValue()));
    }

    @Override
    public List<Application> queryApplications() throws DataAccessException {
        String sql = "select * from applications where status = ? order by time desc";
        return jdbc.query(sql, new ApplicationRowMapper(), Application.ApplStatus.REVIEWING.getIntValue());
    }

    @Override
    public List<Application> queryApplications(String userId) throws NullPointerException, NumberFormatException,
            DataAccessException {
        Objects.requireNonNull(userId);
        String sql = "select * from applications where customer_id = ? order by time desc";
        return jdbc.query(sql, new ApplicationRowMapper(), Integer.parseInt(userId));
    }

    @Override
    public void cancelApplication(String applId) throws NullPointerException, NumberFormatException,
            DataAccessException {
        String sql = "update applications set status = ? where appl_id = ?";
        jdbc.update(sql, Application.ApplStatus.CANCELED.getIntValue(), Integer.parseInt(applId));
    }

    @Override
    public boolean reviewApplication(Review review) throws NullPointerException, NumberFormatException,
            DataAccessException {
        Objects.requireNonNull(review);

        // Update application status
        String updSql = "update applications set status = ? where appl_id = ? and status = ? " +
                "and exists(select 1 from auditors where auditor_id = ? and can_review = true)";
        int num = jdbc.update(updSql,
                review.isApproved() ?
                        Application.ApplStatus.APPROVED.getIntValue() :
                        Application.ApplStatus.REJECTED.getIntValue(),
                Integer.parseInt(review.getApplId()),
                Application.ApplStatus.REVIEWING.getIntValue(),
                review.getAuditorId());

        if (num == 0)
            return false;

        // Create review record
        String revSql = "insert into reviews " +
                "(appl_id, auditor_id, approved, time) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(revSql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, Integer.parseInt(review.getApplId()));
            ps.setInt(2, Integer.parseInt(review.getAuditorId()));
            ps.setBoolean(3, review.isApproved());
            ps.setTimestamp(4, Timestamp.valueOf(review.getTime()));
            return ps;
        }, keyHolder);

        // Get the generated review ID
        Number key = keyHolder.getKey();
        Objects.requireNonNull(key);
        review.setReviewId(String.valueOf(key.intValue()));

        if (review.isApproved()) {
            // Read card information from the approved application
            String qrySql = "select * from applications where appl_id = ?";
            Application appl = jdbc.queryForObject(qrySql, new ApplicationRowMapper(),
                    Integer.parseInt(review.getApplId()));
            Objects.requireNonNull(appl);

            // Generate a card ID
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
            String cardId = String.format("626666%11s%s",
                            appl.getCustomerId(),
                            appl.getTime().format(formatter))
                    .replace(' ', '0');

            // Create a card for the approved application
            String cardSql = "insert into creditcards " +
                    "(card_id, customer_id, deposit_card_id, credit_limit, password, status) " +
                    "values (?, ?, ?, ?, ?, ?)";

            jdbc.update(cardSql,
                    cardId,
                    Integer.parseInt(appl.getCustomerId()),
                    appl.getDepositId(),
                    appl.getLimit(),
                    appl.getPassword(),
                    CreditCard.CardStatus.ACTIVE.getIntValue());

            String updCardSql = "update applications set credit_card_id = ? where appl_id = ?";
            jdbc.update(updCardSql, cardId, Integer.parseInt(review.getApplId()));
        }
        return true;
    }
}
