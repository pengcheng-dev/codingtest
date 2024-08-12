package com.bgl.backend.service;

import com.bgl.backend.model.EntryTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * @author Pengcheng Xiao
 */
@Component
public interface IEntryTransactionService {

    EntryTransaction save(EntryTransaction entryTransaction) throws Exception;
    EntryTransaction update(Long id, EntryTransaction entryTransaction) throws Exception;
    void delete(Long id) throws Exception;

    EntryTransaction findDetailById(Long id);
    Page<EntryTransaction> findAllBriefs(Pageable pageable);
}
