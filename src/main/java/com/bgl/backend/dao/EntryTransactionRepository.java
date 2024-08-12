package com.bgl.backend.dao;

import com.bgl.backend.model.EntryTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * @return
     */
    @Query("SELECT et FROM EntryTransaction et")
    @EntityGraph(value = "EntryTransaction.accountAndEntryBrief", type = EntityGraph.EntityGraphType.FETCH)
    Page<EntryTransaction> findAllBriefs(Pageable pageable);

}
