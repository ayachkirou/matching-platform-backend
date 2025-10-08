package net.java.guides.matching.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyProfileUpdateDTO {
    private String nomEntreprise;
    private String telephone;
    private String adresse;
    private String siteWeb;
    private String registreCommerce;
    private String ice;
    private String secteurActivite;
    private String description;
}