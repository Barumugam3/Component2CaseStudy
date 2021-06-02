package com.iiht.estock.stock.service;

import com.iiht.estock.stock.exception.StockNotFoundException;
import com.iiht.estock.stock.model.Stock;
import com.iiht.estock.stock.repository.StockQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class StockQueryServiceImpl implements StockQueryService {

    private final StockQueryRepository stockQueryRepository;
    private final RestTemplate restTemplate;

    @Override
    public List<Stock> getAllStocksByCompanyCodeBetweenDates(Long companyCode, LocalDate startDate, LocalDate endDate) throws StockNotFoundException {
        try {
            log.info("Searching stock prices for company {} between {} and {} in db...",
                    companyCode,
                    startDate,
                    endDate);
            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = endDate.atTime(23, 59, 59);
            return stockQueryRepository.findAllByCompanyCodeAndStockCreatedDateBetween(companyCode,
                    start, end);
        } catch (Exception e) {
            log.error(e.getClass() + " -- " + e.getMessage());
            throw new StockNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<Stock> getAllStockByCompanyCode(Long companyCode) throws StockNotFoundException {
        try {
            log.info("Searching stock price for company {} in db...", companyCode);
            return stockQueryRepository.findAllByCompanyCode(companyCode);
        } catch (Exception e) {
            log.error(e.getClass() + " -- " + e.getMessage());
            throw new StockNotFoundException(e.getMessage());
        }
    }

    @Override
    public void createStock(Stock stock) {
        log.info("Create new stock: {}", stock);
        stockQueryRepository.save(stock);
    }

    @Override
    public void deleteStock(Stock stock) {
        log.info("Delete stock: {}", stock);
        stockQueryRepository.delete(stock);
    }
}
