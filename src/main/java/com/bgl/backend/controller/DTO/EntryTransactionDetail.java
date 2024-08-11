package com.bgl.backend.controller.DTO;

import com.bgl.backend.controller.DTO.validation.Create;
import com.bgl.backend.controller.DTO.validation.Update;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@Data
@Validated
public class EntryTransactionDetail {

    @Null(groups = Create.class)
    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank
    private String type;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Date transactionDate;

    @NotBlank
    private String fundId;

    @Null
    private Timestamp dateCreated;

    @Null
    private Timestamp lastUpdated;

    //account part

    @NotNull
    private Long accountIncrementalId;

    @NotNull
    private Long accountId;

    private String accountCode;

    private String accountName;

    private String accountClass;

    private String accountType;

    private Long parentId;

    //entry part

    @Null(groups = Create.class)
    @NotNull(groups = Update.class)
    private Long entryId;

    @NotNull
    private BigDecimal entryAmount;

    @NotNull
    private BigDecimal entryGstAmount;

    @NotBlank
    private String entryType;

    //additional fields for sub entries

    private Map<String, ?> nameValueMap;
}