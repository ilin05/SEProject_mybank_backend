package com.mybank.module4_creditcard.dao;

import com.mybank.module4_creditcard.entity.role.Auditor;
import com.mybank.module4_creditcard.entity.role.Authority;

import java.util.List;

public interface AuditorDao {
    /**
     * Create a new auditor.
     *
     * @param auditor the auditor to be created
     */
    void createAuditor(Auditor auditor);

    List<Auditor> queryAuditors();

    /**
     * Grant permission to an auditor.
     *
     * @param auditorId ID of the auditor
     * @param auth      permission to be granted
     */
    void grantAuditorPermission(String auditorId, Authority auth);

    /**
     * Revoke permission from an auditor.
     *
     * @param auditorId ID of the auditor
     * @param auth      permission to be revoked
     */
    void revokeAuditorPermission(String auditorId, Authority auth);

    /**
     * Delete an auditor.
     *
     * @param auditorId ID of the auditor to be deleted
     */
    void deleteAuditor(String auditorId);
}
