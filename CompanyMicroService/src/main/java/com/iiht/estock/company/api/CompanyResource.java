package com.iiht.estock.company.api;

import com.iiht.estock.company.exception.CompanyNotCreatedException;
import com.iiht.estock.company.exception.CompanyNotFoundException;
import com.iiht.estock.company.exception.ExceptionResponseMessage;
import com.iiht.estock.company.model.Company;
import com.iiht.estock.company.service.CompanyService;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/market/company", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Company", description = "Company Resource")
public class CompanyResource {

    private final CompanyService companyService;


    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add a company", description = "", tags = {"Company"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Successful Operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Company.class)))),
            @ApiResponse(responseCode = "500",
                    description = "Company Not Registered",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExceptionResponseMessage.class))))
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    public Company registerCompany(@Parameter(description = "Company to add.",
            required = true, schema = @Schema(implementation = Company.class))
                                   @Valid @RequestBody Company company) throws CompanyNotCreatedException {
        try {
            log.info("adding company ... {}", company.toString());
            return companyService.addCompany(company);
        } catch (CompanyNotCreatedException e) {
            log.error(e.getClass().getName() + " -- " + e.getMessage());
            throw new CompanyNotCreatedException(e.getMessage());
        }
    }

    @GetMapping(value = "/info/{companycode}")
    @Operation(summary = "Get company information by company code", description = "", tags = {"Company"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful Operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Company.class)))),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExceptionResponseMessage.class))))
    })
    public Company getCompanyByCompanyCode(@Parameter(description = "Company Code", required = true)
                                           @PathVariable("companycode") Long companyCode) throws CompanyNotFoundException {
        try {
            log.info("Search company ... {}", companyCode);
            return companyService.getCompany(companyCode);
        } catch (CompanyNotFoundException e) {
            log.error(e.getClass().getName() + " -- " + e.getMessage());
            throw new CompanyNotFoundException(e.getMessage());
        }
    }

    @GetMapping(value = "/getall")
    @Operation(summary = "Get information of all companies", description = "", tags = {"Company"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful Operation",
                    content = @Content(array = @ArraySchema(arraySchema = @Schema(implementation = Company.class)))),
    })
    public List<Company> getAllCompanies() throws CompanyNotFoundException {
        log.info("Getting all the company records ...");
        return companyService.getAllCompanies();
    }

    @DeleteMapping(value = "/delete/{companycode}")
    @Operation(summary = "Deletes a company", description = "", tags = {"Company"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "Company not found")})
    public void deleteCompany(@PathVariable("companycode") Long companyCode) throws CompanyNotFoundException {
        log.info("Deleting company ... {}", companyCode);
        companyService.deleteCompany(companyCode);
    }
}