package net.java.guides.matching.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.java.guides.matching.entity.Candidature;
import net.java.guides.matching.repository.CandidatureRepository;

@Service
public class CandidatureService {

    @Autowired
    private CandidatureRepository candidatureRepository;

    private final String uploadDir = "C:/Users/fatim/Desktop/matching-platform/uploads";

    // AJOUTER CETTE MÉTHODE MANQUANTE
    public Candidature createCandidature(Candidature candidature) {
        // S'assurer que le statut est correct
        if (candidature.getStatut() == null ||
                !candidature.getStatut().equals("en_attente") &&
                        !candidature.getStatut().equals("acceptee") &&
                        !candidature.getStatut().equals("refusee")) {
            candidature.setStatut("en_attente");
        }

        // S'assurer que la date est définie
        if (candidature.getDateCandidature() == null) {
            candidature.setDateCandidature(LocalDateTime.now());
        }

        return candidatureRepository.save(candidature);
    }

    public Candidature saveCandidature(Candidature candidature, MultipartFile cv, MultipartFile lettreMotivation) throws IOException {
        // Créer le dossier s'il n'existe pas
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new IOException("Échec de la création du répertoire d'upload : " + uploadDir);
            }
        }

        // CV
        if (cv != null && !cv.isEmpty()) {
            String cvFileName = System.currentTimeMillis() + "_" + cv.getOriginalFilename();
            File cvFile = Paths.get(uploadDir, cvFileName).toFile();
            cv.transferTo(cvFile);
            candidature.setCvPath(cvFile.getAbsolutePath());
        }

        // Lettre de motivation
        if (lettreMotivation != null && !lettreMotivation.isEmpty()) {
            String lettreFileName = System.currentTimeMillis() + "_" + lettreMotivation.getOriginalFilename();
            File lettreFile = Paths.get(uploadDir, lettreFileName).toFile();
            lettreMotivation.transferTo(lettreFile);
            candidature.setLettreMotivationPath(lettreFile.getAbsolutePath());
        }

        // CORRECTION : Utiliser "en_attente" au lieu de "En attente"
        candidature.setDateCandidature(LocalDateTime.now());
        candidature.setStatut("en_attente"); // ← CHANGEMENT ICI

        return candidatureRepository.save(candidature);
    }
}