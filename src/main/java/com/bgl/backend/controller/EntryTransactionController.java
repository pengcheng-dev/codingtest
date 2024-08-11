package com.bgl.backend.controller;

import com.bgl.backend.controller.DTO.EntityDTOConvertor;
import com.bgl.backend.controller.DTO.validation.Create;
import com.bgl.backend.controller.DTO.EntryTransactionBrief;
import com.bgl.backend.controller.DTO.EntryTransactionDetail;
import com.bgl.backend.controller.DTO.validation.Update;
import com.bgl.backend.model.EntryTransaction;
import com.bgl.backend.service.IEntryTransactionService;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping (value = "/entrytransaction")
public class EntryTransactionController {

    @Autowired
    IEntryTransactionService entryTransactionService;

    @GetMapping (value = "/{id}")
    public ResponseEntity<EntryTransactionDetail> findById(@PathVariable("id") Long id){
        try {
            EntryTransaction entryTransaction = entryTransactionService.findDetailById(id);
            if (entryTransaction == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "EntryTransaction not found");
            }
            EntryTransactionDetail detailDTO = EntityDTOConvertor.mapToDetailDTO(entryTransaction);
            return ResponseEntity.ok(detailDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve entry transaction", e);
        }
    }

    @GetMapping
    public ResponseEntity<List<EntryTransactionBrief>> findAll(){
        try {
            List<EntryTransaction> entryTransactionList = entryTransactionService.findAllBriefs();
            List<EntryTransactionBrief> briefDTOs = entryTransactionList.stream()
                    .map(EntityDTOConvertor::mapToBriefDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(briefDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve entry transactions", e);
        }
    }

    @PostMapping
    public ResponseEntity<EntryTransactionDetail> create(@Validated({Create.class, Default.class}) @RequestBody EntryTransactionDetail entryTransactionDetail){
        try {
            EntryTransaction entryTransaction = EntityDTOConvertor.mapToEntity(entryTransactionDetail);
            EntryTransaction savedTransaction = entryTransactionService.save(entryTransaction);
            EntryTransactionDetail responseDTO = EntityDTOConvertor.mapToDetailDTO(savedTransaction);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create entry transaction", e);
        }
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<EntryTransactionDetail> update(@Validated({Update.class, Default.class}) @RequestBody EntryTransactionDetail entryTransactionDetail, @PathVariable Long id){
        try {
            EntryTransaction entryTransaction = EntityDTOConvertor.mapToEntity(entryTransactionDetail);
            EntryTransaction updatedTransaction = entryTransactionService.update(id, entryTransaction);
            EntryTransactionDetail responseDTO = EntityDTOConvertor.mapToDetailDTO(updatedTransaction);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update entry transaction", e);
        }
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        try {
            entryTransactionService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete entry transaction", e);
        }
    }
}
