package net.java.guides.matching.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Companies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
@Column(nullable = false, length = 255)

private String nom_entreprise;
private String site_web;
private String registre_commerce;
private String document_justificatif;
private String description;
private String logo;
@Enumerated(EnumType.STRING)
    private status_verification statusVerification=status_verification.PENDING;
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore

    private Users user;

}
