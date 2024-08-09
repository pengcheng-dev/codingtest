package com.bgl.backend.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvestmentEntry extends Entry{
    private String field1;
    private BigDecimal field2;
}
