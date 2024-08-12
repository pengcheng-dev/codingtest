package com.bgl.backend.controller.DTO;


import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * @author Pengcheng Xiao
 *
 * DTO defined for list display, only include common fields in an entry transaction entity
 */
@Data
public class EntryTransactionBrief {

    private Long id;

    private String type;

    private BigDecimal amount;

    private String transactionDate;

    private String fundId;

    private String dateCreated;

    private String lastUpdated;

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
