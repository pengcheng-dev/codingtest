package com.bgl.backend.service.impl;

import com.bgl.backend.dao.EntryTransactionRepository;
import com.bgl.backend.dao.projection.EntryTransactionBriefProjection;
import com.bgl.backend.common.exception.SystemException;
import com.bgl.backend.model.EntryTransaction;
import com.bgl.backend.service.IEntryTransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private transient EntryTransactionRepository entryTransactionRepository;

    @Value("${pagination.pagesize.max}")
    private int maximumPageSize;

    /**
     * save an entry transaction, all fields must be valid
     * @param entryTransaction entry transaction entity
     * @return EntryTransaction entry transaction entity
     */
    @Transactional
    @Override
    public EntryTransaction save(final EntryTransaction entryTransaction) {
        validateTransaction(entryTransaction, true);
        return entryTransactionRepository.save(entryTransaction);
    }

    /**
     * update an entry transaction record, when entry type changed, the original entry subtype need to be deleted,
     * and new subtype entry will be created and associated with the entry transaction
     * @param id entry transaction id
     * @param entryTransaction entry transaction entity
     * @return EntryTransaction entry transaction entity
     */
    @Override
    @Transactional
    public EntryTransaction update(final Long id, final EntryTransaction entryTransaction) {
        validateTransaction(entryTransaction, false);

        final Optional<EntryTransaction> existingTransaction = entryTransactionRepository.findById(id);

        if (existingTransaction.isPresent()) {
            final EntryTransaction transactionToUpdate = existingTransaction.get();

            // Update fields as needed
            transactionToUpdate.setAmount(entryTransaction.getAmount());
            transactionToUpdate.setType(entryTransaction.getType());
            transactionToUpdate.setTransactionDate(entryTransaction.getTransactionDate());
            transactionToUpdate.setAmount(entryTransaction.getAmount());
            transactionToUpdate.setFundId(entryTransaction.getFundId());

            //Set relationship with entry and account
            transactionToUpdate.setEntry(entryTransaction.getEntry());
            transactionToUpdate.setAccount(entryTransaction.getAccount());

            return entryTransactionRepository.save(transactionToUpdate);
        } else {
            throw new SystemException("EntryTransaction not found", HttpStatus.NOT_FOUND.value());
        }
    }

    /**
     * business logic of deleting an entry transaction, spring transaction involved
     * @param id entry transaction id
     */
    @Override
    @Transactional
    public void delete(final Long id) {
        if((null == id) || (id < 0)){
            throw new IllegalArgumentException("Invalid Id, must be a positive Id");
        }
        if (entryTransactionRepository.existsById(id)) {
            entryTransactionRepository.deleteById(id);
        } else {
            throw new SystemException("EntryTransaction not found", HttpStatus.NOT_FOUND.value());
        }
    }

    /**
     * business logic of query an entry transaction, validate id
     * @param id entry transaction id
     * @return EntryTransaction
     */
    @Override
    public EntryTransaction findDetailById(final Long id) {
        if((null == id) || (id < 0)){
            throw new IllegalArgumentException("Invalid Id, must be a positive Id");
        }
        return entryTransactionRepository.findById(id).orElse(null);
    }

    /**
     * business logic of query a page of entry transaction
     * @param pageable Pageable object
     * @return Page of EntryTransaction
     */
    @Override
    public Page<EntryTransactionBriefProjection> findAllBriefs(final Pageable pageable) {
        if(null == pageable || pageable.getPageSize() < 1 || pageable.getPageSize() > maximumPageSize || pageable.getPageNumber() < 0) {
            throw new IllegalArgumentException("Pageable cannot be null and page size must be positive");
        }
        return entryTransactionRepository.findAllBriefs(pageable);
    }

    /**
     * a common validation method to validate all fields stand to the design
     * @param entryTransaction entry transaction entity
     * @param forSave or for update
     */
    private void validateTransaction(final EntryTransaction entryTransaction, final boolean forSave) {
        // Implement business logic validations
        if(entryTransaction == null){
            throw new IllegalArgumentException("Entry Transaction entity cannot be null");
        }
        if (entryTransaction.getAmount() == null || entryTransaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (entryTransaction.getType() == null || entryTransaction.getType().isEmpty()) {
            throw new IllegalArgumentException("Transaction type cannot be null or empty");
        }
        if (entryTransaction.getFundId() == null || entryTransaction.getFundId().isEmpty()) {
            throw new IllegalArgumentException("Transaction fundId cannot be null or empty");
        }
        if (entryTransaction.getTransactionDate() == null) {
            throw new IllegalArgumentException("Transaction date cannot be null");
        }
        if (entryTransaction.getEntry() == null) {
            throw new IllegalArgumentException("Entry cannot be null");
        }
        if (entryTransaction.getAccount() == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        if (entryTransaction.getAccount().getId() == null) {
            throw new IllegalArgumentException("Account Id cannot be null");
        }
        if(forSave){
            if (entryTransaction.getEntry().getId() != null) {
                throw new IllegalArgumentException("Entry Id must be null");
            }
        }else{
            if (entryTransaction.getEntry().getId() == null) {
                throw new IllegalArgumentException("Entry Id cannot be null");
            }
        }
    }
}
