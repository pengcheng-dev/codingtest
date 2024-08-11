package com.bgl.backend.service.impl;

import com.bgl.backend.dao.EntryTransactionRepository;
import com.bgl.backend.model.EntryTransaction;
import com.bgl.backend.service.IEntryTransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class EntryTransactionService implements IEntryTransactionService {

    @Autowired
    private EntryTransactionRepository entryTransactionRepository;

    @Transactional
    @Override
    public EntryTransaction save(EntryTransaction entryTransaction) throws Exception {
        validateTransaction(entryTransaction, true);
        try {
            return entryTransactionRepository.save(entryTransaction);
        } catch (Exception e) {
            throw new Exception("Failed to save entry transaction", e);
        }
    }

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
                throw new Exception("EntryTransaction not found");
            }
        } catch (Exception e) {
            throw new Exception("Failed to update entry transaction", e);
        }
    }

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
                throw new Exception("EntryTransaction not found");
            }
        } catch (Exception e) {
            throw new Exception("Failed to delete entry transaction", e);
        }
    }

    @Override
    public EntryTransaction findDetailById(Long id) {
        if((null == id) || (id < 0)){
            throw new RuntimeException("Invalid Id");
        }
        try {
            return entryTransactionRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve entry transaction details", e);
        }
    }

    @Override
    public List<EntryTransaction> findAllBriefs() {
        try {
            return entryTransactionRepository.findAllBriefs();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve entry transactions", e);
        }
    }

    private void validateTransaction(EntryTransaction entryTransaction, boolean forSave) throws Exception {
        // Implement business logic validations
        if (entryTransaction.getAmount() == null || entryTransaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Amount must be positive");
        }
        if (entryTransaction.getType() == null || entryTransaction.getType().isEmpty()) {
            throw new Exception("Transaction type cannot be null or empty");
        }
        if (entryTransaction.getFundId() == null || entryTransaction.getFundId().isEmpty()) {
            throw new Exception("Transaction type cannot be null or empty");
        }
        if (entryTransaction.getTransactionDate() == null) {
            throw new Exception("Transaction type cannot be null");
        }
        if (entryTransaction.getEntry() == null) {
            throw new Exception("Entry cannot be null");
        }
        if (entryTransaction.getAccount() == null) {
            throw new Exception("Account cannot be null");
        }
        if (entryTransaction.getAccount().getId() == null) {
            throw new Exception("Account Id cannot be null");
        }
        if(forSave){
            if (entryTransaction.getEntry().getId() != null) {
                throw new Exception("Entry Id must be null");
            }
        }else{
            if (entryTransaction.getEntry().getId() == null) {
                throw new Exception("Entry Id cannot be null");
            }
        }
    }
}
