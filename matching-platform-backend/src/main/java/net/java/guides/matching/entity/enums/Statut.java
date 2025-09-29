package net.java.guides.matching.entity.enums;

public enum Statut {
    ETUDIANT("Étudiant"),
    DIPLOME("Diplômé"),
    EN_RECHERCHE("En recherche"),
    EN_POSTE("En poste"),
    AUTRE("Autre");

    private final String displayName;

    Statut(String displayName) {
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