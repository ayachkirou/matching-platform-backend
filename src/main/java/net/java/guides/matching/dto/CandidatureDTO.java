package net.java.guides.matching.dto;

import net.java.guides.matching.entity.StatutCandidature;
import java.time.LocalDateTime;

public class CandidatureDTO {
    private Long id;
    private Long studentId;
    private Long offreId;
    private StatutCandidature statut;
    private LocalDateTime dateCandidature;
    private String nom;
    private String prenom;
    private String email;
    private String diplome;
    private String etablissement;
    private String competences;
    private String cv;

    // Constructeur par défaut
    public CandidatureDTO() {}

    // Constructeur pour la requête JPQL - AJOUTEZ CELUI-CI
    public CandidatureDTO(Long id, Long studentId, Long offreId, StatutCandidature statut,
                          LocalDateTime dateCandidature, String nom, String prenom,
                          String email, String diplome, String etablissement,
                          String competences, String cv) {
        this.id = id;
        this.studentId = studentId;
        this.offreId = offreId;
        this.statut = statut;
        this.dateCandidature = dateCandidature;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.diplome = diplome;
        this.etablissement = etablissement;
        this.competences = competences;
        this.cv = cv;
    }

    // Getters et Setters pour tous les champs
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getOffreId() { return offreId; }
    public void setOffreId(Long offreId) { this.offreId = offreId; }

    public StatutCandidature getStatut() { return statut; }
    public void setStatut(StatutCandidature statut) { this.statut = statut; }

    public LocalDateTime getDateCandidature() { return dateCandidature; }
    public void setDateCandidature(LocalDateTime dateCandidature) { this.dateCandidature = dateCandidature; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiplome() { return diplome; }
    public void setDiplome(String diplome) { this.diplome = diplome; }

    public String getEtablissement() { return etablissement; }
    public void setEtablissement(String etablissement) { this.etablissement = etablissement; }

    public String getCompetences() { return competences; }
    public void setCompetences(String competences) { this.competences = competences; }

    public String getCv() { return cv; }
    public void setCv(String cv) { this.cv = cv; }
}