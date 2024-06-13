package com.mybank.module4_creditcard.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class ServiceImpl implements
        ApplicationHistory,
        ApplicationManage,
        ApplicationReview,
        AuditorManage,
        BillHistory,
        CreditCardManage,
        CreditCardSearch,
        Payment,
        ReviewHistory {

    private final JdbcTemplate jdbc;

    public ServiceImpl(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    // application history: see the queryApplications in ApplicationDaoImpl

    // application manage: see the cancelApplications in ApplicationDaoImpl

    // application review: see the reviewApplications in ApplicationDaoImpl

    // nothing should be implemented here.

}
