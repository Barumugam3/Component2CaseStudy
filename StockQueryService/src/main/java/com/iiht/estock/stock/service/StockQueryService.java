package com.iiht.estock.stock.service;

import com.iiht.estock.stock.exception.StockNotFoundException;
import com.iiht.estock.stock.model.Stock;

import java.time.LocalDate;
import java.util.List;

public interface StockQueryService {

    List<Stock> getAllStocksByCompanyCodeBetweenDates(Long companyCode,
                                                      LocalDate startDate,
                                                      LocalDate endDate) throws StockNotFoundException;

    List<Stock> getAllStockByCompanyCode(Long companyCode) throws StockNotFoundException;

    void createStock(Stock stock);

    void deleteStock(Stock stock);
}
