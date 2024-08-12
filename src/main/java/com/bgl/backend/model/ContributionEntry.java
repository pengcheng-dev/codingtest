package com.bgl.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Pengcheng Xiao
 *
 * Contribution entity, a subtype of Entry entity, has own specific additional fields
 */
@Data
@Entity
@Table (name = "TContribution")
@PrimaryKeyJoinColumn (name = "id")
public class ContributionEntry extends Entry{

    public ContributionEntry(){
        this.setEntryType("Contribution");
    }

    @Column(name = "field1")
    private String field1;

    @Column (name = "field2")
    private BigDecimal field2;
}
