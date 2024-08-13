package com.bgl.backend.controller.DTO;

import com.bgl.backend.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pengcheng Xiao
 * a helper convert from entity to brief DTO for list display, unify time formatter as well,
 * convert entity to detail DTO for updating, and help controller converting DTO to entity
 */

public class EntityDTOConvertor {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH-mm-ss");
    // DTO to Entity Mapping Methods

    /**
     * convert detail DTO received form client to entity
     * @param detailDTO
     * @return EntryTransaction
     */
    public static EntryTransaction mapToEntity(EntryTransactionDetail detailDTO) {

        EntryTransaction et = new EntryTransaction();
        et.setId(detailDTO.getId());
        et.setType(detailDTO.getType());
        et.setFundId(detailDTO.getFundId());
        et.setAmount(detailDTO.getAmount());
        et.setTransactionDate(LocalDate.parse(detailDTO.getTransactionDate(), DATE_FORMATTER));

        Account account = new Account();
        account.setId(detailDTO.getAccountIncrementalId());
        et.setAccount(account);

        //backend side and frontend side need to know entry types and specific additional fields for each subtype
        switch (detailDTO.getEntryType()){
            case "BasicBank":
                BasicBankEntry basicBankEntry = new BasicBankEntry();
                basicBankEntry.setId(detailDTO.getEntryId());
                basicBankEntry.setAmount(detailDTO.getEntryAmount());
                basicBankEntry.setGstAmount(detailDTO.getEntryGstAmount());
                basicBankEntry.setField1((String) detailDTO.getNameValueMap().get("Field1"));
                basicBankEntry.setField2(new BigDecimal(detailDTO.getNameValueMap().get("Field2").toString()));
                et.setEntry(basicBankEntry);
                break;
            case "Contribution":
                ContributionEntry contributionEntry = new ContributionEntry();
                contributionEntry.setId(detailDTO.getEntryId());
                contributionEntry.setAmount(detailDTO.getEntryAmount());
                contributionEntry.setGstAmount(detailDTO.getEntryGstAmount());
                contributionEntry.setField1((String) detailDTO.getNameValueMap().get("Field1"));
                contributionEntry.setField2(new BigDecimal(detailDTO.getNameValueMap().get("Field2").toString()));
                et.setEntry(contributionEntry);
                break;
            case "DistributionInterest":
                DistributionInterestEntry distributionInterestEntry = new DistributionInterestEntry();
                distributionInterestEntry.setId(detailDTO.getEntryId());
                distributionInterestEntry.setAmount(detailDTO.getEntryAmount());
                distributionInterestEntry.setGstAmount(detailDTO.getEntryGstAmount());
                distributionInterestEntry.setField1((String) detailDTO.getNameValueMap().get("Field1"));
                distributionInterestEntry.setField2(new BigDecimal(detailDTO.getNameValueMap().get("Field2").toString()));
                et.setEntry(distributionInterestEntry);
                break;
            case "Dividend":
                DividendEntry dividendEntry = new DividendEntry();
                dividendEntry.setId(detailDTO.getEntryId());
                dividendEntry.setAmount(detailDTO.getEntryAmount());
                dividendEntry.setGstAmount(detailDTO.getEntryGstAmount());
                dividendEntry.setField1((String) detailDTO.getNameValueMap().get("Field1"));
                dividendEntry.setField2(new BigDecimal(detailDTO.getNameValueMap().get("Field2").toString()));
                et.setEntry(dividendEntry);
                break;
            case "Investment":
                InvestmentEntry investmentEntry = new InvestmentEntry();
                investmentEntry.setId(detailDTO.getEntryId());
                investmentEntry.setAmount(detailDTO.getEntryAmount());
                investmentEntry.setGstAmount(detailDTO.getEntryGstAmount());
                investmentEntry.setField1((String) detailDTO.getNameValueMap().get("Field1"));
                investmentEntry.setField2(new BigDecimal(detailDTO.getNameValueMap().get("Field2").toString()));
                et.setEntry(investmentEntry);
                break;
        }
        return et;
    }

