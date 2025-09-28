package net.java.guides.matching.controller;

import net.java.guides.matching.dto.StudentRegistrationDTO;
import net.java.guides.matching.entity.Student;
import net.java.guides.matching.entity.User;
import net.java.guides.matching.entity.enums.Statut;
import net.java.guides.matching.repository.StudentRepository;
import net.java.guides.matching.repository.UserRepository;
import net.java.guides.matching.service.EmailService;
import net.java.guides.matching.service.StudentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {
    private final StudentService studentService;
    private final EmailService emailService;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    // Modifiez le constructeur pour inclure les nouveaux repositories
    public StudentController(StudentService studentService,
                             EmailService emailService,
                             StudentRepository studentRepository,
                             UserRepository userRepository) {
        this.studentService = studentService;
        this.emailService = emailService;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    // Endpoint pour récupérer le profil de l'étudiant connecté
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentStudent(@RequestParam String email) {
        try {
            Student student = studentRepository.findByUserEmail(email);
            if (student == null) {
                return ResponseEntity.status(404).body("Étudiant non trouvé");
            }

            // Créer une réponse avec les données combinées
            Map<String, Object> response = new HashMap<>();
            response.put("id", student.getUserId());
            response.put("nom", student.getNom());
            response.put("prenom", student.getPrenom());
            response.put("telephone", student.getTelephone());
            response.put("adresse", student.getAdresse());
            response.put("photoProfil", student.getPhotoProfil());
            response.put("diplome", student.getDiplome());
            response.put("specialite", student.getSpecialite());
            response.put("etablissement", student.getEtablissement());
            response.put("anneeObtention", student.getAnneeObtention());
            response.put("competences", student.getCompetences());
            response.put("experiences", student.getExperiences());
            response.put("cv", student.getCv());
            response.put("statut", student.getStatut());

            // Ajouter les infos de l'user
            User user = student.getUser();
            if (user != null) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("email", user.getEmail());
                userInfo.put("role", user.getRole());
                userInfo.put("dateCreationCompte", user.getDateCreationCompte());
                response.put("user", userInfo);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la récupération du profil");
        }
    }

    // Endpoint pour mettre à jour le profil étudiant
    @PutMapping("/profile")
    public ResponseEntity<?> updateStudentProfile(@RequestParam String email,
                                                  @RequestBody StudentProfileUpdateDTO updateDTO) {
        try {
            Student student = studentRepository.findByUserEmail(email);
            if (student == null) {
                return ResponseEntity.status(404).body("Étudiant non trouvé");
            }

            // Mettre à jour les champs
            if (updateDTO.getNom() != null) student.setNom(updateDTO.getNom());
            if (updateDTO.getPrenom() != null) student.setPrenom(updateDTO.getPrenom());
            if (updateDTO.getTelephone() != null) student.setTelephone(updateDTO.getTelephone());
            if (updateDTO.getAdresse() != null) student.setAdresse(updateDTO.getAdresse());
            if (updateDTO.getDiplome() != null) student.setDiplome(updateDTO.getDiplome());
            if (updateDTO.getSpecialite() != null) student.setSpecialite(updateDTO.getSpecialite());
            if (updateDTO.getEtablissement() != null) student.setEtablissement(updateDTO.getEtablissement());
            if (updateDTO.getAnneeObtention() != null) student.setAnneeObtention(updateDTO.getAnneeObtention());
            if (updateDTO.getCompetences() != null) student.setCompetences(updateDTO.getCompetences());
            if (updateDTO.getExperiences() != null) student.setExperiences(updateDTO.getExperiences());

            // Gérer le statut avec conversion depuis String vers Enum
            if (updateDTO.getStatut() != null) {
                try {
                    student.setStatut(Statut.valueOf(updateDTO.getStatut().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    student.setStatut(Statut.AUTRE);
                }
            }

            // Sauvegarder les modifications
            Student updatedStudent = studentRepository.save(student);

            // Retourner la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profil mis à jour avec succès");
            response.put("student", updatedStudent);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la mise à jour du profil");
        }
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
            // DTO avec ces données
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

    // DTO pour la mise à jour du profil
    public static class StudentProfileUpdateDTO {
        private String nom;
        private String prenom;
        private String telephone;
        private String adresse;
        private String diplome;
        private String specialite;
        private String etablissement;
        private Integer anneeObtention;
        private String competences;
        private String experiences;
        private String statut;

        // Getters et setters
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }

        public String getPrenom() { return prenom; }
        public void setPrenom(String prenom) { this.prenom = prenom; }

        public String getTelephone() { return telephone; }
        public void setTelephone(String telephone) { this.telephone = telephone; }

        public String getAdresse() { return adresse; }
        public void setAdresse(String adresse) { this.adresse = adresse; }

        public String getDiplome() { return diplome; }
        public void setDiplome(String diplome) { this.diplome = diplome; }

        public String getSpecialite() { return specialite; }
        public void setSpecialite(String specialite) { this.specialite = specialite; }

        public String getEtablissement() { return etablissement; }
        public void setEtablissement(String etablissement) { this.etablissement = etablissement; }

        public Integer getAnneeObtention() { return anneeObtention; }
        public void setAnneeObtention(Integer anneeObtention) { this.anneeObtention = anneeObtention; }

        public String getCompetences() { return competences; }
        public void setCompetences(String competences) { this.competences = competences; }

        public String getExperiences() { return experiences; }
        public void setExperiences(String experiences) { this.experiences = experiences; }

        public String getStatut() { return statut; }
        public void setStatut(String statut) { this.statut = statut; }
    }

    @PostMapping(value = "/upload-cv", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadCV(@RequestParam String email,
                                      @RequestParam("cv") MultipartFile cvFile) {
        try {
            Student student = studentRepository.findByUserEmail(email);
            if (student == null) {
                return ResponseEntity.status(404).body("Étudiant non trouvé");
            }

            // Vérifier le type de fichier
            if (!cvFile.getContentType().equals("application/pdf")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seuls les fichiers PDF sont acceptés");
            }

            // Vérifier la taille du fichier (5MB max)
            if (cvFile.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le fichier ne doit pas dépasser 5MB");
            }

            // Sauvegarder le fichier
            String cvFilename = studentService.saveFile(cvFile);
            student.setCv(cvFilename);
            studentRepository.save(student);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "CV téléchargé avec succès");
            response.put("cvFilename", cvFilename);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du téléchargement du CV");
        }
    }

    @PostMapping(value = "/upload-photo", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadPhotoProfil(@RequestParam String email,
                                               @RequestParam("photoProfil") MultipartFile photoFile) {
        try {
            Student student = studentRepository.findByUserEmail(email);
            if (student == null) {
                return ResponseEntity.status(404).body("Étudiant non trouvé");
            }

            // Vérifier le type de fichier
            if (!photoFile.getContentType().startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seules les images sont acceptées");
            }

            // Vérifier la taille du fichier (2MB max)
            if (photoFile.getSize() > 2 * 1024 * 1024) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'image ne doit pas dépasser 2MB");
            }

            // Sauvegarder le fichier
            String photoFilename = studentService.saveFile(photoFile);
            student.setPhotoProfil(photoFilename);
            studentRepository.save(student);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Photo de profil mise à jour avec succès");
            response.put("photoProfilFilename", photoFilename);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du téléchargement de la photo");
        }
    }

    @GetMapping("/download-cv/{filename:.+}")
    public ResponseEntity<Resource> downloadCV(@PathVariable String filename) {
        try {
            // Nettoyer le filename pour sécurité
            String cleanFilename = filename.replace("..", "").replace("/", "");

            Resource resource = studentService.loadFile(cleanFilename);

            String contentType = "application/pdf";
            String originalFilename = cleanFilename.substring(cleanFilename.indexOf("_") + 1);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + originalFilename + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/delete-photo")
    public ResponseEntity<?> deletePhotoProfil(@RequestParam String email,
                                               @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Vérifier le token
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Token manquant ou invalide");
            }

            Student student = studentRepository.findByUserEmail(email);
            if (student == null) {
                return ResponseEntity.status(404).body("Étudiant non trouvé");
            }

            // Supprimer l'ancienne photo si elle existe
            if (student.getPhotoProfil() != null) {
                studentService.deleteFile(student.getPhotoProfil());
                student.setPhotoProfil(null);
                studentRepository.save(student);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Photo de profil supprimée avec succès");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression de la photo");
        }
    }
}