package com.bgl.backend.service.impl;

import com.bgl.backend.dao.EntryTransactionRepository;
import com.bgl.backend.model.EntryTransaction;
import com.bgl.backend.service.IEntryTransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntryTransactionService implements IEntryTransactionService {

    @Autowired
    EntryTransactionRepository entryTransactionRepository;

    @Override
    @Transactional
    public EntryTransaction save(EntryTransaction entryTransaction) throws Exception {
        return entryTransactionRepository.save(entryTransaction);
    }

    @Override
    public EntryTransaction update(Long id, EntryTransaction entryTransaction) throws Exception {
        return entryTransactionRepository.save(entryTransaction);
    }

    @Override
    @Transactional
    public void delete(Long id) throws Exception {
        entryTransactionRepository.deleteById(id);
    }

    @Override
    public EntryTransaction findDetailById(Long id) {
        return null;
    }

    @Override
    public List<EntryTransaction> findAllBriefs() {
        return List.of();
    }
}
