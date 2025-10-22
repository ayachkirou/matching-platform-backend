package net.java.guides.matching.repository;

import net.java.guides.matching.entity.Favori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriRepository extends JpaRepository<Favori, Long> {
    
    Optional<Favori> findByStudentIdAndOfferId(Long studentId, Long offerId);
    
    List<Favori> findByStudentId(Long studentId);
    
    boolean existsByStudentIdAndOfferId(Long studentId, Long offerId);
}