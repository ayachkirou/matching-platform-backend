// Fichier: src/main/java/net/java/guides/matching/controller/FiltreController.java
package net.java.guides.matching.controller;

import net.java.guides.matching.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/filters")
@CrossOrigin(origins = "http://localhost:5173")
public class FiltreController {

    @Autowired
    private OfferRepository offerRepository;

    // GET toutes les localisations distinctes
    @GetMapping("/localisations")
    public ResponseEntity<List<String>> getAllLocalisations() {
        try {
            List<String> localisations = offerRepository.findDistinctLocalisations();
            return ResponseEntity.ok(localisations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // GET tous les types d'offre distincts
    @GetMapping("/types")
    public ResponseEntity<List<String>> getAllTypes() {
        try {
            List<String> types = offerRepository.findDistinctTypeOffres();
            return ResponseEntity.ok(types);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // GET toutes les compétences distinctes (nettoyées)
    @GetMapping("/competences")
    public ResponseEntity<List<String>> getAllCompetences() {
        try {
            List<String> allCompetencesStrings = offerRepository.findAllCompetencesRequises();
            
            List<String> uniqueCompetences = allCompetencesStrings.stream()
                .flatMap(competences -> Arrays.stream(competences.split(",")))
                .map(String::trim)
                // Nettoyer les guillemets et crochets
                .map(competence -> competence.replaceAll("[\"\\[\\]]", "").trim())
                .filter(competence -> !competence.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
                
            return ResponseEntity.ok(uniqueCompetences);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}