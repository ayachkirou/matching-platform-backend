package net.java.guides.matching.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {
    @Id
    private Long userId;

    private String diplome;
    private String specialite;
    private String etablissement;
    private Integer anneeObtention;

    @Column(columnDefinition = "TEXT")
    private String competences;

    @Column(columnDefinition = "TEXT")
    private String experiences;

    private String cv;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('recherche_stage','recherche_emploi','autre')")
    private StatutRecherche statut = StatutRecherche.autre;

    @OneToOne
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    // Constructeurs
    public Student() {}

    public Student(User user) {
        this.user = user;
    }

    // Getters et Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getDiplome() { return diplome; }
    public void setDiplome(String diplome) { this.diplome = diplome; }
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    public String getEtablissement() { return etablissement; }
    public void setEtablissement(String etablissement) { this.etablissement = etablissement; }
    public Integer getAnneeObtention() { return anneeObtention; }
    public void setAnneeObtention(Integer anneeObtention) { this.anneeObtention = anneeObtention; }
    public String getCompetences() { return competences; }
    public void setCompetences(String competences) { this.competences = competences; }
    public String getExperiences() { return experiences; }
    public void setExperiences(String experiences) { this.experiences = experiences; }
    public String getCv() { return cv; }
    public void setCv(String cv) { this.cv = cv; }
    public StatutRecherche getStatut() { return statut; }
    public void setStatut(StatutRecherche statut) { this.statut = statut; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}