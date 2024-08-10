package com.bgl.backend.controller;

import com.bgl.backend.controller.DTO.EntryTransactionBrief;
import com.bgl.backend.controller.DTO.EntryTransactionDetail;
import com.bgl.backend.model.EntryTransaction;
import com.bgl.backend.service.IEntryTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping (value = "/entrytransaction")
public class EntryTransactionController {

    @Autowired
    IEntryTransactionService entryTransactionService;

    @GetMapping (value = "/{id}")
    public EntryTransactionDetail findById(@PathVariable("id") Long id){
        EntryTransaction entryTransaction= entryTransactionService.findDetailById(id);
        return null;
    }

    @GetMapping
    public List<EntryTransactionBrief> findAll(){
        List<EntryTransaction> entryTransactionList = entryTransactionService.findAllBriefs();
        return null;
    }

    @DeleteMapping (value = "/{id}")
    public boolean delete(@PathVariable("id") Long id){
        try {
            entryTransactionService.delete(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @PostMapping
    public EntryTransaction create(@RequestBody EntryTransactionDetail entryTransactionDetail){
        try {
            EntryTransaction entryTransaction = new EntryTransaction();
            return entryTransactionService.save(entryTransaction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping (value = "/{id}")
    public EntryTransaction update(@RequestBody EntryTransactionDetail entryTransactionDetail,@PathVariable Long id){
        try {
            EntryTransaction entryTransaction = new EntryTransaction();
            return entryTransactionService.update(id, entryTransaction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
