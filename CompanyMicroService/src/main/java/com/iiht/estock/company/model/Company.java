package com.iiht.estock.company.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company")
@Schema(name = "Company")
public class Company {

    @Id
    @Schema(example = "111111", name = "Company Code", description = "Code of the company")
    private Long companyCode;

    @NotEmpty(message = "Company name is mandatory")
    @Schema(example = "XYZ Corp", name = "Company Name", description = " Name of the company")
    private String companyName;

    @NotEmpty(message = "Company CEO is mandatory")
    @Schema(example = "Mr. ABC", name = "Company CEO", description = "CEO of the company")
    private String companyCEO;

    @Min(value = 100000001L, message = "Company turn over must be greater than 10Cr")
    @Schema(example = "100000001", name = "Company turn over", description = "Turn over of the company")
    private BigDecimal companyTurnOver;

    @NotEmpty(message = "Company website is mandatory")
    @Schema(example = "www.xyz.com", name = "Company website", description = "Website of the company")
    private String companyWebsite;

    @NotEmpty(message = "Stock exchange in which company is enlisted in is mandatory")
    @Schema(example = "NSE", name = "Stock exchange", description = "Stock exchange of the company")
    private String stockExchange;

    @Schema(example = "0.00", name = "Latest stock price", description = "Latest stock price of the company")
    private BigDecimal latestStockPrice;
}
