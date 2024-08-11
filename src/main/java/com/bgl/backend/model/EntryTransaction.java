package com.bgl.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table (name = "TEntryTransaction")

@NamedEntityGraph(
        name = "EntryTransaction.accountAndEntryBrief",
        attributeNodes = {
                @NamedAttributeNode("account"),
                @NamedAttributeNode("entry")
        }
)

public class EntryTransaction implements Cloneable{

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn (name = "acctId", nullable=false)
    private Account account;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "entryId", nullable = false)
    private Entry entry;

    @Column (name = "type", nullable = false)
    private String type;

    @Column (name = "amount", nullable = false)
    private BigDecimal amount;

    @Column (name = "transactionDate", nullable = false)
    private Date transactionDate;

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
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
