package net.java.guides.matching.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    private String telephone;
    private String adresse;

    @Column(updatable = false)
    private LocalDateTime dateCreationCompte = LocalDateTime.now();

    private Boolean actif = true;

    // getters & setters
}
