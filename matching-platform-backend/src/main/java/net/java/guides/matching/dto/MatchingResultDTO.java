package net.java.guides.matching.dto;

public class MatchingResultDTO {
    private Long offreId;
    private String titre;
    private String companyName;
    private Double matchPercentage;
    private String description;

    public MatchingResultDTO() {}

    public MatchingResultDTO(Long offreId, String titre, String companyName, Double matchPercentage, String description) {
        this.offreId = offreId;
        this.titre = titre;
        this.companyName = companyName;
        this.matchPercentage = matchPercentage;
        this.description = description;
    }

    // Getters and Setters
    public Long getOffreId() { return offreId; }
    public void setOffreId(Long offreId) { this.offreId = offreId; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public Double getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(Double matchPercentage) { this.matchPercentage = matchPercentage; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}