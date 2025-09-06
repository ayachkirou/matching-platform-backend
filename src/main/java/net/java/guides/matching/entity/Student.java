package net.java.guides.matching.entity;

import jakarta.persistence.*;
import net.java.guides.matching.entity.enums.Statut;

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

    // getters & setters
}
