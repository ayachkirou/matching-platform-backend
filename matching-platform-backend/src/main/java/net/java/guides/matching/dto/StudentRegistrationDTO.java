package net.java.guides.matching.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRegistrationDTO {
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String motDePasse;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    private String telephone;
    private String adresse;

    @NotBlank(message = "Le diplôme est obligatoire")
    private String diplome;

    @NotBlank(message = "La spécialité est obligatoire")
    private String specialite;

    @NotBlank(message = "L'établissement est obligatoire")
    private String etablissement;

    @Min(value = 1900, message = "L'année d'obtention doit être valide")
    @Max(value = 2100, message = "L'année d'obtention doit être valide")
    private int anneeObtention;

    private String competences;
    private String experiences;
    private String statut;
}