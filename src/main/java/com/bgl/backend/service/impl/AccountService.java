package com.bgl.backend.service.impl;

import com.bgl.backend.common.exception.SystemException;
import com.bgl.backend.dao.AccountRepository;
import com.bgl.backend.model.Account;
import com.bgl.backend.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Pengcheng Xiao
 *
 * business logic of CRUD account
 */
@Component
public class AccountService implements IAccountService {

    @Autowired
    private transient AccountRepository accountRepository;

    /**
     * query all accounts
     * @return Account list
     */
    @Override
    public List<Account> findAll() {
        try {
            return accountRepository.findAll();
        }catch (Exception e){
            throw new SystemException("Failed to retrieve accounts", e);
        }
    }

    /**
     * query an account by ID
     * @param id
     * @return Account
     */
    @Override
    public Account findById(final Long id) {
        if((null == id) || (id < 0)){
            throw new IllegalArgumentException("Invalid Id");
        }
        try {
            return accountRepository.findById(id).orElse(null);
        }catch (Exception e){
            throw new SystemException("Failed to retrieve account", e);
        }
    }
}
