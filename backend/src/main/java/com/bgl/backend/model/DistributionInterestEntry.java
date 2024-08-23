package com.bgl.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Pengcheng Xiao
 *
 * DistributionInterest entity, a subtype of Entry entity, has own specific additional fields
 */

@Data
@Entity
@Table (name = "TDistributionInterest")
@PrimaryKeyJoinColumn (name = "id")
public class DistributionInterestEntry extends Entry{

    public DistributionInterestEntry(){
        this.setEntryType("DistributionInterest");
    }

    @Column(name = "field1")
    private String field1;

    @Column (name = "field2")
    private BigDecimal field2;
}
