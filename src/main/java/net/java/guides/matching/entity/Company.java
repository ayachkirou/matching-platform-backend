package net.java.guides.matching.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "companies")
public class Company {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "nom_entreprise", nullable = false)
    private String nomEntreprise;

    private String telephone;
    private String adresse;

    @Column(name = "site_web")
    private String siteWeb;

    @Column(name = "secteur_activite")
    private String secteurActivite;

    private String description;
    private String logo;

    @Column(name = "status_verification")
    private String statusVerification = "pending";

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNomEntreprise() { return nomEntreprise; }
    public void setNomEntreprise(String nomEntreprise) { this.nomEntreprise = nomEntreprise; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getSiteWeb() { return siteWeb; }
    public void setSiteWeb(String siteWeb) { this.siteWeb = siteWeb; }
    public String getSecteurActivite() { return secteurActivite; }
    public void setSecteurActivite(String secteurActivite) { this.secteurActivite = secteurActivite; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    public String getStatusVerification() { return statusVerification; }
    public void setStatusVerification(String statusVerification) { this.statusVerification = statusVerification; }
}