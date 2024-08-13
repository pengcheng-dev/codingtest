package com.bgl.backend.controller;

import com.bgl.backend.controller.DTO.EntityDTOConvertor;
import com.bgl.backend.controller.DTO.EntryTransactionDetail;
import com.bgl.backend.model.Account;
import com.bgl.backend.model.BasicBankEntry;
import com.bgl.backend.model.EntryTransaction;
import com.bgl.backend.service.impl.EntryTransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EntryTransactionController.class)
public class EntryTransactionControllerTest {

    @MockBean
    private EntryTransactionService entryTransactionService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFindById() throws Exception {
        Long id = 1L;

        EntryTransaction et = new EntryTransaction();
        et.setId(id);
        et.setType("ABC");
        et.setFundId("123456");
        et.setTransactionDate(LocalDate.now());
        et.setAmount(new BigDecimal("12.45"));

        Account account = new Account();
        account.setId(1L);
        account.setAccountID(123456L);
        account.setCode("Code-123");
        account.setName("abc");
        account.setAccountClass("xyz");
        et.setAccount(account);

        BasicBankEntry entry = new BasicBankEntry();
        entry.setId(1L);
        entry.setEntryType("BasicBank");
        entry.setAmount(new BigDecimal("123.12"));
        entry.setGstAmount(new BigDecimal("125.90"));
        entry.setField1("");
        entry.setField2(new BigDecimal("12.12"));
        et.setEntry(entry);

        when(entryTransactionService.findDetailById(id)).thenReturn(et);

        mockMvc.perform(get("/entrytransaction/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    public void testFindById_WithInvalidDetail() throws Exception {
        Long id = 1L;

        EntryTransaction et = new EntryTransaction();
        et.setId(id);
        et.setType("ABC");

        Account account = new Account();
        account.setId(1L);
        account.setAccountID(123456L);
        account.setCode("Code-123");
        account.setName("abc");
        account.setAccountClass("xyz");
        et.setAccount(account);

        when(entryTransactionService.findDetailById(id)).thenReturn(et);

        mockMvc.perform(get("/entrytransaction/{id}", id))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testFindAll_WithInvalidBrief() throws Exception {
        List<EntryTransaction> list = new ArrayList<>();
        list.add(new EntryTransaction());

        Page<EntryTransaction> page = new PageImpl<>(list);

        when(entryTransactionService.findAllBriefs(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/entrytransaction")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testFindAll_WithValidBrief() throws Exception {

        EntryTransaction et = new EntryTransaction();
        et.setId(1L);
        et.setType("ABC");
        et.setFundId("123456");
        et.setAmount(new BigDecimal("12.45"));
        et.setTransactionDate(LocalDate.now());
        et.setDateCreated(new Timestamp(System.currentTimeMillis()));
        et.setLastUpdated(new Timestamp(System.currentTimeMillis()));

        Account account = new Account();
        account.setId(1L);
        account.setAccountID(123456L);
        account.setCode("Code-123");
        account.setName("abc");
        account.setAccountClass("xyz");
        et.setAccount(account);

        BasicBankEntry entry = new BasicBankEntry();
        entry.setId(1L);
        entry.setEntryType("BasicBank");
        entry.setAmount(new BigDecimal("123.12"));
        entry.setGstAmount(new BigDecimal("125.90"));
//        entry.setField1("");
//        entry.setField2(new BigDecimal("12.12"));
        et.setEntry(entry);


        List<EntryTransaction> list = new ArrayList<>();
        list.add(et);

        Page<EntryTransaction> page = new PageImpl<>(list);

        when(entryTransactionService.findAllBriefs(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/entrytransaction")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    public void testDelete() throws Exception {
        Long id = 1L;
        doNothing().when(entryTransactionService).delete(id);

        mockMvc.perform(delete("/entrytransaction/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testCreate_WithEmptyET() throws Exception {
        EntryTransaction entryTransaction = new EntryTransaction();
        entryTransaction.setId(1L);
        when(entryTransactionService.save(any(EntryTransaction.class))).thenReturn(entryTransaction);

        mockMvc.perform(post("/entrytransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCreate_WithValidDetailHasId() throws Exception {
        EntryTransaction et = new EntryTransaction();
        et.setId(1L);
        et.setType("ABC");
        et.setFundId("123456");
        et.setAmount(new BigDecimal("12.45"));
        et.setTransactionDate(LocalDate.now());

        Account account = new Account();
        account.setId(1L);
        account.setAccountID(123456L);
        account.setCode("Code-123");
        account.setName("abc");
        account.setAccountClass("xyz");
        et.setAccount(account);

        BasicBankEntry entry = new BasicBankEntry();
        entry.setId(1L);
        entry.setEntryType("BasicBank");
        entry.setAmount(new BigDecimal("123.12"));
        entry.setGstAmount(new BigDecimal("125.90"));
        entry.setField1("");
        entry.setField2(new BigDecimal("12.12"));
        et.setEntry(entry);

        EntryTransactionDetail detail = EntityDTOConvertor.mapToDetailDTO(et);
        String jsonString = objectMapper.writeValueAsString(detail);

        when(entryTransactionService.save(any(EntryTransaction.class))).thenReturn(et);

        mockMvc.perform(post("/entrytransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCreate_WithValidDetail() throws Exception {
        EntryTransaction et = new EntryTransaction();
        et.setType("ABC");
        et.setFundId("123456");
        et.setTransactionDate(LocalDate.now());
        et.setAmount(new BigDecimal("12.45"));

        Account account = new Account();
        account.setId(1L);
        account.setAccountID(123456L);
        account.setCode("Code-123");
        account.setName("abc");
        account.setAccountClass("xyz");
        et.setAccount(account);

        BasicBankEntry entry = new BasicBankEntry();
        entry.setEntryType("BasicBank");
        entry.setAmount(new BigDecimal("123.12"));
        entry.setGstAmount(new BigDecimal("125.90"));
        entry.setField1("");
        entry.setField2(new BigDecimal("12.12"));
        et.setEntry(entry);

        EntryTransactionDetail detail = EntityDTOConvertor.mapToDetailDTO(et);
        String jsonString = objectMapper.writeValueAsString(detail);

        EntryTransaction etReturn = et.clone();
        etReturn.setId(1L);
        etReturn.getEntry().setId(1L);

        when(entryTransactionService.save(any(EntryTransaction.class))).thenReturn(etReturn);

        mockMvc.perform(post("/entrytransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("ABC"));
    }

    @Test
    public void testUpdate_WithValidDetail() throws Exception {

        Long id = 1L;

        EntryTransaction et = new EntryTransaction();
        et.setId(id);
        et.setType("ABC");
        et.setFundId("123456");
        et.setTransactionDate(LocalDate.now());
        et.setAmount(new BigDecimal("12.45"));

        Account account = new Account();
        account.setId(1L);
        account.setAccountID(123456L);
        account.setCode("Code-123");
        account.setName("abc");
        account.setAccountClass("xyz");
        et.setAccount(account);

        BasicBankEntry entry = new BasicBankEntry();
        entry.setId(1L);
        entry.setEntryType("BasicBank");
        entry.setAmount(new BigDecimal("123.12"));
        entry.setGstAmount(new BigDecimal("125.90"));
        entry.setField1("");
        entry.setField2(new BigDecimal("12.12"));
        et.setEntry(entry);

        EntryTransactionDetail detail = EntityDTOConvertor.mapToDetailDTO(et);
        String jsonString = objectMapper.writeValueAsString(detail);

        when(entryTransactionService.update(eq(id), any(EntryTransaction.class))).thenReturn(et);

        mockMvc.perform(put("/entrytransaction/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    public void testUpdate_WithValidDetailHasNoId() throws Exception {

        Long id = 1L;

        EntryTransaction et = new EntryTransaction();
        et.setType("ABC");
        et.setFundId("123456");
        et.setTransactionDate(LocalDate.now());
        et.setAmount(new BigDecimal("12.45"));

        Account account = new Account();
        account.setId(1L);
        account.setAccountID(123456L);
        account.setCode("Code-123");
        account.setName("abc");
        account.setAccountClass("xyz");
        et.setAccount(account);

        BasicBankEntry entry = new BasicBankEntry();
        entry.setEntryType("BasicBank");
        entry.setAmount(new BigDecimal("123.12"));
        entry.setGstAmount(new BigDecimal("125.90"));
        entry.setField1("");
        entry.setField2(new BigDecimal("12.12"));
        et.setEntry(entry);

        EntryTransactionDetail detail = EntityDTOConvertor.mapToDetailDTO(et);
        String jsonString = objectMapper.writeValueAsString(detail);

        when(entryTransactionService.update(eq(id), any(EntryTransaction.class))).thenReturn(et);

        mockMvc.perform(put("/entrytransaction/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().is4xxClientError());
    }

}
