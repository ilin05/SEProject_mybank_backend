package com.mybank.module4_creditcard.dao.mapper;

import com.mybank.module4_creditcard.entity.Review;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewRowMapper implements RowMapper<Review> {
    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getString("review_id"));
        review.setAuditorId(rs.getString("auditor_id"));
        review.setApplId(rs.getString("appl_id"));
        review.setApproved(rs.getBoolean("approved"));
        review.setTime(rs.getTimestamp("time").toLocalDateTime());
        return review;
    }
}
