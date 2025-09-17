package net.java.guides.matching.entity;

import jakarta.persistence.*;
import net.java.guides.matching.entity.enums.VerificationStatus;

@Entity
@Table(name = "companies")
public class Company {
    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "nom_entreprise", nullable = false)
    private String nomEntreprise;

    private String telephone;
    private String adresse;
    private String siteWeb;
    private String registreCommerce;
    private String ice;
    private String documentJustificatif;
    private String secteurActivite;
    private String description;
    private String logo;

    @Enumerated(EnumType.STRING)
    private VerificationStatus statusVerification = VerificationStatus.PENDING;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getNomEntreprise() { return nomEntreprise; }
    public void setNomEntreprise(String nomEntreprise) { this.nomEntreprise = nomEntreprise; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getSiteWeb() { return siteWeb; }
    public void setSiteWeb(String siteWeb) { this.siteWeb = siteWeb; }

    public String getRegistreCommerce() { return registreCommerce; }
    public void setRegistreCommerce(String registreCommerce) { this.registreCommerce = registreCommerce; }

    public String getIce() { return ice; }
    public void setIce(String ice) { this.ice = ice; }

    public String getDocumentJustificatif() { return documentJustificatif; }
    public void setDocumentJustificatif(String documentJustificatif) { this.documentJustificatif = documentJustificatif; }

    public String getSecteurActivite() { return secteurActivite; }
    public void setSecteurActivite(String secteurActivite) { this.secteurActivite = secteurActivite; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public VerificationStatus getStatusVerification() { return statusVerification; }
    public void setStatusVerification(VerificationStatus statusVerification) { this.statusVerification = statusVerification; }
}