package net.java.guides.matching.dto;

import lombok.Getter;
import lombok.Setter;
import net.java.guides.matching.entity.enums.Role;

@Getter
@Setter
public class LoginResponseDTO {
    private Long id;
    private String email;
    private Role role;
    private String nom;
    private String prenom;
    private String nomEntreprise;
    private String token;
    private Boolean isVerified;

    public LoginResponseDTO(Long id, String email, Role role, String nom, String prenom,
                            String nomEntreprise, String token, Boolean isVerified) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.nom = nom;
        this.prenom = prenom;
        this.nomEntreprise = nomEntreprise;
        this.token = token;
        this.isVerified = isVerified;
    }
}