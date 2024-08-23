package com.bgl.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * @author Pengcheng Xiao
 *
 *  Entry transaction entity, main entity of this project, reference to an account
 */
@Data
@Entity
@Table (name = "TEntryTransaction")
public class EntryTransaction implements Cloneable{

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;

    /**
     * foreign reference to an account
     */
    @ManyToOne
    @JoinColumn (name = "acctId", nullable=false)
    private Account account;

    /**
     * create an entry transaction record will create an entry record automatically,
     * if entry type updated, a new entry recorded will be created and referenced to the entry transaction,
     * the original entry record should be deleted to keep data integrity.
     */
    @OneToOne (cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn (name = "entryId", referencedColumnName = "id", nullable = false)
    private Entry entry;

    @Column (name = "type", nullable = false)
    private String type;

    @Positive
    @Column (name = "amount", nullable = false)
    private BigDecimal amount;

    @Column (name = "transactionDate", nullable = false)
    private LocalDate transactionDate;

    @Column (name = "fundId", nullable = false)
    private String fundId;

    @Column (name = "date_created", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp dateCreated;

    @Column (name = "late_updated", insertable = false, updatable = true, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp lastUpdated;

    @Override
    public EntryTransaction clone() {
        try {
            EntryTransaction clone = (EntryTransaction) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
