package com.bgl.backend.controller;

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
    public EntryTransaction findById(@PathVariable("id") Long id){
        return entryTransactionService.findById(id);
    }

    @GetMapping
    public List<EntryTransaction> findAll(){
        return entryTransactionService.findAll();
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
    public EntryTransaction save(@RequestBody EntryTransaction entryTransaction){
        try {
            return entryTransactionService.save(entryTransaction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping (value = "/{id}")
    public EntryTransaction update(@RequestBody EntryTransaction entryTransaction,@PathVariable Long id){
        try {
            return entryTransactionService.update(id, entryTransaction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
