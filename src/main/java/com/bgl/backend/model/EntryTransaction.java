package com.bgl.backend.model;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class EntryTransaction {
    private Long id;
    private Account account;
    private Entry entry;
    private String type;
    private BigDecimal amount;
    private Date transactionDate;
    private String fundId;
    private Timestamp dateCreated;
    private Timestamp lastUpdated;
}
