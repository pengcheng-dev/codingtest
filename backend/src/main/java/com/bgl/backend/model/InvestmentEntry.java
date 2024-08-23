package com.bgl.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Pengcheng Xiao
 *
 * Investment entity, a subtype of Entry entity, has own specific additional fields
 */
@Data
@Entity
@Table (name = "TInvestmentEntry")
@PrimaryKeyJoinColumn (name = "id")
public class InvestmentEntry extends Entry{

    public InvestmentEntry(){
        this.setEntryType("Investment");
    }

    @Column (name = "field1")
    private String field1;

    @Column (name = "field2")
    private BigDecimal field2;
}
