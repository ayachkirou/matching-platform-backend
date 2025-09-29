package net.java.guides.matching.entity;

import jakarta.persistence.*;
import net.java.guides.matching.entity.enums.TypeOffre;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "offres")
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String competencesRequises; // JSON ex: ["Java","Spring"]

    @Enumerated(EnumType.STRING)
    private TypeOffre typeOffre;

    private String localisation;

    private Timestamp datePublication;
    private Timestamp dateModification;

    // Méthode pour convertir le JSON en List<String>
    public List<String> getCompetencesList() {
        if (this.competencesRequises == null || this.competencesRequises.trim().isEmpty()) {
            return List.of();
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(this.competencesRequises, new TypeReference<List<String>>(){});
        } catch (JsonProcessingException e) {
            // En cas d'erreur de parsing JSON, retourner une liste vide
            return List.of();
        }
    }

    // Méthode pour définir les compétences depuis une List<String>
    public void setCompetencesList(List<String> competences) {
        if (competences == null || competences.isEmpty()) {
            this.competencesRequises = "[]";
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            this.competencesRequises = mapper.writeValueAsString(competences);
        } catch (JsonProcessingException e) {
            this.competencesRequises = "[]";
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCompetencesRequises() { return competencesRequises; }
    public void setCompetencesRequises(String competencesRequises) { this.competencesRequises = competencesRequises; }

    public TypeOffre getTypeOffre() { return typeOffre; }
    public void setTypeOffre(TypeOffre typeOffre) { this.typeOffre = typeOffre; }

    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }

    public Timestamp getDatePublication() { return datePublication; }
    public void setDatePublication(Timestamp datePublication) { this.datePublication = datePublication; }

    public Timestamp getDateModification() { return dateModification; }
    public void setDateModification(Timestamp dateModification) { this.dateModification = dateModification; }
}