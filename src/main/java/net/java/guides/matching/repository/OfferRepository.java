package net.java.guides.matching.repository;

import net.java.guides.matching.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByCompanyId(Long companyId);
    List<Offer> findByTypeOffre(String typeOffre);
    List<Offer> findByLocalisationContainingIgnoreCase(String localisation);

    @Query("SELECT o FROM Offer o WHERE LOWER(o.titre) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(o.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Offer> searchOffers(@Param("search") String search);

    List<Offer> findByTypeOffreAndLocalisationContainingIgnoreCase(String typeOffre, String localisation);
    List<Offer> findAllByOrderByDatePublicationDesc();

    @Query("SELECT o FROM Offer o WHERE o.id IN (SELECT a.offerId FROM Application a WHERE a.studentId = :studentId)")
    List<Offer> findOffersAppliedByStudent(@Param("studentId") Long studentId);

    // ✅ NOUVELLE MÉTHODE : Recherche avancée
    @Query("SELECT o FROM Offer o WHERE " +
           "(:searchTerm IS NULL OR :searchTerm = '' OR " +
           " LOWER(o.titre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           " LOWER(o.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           " LOWER(o.competencesRequises) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           " LOWER(o.localisation) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND (:type IS NULL OR :type = '' OR o.typeOffre = :type) " +
           "AND (:localisation IS NULL OR :localisation = '' OR LOWER(o.localisation) LIKE LOWER(CONCAT('%', :localisation, '%'))) " +
           "AND (:competence IS NULL OR :competence = '' OR LOWER(o.competencesRequises) LIKE LOWER(CONCAT('%', :competence, '%'))) " +
           "ORDER BY o.datePublication DESC")
    List<Offer> searchOffersAdvanced(
            @Param("searchTerm") String searchTerm,
            @Param("type") String type,
            @Param("localisation") String localisation,
            @Param("competence") String competence);

    // ✅ AJOUTEZ CES 3 METHODES ICI (à la fin, avant la fermeture de l'interface)
    @Query("SELECT DISTINCT o.localisation FROM Offer o WHERE o.localisation IS NOT NULL AND o.localisation != '' ORDER BY o.localisation")
    List<String> findDistinctLocalisations();

    @Query("SELECT DISTINCT o.typeOffre FROM Offer o WHERE o.typeOffre IS NOT NULL AND o.typeOffre != '' ORDER BY o.typeOffre")
    List<String> findDistinctTypeOffres();

    @Query("SELECT o.competencesRequises FROM Offer o WHERE o.competencesRequises IS NOT NULL AND o.competencesRequises != ''")
    List<String> findAllCompetencesRequises();
}