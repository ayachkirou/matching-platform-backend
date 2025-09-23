package net.java.guides.matching.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated (EnumType.STRING)
    @Column(nullable = false)
    private  role role;
    @Column(nullable = false, length = 255)
    private String email;
    @Column(name = "mot_de_passe")
    private String motDePasse;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "adresse")
    private String adresse;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_creation_compte")
    private Date dateCreationCompte;


    private boolean actif;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Companies company;

}
