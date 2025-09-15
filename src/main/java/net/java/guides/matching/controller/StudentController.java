package net.java.guides.matching.controller;

import net.java.guides.matching.dto.StudentRegistrationDTO;
import net.java.guides.matching.entity.Student;
import net.java.guides.matching.service.EmailService;
import net.java.guides.matching.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {
    private final StudentService studentService;
    private final EmailService emailService;

    public StudentController(StudentService studentService, EmailService emailService) {
        this.studentService = studentService;
        this.emailService = emailService;
    }

    // Endpoint pour envoyer le code de vérification
    @PostMapping("/send-verification")
    public ResponseEntity<?> sendVerificationCode(@RequestParam String email) {
        try {
            String code = emailService.generateVerificationCode();
            emailService.sendVerificationEmail(email, code);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'envoi de l'email de vérification");
        }
    }

    // Endpoint pour vérifier le code
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean isValid = emailService.verifyCode(email, code);
        if (isValid) {
            emailService.removeCode(email);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Code de vérification invalide");
        }
    }

    // Endpoint pour les données JSON (sans fichiers)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody StudentRegistrationDTO dto) {
        try {
            Student student = studentService.registerStudent(dto);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'inscription");
        }
    }

    // Nouveau endpoint pour les formulaires avec fichiers
    @PostMapping(value = "/register-with-files", consumes = "multipart/form-data")
    public ResponseEntity<?> registerWithFiles(
            @RequestPart("email") String email,
            @RequestPart("motDePasse") String motDePasse,
            @RequestPart("nom") String nom,
            @RequestPart("prenom") String prenom,
            @RequestPart(value = "telephone", required = false) String telephone,
            @RequestPart(value = "adresse", required = false) String adresse,
            @RequestPart("diplome") String diplome,
            @RequestPart("specialite") String specialite,
            @RequestPart("etablissement") String etablissement,
            @RequestPart("anneeObtention") String anneeObtention,
            @RequestPart(value = "competences", required = false) String competences,
            @RequestPart(value = "experiences", required = false) String experiences,
            @RequestPart(value = "statut", required = false) String statut,
            @RequestPart(value = "cv", required = false) MultipartFile cv,
            @RequestPart(value = "photoProfil", required = false) MultipartFile photoProfil) {

        try {
            // Créez un DTO avec ces données
            StudentRegistrationDTO dto = new StudentRegistrationDTO();
            dto.setEmail(email);
            dto.setMotDePasse(motDePasse);
            dto.setNom(nom);
            dto.setPrenom(prenom);
            dto.setTelephone(telephone);
            dto.setAdresse(adresse);
            dto.setDiplome(diplome);
            dto.setSpecialite(specialite);
            dto.setEtablissement(etablissement);
            dto.setAnneeObtention(Integer.parseInt(anneeObtention));
            dto.setCompetences(competences);
            dto.setExperiences(experiences);
            dto.setStatut(statut);

            Student student = studentService.registerStudent(dto, cv, photoProfil);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'inscription");
        }
    }
}