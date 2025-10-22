package net.java.guides.matching.service;

import net.java.guides.matching.dto.OfferWithCompanyDTO;
import net.java.guides.matching.entity.Offer;
import net.java.guides.matching.entity.Company;
import net.java.guides.matching.repository.OfferRepository;
import net.java.guides.matching.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferService {

    @Autowired
    private FavoriService favoriService;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    // 🔹 Récupérer toutes les offres
    public List<Offer> getAllOffers() {
        return offerRepository.findAllByOrderByDatePublicationDesc();
    }

    // 🔹 Récupérer une offre par ID
    public Optional<Offer> getOfferById(Long id) {
        return offerRepository.findById(id);
    }

    // 🔹 Offres d’une entreprise
    public List<Offer> getOffersByCompany(Long companyId) {
        return offerRepository.findByCompanyId(companyId);
    }

    // 🔹 Recherche
    public List<Offer> searchOffers(String searchTerm) {
        return offerRepository.searchOffers(searchTerm);
    }

    // 🔹 Filtrage
    public List<Offer> filterOffers(String type, String location) {
        if (type != null && location != null) {
            return offerRepository.findByTypeOffreAndLocalisationContainingIgnoreCase(type, location);
        } else if (type != null) {
            return offerRepository.findByTypeOffre(type);
        } else if (location != null) {
            return offerRepository.findByLocalisationContainingIgnoreCase(location);
        }
        return getAllOffers();
    }

    // 🔹 Offres sur lesquelles un étudiant a postulé
    public List<Offer> getOffersAppliedByStudent(Long studentId) {
        return offerRepository.findOffersAppliedByStudent(studentId);
    }

    // 🔹 Créer une nouvelle offre
    public Offer createOffer(Offer offer) {
        offer.setDatePublication(LocalDateTime.now());
        offer.setDateModification(LocalDateTime.now());
        return offerRepository.save(offer);
    }

    // 🔹 Modifier une offre
    public Offer updateOffer(Long id, Offer offerDetails) {
        Optional<Offer> optionalOffer = offerRepository.findById(id);
        if (optionalOffer.isPresent()) {
            Offer offer = optionalOffer.get();
            offer.setTitre(offerDetails.getTitre());
            offer.setDescription(offerDetails.getDescription());
            offer.setCompetencesRequises(offerDetails.getCompetencesRequises());
            offer.setTypeOffre(offerDetails.getTypeOffre());
            offer.setLocalisation(offerDetails.getLocalisation());
            offer.setSalaire(offerDetails.getSalaire());
            offer.setDateModification(LocalDateTime.now());
            return offerRepository.save(offer);
        }
        return null;
    }

    // 🔹 Supprimer une offre
    public boolean deleteOffer(Long id) {
        if (offerRepository.existsById(id)) {
            offerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // 🔹 Obtenir le nom de l’entreprise liée à une offre
    public String getCompanyNameByOfferId(Long offerId) {
        Optional<Offer> offer = offerRepository.findById(offerId);
        if (offer.isPresent()) {
            Optional<Company> company = companyRepository.findByUserId(offer.get().getCompanyId());
            return company.map(Company::getNomEntreprise).orElse("Entreprise inconnue");
        }
        return "Entreprise inconnue";
    }

    // ✅ Version améliorée : Récupérer toutes les offres avec infos entreprise + favoris
    public List<OfferWithCompanyDTO> getAllOffersWithCompanyInfo(Long studentId) {
        List<Offer> offers = offerRepository.findAllByOrderByDatePublicationDesc();

        return offers.stream().map(offer -> {
            OfferWithCompanyDTO dto = new OfferWithCompanyDTO();

            // Copier les données de l’offre
            dto.setId(offer.getId());
            dto.setTitre(offer.getTitre());
            dto.setDescription(offer.getDescription());
            dto.setCompetencesRequises(offer.getCompetencesRequises());
            dto.setTypeOffre(offer.getTypeOffre());
            dto.setLocalisation(offer.getLocalisation());
            dto.setSalaire(offer.getSalaire());
            dto.setDatePublication(offer.getDatePublication());
            dto.setDateModification(offer.getDateModification());
            dto.setCompanyId(offer.getCompanyId());

            // 🔹 Ajouter les infos de l’entreprise
            Optional<Company> companyOpt = companyRepository.findByUserId(offer.getCompanyId());
            if (companyOpt.isPresent()) {
                Company company = companyOpt.get();
                dto.setCompanyName(company.getNomEntreprise());
                dto.setCompanyLogo(company.getLogo());
            } else {
                dto.setCompanyName("Entreprise inconnue");
                dto.setCompanyLogo(null);
            }

            // 🔹 Vérifier si l’offre est en favori pour l’étudiant
            if (studentId != null) {
                dto.setSaved(favoriService.isFavori(studentId, offer.getId()));
            } else {
                dto.setSaved(false);
            }

            return dto;
        }).collect(Collectors.toList());
    }
}
