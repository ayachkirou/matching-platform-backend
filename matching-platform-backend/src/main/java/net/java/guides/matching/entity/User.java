package net.java.guides.matching.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import net.java.guides.matching.entity.enums.Role;

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

    @Column(updatable = false)
    private LocalDateTime dateCreationCompte = LocalDateTime.now();

    private Boolean actif = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public LocalDateTime getDateCreationCompte() { return dateCreationCompte; }
    public void setDateCreationCompte(LocalDateTime dateCreationCompte) { this.dateCreationCompte = dateCreationCompte; }

    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }

}
