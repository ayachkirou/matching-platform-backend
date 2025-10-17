package net.java.guides.matching.controllers;

import net.java.guides.matching.entity.Offre;
import net.java.guides.matching.entity.TypeOffre;
import net.java.guides.matching.services.OffreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/offres")
@CrossOrigin(origins = "http://localhost:5173")
public class OffreController{

    @Autowired
    private OffreService offreService;

    // GET all offers
    @GetMapping
    public List<Offre> getAllOffres() {
        return offreService.getAllOffres();
    }

    // GET offer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Offre> getOffreById(@PathVariable Long id) {
        Optional<Offre> offre = offreService.getOffreById(id);
        return offre.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET offers by company
    @GetMapping("/company/{companyId}")
    public List<Offre> getOffresByCompany(@PathVariable Long companyId) {
        return offreService.getOffresByCompany(companyId);
    }

    // POST create new offer
    @PostMapping
    public Offre createOffre(@RequestBody Offre offre) {
        return offreService.createOffre(offre);
    }

    // PUT update existing offer
    @PutMapping("/{id}")
    public ResponseEntity<Offre> updateOffre(@PathVariable Long id, @RequestBody Offre offreDetails) {
        try {
            Offre updatedOffre = offreService.updateOffre(id, offreDetails);
            return ResponseEntity.ok(updatedOffre);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE offer
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOffre(@PathVariable Long id) {
        try {
            offreService.deleteOffre(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // SEARCH offers
    @GetMapping("/search")
    public List<Offre> searchOffres(@RequestParam String keyword) {
        return offreService.searchOffres(keyword);
    }

    // GET offers by type
    @GetMapping("/type/{type}")
    public List<Offre> getOffresByType(@PathVariable TypeOffre type) {
        return offreService.getOffresByType(type);
    }

    // TEST endpoint - pour vérifier que l'API fonctionne
    @GetMapping("/test")
    public String test() {
        return "API Offres fonctionne! " + new java.util.Date();
    }
}