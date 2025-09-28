package net.java.guides.matching.service;

import net.java.guides.matching.dto.StudentRegistrationDTO;
import net.java.guides.matching.entity.Student;
import net.java.guides.matching.entity.User;
import net.java.guides.matching.entity.enums.Role;
import net.java.guides.matching.entity.enums.Statut;
import net.java.guides.matching.repository.StudentRepository;
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
public class StudentService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final String uploadDir = "uploads/";

    public StudentService(UserRepository userRepository,
                          StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;

        // Créer le dossier d'upload s'il n'existe pas
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory");
        }
    }

    @Transactional
    public Student registerStudent(StudentRegistrationDTO dto) {
        return registerStudent(dto, null, null);
    }

    @Transactional
    public Student registerStudent(StudentRegistrationDTO dto, MultipartFile cv, MultipartFile photoProfil) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        // 1. Créer user
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        user.setRole(Role.STUDENT);
        user = userRepository.save(user);

        // 2. Créer student lié à user
        Student student = new Student();
        student.setUser(user);
        student.setNom(dto.getNom());
        student.setPrenom(dto.getPrenom());
        student.setTelephone(dto.getTelephone());
        student.setAdresse(dto.getAdresse());
        student.setDiplome(dto.getDiplome());
        student.setSpecialite(dto.getSpecialite());
        student.setEtablissement(dto.getEtablissement());
        student.setAnneeObtention(dto.getAnneeObtention());
        student.setCompetences(dto.getCompetences());
        student.setExperiences(dto.getExperiences());

        // Gérer les fichiers
        if (cv != null && !cv.isEmpty()) {
            student.setCv(saveFile(cv));
        }

        if (photoProfil != null && !photoProfil.isEmpty()) {
            student.setPhotoProfil(saveFile(photoProfil));
        }

        // Gérer le statut
        if (dto.getStatut() != null) {
            try {
                student.setStatut(Statut.valueOf(dto.getStatut().toUpperCase()));
            } catch (IllegalArgumentException e) {
                student.setStatut(Statut.AUTRE);
            }
        }

        return studentRepository.save(student);
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
}