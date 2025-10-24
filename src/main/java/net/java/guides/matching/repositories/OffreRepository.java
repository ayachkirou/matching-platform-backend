package net.java.guides.matching.repositories;

import net.java.guides.matching.entity.Offre;
import net.java.guides.matching.entity.TypeOffre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreRepository extends JpaRepository<Offre, Long> {

    // Trouver les offres par entreprise
    List<Offre> findByCompanyId(Long companyId);

    // Trouver les offres par entreprise avec pagination
    Page<Offre> findByCompanyId(Long companyId, Pageable pageable);

    // Trouver les offres par type
    List<Offre> findByTypeOffre(TypeOffre typeOffre);

    // Trouver les offres par type avec pagination
    Page<Offre> findByTypeOffre(TypeOffre typeOffre, Pageable pageable);

    // Recherche d'offres par mot-clé (sans pagination)
    @Query("SELECT o FROM Offre o WHERE " +
            "LOWER(o.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.localisation) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Offre> searchOffres(@Param("keyword") String keyword);

    // Recherche d'offres par mot-clé avec pagination
    @Query("SELECT o FROM Offre o WHERE " +
            "LOWER(o.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.localisation) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Offre> searchOffres(@Param("keyword") String keyword, Pageable pageable);

    // Recherche avec méthodes de dérivation (plus simple)
    Page<Offre> findByTitreContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titre, String description, Pageable pageable);

    // Recherche par titre uniquement
    Page<Offre> findByTitreContainingIgnoreCase(String titre, Pageable pageable);

    // Recherche par localisation
    Page<Offre> findByLocalisationContainingIgnoreCase(String localisation, Pageable pageable);

    // Compter le nombre d'offres par type
    long countByTypeOffre(TypeOffre typeOffre);

    // Compter le nombre d'offres par entreprise
    long countByCompanyId(Long companyId);

    // Trouver les offres les plus récentes
    List<Offre> findTop10ByOrderByDatePublicationDesc();

    // Trouver les N offres les plus récentes
    List<Offre> findTopNByOrderByDatePublicationDesc(int n);

    // Vérifier si une offre existe pour une entreprise
    boolean existsByCompanyIdAndTitre(Long companyId, String titre);

    // Trouver les offres par type et entreprise
    List<Offre> findByTypeOffreAndCompanyId(TypeOffre typeOffre, Long companyId);

    // Trouver les offres par type et entreprise avec pagination
    Page<Offre> findByTypeOffreAndCompanyId(TypeOffre typeOffre, Long companyId, Pageable pageable);
}