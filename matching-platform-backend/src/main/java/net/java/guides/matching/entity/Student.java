package net.java.guides.matching.entity;

import jakarta.persistence.*;
import net.java.guides.matching.entity.enums.Statut;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {
    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;
    private String photoProfil;
    private String diplome;
    private String specialite;
    private String etablissement;
    private int anneeObtention;
    private String competences; // JSON string
    private String experiences;
    private String cv;

    @Enumerated(EnumType.STRING)
    private Statut statut = Statut.AUTRE;


    public List<String> getCompetencesList() {
        if (this.competences == null || this.competences.trim().isEmpty()) {
            return List.of();
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(this.competences, new TypeReference<List<String>>(){});
        } catch (JsonProcessingException e) {

            return List.of();
        }
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getPhotoProfil() { return photoProfil; }
    public void setPhotoProfil(String photoProfil) { this.photoProfil = photoProfil; }

    public String getDiplome() { return diplome; }
    public void setDiplome(String diplome) { this.diplome = diplome; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public String getEtablissement() { return etablissement; }
    public void setEtablissement(String etablissement) { this.etablissement = etablissement; }

    public int getAnneeObtention() { return anneeObtention; }
    public void setAnneeObtention(int anneeObtention) { this.anneeObtention = anneeObtention; }

    public String getCompetences() { return competences; }
    public void setCompetences(String competences) { this.competences = competences; }

    public String getExperiences() { return experiences; }
    public void setExperiences(String experiences) { this.experiences = experiences; }

    public String getCv() { return cv; }
    public void setCv(String cv) { this.cv = cv; }

    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }
}