package com.mybank.module4_creditcard.dao;

import com.mybank.module4_creditcard.entity.Bill;

import java.util.List;

public interface BillDao {
    /**
     * Add a payment to the bill, and update the credit card balance.
     *
     * @param bill Payment object
     * @return true if the payment is successful, false otherwise.
     */
    boolean payBill(Bill bill);

    /**
     * Query bills by card ID.
     *
     * @param cardId ID of the card
     * @return List of Bill objects
     */
    List<Bill> queryCardBills(String cardId);
}
