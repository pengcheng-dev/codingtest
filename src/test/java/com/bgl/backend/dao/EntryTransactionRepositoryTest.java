package com.bgl.backend.dao;

import com.bgl.backend.dao.projection.EntryTransactionBriefProjection;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles ("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EntryTransactionRepositoryTest {

    @Autowired
    private transient EntryTransactionRepository entryTransactionRepository;

    @Autowired
    private transient AccountRepository accountRepository;

    private transient Long accountId = null;

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
        final List entryTransactionList = entryTransactionRepository.findAll();
        assert(entryTransactionList.isEmpty());
    }

    @Test
    @Transactional
    public void testSaveBasicBankEntry(){
        //given
        final BasicBankEntry entry = new BasicBankEntry();
        entry.setAmount(new BigDecimal("10.50"));
        entry.setGstAmount(new BigDecimal("12.50"));
        entry.setField1("_blank");

        //the incomplete account object will be cached
        final Account account = new Account();
        account.setId(accountId);

        final EntryTransaction et = new EntryTransaction();
        et.setEntry(entry);
        et.setAccount(account);

        et.setAmount(new BigDecimal("10.50"));
        et.setFundId("123456");
        et.setType("sample");
        et.setTransactionDate(LocalDate.now());

        //when
        final EntryTransaction savedET = entryTransactionRepository.save(et);

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
        final InvestmentEntry entry = new InvestmentEntry();
        entry.setAmount(new BigDecimal("10.50"));
        entry.setGstAmount(new BigDecimal("12.50"));
        entry.setField1("_blank");

        //the incomplete account object will be cached
        final Account account = new Account();
        account.setId(accountId);

        final EntryTransaction et = new EntryTransaction();
        et.setEntry(entry);
        et.setAccount(account);

        et.setAmount(new BigDecimal("10.50"));
        et.setFundId("123456");
        et.setType("sample");
        et.setTransactionDate(LocalDate.now());

        //when
        final EntryTransaction savedET = entryTransactionRepository.save(et);

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
        final InvestmentEntry entry = new InvestmentEntry();
        entry.setAmount(new BigDecimal("10.50"));
        entry.setGstAmount(new BigDecimal("12.50"));
        entry.setField1("_blank");

        //the incomplete account object will be cached
        final Account account = new Account();
        account.setId(accountId);

        final EntryTransaction et = new EntryTransaction();
        et.setEntry(entry);
        et.setAccount(account);

        et.setAmount(new BigDecimal("10.50"));
        et.setFundId("123456");
        et.setType("sample");
        et.setTransactionDate(LocalDate.now());

        //when
        final EntryTransaction savedET = entryTransactionRepository.save(et);
        entryTransactionRepository.deleteById(savedET.getId());
        final Optional<EntryTransaction> optional = entryTransactionRepository.findById(savedET.getId());

        //then
        assertTrue(optional.isEmpty());
    }

    @Test
    @Transactional
    public void testSaveAndFindById(){
        //given
        final InvestmentEntry entry = new InvestmentEntry();
        entry.setAmount(new BigDecimal("10.50"));
        entry.setGstAmount(new BigDecimal("12.50"));
        entry.setField1("_blank");

        final Account account = accountRepository.findById(accountId).get();

        final EntryTransaction et = new EntryTransaction();
        et.setEntry(entry);
        et.setAccount(account);

        et.setAmount(new BigDecimal("10.50"));
        et.setFundId("123456");
        et.setType("sample");
        et.setTransactionDate(LocalDate.now());

        //when
        final EntryTransaction savedET = entryTransactionRepository.save(et);
        final Optional<EntryTransaction> optional = entryTransactionRepository.findById(savedET.getId());

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
        final InvestmentEntry entry = new InvestmentEntry();
        entry.setAmount(new BigDecimal("10.50"));
        entry.setGstAmount(new BigDecimal("12.50"));
        entry.setField1("_blank");

        final Account account = accountRepository.findById(accountId).get();

        final EntryTransaction et = new EntryTransaction();
        et.setEntry(entry);
        et.setAccount(account);

        et.setAmount(new BigDecimal("10.50"));
        et.setFundId("123456");
        et.setType("sample");
        et.setTransactionDate(LocalDate.now());

        final EntryTransaction savedET = entryTransactionRepository.save(et);

        //when
        final DividendEntry newEntry = new DividendEntry();
        newEntry.setAmount(new BigDecimal("11.50"));
        newEntry.setGstAmount(new BigDecimal("13.50"));
        newEntry.setField1("_blank");

        savedET.setEntry(newEntry);
        final EntryTransaction updatedET = entryTransactionRepository.save(savedET);

        //then
        assertEquals("123456", updatedET.getFundId());
        assertEquals("Dividend", updatedET.getEntry().getEntryType());
        assertEquals(new BigDecimal("11.50"), updatedET.getEntry().getAmount());
        assertEquals(123456L, updatedET.getAccount().getAccountID());
        assertEquals("abc", updatedET.getAccount().getName());
    }

    @Test
    public void testFindAllBriefs(){
        final Page<EntryTransactionBriefProjection> page = entryTransactionRepository.findAllBriefs(PageRequest.of(0, 10));
        assertNotNull(page);
        assertEquals(0, page.getTotalPages());
        assertEquals(0, page.getTotalElements());
    }

    @Test
    public void testSaveAndFindAllBriefs(){
        //given
        final BasicBankEntry entry = new BasicBankEntry();
        entry.setAmount(new BigDecimal("10.50"));
        entry.setGstAmount(new BigDecimal("12.50"));
        entry.setField1("_blank");

        //the incomplete account object will be cached
        final Account account = new Account();
        account.setId(accountId);

        final EntryTransaction et = new EntryTransaction();
        et.setEntry(entry);
        et.setAccount(account);

        et.setAmount(new BigDecimal("10.50"));
        et.setFundId("123456");
        et.setType("sample");
        et.setTransactionDate(LocalDate.now());

        final EntryTransaction savedET = entryTransactionRepository.save(et);

        //when
        final Page<EntryTransactionBriefProjection> page = entryTransactionRepository.findAllBriefs(PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
        final Optional<EntryTransactionBriefProjection> optional = page.stream().findFirst();

        assertTrue(optional.isPresent());

        assertNotNull(optional.get().getId());
        assertEquals("123456", optional.get().getFundId());
        assertNotNull(optional.get().getEntryId());
        assertEquals("BasicBank", optional.get().getEntryType());
        assertEquals(new BigDecimal("10.50"), optional.get().getEntryAmount());
    }
}
