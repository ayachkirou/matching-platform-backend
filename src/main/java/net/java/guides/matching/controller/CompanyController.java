package net.java.guides.matching.controller;

import net.java.guides.matching.dto.CompanyRegistrationDTO;
import net.java.guides.matching.entity.Company;
import net.java.guides.matching.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import net.java.guides.matching.service.EmailService;



@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "http://localhost:5173")
public class CompanyController {
    private final CompanyService companyService;
    private final EmailService emailService;

    public CompanyController(CompanyService companyService, EmailService emailService) {
        this.companyService = companyService;
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