package com.bgl.backend.dao;

import com.bgl.backend.model.EntryTransaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryTransactionRepository extends JpaRepository<EntryTransaction, Long> {

    @Query("SELECT et FROM EntryTransaction et")
    @EntityGraph(value = "EntryTransaction.accountAndEntryBrief", type = EntityGraph.EntityGraphType.FETCH)
    List<EntryTransaction> findAllBriefs();

}
