package com.bgl.backend.dao;

import com.bgl.backend.model.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles ("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EntryTransactionRepositoryTest {

    @Autowired
    private EntryTransactionRepository entryTransactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Long accountId = null;

    @BeforeEach
    @Transactional
    public void setUp(){
        Account account = new Account();
        account.setAccountID(123456L);
        account.setName("abc");
        account.setCode("xyz");
        account.setAccountClass("corporate");

        accountId = accountRepository.save(account).getId();
        account = null;
    }

    @AfterEach
    @Transactional
    public void tearDown(){
        //must delete ertrytransaction first due to foreign key reference
        entryTransactionRepository.deleteAll();

        accountRepository.deleteById(accountId);
        accountId = null;
    }



    @Test
    public void testFindAll(){
        List entryTransactionList = entryTransactionRepository.findAll();
        assert(entryTransactionList.isEmpty());
    }

    @Test
    @Transactional
    public void testSaveBasicBankEntry(){
        //given
        BasicBankEntry entry = new BasicBankEntry();
        entry.setAmount(new BigDecimal("10.50"));
        entry.setGstAmount(new BigDecimal("12.50"));
        entry.setField1("_blank");

        //the incomplete account object will be cached
        Account account = new Account();
        account.setId(accountId);

        EntryTransaction et = new EntryTransaction();
        et.setEntry(entry);
        et.setAccount(account);

        et.setAmount(new BigDecimal("10.50"));
        et.setFundId("123456");
        et.setType("sample");
        et.setTransactionDate(LocalDate.now());

        //when
        EntryTransaction savedET = entryTransactionRepository.save(et);

        //then
        assertNotNull(savedET.getId());
        assertEquals(savedET.getFundId(), et.getFundId());
        assertNotNull(savedET.getEntry().getId());
        assertEquals("BasicBank", savedET.getEntry().getEntryType());
        assertEquals(new BigDecimal("10.50"), savedET.getEntry().getAmount());
    }

    @Test
    @Transactional
    public void testSaveInvestmentEntry(){
        //given
        InvestmentEntry entry = new InvestmentEntry();
        entry.setAmount(new BigDecimal("10.50"));
        entry.setGstAmount(new BigDecimal("12.50"));
        entry.setField1("_blank");

        //the incomplete account object will be cached
        Account account = new Account();
        account.setId(accountId);

        EntryTransaction et = new EntryTransaction();
        et.setEntry(entry);
        et.setAccount(account);

        et.setAmount(new BigDecimal("10.50"));
        et.setFundId("123456");
        et.setType("sample");
        et.setTransactionDate(LocalDate.now());

        //when
        EntryTransaction savedET = entryTransactionRepository.save(et);

        //then
        assertNotNull(savedET.getId());
        assertEquals(savedET.getFundId(), et.getFundId());
        assertNotNull(savedET.getEntry().getId());
        assertEquals("Investment", savedET.getEntry().getEntryType());
        assertEquals(new BigDecimal("10.50"), savedET.getEntry().getAmount());
    }

    @Test
    @Transactional
    public void testSaveAndDelete(){
        //given
        InvestmentEntry entry = new InvestmentEntry();
        entry.setAmount(new BigDecimal("10.50"));
        entry.setGstAmount(new BigDecimal("12.50"));
        entry.setField1("_blank");

        //the incomplete account object will be cached
        Account account = new Account();
        account.setId(accountId);

        EntryTransaction et = new EntryTransaction();
        et.setEntry(entry);
        et.setAccount(account);

        et.setAmount(new BigDecimal("10.50"));
        et.setFundId("123456");
        et.setType("sample");
        et.setTransactionDate(LocalDate.now());

        //when
        EntryTransaction savedET = entryTransactionRepository.save(et);
        entryTransactionRepository.deleteById(savedET.getId());
        Optional<EntryTransaction> optional = entryTransactionRepository.findById(savedET.getId());

        //then
        assertTrue(optional.isEmpty());
    }

    @Test
    @Transactional
    public void testSaveAndFindById(){
        //given
        InvestmentEntry entry = new InvestmentEntry();
        entry.setAmount(new BigDecimal("10.50"));
        entry.setGstAmount(new BigDecimal("12.50"));
        entry.setField1("_blank");

        Account account = accountRepository.findById(accountId).get();

        EntryTransaction et = new EntryTransaction();
        et.setEntry(entry);
        et.setAccount(account);

        et.setAmount(new BigDecimal("10.50"));
        et.setFundId("123456");
        et.setType("sample");
        et.setTransactionDate(LocalDate.now());

        //when
        EntryTransaction savedET = entryTransactionRepository.save(et);
        Optional<EntryTransaction> optional = entryTransactionRepository.findById(savedET.getId());

        //then
        assert(optional.isPresent());
        assertEquals("123456", optional.get().getFundId());
        assertEquals("Investment", optional.get().getEntry().getEntryType());
        assertEquals(123456L, optional.get().getAccount().getAccountID());
        assertEquals("abc", optional.get().getAccount().getName());
        Object entryObject = optional.get().getEntry();
        assertTrue(entryObject instanceof InvestmentEntry);
    }

    @Test
    @Transactional
    public void testSaveAndUpdate(){
        //given
        InvestmentEntry entry = new InvestmentEntry();
        entry.setAmount(new BigDecimal("10.50"));
        entry.setGstAmount(new BigDecimal("12.50"));
        entry.setField1("_blank");

        Account account = accountRepository.findById(accountId).get();

        EntryTransaction et = new EntryTransaction();
        et.setEntry(entry);
        et.setAccount(account);

        et.setAmount(new BigDecimal("10.50"));
        et.setFundId("123456");
        et.setType("sample");
        et.setTransactionDate(LocalDate.now());

        EntryTransaction savedET = entryTransactionRepository.save(et);

        //when
        DividendEntry newEntry = new DividendEntry();
        newEntry.setAmount(new BigDecimal("11.50"));
        newEntry.setGstAmount(new BigDecimal("13.50"));
        newEntry.setField1("_blank");

        savedET.setEntry(newEntry);
        EntryTransaction updatedET = entryTransactionRepository.save(savedET);

        //then
        assertEquals("123456", updatedET.getFundId());
        assertEquals("Dividend", updatedET.getEntry().getEntryType());
        assertEquals(new BigDecimal("11.50"), updatedET.getEntry().getAmount());
        assertEquals(123456L, updatedET.getAccount().getAccountID());
        assertEquals("abc", updatedET.getAccount().getName());
    }

    @Test
    public void testFindAllBriefs(){
        Page<EntryTransaction> page = entryTransactionRepository.findAllBriefs(PageRequest.of(0, 10));
        assertNotNull(page);
        assertEquals(0, page.getTotalPages());
        assertEquals(0, page.getTotalElements());
    }

    @Test
    public void testSaveAndFindAllBriefs(){
        //given
        BasicBankEntry entry = new BasicBankEntry();
        entry.setAmount(new BigDecimal("10.50"));
        entry.setGstAmount(new BigDecimal("12.50"));
        entry.setField1("_blank");

        //the incomplete account object will be cached
        Account account = new Account();
        account.setId(accountId);

        EntryTransaction et = new EntryTransaction();
        et.setEntry(entry);
        et.setAccount(account);

        et.setAmount(new BigDecimal("10.50"));
        et.setFundId("123456");
        et.setType("sample");
        et.setTransactionDate(LocalDate.now());

        EntryTransaction savedET = entryTransactionRepository.save(et);

        //when
        Page<EntryTransaction> page = entryTransactionRepository.findAllBriefs(PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
        Optional<EntryTransaction> optional = page.stream().findFirst();

        assertTrue(optional.isPresent());

        assertNotNull(optional.get().getId());
        assertEquals("123456", optional.get().getFundId());
        assertNotNull(optional.get().getEntry().getId());
        assertEquals("BasicBank", optional.get().getEntry().getEntryType());
        assertEquals(new BigDecimal("10.50"), optional.get().getEntry().getAmount());
    }
}
