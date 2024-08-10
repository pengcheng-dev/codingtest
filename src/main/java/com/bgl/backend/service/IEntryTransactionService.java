package com.bgl.backend.service;

import com.bgl.backend.model.EntryTransaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IEntryTransactionService {

    EntryTransaction save(EntryTransaction entryTransaction) throws Exception;
    EntryTransaction update(Long id, EntryTransaction entryTransaction) throws Exception;
    void delete(Long id) throws Exception;

    EntryTransaction findDetailById(Long id);
    List<EntryTransaction> findAllBriefs();
}
