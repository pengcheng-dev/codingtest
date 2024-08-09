package com.bgl.backend.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContributionEntry extends Entry{
    private String field1;
    private BigDecimal field2;
}
