package net.java.guides.matching.service;

import net.java.guides.matching.dto.CompanyRegistrationDTO;
import net.java.guides.matching.entity.Company;
import net.java.guides.matching.entity.User;
import net.java.guides.matching.entity.enums.Role;
import net.java.guides.matching.repository.CompanyRepository;
import net.java.guides.matching.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CompanyService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final String uploadDir = "uploads/";

    public CompanyService(UserRepository userRepository,
                          CompanyRepository companyRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;

        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory");
        }
    }

    @Transactional
    public Company registerCompany(CompanyRegistrationDTO dto, MultipartFile documentJustificatif, MultipartFile logo) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        if (companyRepository.existsByNomEntreprise(dto.getNomEntreprise())) {
            throw new RuntimeException("Nom d'entreprise déjà utilisé");
        }

        // Créer user
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        user.setRole(Role.COMPANY);
        user = userRepository.save(user);

        // Créer company
        Company company = new Company();
        company.setUser(user);
        company.setNomEntreprise(dto.getNomEntreprise());
        company.setTelephone(dto.getTelephone());
        company.setAdresse(dto.getAdresse());
        company.setSiteWeb(dto.getSiteWeb());
        company.setRegistreCommerce(dto.getRegistreCommerce());
        company.setIce(dto.getIce());
        company.setSecteurActivite(dto.getSecteurActivite());
        company.setDescription(dto.getDescription());

        // Gérer les fichiers
        if (documentJustificatif != null && !documentJustificatif.isEmpty()) {
            company.setDocumentJustificatif(saveFile(documentJustificatif));
        }

        if (logo != null && !logo.isEmpty()) {
            company.setLogo(saveFile(logo));
        }

        Company savedCompany = companyRepository.save(company);

        // Envoyer email de confirmation
        try {
            emailService.sendCompanyConfirmationEmail(dto.getEmail(), dto.getNomEntreprise());
        } catch (Exception e) {
            // Log l'erreur mais ne pas faire échouer l'inscription
            System.out.println("Erreur lors de l'envoi de l'email de confirmation: " + e.getMessage());
        }

        return savedCompany;
    }

    public String saveFile(MultipartFile file) {
        try {
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + filename);
            Files.copy(file.getInputStream(), path);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Could not save file: " + e.getMessage());
        }
    }

    public Resource loadFile(String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteFile(String filename) {
        try {
            Path filePath = Paths.get(uploadDir + filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + e.getMessage());
        }
    }

    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean checkCompanyNameExists(String nomEntreprise) {
        return companyRepository.existsByNomEntreprise(nomEntreprise);
    }
}