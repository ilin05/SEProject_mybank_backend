package com.mybank.module4_creditcard.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Review {
    private String reviewId;
    private String auditorId;
    private String applId;
    private boolean approved;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    public Review() {
    }

    public Review(String auditorId, String applId, boolean approved, LocalDateTime time) {
        this.auditorId = auditorId;
        this.applId = applId;
        this.approved = approved;
        this.time = time;
    }

    public String getReviewId() { return reviewId; }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getApplId() {
        return applId;
    }

    public void setApplId(String applId) {
        this.applId = applId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

}
