package com.bgl.backend.controller;

import com.bgl.backend.common.exception.SystemException;
import com.bgl.backend.common.logging.LoggerWrapper;
import com.bgl.backend.model.Account;
import com.bgl.backend.service.IAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * @author Pengcheng Xiao
 *
 * RESTful API for query account list and account details for an account
 */
@RestController
@RequestMapping(value = "/accounts")
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private transient LoggerWrapper logger;

    @Autowired
    private transient IAccountService accountService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Account>> findAll(){
        try {
            final List<Account> accountList = accountService.findAll();
            return ResponseEntity.ok(accountList);
        } catch (SystemException e) {
            logger.logErrorWithException(LOG, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve accounts", e);
        }
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<Account> findById(@PathVariable("id") final Long id){

        if(id == null || id <= 0){
            throw new IllegalArgumentException("ID must be positive");
        }
        final Account account = accountService.findById(id);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        return ResponseEntity.ok(account);
    }
}
