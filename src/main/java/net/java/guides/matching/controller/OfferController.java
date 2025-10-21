package net.java.guides.matching.controller;

import net.java.guides.matching.dto.OfferWithCompanyDTO;
import net.java.guides.matching.entity.Offer;
import net.java.guides.matching.entity.Application;
import net.java.guides.matching.service.OfferService;
import net.java.guides.matching.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/offers")
@CrossOrigin(origins = "http://localhost:5173")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @Autowired
    private ApplicationService applicationService;

    // ✅ GET toutes les offres avec infos entreprise (incluant logo)
    @GetMapping("/with-company")
    public List<OfferWithCompanyDTO> getAllOffersWithCompanyInfo() {
        return offerService.getAllOffersWithCompanyInfo();
    }

    // GET toutes les offres
    @GetMapping
    public List<Offer> getAllOffers() {
        return offerService.getAllOffers();
    }

    // GET offre par ID
    @GetMapping("/{id}")
    public ResponseEntity<Offer> getOfferById(@PathVariable Long id) {
        Optional<Offer> offer = offerService.getOfferById(id);
        return offer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET offres par entreprise
    @GetMapping("/company/{companyId}")
    public List<Offer> getOffersByCompany(@PathVariable Long companyId) {
        return offerService.getOffersByCompany(companyId);
    }

    // Recherche d'offres
    @GetMapping("/search")
    public List<Offer> searchOffers(@RequestParam String q) {
        return offerService.searchOffers(q);
    }

    // Filtrage d'offres
    @GetMapping("/filter")
    public List<Offer> filterOffers(@RequestParam(required = false) String type,
                                    @RequestParam(required = false) String location) {
        return offerService.filterOffers(type, location);
    }

    // POST nouvelle offre
    @PostMapping
    public Offer createOffer(@RequestBody Offer offer) {
        return offerService.createOffer(offer);
    }

    // PUT modifier offre
    @PutMapping("/{id}")
    public ResponseEntity<Offer> updateOffer(@PathVariable Long id, @RequestBody Offer offerDetails) {
        Offer updatedOffer = offerService.updateOffer(id, offerDetails);
        if (updatedOffer != null) {
            return ResponseEntity.ok(updatedOffer);
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE offre
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        boolean deleted = offerService.deleteOffer(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // POST postuler à une offre
    @PostMapping("/{offerId}/apply/{studentId}")
    public ResponseEntity<?> applyToOffer(@PathVariable Long offerId, @PathVariable Long studentId) {
        try {
            Application application = applicationService.createApplication(studentId, offerId);
            return ResponseEntity.ok(application);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET candidatures pour une offre
    @GetMapping("/{offerId}/applications")
    public ResponseEntity<?> getOfferApplications(@PathVariable Long offerId) {
        return ResponseEntity.ok(applicationService.getApplicationsByOffer(offerId));
    }

    // GET nombre de candidatures pour une offre
    @GetMapping("/{offerId}/applications/count")
    public Long getApplicationsCount(@PathVariable Long offerId) {
        return applicationService.countApplicationsByOffer(offerId);
    }

    // GET vérifier si l'étudiant a postulé
    @GetMapping("/{offerId}/has-applied/{studentId}")
    public boolean hasApplied(@PathVariable Long offerId, @PathVariable Long studentId) {
        return applicationService.hasApplied(studentId, offerId);
    }
}
