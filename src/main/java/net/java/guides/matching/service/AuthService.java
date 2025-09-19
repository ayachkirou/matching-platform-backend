package net.java.guides.matching.service;

import net.java.guides.matching.dto.LoginRequestDTO;
import net.java.guides.matching.dto.LoginResponseDTO;
import net.java.guides.matching.entity.Company;
import net.java.guides.matching.entity.Student;
import net.java.guides.matching.entity.User;
import net.java.guides.matching.entity.enums.Role;
import net.java.guides.matching.entity.enums.VerificationStatus;
import net.java.guides.matching.repository.CompanyRepository;
import net.java.guides.matching.repository.StudentRepository;
import net.java.guides.matching.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       CompanyRepository companyRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            // Authentifier l'utilisateur
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getMotDePasse()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Récupérer l'utilisateur
            User user = userRepository.findByEmail(loginRequest.getEmail());
            if (user == null) {
                throw new RuntimeException("Utilisateur non trouvé");
            }

            // Vérifier si le compte est actif
            if (!user.getActif()) {
                throw new RuntimeException("Compte désactivé");
            }

            // Générer le token JWT
            String token = jwtTokenProvider.generateToken(authentication);

            // Récupérer les informations supplémentaires selon le rôle
            String nom = null;
            String prenom = null;
            String nomEntreprise = null;
            Boolean isVerified = true;

            if (user.getRole() == Role.STUDENT) {
                Student student = studentRepository.findById(user.getId()).orElse(null);
                if (student != null) {
                    nom = student.getNom();
                    prenom = student.getPrenom();
                }
            } else if (user.getRole() == Role.COMPANY) {
                Company company = companyRepository.findById(user.getId()).orElse(null);
                if (company != null) {
                    nomEntreprise = company.getNomEntreprise();
                    isVerified = company.getStatusVerification() == VerificationStatus.VERIFIED;
                }
            }

            return new LoginResponseDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getRole(),
                    nom,
                    prenom,
                    nomEntreprise,
                    token,
                    isVerified
            );

        } catch (Exception e) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }
    }

    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }
}