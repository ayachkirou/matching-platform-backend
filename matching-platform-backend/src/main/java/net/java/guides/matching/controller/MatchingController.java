package net.java.guides.matching.controller;

import net.java.guides.matching.service.MatchingService;
import net.java.guides.matching.service.MatchingService.OffreMatchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/matching")
@CrossOrigin(origins = "http://localhost:5173")
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<OffreMatchDTO>> getMatchingOffers(
            @PathVariable Long studentId,
            @RequestParam(defaultValue = "0.9") double minMatch) {

        // Convertir le pourcentage (90%) en décimal (0.9)
        double minMatchScore = minMatch / 100.0;

        List<OffreMatchDTO> matchingOffers = matchingService.findMatchingOffersForStudent(studentId, minMatchScore);

        return ResponseEntity.ok(matchingOffers);
    }

    @GetMapping("/student/{studentId}/all")
    public ResponseEntity<List<OffreMatchDTO>> getAllOffersWithMatch(
            @PathVariable Long studentId) {

        List<OffreMatchDTO> allOffers = matchingService.findMatchingOffersForStudent(studentId, 0.0);

        return ResponseEntity.ok(allOffers);
    }
}