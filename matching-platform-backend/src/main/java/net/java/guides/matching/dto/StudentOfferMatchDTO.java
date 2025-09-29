package net.java.guides.matching.dto;

public class StudentOfferMatchDTO {
    private Long offreId;
    private String titre;
    private String description;
    private String companyName;
    private String typeOffre;
    private String localisation;
    private Double matchPercentage;
    private String competencesRequises;

    public StudentOfferMatchDTO() {}

    public StudentOfferMatchDTO(Long offreId, String titre, String description, String companyName,
                                String typeOffre, String localisation, Double matchPercentage,
                                String competencesRequises) {
        this.offreId = offreId;
        this.titre = titre;
        this.description = description;
        this.companyName = companyName;
        this.typeOffre = typeOffre;
        this.localisation = localisation;
        this.matchPercentage = matchPercentage;
        this.competencesRequises = competencesRequises;
    }

    // Getters and Setters
    public Long getOffreId() { return offreId; }
    public void setOffreId(Long offreId) { this.offreId = offreId; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getTypeOffre() { return typeOffre; }
    public void setTypeOffre(String typeOffre) { this.typeOffre = typeOffre; }

    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }

    public Double getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(Double matchPercentage) { this.matchPercentage = matchPercentage; }

    public String getCompetencesRequises() { return competencesRequises; }
    public void setCompetencesRequises(String competencesRequises) { this.competencesRequises = competencesRequises; }
}