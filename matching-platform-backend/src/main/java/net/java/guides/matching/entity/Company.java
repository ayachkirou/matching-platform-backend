package net.java.guides.matching.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "nom_entreprise", nullable = false)
    private String nomEntreprise;

    private String telephone;
    private String adresse;

    @Column(name = "site_web")
    private String siteWeb;

    @Column(name = "registre_commerce")
    private String registreCommerce;

    private String ice;

    @Column(name = "document_justificatif")
    private String documentJustificatif;

    @Column(name = "secteur_activite")
    private String secteurActivite;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String logo;

    @Column(name = "status_verification")
    private String statusVerification = "pending";

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getNomEntreprise() { return nomEntreprise; }
    public void setNomEntreprise(String nomEntreprise) { this.nomEntreprise = nomEntreprise; }

    // ... autres getters et setters

    // Méthode pour les initiales
    public String getInitials() {
        if (nomEntreprise == null || nomEntreprise.trim().isEmpty()) {
            return "CO";
        }

        String[] words = nomEntreprise.split("\\s+");
        StringBuilder initials = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty() && initials.length() < 2) {
                initials.append(Character.toUpperCase(word.charAt(0)));
            }
        }

        return initials.length() > 0 ? initials.toString() : "CO";
    }
}