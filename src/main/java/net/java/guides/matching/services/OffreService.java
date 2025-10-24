package net.java.guides.matching.services;

import net.java.guides.matching.entity.Offre;
import net.java.guides.matching.entity.TypeOffre;
import net.java.guides.matching.repositories.OffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OffreService {

    private final OffreRepository offreRepository;

    @Autowired
    public OffreService(OffreRepository offreRepository) {
        this.offreRepository = offreRepository;
    }

    // Récupérer toutes les offres (sans pagination)
    public List<Offre> getAllOffres() {
        return offreRepository.findAll();
    }

    // Récupérer toutes les offres avec pagination
    public Page<Offre> getAllOffres(Pageable pageable) {
        return offreRepository.findAll(pageable);
    }

    // Récupérer une offre par son ID
    public Offre getOffreById(Long id) {
        return offreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée avec l'id: " + id));
    }

    // Récupérer les offres par entreprise
    public List<Offre> getOffresByCompany(Long companyId) {
        return offreRepository.findByCompanyId(companyId);
    }

    // Récupérer les offres par entreprise avec pagination
    public Page<Offre> getOffresByCompany(Long companyId, Pageable pageable) {
        return offreRepository.findByCompanyId(companyId, pageable);
    }

    // Récupérer les offres par type
    public List<Offre> getOffresByType(TypeOffre type) {
        return offreRepository.findByTypeOffre(type);
    }

    // Récupérer les offres par type avec pagination
    public Page<Offre> getOffresByType(TypeOffre type, Pageable pageable) {
        return offreRepository.findByTypeOffre(type, pageable);
    }

    // Créer une nouvelle offre
    public Offre createOffre(Offre offre) {
        offre.setDatePublication(LocalDateTime.now());
        offre.setDateModification(LocalDateTime.now());
        return offreRepository.save(offre);
    }

    // Mettre à jour une offre existante
    public Offre updateOffre(Long id, Offre offreDetails) {
        Offre offre = getOffreById(id);

        offre.setTitre(offreDetails.getTitre());
        offre.setDescription(offreDetails.getDescription());
        offre.setCompetencesRequises(offreDetails.getCompetencesRequises());
        offre.setTypeOffre(offreDetails.getTypeOffre());
        offre.setLocalisation(offreDetails.getLocalisation());
        offre.setDateModification(LocalDateTime.now());

        return offreRepository.save(offre);
    }

    // Supprimer une offre
    public void deleteOffre(Long id) {
        Offre offre = getOffreById(id);
        offreRepository.delete(offre);
    }

    // Rechercher des offres (sans pagination)
    public List<Offre> searchOffres(String keyword) {
        return offreRepository.searchOffres(keyword);
    }

    // Rechercher des offres avec pagination
    public Page<Offre> searchOffres(String searchTerm, Pageable pageable) {
        return offreRepository.findByTitreContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                searchTerm, searchTerm, pageable);
    }

    // Compter le nombre total d'offres
    public long getTotalOffres() {
        return offreRepository.count();
    }

    // Compter les offres par type
    public long countByType(TypeOffre typeOffre) {
        return offreRepository.countByTypeOffre(typeOffre);
    }

    // Compter les offres par entreprise
    public long countByCompany(Long companyId) {
        return offreRepository.countByCompanyId(companyId);
    }

    // Récupérer les offres récentes
    public List<Offre> getRecentOffres(int limit) {
        return offreRepository.findTopNByOrderByDatePublicationDesc(limit);
    }

    // Vérifier si une offre existe
    public boolean offreExists(Long id) {
        return offreRepository.existsById(id);
    }
}