package com.bgl.backend.service;

import com.bgl.backend.dao.EntryTransactionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class EntryTransactionServiceTest {

    @Mock
    private EntryTransactionRepository entryTransactionRepository;

    @InjectMocks
    private IEntryTransactionService entryTransactionService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(true);
    }

    @Test
    public void testSaveEntryTransaction(){

    }

    @Test
    public void testUpdateEntryTransaction(){

    }

    @Test
    public void testFindEntryTransactionById(){

    }

    @Test
    public void testFindAllEntryTransaction(){

    }
}
