package com.bgl.backend.controller.DTO;

import com.bgl.backend.controller.DTO.validation.Create;
import com.bgl.backend.controller.DTO.validation.Update;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Pengcheng Xiao
 *
 * DTO defined for detailed display and updating, including specific additional fields for each subtype entry
 */
@Data
@Validated
public class EntryTransactionDetailDTO {

    @Null(groups = Create.class)
    private Long id;

    @NotBlank
    private String type;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String transactionDate;

    @NotBlank
    private String fundId;

    @Null
    private String dateCreated;

    @Null
    private String lastUpdated;

    //account part

    @NotNull
    private Long accountIncrementalId;

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

    @Schema(
        description = "A map containing dynamic name-value pairs based on the entry type. " +
            "The keys and values vary depending on the specific entry type.",
        example = "{ 'field1': 'value1', 'field2': 'value2' }"
    )
    //additional fields for sub entries
    private Map<String, ?> nameValueMap;

}