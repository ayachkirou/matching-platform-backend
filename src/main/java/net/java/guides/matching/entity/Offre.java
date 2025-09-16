package net.java.guides.matching.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "offres")
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String competencesRequises;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeOffre typeOffre;

    private String localisation;

    @Column(name = "date_publication")
    private LocalDateTime datePublication;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    // Constructeurs
    public Offre() {
    }

    public Offre(Long companyId, String titre, String description, String competencesRequises,
                 TypeOffre typeOffre, String localisation) {
        this.companyId = companyId;
        this.titre = titre;
        this.description = description;
        this.competencesRequises = competencesRequises;
        this.typeOffre = typeOffre;
        this.localisation = localisation;
        this.datePublication = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompetencesRequises() {
        return competencesRequises;
    }

    public void setCompetencesRequises(String competencesRequises) {
        this.competencesRequises = competencesRequises;
    }

    public TypeOffre getTypeOffre() {
        return typeOffre;
    }

    public void setTypeOffre(TypeOffre typeOffre) {
        this.typeOffre = typeOffre;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public LocalDateTime getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDateTime datePublication) {
        this.datePublication = datePublication;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }
}