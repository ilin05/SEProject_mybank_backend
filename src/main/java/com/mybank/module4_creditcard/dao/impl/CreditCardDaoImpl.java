package com.mybank.module4_creditcard.dao.impl;

import com.mybank.module4_creditcard.dao.CreditCardDao;
import com.mybank.module4_creditcard.dao.mapper.CreditCardRowMapper;
import com.mybank.module4_creditcard.entity.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Transactional
@Repository
public class CreditCardDaoImpl implements CreditCardDao {
    JdbcTemplate jdbc;

    @Autowired
    public CreditCardDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<CreditCard> queryCreditCards(String userId) {
        Objects.requireNonNull(userId);
        String sql = "SELECT * FROM creditcards WHERE customer_id = ?";
        return jdbc.query(sql, new CreditCardRowMapper(), Integer.parseInt(userId));
    }

    @Override
    public CreditCard queryCreditCard(String cardId) {
        Objects.requireNonNull(cardId);
        String sql = "SELECT * FROM creditcards WHERE card_id = ?";
        return jdbc.queryForObject(sql, new CreditCardRowMapper(), cardId);
    }

    @Override
    public void updateCardStatus(String cardId, CreditCard.CardStatus status) {
        Objects.requireNonNull(cardId);
        Objects.requireNonNull(status);
        String sql = "UPDATE creditcards SET status = ? WHERE card_id = ?";
        jdbc.update(sql, status.getIntValue(), cardId);
    }

    @Override
    public boolean updateCardPassword(String cardId, String oldPassword, String newPassword) {
        Objects.requireNonNull(cardId);
        Objects.requireNonNull(oldPassword);
        Objects.requireNonNull(newPassword);
        String sql = "UPDATE creditcards SET password = ? " +
                "WHERE card_id = ? and password = ? and status = ?";
        return jdbc.update(sql, newPassword, cardId, oldPassword, CreditCard.CardStatus.ACTIVE.getIntValue()) == 1;
    }

    @Override
    public boolean repay(String cardId) {
        Objects.requireNonNull(cardId);

        String qryDeposit = "SELECT deposit_card_id FROM creditcards WHERE card_id = ?";
        String depositId = jdbc.queryForObject(qryDeposit, String.class, cardId);

        String qryConsumption = "SELECT consumption FROM creditcards WHERE card_id = ?";
        BigDecimal consumption = jdbc.queryForObject(qryConsumption, BigDecimal.class, cardId);

        String sql = "UPDATE creditcards AS c JOIN saving_account AS s " +
                "ON c.deposit_card_id = s.account_id " +
                "SET s.balance = s.balance - c.consumption, c.consumption = 0 " +
                "WHERE c.card_id = ? AND c.status = ? AND s.loss_state = 0 AND s.freeze_state = 0";
        jdbc.update(sql, cardId, CreditCard.CardStatus.ACTIVE.getIntValue());

        LocalDateTime now = LocalDateTime.now();
        String transSql = "INSERT INTO transaction " +
                "(card_id, card_type, transaction_time, transaction_amount, transaction_type, transaction_channel, " +
                "money_source, money_goes) " +
                "VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?)";
        jdbc.update(transSql, cardId, "credit", now, consumption, "repayment", "internet", null, depositId);
        jdbc.update(transSql, depositId, "save", now, consumption, "repayment", "internet", cardId, null);

        return true;
    }

    @Scheduled(cron = "0 0 0 * * ? ")
    private void repay() {
        String qryCard = "SELECT card_id FROM creditcards WHERE status = ? AND consumption > 0 ";
        List<String> cardIds = jdbc.queryForList(qryCard, String.class, CreditCard.CardStatus.ACTIVE.getIntValue());

        for (String cardId : cardIds) {
            repay(cardId);
        }
    }
}

