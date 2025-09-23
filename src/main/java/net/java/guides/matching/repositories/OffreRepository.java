package net.java.guides.matching.repositories;

import net.java.guides.matching.entity.Offres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OffreRepository extends JpaRepository<Offres, Long> {
}
