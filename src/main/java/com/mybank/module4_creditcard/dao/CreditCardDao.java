package com.mybank.module4_creditcard.dao;

import com.mybank.module4_creditcard.entity.CreditCard;

import java.util.List;

public interface CreditCardDao {
    /**
     * Query credit cards by user ID.
     *
     * @param userId ID of the user
     * @return List of CreditCard objects
     */
    List<CreditCard> queryCreditCards(String userId);

    /**
     * Query a credit card by card ID.
     *
     * @param cardId ID of the card
     * @return CreditCard object
     */
    CreditCard queryCreditCard(String cardId);

    /**
     * Change the status of a credit card.
     *
     * @param cardId ID of the card
     */
    void updateCardStatus(String cardId, CreditCard.CardStatus status);

    /**
     * Change the password of a credit card.
     *
     * @param cardId      ID of the card
     * @param oldPassword Old password used for verification
     * @param newPassword New password
     * @return
     */
    boolean updateCardPassword(String cardId, String oldPassword, String newPassword);

    /**
     * Repay the credit card.
     *
     * @param cardId ID of the card
     * @return true if the repayment is successful, false otherwise
     */
    boolean repay(String cardId);
}
