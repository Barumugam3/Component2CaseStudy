package com.iiht.estock.stock.service;

import com.iiht.estock.stock.eventsourcing.KafkaAddStockEventSourcing;
import com.iiht.estock.stock.eventsourcing.KafkaDeleteStockEventSourcing;
import com.iiht.estock.stock.eventsourcing.events.AddStockEvent;
import com.iiht.estock.stock.exception.StockNotCreatedException;
import com.iiht.estock.stock.model.Company;
import com.iiht.estock.stock.model.Stock;
import com.iiht.estock.stock.repository.StockCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class StockCommandServiceImpl implements StockCommandService {

    private final StockCommandRepository stockCommandRepository;
    private final RestTemplate restTemplate;
    private final KafkaAddStockEventSourcing kafkaAddStockEventSourcing;
    private final KafkaDeleteStockEventSourcing kafkaDeleteStockEventSourcing;

    @Value(value = "#{environment['COMPANY_SERVICE'] ?: 'http://localhost:8089'}")
    private String companyServiceEndpoint;

    @Override
    public AddStockEvent addStock(Stock stock) throws StockNotCreatedException {
        try {
            log.info("Adding stock price for company {} in db...", stock.getCompanyCode());
            stock.setStockCreatedDate(LocalDateTime.now());
            Company company = getCompanyByCompanyCode(stock.getCompanyCode());
            if (company != null) {
                stockCommandRepository.save(stock);
                return kafkaAddStockEventSourcing.publicAddStockEvent(stock);
            }
            else
                throw new StockNotCreatedException("Company " + stock.getCompanyCode() + " not found.");
        } catch (Exception e) {
            log.error(e.getClass() + " -- " + e.getMessage());
            throw new StockNotCreatedException(e.getMessage());
        }
    }

    @Override
    public boolean deleteAll(Long companyCode) {
        log.info("Deleting all stock price for company {} in db...", companyCode);
        List<Stock> stockPrices = stockCommandRepository.findAllByCompanyCode(companyCode);
        for (Stock stockPrice : stockPrices) {
            try {
                kafkaDeleteStockEventSourcing.publicDeleteStockEvent(stockPrice);
            } catch (Exception e) {
                log.error(e.getClass() + " -- " + e.getMessage());
                return false;
            }
            stockPrice.setDeleted(true);
            stockCommandRepository.save(stockPrice);
        }
        return true;
    }

    private Company getCompanyByCompanyCode(Long companyCode) {
        return restTemplate.getForEntity(companyServiceEndpoint + "/api/v1.0/market/company/info/" + companyCode,
                Company.class).getBody();
    }
}
