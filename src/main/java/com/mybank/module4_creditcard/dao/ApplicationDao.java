package com.mybank.module4_creditcard.dao;

import com.mybank.module4_creditcard.entity.Application;
import com.mybank.module4_creditcard.entity.Review;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ApplicationDao {
    /**
     * Create a new credit card application.
     *
     * @param appl Application object
     */
    void addApplication(Application appl);

    /**
     * Query all credit card applications to be reviewed.
     *
     * @return List of Application objects
     */
    List<Application> queryApplications() throws DataAccessException;

    /**
     * Query credit card applications by user ID.
     *
     * @param userId ID of the user
     * @return List of Application objects
     */
    List<Application> queryApplications(String userId);

    /**
     * Change the status of a credit card application to "cancelled".
     *
     * @param applId ID of the application
     */
    void cancelApplication(String applId);

    /**
     * Change the status of a credit card application according to the review
     * result, and add review record to database.
     * <p>
     * In addition, if the application is approved, a credit card will be
     * created according to the information of the application.
     *
     * @param review Review object
     * @return
     */
    boolean reviewApplication(Review review);
}
