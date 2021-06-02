package com.iiht.estock.stock.model;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document(collection = "stock")
@Schema(name = "Stock")
public class Stock {

    @Id
    @ApiModelProperty(hidden = true)
    @Schema(example = "1", name = "Stock id", description = "id of the stock price")
    private String stockId;

    @Digits(integer = 10, fraction = 2)
    @Schema(example = "00.00", name = "Stock price", description = "stock price")
    private BigDecimal stockPrice;

    @Indexed
    @Schema(example = "2021-04-22", name = "Stock created date",
            description = "creation date of the stock price",
            format = "string",
            pattern = "^\\d{4}-\\d{2}-\\d{2}$")
    private LocalDateTime stockCreatedDate;

    @Indexed
    @Schema(example = "11111", name = "Company code", description = "company code of the company")
    private Long companyCode;

    @ApiModelProperty(hidden = true)
    private boolean isDeleted;

}
