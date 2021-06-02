package com.iiht.estock.company.service;

import com.iiht.estock.company.exception.CompanyNotCreatedException;
import com.iiht.estock.company.exception.CompanyNotFoundException;
import com.iiht.estock.company.model.Company;

import java.util.List;

public interface CompanyService {

    Company addCompany(Company company) throws CompanyNotCreatedException;

    Company getCompany(Long companyCode) throws CompanyNotFoundException;

    List<Company> getAllCompanies() throws CompanyNotFoundException;

    void deleteCompany(Long companyCode) throws CompanyNotFoundException;
}
