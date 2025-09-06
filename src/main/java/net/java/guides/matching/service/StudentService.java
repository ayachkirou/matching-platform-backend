package net.java.guides.matching.service;

import net.java.guides.matching.dto.StudentRegistrationDTO;
import net.java.guides.matching.entity.Student;
import net.java.guides.matching.entity.User;
import net.java.guides.matching.entity.enums.Role;
import net.java.guides.matching.repository.StudentRepository;
import net.java.guides.matching.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(UserRepository userRepository,
                          StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Student registerStudent(StudentRegistrationDTO dto) {
        // 1. Créer user
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        user.setRole(Role.STUDENT);
        userRepository.save(user);

        // 2. Créer student lié à user
        Student student = new Student();
        student.setUser(user);
        student.setNom(dto.getNom());
        student.setPrenom(dto.getPrenom());
        student.setDiplome(dto.getDiplome());
        student.setSpecialite(dto.getSpecialite());
        student.setEtablissement(dto.getEtablissement());
        student.setAnneeObtention(dto.getAnneeObtention());

        return studentRepository.save(student);
    }
}
