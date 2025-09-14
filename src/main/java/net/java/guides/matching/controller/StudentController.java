package net.java.guides.matching.controller;

import net.java.guides.matching.dto.StudentRegistrationDTO;
import net.java.guides.matching.entity.Student;
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

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
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