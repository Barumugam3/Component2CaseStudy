package com.iiht.estock.stock.api;

import com.iiht.estock.stock.exception.ExceptionResponseMessage;
import com.iiht.estock.stock.exception.StockNotFoundException;
import com.iiht.estock.stock.model.Stock;
import com.iiht.estock.stock.model.StockResponseDto;
import com.iiht.estock.stock.service.StockQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1.0/market/stock")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "StockPrice", description = "Stock Price Resource")
public class StockQuery {

    private final StockQueryService stockQueryService;

    /**
     * Get list of stock prices (reverse ordered according to stock created date)
     * along with max, min and avg stock prices
     * @param companyCode
     * @param startDate
     * @param endDate
     * @return {@link StockResponseDto}
     * @throws StockNotFoundException
     */
    @Operation(summary = "Get stock price by company code between date range", description = "", tags = { "StockPrice" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful Operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = StockResponseDto.class)))),
            @ApiResponse(responseCode = "404",
                    description = "Stock price Not Found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExceptionResponseMessage.class))))
    })
    @GetMapping(value = "/get/{companycode}/{startdate}/{enddate}")
    public StockResponseDto getStockByCompanyBetweenDates(@Parameter(description = "Company Code", required = true)
                                                       @PathVariable("companycode") Long companyCode,
                                                          @Parameter(description = "Start Date", required = true)
                                                       @PathVariable("startdate") String startDate,
                                                          @Parameter(description = "End Date", required = true)
                                                       @PathVariable("enddate") String endDate) throws StockNotFoundException {
        try {

            log.info("Retrieving stock prices for company {} between dates {} and {}",
                    companyCode,
                    startDate,
                    endDate);
            StockResponseDto stockPriceDTO = new StockResponseDto();

            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

            List<Stock> stockPrices = stockQueryService.getAllStocksByCompanyCodeBetweenDates(companyCode, start, end);

            // stock list with reverse order of stock created date
            stockPriceDTO.setStockPrices(stockPrices.stream()
                    .sorted(Comparator.comparing(Stock::getStockCreatedDate, Comparator.reverseOrder()))
                    .collect(Collectors.toList()));

            // min stock price
            stockPriceDTO.setMinStockPrice(stockPrices.stream()
                    .min(Comparator.comparing(Stock::getStockPrice)).get().getStockPrice());

            // max stock price
            stockPriceDTO.setMaxStockPrice(stockPrices.stream()
                    .max(Comparator.comparing(Stock::getStockPrice)).get().getStockPrice());

            // avg stock price
            stockPriceDTO.setAvgStockPrice(stockPrices.stream().map(Stock::getStockPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(stockPrices.stream().count()),
                            2,
                            RoundingMode.HALF_UP));

            log.info("Stock prices for company {} retrieved", companyCode);
            log.info("MAX {}, MIN {}, AVG {}",
                    stockPriceDTO.getMaxStockPrice(),
                    stockPriceDTO.getMinStockPrice(),
                    stockPriceDTO.getAvgStockPrice());
            return stockPriceDTO;
        } catch (StockNotFoundException e) {
            log.error(e.getClass().getName() + " -- " + e.getMessage());
            throw new StockNotFoundException(e.getMessage());
        }
    }

    /**
     * Get the latest stock price for the company
     * @param companyCode
     * @return {@link Stock}'s latest stock price
     * @throws StockNotFoundException
     */
    @ApiIgnore
    @GetMapping(value = "/get/{companycode}")
    public BigDecimal getLatestStockByCompanyCode(@PathVariable("companycode") Long companyCode) throws StockNotFoundException {
        try {
            log.info("Retrieving latest stock price for company {}", companyCode);
            List<Stock> stockPrices = stockQueryService.getAllStockByCompanyCode(companyCode);
            if (stockPrices.isEmpty()) return BigDecimal.valueOf(0.0);
            return stockPrices.stream()
                    .sorted(Comparator.comparing(Stock::getStockCreatedDate, Comparator.reverseOrder()))
                    .findFirst().get().getStockPrice();
        } catch (StockNotFoundException e) {
            log.error(e.getClass().getName() + " -- " + e.getMessage());
            throw new StockNotFoundException(e.getMessage());
        }
    }
}
