package com.iiht.estock.stock.service;

import com.iiht.estock.stock.eventsourcing.events.AddStockEvent;
import com.iiht.estock.stock.exception.StockNotCreatedException;
import com.iiht.estock.stock.model.Stock;

public interface StockCommandService {

    AddStockEvent addStock(Stock stock) throws StockNotCreatedException;

    boolean deleteAll(Long companyCode);
}
