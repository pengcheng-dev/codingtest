package com.bgl.backend.controller;

import com.bgl.backend.common.exception.SystemException;
import com.bgl.backend.common.logging.LoggerWrapper;
import com.bgl.backend.model.Account;
import com.bgl.backend.service.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
@Tag(name = "Account", description = "API for querying accounts")
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private transient LoggerWrapper logger;

    @Autowired
    private transient IAccountService accountService;

    @Operation(summary = "Get a list of all accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Account.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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

    @Operation(summary = "Get details of a specific account by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Account.class))),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping (value = "/{id}")
    public ResponseEntity<Account> findById(@PathVariable("id") final @NotNull @Positive Long id){
        final Account account = accountService.findById(id);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        return ResponseEntity.ok(account);
    }
}
