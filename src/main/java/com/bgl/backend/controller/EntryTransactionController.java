package com.bgl.backend.controller;

import com.bgl.backend.common.exception.SystemException;
import com.bgl.backend.common.logging.LoggerWrapper;
import com.bgl.backend.controller.DTO.EntityDTOConvertor;
import com.bgl.backend.controller.DTO.validation.Create;
import com.bgl.backend.controller.DTO.EntryTransactionBriefDTO;
import com.bgl.backend.controller.DTO.EntryTransactionDetailDTO;
import com.bgl.backend.controller.DTO.validation.Update;
import com.bgl.backend.dao.projection.EntryTransactionBriefProjection;
import com.bgl.backend.model.EntryTransaction;
import com.bgl.backend.service.IEntryTransactionService;
import jakarta.validation.groups.Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Pengcheng Xiao
 *
 * Main RESTful API endpoint to CRUD entry transaction
 */
@RestController
@RequestMapping (value = "/entrytransaction")
public class EntryTransactionController {

    private static final Logger LOG = LoggerFactory.getLogger(EntryTransactionController.class);
    @Autowired
    private transient LoggerWrapper loggerWrapper;

    @Autowired
    private transient IEntryTransactionService entryTransactionService;

    /**
     * RESTful API for query a dedicated entry transaction
     * @param id
     * @return EntryTransactionDetail DTO
     */
    @GetMapping (value = "/{id}")
    public ResponseEntity<EntryTransactionDetailDTO> findById(@PathVariable("id") final Long id){
        try {
            final EntryTransaction entryTransaction = entryTransactionService.findDetailById(id);
            if (entryTransaction == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "EntryTransaction not found");
            }
            final EntryTransactionDetailDTO detailDTO = EntityDTOConvertor.mapEntityToDetailDTO(entryTransaction);
            return ResponseEntity.ok(detailDTO);
        } catch (SystemException e) {
            loggerWrapper.logErrorWithException(LOG, "Error occurred while handling find by id request", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve entry transaction", e);
        }
    }

    /**
     * RESTful API for query a page of entry transaction
     * @param pageable
     * @return Page of EntryTransactionBrief
     */
    @GetMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<EntryTransactionBriefDTO>> findAll(@PageableDefault(size = 10) final Pageable pageable){
        if(pageable == null || pageable.getPageSize() < 1 || pageable.getPageNumber() < 0) {
            throw new IllegalArgumentException("Pageable cannot be null and page size must be positive");
        }
        try {
            final Page<EntryTransactionBriefProjection> entryTransactionPage = entryTransactionService.findAllBriefs(pageable);
            final Page<EntryTransactionBriefDTO> briefDTOs = entryTransactionPage.map(EntityDTOConvertor::mapEntityProjectionToBriefDTO);
            return ResponseEntity.ok(briefDTOs);
        } catch (SystemException e) {
            loggerWrapper.logErrorWithException(LOG, "Error occurred while handling find all request", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve entry transactions", e);
        }
    }

    /**
     * RESTful API for create an entry transaction
     * receive DTO from client and convert it to entity before calling service
     * @param entryTransactionDetailDTO
     * @return EntryTransactionDetail DTO
     */
    @PostMapping
    public ResponseEntity<EntryTransactionDetailDTO> create(@Validated({Create.class, Default.class}) @RequestBody final EntryTransactionDetailDTO entryTransactionDetailDTO){
        try {
            final EntryTransaction entryTransaction = EntityDTOConvertor.mapDetailDTOToEntity(entryTransactionDetailDTO);
            final EntryTransaction savedTransaction = entryTransactionService.save(entryTransaction);
            final EntryTransactionDetailDTO responseDTO = EntityDTOConvertor.mapEntityToDetailDTO(savedTransaction);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (SystemException e) {
            loggerWrapper.logErrorWithException(LOG, "Error occurred while handling create entry transaction request", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create entry transaction", e);
        }
    }

    /**
     * RESTful API for updating an entry transaction
     * receive DTO from client and convert it to entity before calling service
     * @param entryTransactionDetailDTO
     * @param id
     * @return EntryTransactionDetail DTO
     */
    @PutMapping (value = "/{id}")
    public ResponseEntity<EntryTransactionDetailDTO> update(@Validated({Update.class, Default.class}) @RequestBody final EntryTransactionDetailDTO entryTransactionDetailDTO, @PathVariable final Long id){
        try {
            final EntryTransaction entryTransaction = EntityDTOConvertor.mapDetailDTOToEntity(entryTransactionDetailDTO);
            final EntryTransaction updatedTransaction = entryTransactionService.update(id, entryTransaction);
            final EntryTransactionDetailDTO responseDTO = EntityDTOConvertor.mapEntityToDetailDTO(updatedTransaction);
            return ResponseEntity.ok(responseDTO);
        } catch (SystemException e) {
            loggerWrapper.logErrorWithException(LOG, "Error occurred while handling update entry transaction request", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update entry transaction", e);
        }
    }

    /**
     * RESTful API for delete an entry transaction
     * @param id
     * @return HTTP_STATUS
     */
    @DeleteMapping (value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final Long id){
        try {
            entryTransactionService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (SystemException e) {
            loggerWrapper.logErrorWithException(LOG, "Error occurred while handling delete entry transaction request", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete entry transaction", e);
        }
    }
}
