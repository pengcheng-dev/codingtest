package com.bgl.backend.dao;

import com.bgl.backend.dao.projection.EntryTransactionBriefProjection;
import com.bgl.backend.model.EntryTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Pengcheng Xiao
 *
 * repository for entry transaction entity
 */
@Repository
public interface EntryTransactionRepository extends JpaRepository<EntryTransaction, Long> {

    /**
     * customized query for selecting common fields of parent entry entity only
     * @param pageable
     * @return Entry transaction page
     */
    @Query("SELECT et.id AS id, et.type AS type, et.amount AS amount, et.transactionDate AS transactionDate, " +
        "et.fundId AS fundId, et.dateCreated AS dateCreated, et.lastUpdated AS lastUpdated, " +
        "a.id AS accountIncrementalId, a.accountID AS accountID, a.code AS accountCode, a.name AS accountName, " +
        "a.accountClass AS accountClass, a.accountType AS accountType, a.parentAccount.id AS accountParentID, " +
        "e.id AS entryId, e.amount AS entryAmount, e.gstAmount AS entryGstAmount, e.entryType AS entryType " +
        "FROM EntryTransaction et " +
        "LEFT JOIN et.account a " +
        "LEFT JOIN et.entry e")
    Page<EntryTransactionBriefProjection> findAllBriefs(final Pageable pageable);

}
