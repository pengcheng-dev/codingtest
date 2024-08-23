package com.bgl.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Pengcheng Xiao
 *
 * Dividend entity, a subtype of Entry entity, has own specific additional fields
 */
@Data
@Entity
@Table (name = "TDividendEntry")
@PrimaryKeyJoinColumn (name = "id")
public class DividendEntry extends Entry{

    public DividendEntry(){
        this.setEntryType("Dividend");
    }

    @Column (name = "field1")
    private String field1;

    @Column (name = "field2")
    private BigDecimal field2;
}
