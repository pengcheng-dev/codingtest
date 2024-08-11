package com.bgl.backend.controller;

import com.bgl.backend.controller.DTO.EntityDTOConvertor;
import com.bgl.backend.controller.DTO.EntryTransactionBrief;
import com.bgl.backend.controller.DTO.EntryTransactionDetail;
import com.bgl.backend.model.Account;
import com.bgl.backend.model.BasicBankEntry;
import com.bgl.backend.model.EntryTransaction;
import com.bgl.backend.service.IEntryTransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EntryTransactionControllerTest {

    @Mock
    private IEntryTransactionService entryTransactionService;

    @InjectMocks
    private EntryTransactionController entryTransactionController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(entryTransactionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFindById() throws Exception {
        Long id = 1L;

        EntryTransaction et = new EntryTransaction();
        et.setId(id);
        et.setType("ABC");
        et.setFundId("123456");
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
    public void testFindAll() throws Exception {
        List<EntryTransactionBrief> briefList = new ArrayList<>();
        briefList.add(new EntryTransactionBrief());

        when(entryTransactionService.findAllBriefs()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/entrytransaction"))
                .andExpect(status().isOk());
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
        et.setTransactionDate(new Date());
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
        et.setTransactionDate(new Date());
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
        et.setTransactionDate(new Date());
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
