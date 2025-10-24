package net.java.guides.matching.services;

import net.java.guides.matching.dto.CandidatureDTO;
import net.java.guides.matching.entity.Candidature;
import net.java.guides.matching.entity.StatutCandidature;
import net.java.guides.matching.repositories.CandidatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidatureService {

    @Autowired
    private CandidatureRepository candidatureRepository;

    // MÉTHODES EXISTANTES
    public List<CandidatureDTO> getCandidaturesByOffre(Long offreId) {
        return candidatureRepository.findCandidaturesWithStudentInfo(offreId);
    }

    public Candidature createCandidature(Long studentId, Long offreId) {
        if (candidatureRepository.existsByStudentIdAndOffreId(studentId, offreId)) {
            throw new RuntimeException("Vous avez déjà postulé à cette offre");
        }
        Candidature candidature = new Candidature(studentId, offreId);
        return candidatureRepository.save(candidature);
    }

    public void updateStatut(Long id, StatutCandidature statut) {
        Candidature candidature = candidatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée"));
        candidature.setStatut(statut);
        candidatureRepository.save(candidature);
    }

    public int getNombreCandidatures(Long offreId) {
        return candidatureRepository.countByOffreId(offreId);
    }

    // ⭐⭐⭐ NOUVELLES MÉTHODES À AJOUTER ⭐⭐⭐

    public List<Candidature> getAllCandidatures() {
        return candidatureRepository.findAll();
    }

    public Optional<Candidature> getCandidatureById(Long id) {
        return candidatureRepository.findById(id);
    }

    public void deleteCandidature(Long id) {
        if (!candidatureRepository.existsById(id)) {
            throw new RuntimeException("Candidature non trouvée");
        }
        candidatureRepository.deleteById(id);
    }

    public List<Candidature> getCandidaturesByStudent(Long studentId) {
        return candidatureRepository.findByStudentId(studentId);
    }
}