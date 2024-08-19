package com.bgl.backend.service;

import com.bgl.backend.dao.projection.EntryTransactionBriefProjection;
import com.bgl.backend.model.EntryTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * @author Pengcheng Xiao
 */
@Component
public interface IEntryTransactionService {

    EntryTransaction save(final EntryTransaction entryTransaction);
    EntryTransaction update(final Long id, final EntryTransaction entryTransaction);
    void delete(final Long id);

    EntryTransaction findDetailById(final Long id);
    Page<EntryTransactionBriefProjection> findAllBriefs(final Pageable pageable);
}
