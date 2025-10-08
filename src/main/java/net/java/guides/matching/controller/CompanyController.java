package net.java.guides.matching.controller;

import net.java.guides.matching.dto.CompanyRegistrationDTO;
import net.java.guides.matching.dto.CompanyProfileUpdateDTO;
import net.java.guides.matching.entity.Company;
import net.java.guides.matching.entity.User;
import net.java.guides.matching.repository.CompanyRepository;
import net.java.guides.matching.repository.UserRepository;
import net.java.guides.matching.service.CompanyService;
import net.java.guides.matching.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "http://localhost:5173")
public class CompanyController {
    private final CompanyService companyService;
    private final EmailService emailService;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyController(CompanyService companyService,
                             EmailService emailService,
                             CompanyRepository companyRepository,
                             UserRepository userRepository) {
        this.companyService = companyService;
        this.emailService = emailService;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    // Endpoint pour récupérer le profil de l'entreprise connectée
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentCompany(@RequestParam String email) {
        try {
            Company company = companyRepository.findByUserEmail(email);
            if (company == null) {
                return ResponseEntity.status(404).body("Entreprise non trouvée");
            }

            // Créer une réponse avec les données combinées
            Map<String, Object> response = new HashMap<>();
            response.put("id", company.getUserId());
            response.put("nomEntreprise", company.getNomEntreprise());
            response.put("telephone", company.getTelephone());
            response.put("adresse", company.getAdresse());
            response.put("siteWeb", company.getSiteWeb());
            response.put("registreCommerce", company.getRegistreCommerce());
            response.put("ice", company.getIce());
            response.put("secteurActivite", company.getSecteurActivite());
            response.put("description", company.getDescription());
            response.put("logo", company.getLogo());
            response.put("documentJustificatif", company.getDocumentJustificatif());
            response.put("statusVerification", company.getStatusVerification());

            // Ajouter les infos de l'user
            User user = company.getUser();
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

    // Endpoint pour mettre à jour le profil entreprise
    @PutMapping("/profile")
    public ResponseEntity<?> updateCompanyProfile(@RequestParam String email,
                                                  @RequestBody CompanyProfileUpdateDTO updateDTO) {
        try {
            Company company = companyRepository.findByUserEmail(email);
            if (company == null) {
                return ResponseEntity.status(404).body("Entreprise non trouvée");
            }

            // Mettre à jour les champs
            if (updateDTO.getNomEntreprise() != null) company.setNomEntreprise(updateDTO.getNomEntreprise());
            if (updateDTO.getTelephone() != null) company.setTelephone(updateDTO.getTelephone());
            if (updateDTO.getAdresse() != null) company.setAdresse(updateDTO.getAdresse());
            if (updateDTO.getSiteWeb() != null) company.setSiteWeb(updateDTO.getSiteWeb());
            if (updateDTO.getRegistreCommerce() != null) company.setRegistreCommerce(updateDTO.getRegistreCommerce());
            if (updateDTO.getIce() != null) company.setIce(updateDTO.getIce());
            if (updateDTO.getSecteurActivite() != null) company.setSecteurActivite(updateDTO.getSecteurActivite());
            if (updateDTO.getDescription() != null) company.setDescription(updateDTO.getDescription());

            // Sauvegarder les modifications
            Company updatedCompany = companyRepository.save(company);

            // Retourner la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profil mis à jour avec succès");
            response.put("company", updatedCompany);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la mise à jour du profil");
        }
    }

    // Endpoint pour uploader le logo
    @PostMapping(value = "/upload-logo", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadLogo(@RequestParam String email,
                                        @RequestParam("logo") MultipartFile logoFile) {
        try {
            Company company = companyRepository.findByUserEmail(email);
            if (company == null) {
                return ResponseEntity.status(404).body("Entreprise non trouvée");
            }

            // Vérifier le type de fichier
            if (!logoFile.getContentType().startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seules les images sont acceptées");
            }

            // Vérifier la taille du fichier (2MB max)
            if (logoFile.getSize() > 2 * 1024 * 1024) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'image ne doit pas dépasser 2MB");
            }

            // Sauvegarder le fichier
            String logoFilename = companyService.saveFile(logoFile);

            // Supprimer l'ancien logo si il existe
            if (company.getLogo() != null) {
                companyService.deleteFile(company.getLogo());
            }

            company.setLogo(logoFilename);
            companyRepository.save(company);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Logo mis à jour avec succès");
            response.put("logoFilename", logoFilename);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du téléchargement du logo");
        }
    }

    // Endpoint pour supprimer le logo
    @DeleteMapping("/delete-logo")
    public ResponseEntity<?> deleteLogo(@RequestParam String email) {
        try {
            Company company = companyRepository.findByUserEmail(email);
            if (company == null) {
                return ResponseEntity.status(404).body("Entreprise non trouvée");
            }

            // Supprimer l'ancien logo si il existe
            if (company.getLogo() != null) {
                companyService.deleteFile(company.getLogo());
                company.setLogo(null);
                companyRepository.save(company);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Logo supprimé avec succès");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression du logo");
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

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<?> registerCompany(
            @RequestPart("email") String email,
            @RequestPart("motDePasse") String motDePasse,
            @RequestPart("nomEntreprise") String nomEntreprise,
            @RequestPart(value = "telephone", required = false) String telephone,
            @RequestPart(value = "adresse", required = false) String adresse,
            @RequestPart(value = "siteWeb", required = false) String siteWeb,
            @RequestPart(value = "registreCommerce", required = false) String registreCommerce,
            @RequestPart(value = "ice", required = false) String ice,
            @RequestPart(value = "secteurActivite", required = false) String secteurActivite,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart(value = "documentJustificatif", required = false) MultipartFile documentJustificatif,
            @RequestPart(value = "logo", required = false) MultipartFile logo) {

        try {
            CompanyRegistrationDTO dto = new CompanyRegistrationDTO();
            dto.setEmail(email);
            dto.setMotDePasse(motDePasse);
            dto.setNomEntreprise(nomEntreprise);
            dto.setTelephone(telephone);
            dto.setAdresse(adresse);
            dto.setSiteWeb(siteWeb);
            dto.setRegistreCommerce(registreCommerce);
            dto.setIce(ice);
            dto.setSecteurActivite(secteurActivite);
            dto.setDescription(description);

            Company company = companyService.registerCompany(dto, documentJustificatif, logo);
            return ResponseEntity.ok(company);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'inscription");
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailExists(@RequestParam String email) {
        boolean exists = companyService.checkEmailExists(email);
        return ResponseEntity.ok().body("{\"exists\":" + exists + "}");
    }

    @GetMapping("/check-company-name")
    public ResponseEntity<?> checkCompanyNameExists(@RequestParam String nomEntreprise) {
        boolean exists = companyService.checkCompanyNameExists(nomEntreprise);
        return ResponseEntity.ok().body("{\"exists\":" + exists + "}");
    }
}