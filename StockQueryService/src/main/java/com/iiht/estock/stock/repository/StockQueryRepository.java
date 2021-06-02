package com.iiht.estock.stock.repository;

import com.iiht.estock.stock.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockQueryRepository extends MongoRepository<Stock, Long> {

    @Query(value = "{ 'companyCode': ?0, 'stockCreatedDate': {$gte: ?1, $lte: ?2} }")
    List<Stock> findAllByCompanyCodeAndStockCreatedDateBetween(Long companyCode,
                                                               LocalDateTime startDate,
                                                               LocalDateTime endDate);

    List<Stock> findAllByCompanyCode(Long companyCode);
}
