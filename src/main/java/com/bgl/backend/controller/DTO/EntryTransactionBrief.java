package com.bgl.backend.controller.DTO;


import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class EntryTransactionBrief {

    private Long id;

    private String type;

    private BigDecimal amount;

    private Date transactionDate;

    private String fundId;

    private Timestamp dateCreated;

    private Timestamp lastUpdated;

    //account part

    private Long accountIncrementalId;

    private Long accountId;

    private String accountCode;

    private String accountName;

    private String accountClass;

    private String accountType;

    private Long parentId;

    //entry part

    private Long entryId;

    private BigDecimal entryAmount;

    private BigDecimal entryGstAmount;

    private String entryType;

}
