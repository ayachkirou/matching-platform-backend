package net.java.guides.matching.service;

import net.java.guides.matching.entity.Company;
import net.java.guides.matching.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public Optional<Company> getCompanyByUserId(Long userId) {
        return companyRepository.findByUserId(userId);
    }

    public Optional<Company> getCompanyByName(String nomEntreprise) {
        return companyRepository.findByNomEntreprise(nomEntreprise);
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company updateCompany(Long userId, Company companyDetails) {
        Optional<Company> optionalCompany = companyRepository.findByUserId(userId);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            company.setNomEntreprise(companyDetails.getNomEntreprise());
            company.setTelephone(companyDetails.getTelephone());
            company.setAdresse(companyDetails.getAdresse());
            company.setSiteWeb(companyDetails.getSiteWeb());
            company.setSecteurActivite(companyDetails.getSecteurActivite());
            company.setDescription(companyDetails.getDescription());
            company.setLogo(companyDetails.getLogo());
            company.setStatusVerification(companyDetails.getStatusVerification());
            return companyRepository.save(company);
        }
        return null;
    }
}