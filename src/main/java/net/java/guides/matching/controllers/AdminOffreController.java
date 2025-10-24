package net.java.guides.matching.controllers;

import net.java.guides.matching.entity.Offre;
import net.java.guides.matching.entity.TypeOffre;
import net.java.guides.matching.services.OffreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/offres")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminOffreController {

    private final OffreService offreService;

    @Autowired
    public AdminOffreController(OffreService offreService) {
        this.offreService = offreService;
    }

    /**
     * Récupérer toutes les offres avec pagination et tri
     */
    @GetMapping
    public ResponseEntity<?> getAllOffres(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "datePublication") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        try {
            Sort sort = sortDirection.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Offre> offresPage = offreService.getAllOffres(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("offres", offresPage.getContent());
            response.put("currentPage", offresPage.getNumber());
            response.put("totalItems", offresPage.getTotalElements());
            response.put("totalPages", offresPage.getTotalPages());
            response.put("hasNext", offresPage.hasNext());
            response.put("hasPrevious", offresPage.hasPrevious());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur lors de la récupération des offres"));
        }
    }

    /**
     * Récupérer les offres par type avec pagination
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<?> getOffresByType(
            @PathVariable String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            TypeOffre typeOffre;
            try {
                typeOffre = TypeOffre.valueOf(type.toLowerCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Type d'offre invalide: " + type));
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by("datePublication").descending());
            Page<Offre> offresPage = offreService.getOffresByType(typeOffre, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("offres", offresPage.getContent());
            response.put("currentPage", offresPage.getNumber());
            response.put("totalItems", offresPage.getTotalElements());
            response.put("totalPages", offresPage.getTotalPages());
            response.put("type", typeOffre);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur lors de la récupération des offres par type"));
        }
    }

    /**
     * Rechercher des offres par titre ou description
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchOffres(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            if (q == null || q.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Le terme de recherche ne peut pas être vide"));
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by("datePublication").descending());
            Page<Offre> offresPage = offreService.searchOffres(q.trim(), pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("offres", offresPage.getContent());
            response.put("currentPage", offresPage.getNumber());
            response.put("totalItems", offresPage.getTotalElements());
            response.put("totalPages", offresPage.getTotalPages());
            response.put("searchTerm", q);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur lors de la recherche des offres"));
        }
    }

    /**
     * Récupérer une offre spécifique par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOffreById(@PathVariable Long id) {
        try {
            Offre offre = offreService.getOffreById(id);
            return ResponseEntity.ok(offre);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur lors de la récupération de l'offre"));
        }
    }

    /**
     * Supprimer une offre
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOffre(@PathVariable Long id) {
        try {
            // Vérifier si l'offre existe avant suppression
            Offre offre = offreService.getOffreById(id);
            offreService.deleteOffre(id);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Offre supprimée avec succès");
            response.put("offreId", id);
            response.put("offreTitre", offre.getTitre());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur lors de la suppression de l'offre"));
        }
    }

    /**
     * Supprimer plusieurs offres en batch
     */
    @DeleteMapping("/batch")
    public ResponseEntity<?> deleteOffres(@RequestBody Map<String, Long[]> request) {
        try {
            Long[] offreIds = request.get("ids");
            if (offreIds == null || offreIds.length == 0) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Aucun ID d'offre fourni"));
            }

            int deletedCount = 0;
            for (Long id : offreIds) {
                try {
                    offreService.deleteOffre(id);
                    deletedCount++;
                } catch (RuntimeException e) {
                    // Log l'erreur mais continue avec les autres IDs
                    System.err.println("Erreur suppression offre " + id + ": " + e.getMessage());
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", deletedCount + " offre(s) supprimée(s) sur " + offreIds.length + " demandée(s)");
            response.put("deletedCount", deletedCount);
            response.put("requestedCount", offreIds.length);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur lors de la suppression multiple des offres"));
        }
    }

    /**
     * Récupérer les statistiques des offres
     */
    @GetMapping("/statistiques")
    public ResponseEntity<?> getStatistiques() {
        try {
            long totalOffres = offreService.getTotalOffres();
            long offresStage = offreService.countByType(TypeOffre.stage);
            long offresEmploi = offreService.countByType(TypeOffre.emploi);

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalOffres", totalOffres);
            stats.put("offresStage", offresStage);
            stats.put("offresEmploi", offresEmploi);
            stats.put("derniereMiseAJour", new java.util.Date());

            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur lors du calcul des statistiques"));
        }
    }

    /**
     * Récupérer les offres récentes (7 derniers jours)
     */
    @GetMapping("/recentes")
    public ResponseEntity<?> getOffresRecentes() {
        try {
            // Récupérer les 10 offres les plus récentes
            Pageable pageable = PageRequest.of(0, 10, Sort.by("datePublication").descending());
            Page<Offre> offresRecentes = offreService.getAllOffres(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("offres", offresRecentes.getContent());
            response.put("count", offresRecentes.getNumberOfElements());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur lors de la récupération des offres récentes"));
        }
    }

    /**
     * Méthode utilitaire pour créer des réponses d'erreur standardisées
     */
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        errorResponse.put("timestamp", new java.util.Date().toString());
        return errorResponse;
    }
}