package com.bgl.backend.controller;

import com.bgl.backend.config.RequestInfoLoggingInjector;
import com.bgl.backend.controller.DTO.EntityDTOConvertor;
import com.bgl.backend.controller.DTO.EntryTransactionDetailDTO;
import com.bgl.backend.dao.projection.EntryTransactionBriefProjection;
import com.bgl.backend.model.Account;
import com.bgl.backend.model.BasicBankEntry;
import com.bgl.backend.model.EntryTransaction;
import com.bgl.backend.service.impl.EntryTransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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

@SpringBootTest
@AutoConfigureMockMvc
public class EntryTransactionControllerTest {

    @MockBean
    private transient EntryTransactionService entryTransactionService;

    @Autowired
    private transient MockMvc mockMvc;

    private transient ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFindById() throws Exception {
        final Long id = 1L;

        final EntryTransaction et = new EntryTransaction();
        et.setId(id);
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

        when(entryTransactionService.findDetailById(id)).thenReturn(et);

        mockMvc.perform(get("/entrytransaction/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.entryType").value("BasicBank"));
    }

    @Test
    public void testFindById_WithInvalidDetail() throws Exception {
        final Long id = 1L;

        final EntryTransaction et = new EntryTransaction();
        et.setId(id);
        et.setType("ABC");

        final Account account = new Account();
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
        final List<EntryTransactionBriefProjection> list = new ArrayList<>();

        final Page<EntryTransactionBriefProjection> page = new PageImpl<>(list);

        when(entryTransactionService.findAllBriefs(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/entrytransaction")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindAll_WithValidBrief() throws Exception {

        final EntryTransactionBriefProjection projection = Mockito.mock(EntryTransactionBriefProjection.class);
        Mockito.when(projection.getId()).thenReturn(1L);
        Mockito.when(projection.getType()).thenReturn("ABC");
        Mockito.when(projection.getAmount()).thenReturn(new BigDecimal("12.45"));
        Mockito.when(projection.getTransactionDate()).thenReturn(LocalDate.now());
        Mockito.when(projection.getFundId()).thenReturn("123456");
        Mockito.when(projection.getDateCreated()).thenReturn(new Timestamp(System.currentTimeMillis()));
        Mockito.when(projection.getLastUpdated()).thenReturn(new Timestamp(System.currentTimeMillis()));

        // Mock Entry data
        Mockito.when(projection.getEntryId()).thenReturn(1L);
        Mockito.when(projection.getEntryAmount()).thenReturn(new BigDecimal("90.00"));
        Mockito.when(projection.getEntryGstAmount()).thenReturn(new BigDecimal("10.00"));
        Mockito.when(projection.getEntryType()).thenReturn("BasicBank");

        // Mock Account data
        Mockito.when(projection.getAccountIncrementalId()).thenReturn(1L);
        Mockito.when(projection.getAccountID()).thenReturn(12345L);
        Mockito.when(projection.getAccountCode()).thenReturn("Code-123");
        Mockito.when(projection.getAccountName()).thenReturn("abc");
        Mockito.when(projection.getAccountClass()).thenReturn("ClassA");
        Mockito.when(projection.getAccountType()).thenReturn("TypeB");


        final List<EntryTransactionBriefProjection> list = new ArrayList<>();
        list.add(projection);

        final Page<EntryTransactionBriefProjection> page = new PageImpl<>(list);

        when(entryTransactionService.findAllBriefs(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/entrytransaction")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements").value(1))
                    .andExpect(jsonPath("$.content[0].entryType").value("BasicBank"))
                    .andExpect(jsonPath("$.content[0].amount").value("12.45"))
                    .andExpect(jsonPath("$.content[0].accountType").value("TypeB"));
    }

    @Test
    public void testDelete() throws Exception {
        final Long id = 1L;
        doNothing().when(entryTransactionService).delete(id);

        mockMvc.perform(delete("/entrytransaction/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testCreate_WithEmptyET() throws Exception {
        final EntryTransaction entryTransaction = new EntryTransaction();
        entryTransaction.setId(1L);
        when(entryTransactionService.save(any(EntryTransaction.class))).thenReturn(entryTransaction);

        mockMvc.perform(post("/entrytransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCreate_WithValidDetailHasId() throws Exception {
        final EntryTransaction et = new EntryTransaction();
        et.setId(1L);
        et.setType("ABC");
        et.setFundId("123456");
        et.setAmount(new BigDecimal("12.45"));
        et.setTransactionDate(LocalDate.now());

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

        final EntryTransactionDetailDTO detail = EntityDTOConvertor.mapEntityToDetailDTO(et);
        final String jsonString = objectMapper.writeValueAsString(detail);

        when(entryTransactionService.save(any(EntryTransaction.class))).thenReturn(et);

        mockMvc.perform(post("/entrytransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCreate_WithValidDetail() throws Exception {
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

        final EntryTransactionDetailDTO detail = EntityDTOConvertor.mapEntityToDetailDTO(et);
        final String jsonString = objectMapper.writeValueAsString(detail);

        final EntryTransaction etReturn = et.clone();
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

        final Long id = 1L;

        final EntryTransaction et = new EntryTransaction();
        et.setId(id);
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

        final EntryTransactionDetailDTO detail = EntityDTOConvertor.mapEntityToDetailDTO(et);
        final String jsonString = objectMapper.writeValueAsString(detail);

        when(entryTransactionService.update(eq(id), any(EntryTransaction.class))).thenReturn(et);

        mockMvc.perform(put("/entrytransaction/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    public void testUpdate_WithValidDetailHasNoId() throws Exception {

        final Long id = 1L;

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

        final EntryTransactionDetailDTO detail = EntityDTOConvertor.mapEntityToDetailDTO(et);
        final String jsonString = objectMapper.writeValueAsString(detail);

        when(entryTransactionService.update(eq(id), any(EntryTransaction.class))).thenReturn(et);

        mockMvc.perform(put("/entrytransaction/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().is4xxClientError());
    }

}
