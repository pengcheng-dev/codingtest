package com.bgl.backend.dao;

import com.bgl.backend.model.EntryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryTransactionRepository extends JpaRepository<EntryTransaction, Long> {

}
