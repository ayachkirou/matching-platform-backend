package net.java.guides.matching.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidatures")
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "offre_id", nullable = false)
    private Long offreId;

    @Column(name = "date_candidature")
    private LocalDateTime dateCandidature;

    // CORRECTION : Utiliser "en_attente" exactement comme dans la contrainte CHECK
    @Column(name = "statut")
    private String statut = "en_attente"; // ← CHANGEMENT ICI

    @Column(name = "cv")
    private String cvPath;

    @Column(name = "lettre_motivation")
    private String lettreMotivationPath;

    // Champs supplémentaires requis par le controller
    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "email")
    private String email;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "niveau_etudes")
    private String niveauEtudes;

    @Column(name = "experience")
    private String experience;

    @Column(name = "competences")
    private String competences;

    @Column(name = "date_debut")
    private String dateDebut;

    @Column(name = "pretention_salariale")
    private String pretentionSalariale;

    // Constructeur pour initialiser les valeurs par défaut
    public Candidature() {
        this.statut = "en_attente"; // Valeur exacte requise
        this.dateCandidature = LocalDateTime.now();
    }

    // Getters et setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getOffreId() { return offreId; }
    public void setOffreId(Long offreId) { this.offreId = offreId; }

    public LocalDateTime getDateCandidature() { return dateCandidature; }
    public void setDateCandidature(LocalDateTime dateCandidature) { this.dateCandidature = dateCandidature; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) {
        // Validation pour utiliser uniquement les valeurs autorisées
        if (statut != null &&
                (statut.equals("en_attente") || statut.equals("acceptee") || statut.equals("refusee"))) {
            this.statut = statut;
        } else {
            this.statut = "en_attente"; // Valeur par défaut si valeur invalide
        }
    }

    public String getCvPath() { return cvPath; }
    public void setCvPath(String cvPath) { this.cvPath = cvPath; }

    public String getLettreMotivationPath() { return lettreMotivationPath; }
    public void setLettreMotivationPath(String lettreMotivationPath) { this.lettreMotivationPath = lettreMotivationPath; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getNiveauEtudes() { return niveauEtudes; }
    public void setNiveauEtudes(String niveauEtudes) { this.niveauEtudes = niveauEtudes; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getCompetences() { return competences; }
    public void setCompetences(String competences) { this.competences = competences; }

    public String getDateDebut() { return dateDebut; }
    public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }

    public String getPretentionSalariale() { return pretentionSalariale; }
    public void setPretentionSalariale(String pretentionSalariale) {
        this.pretentionSalariale = pretentionSalariale;
    }
}