package net.java.guides.matching.repositories;

import net.java.guides.matching.entity.Candidature;
import net.java.guides.matching.dto.CandidatureDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {

    // Méthode pour vérifier si une candidature existe déjà
    boolean existsByStudentIdAndOffreId(Long studentId, Long offreId);

    // Méthode pour compter les candidatures par offre
    int countByOffreId(Long offreId);

    // Méthode pour trouver les candidatures par offre avec les infos étudiant
    @Query("SELECT new net.java.guides.matching.dto.CandidatureDTO(c.id, c.studentId, c.offreId, c.statut, c.dateCandidature, u.nom, u.prenom, u.email, s.diplome, s.etablissement, s.competences, s.cv) " +
            "FROM Candidature c JOIN Student s ON c.studentId = s.userId JOIN User u ON s.userId = u.id WHERE c.offreId = :offreId")
    List<CandidatureDTO> findCandidaturesWithStudentInfo(@Param("offreId") Long offreId);

    // Optionnel: méthodes supplémentaires utiles
    List<Candidature> findByStudentId(Long studentId);
    List<Candidature> findByOffreId(Long offreId);
    Optional<Candidature> findByStudentIdAndOffreId(Long studentId, Long offreId);
}