package net.java.guides.matching.services;

import net.java.guides.matching.entity.Offre;
import net.java.guides.matching.entity.TypeOffre;
import net.java.guides.matching.repositories.OffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OffreService {

    @Autowired
    private OffreRepository offreRepository;

    public List<Offre> getAllOffres() {
        return offreRepository.findAll();
    }

    public Optional<Offre> getOffreById(Long id) {
        return offreRepository.findById(id);
    }

    public List<Offre> getOffresByCompany(Long companyId) {
        return offreRepository.findByCompanyId(companyId);
    }

    public Offre createOffre(Offre offre) {
        return offreRepository.save(offre);
    }

    public Offre updateOffre(Long id, Offre offreDetails) {
        Offre offre = offreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée avec l'id: " + id));

        offre.setTitre(offreDetails.getTitre());
        offre.setDescription(offreDetails.getDescription());
        offre.setCompetencesRequises(offreDetails.getCompetencesRequises());
        offre.setTypeOffre(offreDetails.getTypeOffre());
        offre.setLocalisation(offreDetails.getLocalisation());
        offre.setDateModification(java.time.LocalDateTime.now());

        return offreRepository.save(offre);
    }

    public void deleteOffre(Long id) {
        Offre offre = offreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée avec l'id: " + id));

        offreRepository.delete(offre);
    }

    public List<Offre> searchOffres(String keyword) {
        return offreRepository.searchOffres(keyword);
    }

    public List<Offre> getOffresByType(TypeOffre type) {
        return offreRepository.findByTypeOffre(type);
    }
}