package net.java.guides.matching.repository;

import net.java.guides.matching.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByStudentId(Long studentId);

    List<Application> findByOfferId(Long offerId);

    Optional<Application> findByStudentIdAndOfferId(Long studentId, Long offerId);

    Long countByOfferId(Long offerId);

    Long countByStudentId(Long studentId);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Application a WHERE a.studentId = :studentId AND a.offerId = :offerId")
    boolean existsByStudentIdAndOfferId(@Param("studentId") Long studentId, @Param("offerId") Long offerId);

    @Query("SELECT a FROM Application a WHERE a.studentId = :studentId ORDER BY a.dateCandidature DESC")
    List<Application> findRecentApplicationsByStudent(@Param("studentId") Long studentId);
}