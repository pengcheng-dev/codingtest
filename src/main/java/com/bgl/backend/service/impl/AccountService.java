package com.bgl.backend.service.impl;

import com.bgl.backend.dao.AccountRepository;
import com.bgl.backend.exception.BusinessException;
import com.bgl.backend.model.Account;
import com.bgl.backend.service.IAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    /**
     * query all accounts
     * @return Account list
     */
    @Override
    public List<Account> findAll() {
        try {
            return accountRepository.findAll();
        }catch (Exception e){
            logger.error("error occurred while performing find all accounts", e);
            throw new BusinessException("Failed to retrieve accounts", e);
        }
    }

    /**
     * query an account by ID
     * @param id
     * @return Account
     */
    @Override
    public Account findById(Long id) {
        if((null == id) || (id < 0)){
            throw new BusinessException("Invalid Id");
        }
        try {
            return accountRepository.findById(id).orElse(null);
        }catch (Exception e){
            logger.error("error occurred while performing find account", e);
            throw new BusinessException("Failed to retrieve account", e);
        }
    }
}
