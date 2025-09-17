package net.java.guides.matching.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRegistrationDTO {
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String motDePasse;

    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    private String nomEntreprise;

    private String telephone;
    private String adresse;
    private String siteWeb;
    private String registreCommerce;
    private String ice;
    private String secteurActivite;
    private String description;
}