    /**
     * convert entity to detail DTO for updating or detail view
     * @param entryTransaction
     * @return EntryTransaction DTO with additional fields of subtype
     */
    public static EntryTransactionDetail mapToDetailDTO(EntryTransaction entryTransaction) {
        // Implement the mapping logic from EntryTransaction entity to EntryTransactionDetail DTO

        EntryTransactionDetail detailDTO = new EntryTransactionDetail();
        detailDTO.setId(entryTransaction.getId());
        detailDTO.setType(entryTransaction.getType());
        detailDTO.setFundId(entryTransaction.getFundId());
        detailDTO.setAmount(entryTransaction.getAmount());
        detailDTO.setTransactionDate(entryTransaction.getTransactionDate().format(DATE_FORMATTER));

        detailDTO.setAccountIncrementalId(entryTransaction.getAccount().getId());
        detailDTO.setAccountId(entryTransaction.getAccount().getAccountID());
        detailDTO.setAccountCode(entryTransaction.getAccount().getCode());
        detailDTO.setAccountName(entryTransaction.getAccount().getName());
        detailDTO.setAccountClass(entryTransaction.getAccount().getAccountClass());

        detailDTO.setEntryId(entryTransaction.getEntry().getId());
        detailDTO.setEntryType(entryTransaction.getEntry().getEntryType());
        detailDTO.setEntryAmount(entryTransaction.getEntry().getAmount());
        detailDTO.setEntryGstAmount(entryTransaction.getEntry().getGstAmount());

        Map<String, Object> nameValueMap = new HashMap<String, Object>();
        switch (entryTransaction.getEntry().getEntryType()) {
            case "BasicBank":
                nameValueMap.put("Field1", ((BasicBankEntry)entryTransaction.getEntry()).getField1());
                nameValueMap.put("Field2", ((BasicBankEntry)entryTransaction.getEntry()).getField2());
                break;
            case "Contribution":
                nameValueMap.put("Field1", ((ContributionEntry)entryTransaction.getEntry()).getField1());
                nameValueMap.put("Field2", ((ContributionEntry)entryTransaction.getEntry()).getField2());
                break;
            case "DistributionInterest":
                nameValueMap.put("Field1", ((DistributionInterestEntry)entryTransaction.getEntry()).getField1());
                nameValueMap.put("Field2", ((DistributionInterestEntry)entryTransaction.getEntry()).getField2());
                break;
            case "Dividend":
                nameValueMap.put("Field1", ((DividendEntry)entryTransaction.getEntry()).getField1());
                nameValueMap.put("Field2", ((DividendEntry)entryTransaction.getEntry()).getField2());
                break;
            case "Investment":
                nameValueMap.put("Field1", ((InvestmentEntry)entryTransaction.getEntry()).getField1());
                nameValueMap.put("Field2", ((InvestmentEntry)entryTransaction.getEntry()).getField2());
                break;
        }
        detailDTO.setNameValueMap(nameValueMap);
        return detailDTO;
    }

    /**
     * convert entity list to DTO list for list display, only display common fields
     * @param entryTransaction
     * @return Entry transaction DTO with common fields
     */
    public static EntryTransactionBrief mapToBriefDTO(EntryTransaction entryTransaction) {
        // Implement the mapping logic from EntryTransaction entity to EntryTransactionDetail DTO

        EntryTransactionBrief briefDTO = new EntryTransactionBrief();
        briefDTO.setId(entryTransaction.getId());
        briefDTO.setType(entryTransaction.getType());
        briefDTO.setFundId(entryTransaction.getFundId());
        briefDTO.setAmount(entryTransaction.getAmount());
        briefDTO.setTransactionDate(entryTransaction.getTransactionDate().format(DATE_FORMATTER));
        briefDTO.setDateCreated(entryTransaction.getDateCreated().toLocalDateTime().format(DATE_TIME_FORMATTER));
        briefDTO.setLastUpdated(entryTransaction.getLastUpdated().toLocalDateTime().format(DATE_TIME_FORMATTER));

        briefDTO.setAccountIncrementalId(entryTransaction.getAccount().getId());
        briefDTO.setAccountId(entryTransaction.getAccount().getAccountID());
        briefDTO.setAccountCode(entryTransaction.getAccount().getCode());
        briefDTO.setAccountType(entryTransaction.getAccount().getAccountType());
        briefDTO.setAccountName(entryTransaction.getAccount().getName());
        briefDTO.setAccountClass(entryTransaction.getAccount().getAccountClass());

        briefDTO.setEntryId(entryTransaction.getEntry().getId());
        briefDTO.setEntryType(entryTransaction.getEntry().getEntryType());
        briefDTO.setEntryAmount(entryTransaction.getEntry().getAmount());
        briefDTO.setEntryGstAmount(entryTransaction.getEntry().getGstAmount());

        return briefDTO;
    }
}
