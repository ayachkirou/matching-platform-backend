// OffreRepository.java
package net.java.guides.matching.repository;

import net.java.guides.matching.entity.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreRepository extends JpaRepository<Offre, Long> {
    List<Offre> findByCompanyId(Long companyId);
    List<Offre> findByTitreContainingIgnoreCase(String titre);
    List<Offre> findByLocalisationContainingIgnoreCase(String localisation);
}
