package com.bgl.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table (name = "TInvestmentEntry")
@PrimaryKeyJoinColumn (name = "id")
public class InvestmentEntry extends Entry{

    @PrePersist
    private void prePersist(){
        this.setEntryType("Investment");
    }

    @Column (name = "field1")
    private String field1;

    @Column (name = "field2")
    private BigDecimal field2;
}
