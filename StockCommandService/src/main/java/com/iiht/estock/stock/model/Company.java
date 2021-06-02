package com.iiht.estock.stock.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    private Long companyCode;
    private String companyName;
    private String companyCEO;
    private String companyTurnOver;
    private String companyWebsite;
    private String stockExchange;
    private BigDecimal latestStockPrice;
}
