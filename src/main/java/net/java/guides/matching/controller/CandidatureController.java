package net.java.guides.matching.controller;

import net.java.guides.matching.entity.Candidature;
import net.java.guides.matching.service.CandidatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/candidatures")
@CrossOrigin(origins = "http://localhost:5173")
public class CandidatureController {

    @Autowired
    private CandidatureService candidatureService;

    private final String UPLOAD_DIR = "C:/Users/fatim/Desktop/matching-platform/uploads/";

    @PostMapping
    public ResponseEntity<?> createCandidature(
            @RequestParam("offre_id") Long offreId,
            @RequestParam("student_id") Long studentId,
            @RequestParam(value = "cv", required = false) MultipartFile cvFile,
            @RequestParam(value = "lettreMotivation", required = false) MultipartFile lettreMotivationFile,
            @RequestParam("nom") String nom,
            @RequestParam("prenom") String prenom,
            @RequestParam("email") String email,
            @RequestParam("telephone") String telephone,
            @RequestParam("adresse") String adresse,
            @RequestParam("niveau_etudes") String niveauEtudes,
            @RequestParam("experience") String experience,
            @RequestParam("competences") String competences,
            @RequestParam("date_debut") String dateDebut,
            @RequestParam(value = "pretention_salariale", required = false) String pretentionSalariale) {

        try {
            Candidature candidature = new Candidature();

            // Définir les valeurs de base avec le statut correct
            candidature.setOffreId(offreId);
            candidature.setStudentId(studentId);
            candidature.setStatut("en_attente"); // ← ICI AUSSI
            candidature.setDateCandidature(LocalDateTime.now());

            // Définir les autres champs
            candidature.setNom(nom);
            candidature.setPrenom(prenom);
            candidature.setEmail(email);
            candidature.setTelephone(telephone);
            candidature.setAdresse(adresse);
            candidature.setNiveauEtudes(niveauEtudes);
            candidature.setExperience(experience);
            candidature.setCompetences(competences);
            candidature.setDateDebut(dateDebut);
            candidature.setPretentionSalariale(pretentionSalariale);

            // Gérer les fichiers
            if (cvFile != null && !cvFile.isEmpty()) {
                String cvFileName = System.currentTimeMillis() + "_" + cvFile.getOriginalFilename();
                Path cvPath = Paths.get(UPLOAD_DIR + cvFileName);
                Files.createDirectories(cvPath.getParent());
                Files.write(cvPath, cvFile.getBytes());
                candidature.setCvPath(cvPath.toString());
            }

            if (lettreMotivationFile != null && !lettreMotivationFile.isEmpty()) {
                String lettreFileName = System.currentTimeMillis() + "_" + lettreMotivationFile.getOriginalFilename();
                Path lettrePath = Paths.get(UPLOAD_DIR + lettreFileName);
                Files.createDirectories(lettrePath.getParent());
                Files.write(lettrePath, lettreMotivationFile.getBytes());
                candidature.setLettreMotivationPath(lettrePath.toString());
            }

            Candidature savedCandidature = candidatureService.createCandidature(candidature);
            return ResponseEntity.ok(savedCandidature);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erreur lors de l'enregistrement des fichiers: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la création de la candidature: " + e.getMessage());
        }
    }
}