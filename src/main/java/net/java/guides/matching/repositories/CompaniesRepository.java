package net.java.guides.matching.repositories;

import net.java.guides.matching.entity.Companies;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompaniesRepository extends JpaRepository<Companies,Long> {
}
