package com.bgl.backend.service.impl;

import com.bgl.backend.dao.EntryTransactionRepository;
import com.bgl.backend.exception.BusinessException;
import com.bgl.backend.model.Account;
import com.bgl.backend.model.EntryTransaction;
import com.bgl.backend.service.IAccountService;
import com.bgl.backend.service.IEntryTransactionService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Pengcheng Xiao
 *
 * Entry transaction service, including business logic of CRUD and validation, use transaction guarantee data integrity
 */

@Component
public class EntryTransactionService implements IEntryTransactionService {

    private static final Logger logger = LoggerFactory.getLogger(EntryTransactionService.class);

    @Autowired
    private EntryTransactionRepository entryTransactionRepository;

    /**
     * save an entry transaction, all fields must be valid
     * @param entryTransaction
     * @return
     * @throws Exception
     */
    @Transactional
    @Override
    public EntryTransaction save(EntryTransaction entryTransaction) throws Exception {
        validateTransaction(entryTransaction, true);
        try {
            return entryTransactionRepository.save(entryTransaction);
        } catch (Exception e) {
            logger.error("Error occurred while performing saving entry transaction", e);
            throw new BusinessException("Failed to save entry transaction", e);
        }
    }

    /**
     * update an entry transaction record, when entry type changed, the original entry subtype need to be deleted,
     * and new subtype entry will be created and associated with the entry transaction
     * @param id
     * @param entryTransaction
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public EntryTransaction update(Long id, EntryTransaction entryTransaction) throws Exception {
        validateTransaction(entryTransaction, false);
        try {
            Optional<EntryTransaction> existingTransaction = entryTransactionRepository.findById(id);
            if (existingTransaction.isPresent()) {
                EntryTransaction transactionToUpdate = existingTransaction.get();

                // Update fields as needed
                transactionToUpdate.setAmount(entryTransaction.getAmount());
                transactionToUpdate.setType(entryTransaction.getType());
                transactionToUpdate.setTransactionDate(entryTransaction.getTransactionDate());
                transactionToUpdate.setAmount(entryTransaction.getAmount());
                transactionToUpdate.setFundId(entryTransaction.getFundId());

                transactionToUpdate.setEntry(entryTransaction.getEntry());
                transactionToUpdate.setAccount(entryTransaction.getAccount());

                return entryTransactionRepository.save(transactionToUpdate);
            } else {
                throw new BusinessException("EntryTransaction not found");
            }
        } catch (Exception e) {
            logger.error("Error occurred while performing updating entry transaction", e);
            throw new BusinessException("Failed to update entry transaction", e);
        }
    }

    /**
     * business logic of deleting an entry transaction, spring transaction involved
     * @param id
     * @throws Exception
     */
    @Override
    @Transactional
    public void delete(Long id) throws Exception {
        if((null == id) || (id < 0)){
            throw new RuntimeException("Invalid Id");
        }
        try {
            if (entryTransactionRepository.existsById(id)) {
                entryTransactionRepository.deleteById(id);
            } else {
                throw new BusinessException("EntryTransaction not found");
            }
        } catch (Exception e) {
            logger.error("Error occurred while performing deleting entry transaction", e);
            throw new BusinessException("Failed to delete entry transaction", e);
        }
    }

    /**
     * business logic of query an entry transaction, validate id
     * @param id
     * @return
     */
    @Override
    public EntryTransaction findDetailById(Long id) {
        if((null == id) || (id < 0)){
            throw new BusinessException("Invalid Id");
        }
        try {
            return entryTransactionRepository.findById(id).orElse(null);
        } catch (Exception e) {
            logger.error("Error occurred while performing finding an entry transaction", e);
            throw new BusinessException("Failed to retrieve entry transaction details", e);
        }
    }

    /**
     * business logic of query a page of entry transaction
     * @param pageable
     * @return
     */
    @Override
    public Page<EntryTransaction> findAllBriefs(Pageable pageable) {
        try {
            return entryTransactionRepository.findAllBriefs(pageable);
        } catch (Exception e) {
            logger.error("Error occurred while performing finding entry transactions", e);
            throw new BusinessException("Failed to retrieve entry transactions", e);
        }
    }

    /**
     * a common validation method to validate all fields stand to the design
     * @param entryTransaction
     * @param forSave
     * @throws Exception
     */
    private void validateTransaction(EntryTransaction entryTransaction, boolean forSave) throws Exception {
        // Implement business logic validations
        if (entryTransaction.getAmount() == null || entryTransaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Amount must be positive");
        }
        if (entryTransaction.getType() == null || entryTransaction.getType().isEmpty()) {
            throw new BusinessException("Transaction type cannot be null or empty");
        }
        if (entryTransaction.getFundId() == null || entryTransaction.getFundId().isEmpty()) {
            throw new BusinessException("Transaction fundId cannot be null or empty");
        }
        if (entryTransaction.getTransactionDate() == null) {
            throw new BusinessException("Transaction date cannot be null");
        }
        if (entryTransaction.getEntry() == null) {
            throw new BusinessException("Entry cannot be null");
        }
        if (entryTransaction.getAccount() == null) {
            throw new BusinessException("Account cannot be null");
        }
        if (entryTransaction.getAccount().getId() == null) {
            throw new BusinessException("Account Id cannot be null");
        }
        if(forSave){
            if (entryTransaction.getEntry().getId() != null) {
                throw new BusinessException("Entry Id must be null");
            }
        }else{
            if (entryTransaction.getEntry().getId() == null) {
                throw new BusinessException("Entry Id cannot be null");
            }
        }
    }
}
