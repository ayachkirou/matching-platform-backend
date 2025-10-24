package net.java.guides.matching.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidatures")
public class Candidature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private Long offreId;

    @Enumerated(EnumType.STRING)
    private StatutCandidature statut = StatutCandidature.EN_ATTENTE;

    private LocalDateTime dateCandidature = LocalDateTime.now();

    // Constructeurs
    public Candidature() {}

    public Candidature(Long studentId, Long offreId) {
        this.studentId = studentId;
        this.offreId = offreId;
    }

    // Getters et Setters
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
}