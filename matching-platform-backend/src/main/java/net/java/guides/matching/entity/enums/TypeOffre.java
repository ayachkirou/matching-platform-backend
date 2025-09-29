package net.java.guides.matching.entity.enums;

public enum TypeOffre {
    EMPLOI("Emploi"),
    STAGE("Stage"),
    FREELANCE("Freelance"),
    ALTERNANCE("Alternance"),
    TEMPS_PARTIEL("Temps partiel");

    private final String displayName;

    TypeOffre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}