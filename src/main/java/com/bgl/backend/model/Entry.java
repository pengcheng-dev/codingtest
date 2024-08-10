package com.bgl.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Inheritance (strategy = InheritanceType.JOINED)
@Table (name = "TEntry")
public class Entry {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;

    @Column (name = "amount", nullable = false)
    private BigDecimal amount;

    @Column (name = "gstAmount", nullable = false)
    private BigDecimal gstAmount;

    @Column (name = "entryType", nullable = false)
    private String entryType;
}