package com.bgl.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table (name = "TDistributionInterest")
@PrimaryKeyJoinColumn (name = "id")
public class DistributionInterestEntry extends Entry{
    @PrePersist
    private void prePersist(){
        this.setEntryType("DistributionInterest");
    }

    @Column(name = "field1")
    private String field1;

    @Column (name = "field2")
    private BigDecimal field2;
}
