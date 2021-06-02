package com.iiht.estock.stock.model;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@ToString
public class StockResponseDto {

    private List<Stock> stockPrices;
    private BigDecimal maxStockPrice;
    private BigDecimal minStockPrice;
    private BigDecimal avgStockPrice;
}
