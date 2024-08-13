package com.bgl.backend.controller;

import com.bgl.backend.controller.DTO.EntityDTOConvertor;
import com.bgl.backend.controller.DTO.validation.Create;
import com.bgl.backend.controller.DTO.EntryTransactionBrief;
import com.bgl.backend.controller.DTO.EntryTransactionDetail;
import com.bgl.backend.controller.DTO.validation.Update;
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

    private static final Logger logger = LoggerFactory.getLogger(EntryTransactionController.class);

    @Autowired
    IEntryTransactionService entryTransactionService;

    /**
     * RESTful API for query a dedicated entry transaction
     * @param id
     * @return EntryTransactionDetail DTO
     */
    @GetMapping (value = "/{id}")
    public ResponseEntity<EntryTransactionDetail> findById(@PathVariable("id") Long id){
        logger.info("performing find entry transaction by id request");
        try {
            EntryTransaction entryTransaction = entryTransactionService.findDetailById(id);
            if (entryTransaction == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "EntryTransaction not found");
            }
            EntryTransactionDetail detailDTO = EntityDTOConvertor.mapToDetailDTO(entryTransaction);
            return ResponseEntity.ok(detailDTO);
        } catch (Exception e) {
            logger.error("Error occurred while handling find by id request", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve entry transaction", e);
        }
    }

    /**
     * RESTful API for query a page of entry transaction
     * @param pageable
     * @return Page of EntryTransactionBrief
     */
    @GetMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<EntryTransactionBrief>> findAll(@PageableDefault(size = 10) Pageable pageable){
        logger.info("performing find all entry transactions request");
        try {
            Page<EntryTransaction> entryTransactionPage = entryTransactionService.findAllBriefs(pageable);
            Page<EntryTransactionBrief> briefDTOs = entryTransactionPage.map(EntityDTOConvertor::mapToBriefDTO);
            return ResponseEntity.ok(briefDTOs);
        } catch (Exception e) {
            logger.error("Error occurred while handling find all request", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve entry transactions", e);
        }
    }

    /**
     * RESTful API for create an entry transaction
     * receive DTO from client and convert it to entity before calling service
     * @param entryTransactionDetail
     * @return EntryTransactionDetail DTO
     */
    @PostMapping
    public ResponseEntity<EntryTransactionDetail> create(@Validated({Create.class, Default.class}) @RequestBody EntryTransactionDetail entryTransactionDetail){
        logger.info("performing create entry transaction request");
        try {
            EntryTransaction entryTransaction = EntityDTOConvertor.mapToEntity(entryTransactionDetail);
            EntryTransaction savedTransaction = entryTransactionService.save(entryTransaction);
            EntryTransactionDetail responseDTO = EntityDTOConvertor.mapToDetailDTO(savedTransaction);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error occurred while handling create entry transaction request", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create entry transaction", e);
        }
    }

    /**
     * RESTful API for updating an entry transaction
     * receive DTO from client and convert it to entity before calling service
     * @param entryTransactionDetail
     * @param id
     * @return EntryTransactionDetail DTO
     */
    @PutMapping (value = "/{id}")
    public ResponseEntity<EntryTransactionDetail> update(@Validated({Update.class, Default.class}) @RequestBody EntryTransactionDetail entryTransactionDetail, @PathVariable Long id){
        logger.info("performing update entry transaction request");
        try {
            EntryTransaction entryTransaction = EntityDTOConvertor.mapToEntity(entryTransactionDetail);
            EntryTransaction updatedTransaction = entryTransactionService.update(id, entryTransaction);
            EntryTransactionDetail responseDTO = EntityDTOConvertor.mapToDetailDTO(updatedTransaction);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            logger.error("Error occurred while handling update entry transaction request", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update entry transaction", e);
        }
    }

    /**
     * RESTful API for delete an entry transaction
     * @param id
     * @return HTTP_STATUS
     */
    @DeleteMapping (value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        logger.info("performing delete entry transaction request");
        try {
            entryTransactionService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error occurred while handling delete entry transaction request", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete entry transaction", e);
        }
    }
}
