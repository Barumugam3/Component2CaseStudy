package com.iiht.estock.company.service;

import com.iiht.estock.company.exception.CompanyNotCreatedException;
import com.iiht.estock.company.exception.CompanyNotFoundException;
import com.iiht.estock.company.model.Company;
import com.iiht.estock.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    private final RestTemplate restTemplate;

    @Value(value = "#{environment['STOCK_QUERY_SERVICE'] ?: 'http://localhost:8091'}")
    private String stockQueryServiceEndpoint;

    @Value(value = "#{environment['STOCK_COMMAND_SERVICE'] ?: 'http://localhost:8090'}")
    private String stockCommandServiceEndpoint;

    @Override
    public Company addCompany(Company company) throws CompanyNotCreatedException {
        try {
            log.info("adding company in db ... {}", company.toString());
            return companyRepository.save(company);
        } catch (Exception e) {
            log.error(e.getClass() + " -- " + e.getMessage());
            throw new CompanyNotCreatedException(e.getMessage());
        }
    }

    @Override
    public Company getCompany(Long companyCode) throws CompanyNotFoundException {
        try {
            log.info("Searching company in db ... {}", companyCode);
            Optional<Company> optionalCompany = companyRepository.findById(companyCode);
            Company company = optionalCompany.orElseThrow(() -> new CompanyNotFoundException("Company Code " + companyCode + " not found."));
            log.info("Searched company in db ... {}", company.toString());
            company.setLatestStockPrice(getLatestStockPrice(companyCode));
            return company;
        } catch (Exception e) {
            log.error(e.getClass() + " -- " + e.getMessage());
            throw new CompanyNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        for (Company company : companies) {
            company.setLatestStockPrice(getLatestStockPrice(company.getCompanyCode()));
        }
        return companies;
    }

    @Override
    public void deleteCompany(Long companyCode) throws CompanyNotFoundException {
        Optional<Company> optionalCompany = companyRepository.findById(companyCode);
        Company company = optionalCompany.orElseThrow(() -> new CompanyNotFoundException("Company Code " + companyCode + " not found."));
        restTemplate.delete(stockCommandServiceEndpoint + "/api/v1.0/market/stock/delete/" + companyCode);
        companyRepository.delete(company);
    }

    private BigDecimal getLatestStockPrice(Long companyCode) {
        log.info("Calling stock query service to get latest stock price for company {}", companyCode);
        return restTemplate.getForEntity(stockQueryServiceEndpoint + "/api/v1.0/market/stock/get/" + companyCode,
                BigDecimal.class).getBody();
    }
}
