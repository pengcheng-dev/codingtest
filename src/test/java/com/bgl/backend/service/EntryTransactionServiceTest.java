package com.bgl.backend.service;

import com.bgl.backend.dao.projection.EntryTransactionBriefProjection;
import com.bgl.backend.model.Account;
import com.bgl.backend.model.BasicBankEntry;
import com.bgl.backend.model.EntryTransaction;
import com.bgl.backend.dao.EntryTransactionRepository;
import com.bgl.backend.service.impl.EntryTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EntryTransactionServiceTest {

    @Mock
    private transient EntryTransactionRepository entryTransactionRepository;

    @InjectMocks
    private transient EntryTransactionService entryTransactionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave_WithInvalidEntity() throws Exception {
        final EntryTransaction entryTransaction = new EntryTransaction();
        entryTransaction.setId(1L);
        when(entryTransactionRepository.save(any(EntryTransaction.class))).thenReturn(entryTransaction);

        assertThrows(Exception.class, ()->{
            EntryTransaction savedTransaction = entryTransactionService.save(entryTransaction);
        });
    }

    @Test
    public void testSave_WithValidEntity() throws Exception {

        final EntryTransaction et = new EntryTransaction();

        et.setType("ABC");
        et.setFundId("123456");
        et.setTransactionDate(LocalDate.now());
        et.setAmount(new BigDecimal("12.45"));

        final Account account = new Account();
        account.setId(1L);
        account.setAccountID(123456L);
        account.setCode("Code-123");
        account.setName("abc");
        account.setAccountClass("xyz");
        et.setAccount(account);

        final BasicBankEntry entry = new BasicBankEntry();
        entry.setEntryType("BasicBank");
        entry.setAmount(new BigDecimal("123.12"));
        entry.setGstAmount(new BigDecimal("125.90"));
        entry.setField1("");
        entry.setField2(new BigDecimal("12.12"));
        et.setEntry(entry);
        when(entryTransactionRepository.save(any(EntryTransaction.class))).thenReturn(et);

        //when
        final EntryTransaction savedTransaction = entryTransactionService.save(et);

        //then
        assertNotNull(savedTransaction);
        verify(entryTransactionRepository, times(1)).save(et);
    }


    @Test
    public void testUpdate_WithInvalidEntity() throws Exception {
        final EntryTransaction et = new EntryTransaction();

        et.setType("ABC");
        et.setFundId("123456");
        et.setTransactionDate(LocalDate.now());
        et.setAmount(new BigDecimal("12.45"));

        final Account account = new Account();
        account.setId(1L);
        account.setAccountID(123456L);
        account.setCode("Code-123");
        account.setName("abc");
        account.setAccountClass("xyz");
        et.setAccount(account);

        final BasicBankEntry entry = new BasicBankEntry();
        entry.setEntryType("BasicBank");
        entry.setAmount(new BigDecimal("123.12"));
        entry.setGstAmount(new BigDecimal("125.90"));
        entry.setField1("");
        entry.setField2(new BigDecimal("12.12"));
        et.setEntry(entry);

        when(entryTransactionRepository.findById(1L)).thenReturn(Optional.of(et));
        when(entryTransactionRepository.save(any(EntryTransaction.class))).thenReturn(et);

        assertThrows(Exception.class, () -> {
            EntryTransaction updatedTransaction = entryTransactionService.update(1L, et);
        });
    }

    @Test
    public void testUpdate_WithValidEntity() throws Exception {

        final EntryTransaction et = new EntryTransaction();
        et.setId(1L);
        et.setType("ABC");
        et.setFundId("123456");
        et.setTransactionDate(LocalDate.now());
        et.setAmount(new BigDecimal("12.45"));

        final Account account = new Account();
        account.setId(1L);
        account.setAccountID(123456L);
        account.setCode("Code-123");
        account.setName("abc");
        account.setAccountClass("xyz");
        et.setAccount(account);

        final BasicBankEntry entry = new BasicBankEntry();
        entry.setId(1L);
        entry.setEntryType("BasicBank");
        entry.setAmount(new BigDecimal("123.12"));
        entry.setGstAmount(new BigDecimal("125.90"));
        entry.setField1("");
        entry.setField2(new BigDecimal("12.12"));
        et.setEntry(entry);

        when(entryTransactionRepository.findById(1L)).thenReturn(Optional.of(et));
        when(entryTransactionRepository.save(any(EntryTransaction.class))).thenReturn(et);

        final EntryTransaction updatedTransaction = entryTransactionService.update(1L, et);

        assertNotNull(updatedTransaction);
        assertEquals(1L, updatedTransaction.getId());
    }

    @Test
    public void testDelete() throws Exception {
        final Long id = 1L;
        when(entryTransactionRepository.existsById(id)).thenReturn(true);
        doNothing().when(entryTransactionRepository).deleteById(id);

        entryTransactionService.delete(id);

        verify(entryTransactionRepository, times(1)).deleteById(id);
    }

    @Test
    public void testFindDetailById() {
        final Long id = 1L;
        final EntryTransaction entryTransaction = new EntryTransaction();
        entryTransaction.setId(id);
        when(entryTransactionRepository.findById(id)).thenReturn(Optional.of(entryTransaction));

        final EntryTransaction foundTransaction = entryTransactionService.findDetailById(id);

        assertNotNull(foundTransaction);
        assertEquals(id, foundTransaction.getId());
    }

    @Test
    public void testFindAllBriefs() {
        final Page<EntryTransactionBriefProjection> page = new PageImpl<EntryTransactionBriefProjection>(Arrays.asList());
        when(entryTransactionRepository.findAllBriefs(any(Pageable.class))).thenReturn(page);

        final Page<EntryTransactionBriefProjection> result = entryTransactionService.findAllBriefs(PageRequest.of(0, 10));
        assertNotNull(result);
        assertEquals(0, result.getContent().size());

        verify(entryTransactionRepository).findAllBriefs(any(Pageable.class));
    }
}
