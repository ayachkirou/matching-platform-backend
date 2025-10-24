package net.java.guides.matching.controllers;

import net.java.guides.matching.dto.CandidatureDTO;
import net.java.guides.matching.entity.Candidature;
import net.java.guides.matching.entity.StatutCandidature;
import net.java.guides.matching.services.CandidatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidatures")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}) // ✅ Ajoutez les deux ports
public class CandidatureController {

    @Autowired
    private CandidatureService candidatureService;

    @GetMapping("/offre/{offreId}")
    public ResponseEntity<List<CandidatureDTO>> getCandidaturesByOffre(@PathVariable Long offreId) {
        try {
            List<CandidatureDTO> candidatures = candidatureService.getCandidaturesByOffre(offreId);
            return ResponseEntity.ok(candidatures);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createCandidature(
            @RequestParam Long studentId,
            @RequestParam Long offreId) {
        try {
            Candidature candidature = candidatureService.createCandidature(studentId, offreId);
            return ResponseEntity.ok(candidature);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // ✅ Retourne le message d'erreur
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur interne du serveur");
        }
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<?> updateStatut(
            @PathVariable Long id,
            @RequestParam StatutCandidature statut) {
        try {
            candidatureService.updateStatut(id, statut);
            return ResponseEntity.ok("Statut mis à jour avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // ✅ Retourne le message d'erreur
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur interne du serveur");
        }
    }

    @GetMapping("/offre/{offreId}/count")
    public ResponseEntity<Integer> getNombreCandidatures(@PathVariable Long offreId) {
        try {
            int count = candidatureService.getNombreCandidatures(offreId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ✅ AJOUTEZ CES ENDPOINTS SUPPLEMENTAIRES :

    // Récupérer toutes les candidatures (pour admin)
    @GetMapping
    public ResponseEntity<List<Candidature>> getAllCandidatures() {
        try {
            List<Candidature> candidatures = candidatureService.getAllCandidatures();
            return ResponseEntity.ok(candidatures);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Récupérer une candidature spécifique
    @GetMapping("/{id}")
    public ResponseEntity<Candidature> getCandidatureById(@PathVariable Long id) {
        try {
            Candidature candidature = candidatureService.getCandidatureById(id)
                    .orElseThrow(() -> new RuntimeException("Candidature non trouvée"));
            return ResponseEntity.ok(candidature);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Supprimer une candidature
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCandidature(@PathVariable Long id) {
        try {
            candidatureService.deleteCandidature(id);
            return ResponseEntity.ok("Candidature supprimée avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la suppression");
        }
    }

    // Récupérer les candidatures d'un étudiant
    @GetMapping("/etudiant/{studentId}")
    public ResponseEntity<List<Candidature>> getCandidaturesByStudent(@PathVariable Long studentId) {
        try {
            List<Candidature> candidatures = candidatureService.getCandidaturesByStudent(studentId);
            return ResponseEntity.ok(candidatures);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}