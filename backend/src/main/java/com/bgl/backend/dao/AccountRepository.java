package com.bgl.backend.dao;

import com.bgl.backend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Pengcheng Xiao
 *
 * repository for account entity
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
