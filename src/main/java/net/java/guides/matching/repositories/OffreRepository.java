package net.java.guides.matching.repositories;

import net.java.guides.matching.entity.Offre;
import net.java.guides.matching.entity.TypeOffre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreRepository extends JpaRepository<Offre, Long> {
    List<Offre> findByCompanyId(Long companyId);

    @Query("SELECT o FROM Offre o WHERE " +
            "LOWER(o.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.localisation) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Offre> searchOffres(@Param("keyword") String keyword);

    List<Offre> findByTypeOffre(TypeOffre typeOffre);
}