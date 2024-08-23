package com.bgl.backend.service;

import com.bgl.backend.model.Account;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Pengcheng Xiao
 */
@Component
public interface IAccountService {
    List<Account> findAll();

    Account findById(final Long id);
}
