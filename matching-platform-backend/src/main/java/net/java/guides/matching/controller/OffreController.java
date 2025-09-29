package net.java.guides.matching.controller;

import net.java.guides.matching.service.MatchingService;
import net.java.guides.matching.service.MatchingService.OffreMatchDTO;
import net.java.guides.matching.service.MatchingService.OffreResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offres")
@CrossOrigin(origins = "http://localhost:5173")
public class OffreController {

    @Autowired
    private MatchingService matchingService;

    // Endpoint pour lister toutes les offres (format pour React)
    @GetMapping
    public ResponseEntity<List<OffreResponseDTO>> getAllOffers() {
        List<OffreResponseDTO> offers = matchingService.getAllOffresForDisplay();
        return ResponseEntity.ok(offers);
    }

    // Endpoint pour le matching des offres d'un étudiant
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<OffreMatchDTO>> getMatchingOffers(
            @PathVariable Long studentId,
            @RequestParam(defaultValue = "90") double minMatch) {

        double minMatchScore = minMatch / 100.0;
        List<OffreMatchDTO> matchingOffers = matchingService.findMatchingOffersForStudent(studentId, minMatchScore);

        return ResponseEntity.ok(matchingOffers);
    }

    // Endpoint pour toutes les offres avec score de match (même faible)
    @GetMapping("/student/{studentId}/all")
    public ResponseEntity<List<OffreMatchDTO>> getAllOffersWithMatch(
            @PathVariable Long studentId) {

        List<OffreMatchDTO> allOffers = matchingService.findMatchingOffersForStudent(studentId, 0.0);
        return ResponseEntity.ok(allOffers);
    }

    // Endpoint de test
    @GetMapping("/test")
    public String testConnection() {
        return "Connexion réussie entre Spring Boot et React!";
    }
}