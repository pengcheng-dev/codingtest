package com.bgl.backend.dao.projection;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

public interface EntryTransactionBriefProjection {

    Long getId();
    String getType();
    BigDecimal getAmount();
    LocalDate getTransactionDate();
    String getFundId();
    Timestamp getDateCreated();
    Timestamp getLastUpdated();

    Long getEntryId();
    BigDecimal getEntryAmount();
    BigDecimal getEntryGstAmount();
    String getEntryType();

    Long getAccountIncrementalId();
    Long getAccountID();
    String getAccountCode();
    String getAccountName();
    String getAccountClass();
    String getAccountType();
    Long getAccountParentID();
}
