package com.bgl.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Pengcheng Xiao
 *
 * parent entity, define common fields of all entry types
 */
@Data
@Entity
@Inheritance (strategy = InheritanceType.JOINED)
@Table (name = "TEntry")
public class Entry {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;

    @Positive
    @Column (name = "amount", nullable = false)
    private BigDecimal amount;

    @Positive
    @Column (name = "gstAmount", nullable = false)
    private BigDecimal gstAmount;

    @Column (name = "entryType", nullable = false)
    private String entryType;
}