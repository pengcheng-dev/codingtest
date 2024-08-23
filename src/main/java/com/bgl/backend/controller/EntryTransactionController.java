package com.bgl.backend.controller;

import com.bgl.backend.common.exception.SystemException;
import com.bgl.backend.common.logging.LoggerWrapper;
import com.bgl.backend.controller.DTO.EntityDTOConvertor;
import com.bgl.backend.controller.DTO.validation.Create;
import com.bgl.backend.controller.DTO.EntryTransactionBriefDTO;
import com.bgl.backend.controller.DTO.EntryTransactionDetailDTO;
import com.bgl.backend.controller.DTO.validation.Update;
import com.bgl.backend.dao.projection.EntryTransactionBriefProjection;
import com.bgl.backend.model.EntryTransaction;
import com.bgl.backend.service.IEntryTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.groups.Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Pengcheng Xiao
 *
 * Main RESTful API endpoint to CRUD entry transaction
 */
@RestController
@RequestMapping (value = "/entrytransaction")
@Tag(name = "EntryTransaction", description = "APIs for CRUD operations on Entry Transactions")
public class EntryTransactionController {

    private static final Logger LOG = LoggerFactory.getLogger(EntryTransactionController.class);
    @Autowired
    private transient LoggerWrapper loggerWrapper;

    @Autowired
    private transient IEntryTransactionService entryTransactionService;

    @Value("${pagination.pagesize.max}")
    private int maximumPageSize;

    @Operation(summary = "Get an entry transaction by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "EntryTransaction found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EntryTransactionDetailDTO.class))),
        @ApiResponse(responseCode = "404", description = "EntryTransaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping (value = "/{id}")
    public ResponseEntity<EntryTransactionDetailDTO> findById(@PathVariable("id") final @NotNull @Positive Long id){
        try {
            final EntryTransaction entryTransaction = entryTransactionService.findDetailById(id);
            if (entryTransaction == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "EntryTransaction not found");
            }
            final EntryTransactionDetailDTO detailDTO = EntityDTOConvertor.mapEntityToDetailDTO(entryTransaction);
            return ResponseEntity.ok(detailDTO);
        } catch (SystemException e) {
            loggerWrapper.logErrorWithException(LOG, "Error occurred while handling find by id request", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve entry transaction", e);
        }
    }

    @Operation(summary = "Get a page of entry transactions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Page of EntryTransactionBrief found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EntryTransactionBriefDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid pageable parameter"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<EntryTransactionBriefDTO>> findAll(
        @Parameter(description = "Page index (0..N)", example = "0")
        @RequestParam(value = "page", defaultValue = "0") int page,
        @Parameter(description = "Page size (0..N]", example = "10")
        @RequestParam(value = "size", defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        if(pageable.getPageSize() < 1 || pageable.getPageSize() > maximumPageSize || pageable.getPageNumber() < 0) {
            throw new IllegalArgumentException("Illegal pageable parameter");
        }
        try {
            final Page<EntryTransactionBriefProjection> entryTransactionPage = entryTransactionService.findAllBriefs(pageable);
            final Page<EntryTransactionBriefDTO> briefDTOs = entryTransactionPage.map(EntityDTOConvertor::mapEntityProjectionToBriefDTO);
            return ResponseEntity.ok(briefDTOs);
        } catch (SystemException e) {
            loggerWrapper.logErrorWithException(LOG, "Error occurred while handling find all request", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve entry transactions", e);
        }
    }

    @Operation(summary = "Create a new entry transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "EntryTransaction created",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EntryTransactionDetailDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<EntryTransactionDetailDTO> create(@Validated({Create.class, Default.class}) @RequestBody final EntryTransactionDetailDTO entryTransactionDetailDTO){
        try {
            final EntryTransaction entryTransaction = EntityDTOConvertor.mapDetailDTOToEntity(entryTransactionDetailDTO);
            final EntryTransaction savedTransaction = entryTransactionService.save(entryTransaction);
            final EntryTransactionDetailDTO responseDTO = EntityDTOConvertor.mapEntityToDetailDTO(savedTransaction);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (SystemException e) {
            loggerWrapper.logErrorWithException(LOG, "Error occurred while handling create entry transaction request", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create entry transaction", e);
        }
    }

    @Operation(summary = "Update an existing entry transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "EntryTransaction updated",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EntryTransactionDetailDTO.class))),
        @ApiResponse(responseCode = "404", description = "EntryTransaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping (value = "/{id}")
    public ResponseEntity<EntryTransactionDetailDTO> update(@Validated({Update.class, Default.class}) @RequestBody final EntryTransactionDetailDTO entryTransactionDetailDTO, @PathVariable final @NotNull @Positive Long id){
        try {
            final EntryTransaction entryTransaction = EntityDTOConvertor.mapDetailDTOToEntity(entryTransactionDetailDTO);
            final EntryTransaction updatedTransaction = entryTransactionService.update(id, entryTransaction);
            final EntryTransactionDetailDTO responseDTO = EntityDTOConvertor.mapEntityToDetailDTO(updatedTransaction);
            return ResponseEntity.ok(responseDTO);
        } catch (SystemException e) {
            loggerWrapper.logErrorWithException(LOG, "Error occurred while handling update entry transaction request",
                e);
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Failed to find entry transaction when updating", e);
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to update entry transaction", e);
            }
        }
    }

    @Operation(summary = "Delete an entry transaction by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "EntryTransaction deleted"),
        @ApiResponse(responseCode = "404", description = "EntryTransaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping (value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final @NotNull @Positive Long id){
        try {
            entryTransactionService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (SystemException e) {
            loggerWrapper.logErrorWithException(LOG,
                "Error occurred while handling delete entry transaction request", e);
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Failed to find entry transaction when deleting", e);
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to delete entry transaction", e);
            }}
    }
}
