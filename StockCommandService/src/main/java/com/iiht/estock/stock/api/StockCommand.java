package com.iiht.estock.stock.api;

import com.iiht.estock.stock.eventsourcing.events.AddStockEvent;
import com.iiht.estock.stock.exception.ExceptionResponseMessage;
import com.iiht.estock.stock.exception.StockNotCreatedException;
import com.iiht.estock.stock.model.Stock;
import com.iiht.estock.stock.service.StockCommandService;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/api/v1.0/market/stock")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "StockPrice", description = "Stock Price Resource")
public class StockCommand {

    private final StockCommandService stockCommandService;

    /**
     * Add a {@link Stock} based on company code
     * @param companyCode
     * @param stockPrice
     * @return
     */
    @PostMapping(value = "/add/{companycode}")
    @Operation(summary = "Add stock price by Company Code", description = "", tags = { "StockPrice" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Successful Operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AddStockEvent.class)))),
            @ApiResponse(responseCode = "500",
                    description = "Stock price not created",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExceptionResponseMessage.class))))
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    public AddStockEvent addStockPriceByCompanyCode(@Parameter(description = "Company Code", required = true)
                                                 @PathVariable("companycode") Long companyCode,
                                                    @Parameter(description="Stock Price to add.",
                                                         required=true, schema=@Schema(implementation = Stock.class))
                                                 @RequestBody Stock stockPrice) throws StockNotCreatedException {

        try {
            log.info("Adding stock price for company {}", companyCode);
            stockPrice.setCompanyCode(companyCode);
            return stockCommandService.addStock(stockPrice);
        } catch (Exception e) {
            log.error(e.getClass().getName() + " -- " + e.getMessage());
            throw new StockNotCreatedException(e.getMessage());
        }
    }

    @ApiIgnore
    @DeleteMapping(value = "/delete/{companycode}")
    public boolean deleteStockPricesByCompanyCode(@PathVariable("companycode") Long companyCode) {
        return stockCommandService.deleteAll(companyCode);
    }
}
