package net.java.guides.matching.repository;

import net.java.guides.matching.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByUserEmail(String email);
    boolean existsByNomEntreprise(String nomEntreprise);
}