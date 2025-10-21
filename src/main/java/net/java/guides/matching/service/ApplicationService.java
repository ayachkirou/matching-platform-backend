package net.java.guides.matching.service;

import net.java.guides.matching.entity.Application;
import net.java.guides.matching.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public List<Application> getApplicationsByStudent(Long studentId) {
        return applicationRepository.findByStudentId(studentId);
    }

    public List<Application> getApplicationsByOffer(Long offerId) {
        return applicationRepository.findByOfferId(offerId);
    }

    public List<Application> getRecentApplicationsByStudent(Long studentId) {
        return applicationRepository.findRecentApplicationsByStudent(studentId);
    }

    public Application createApplication(Long studentId, Long offerId) {
        if (applicationRepository.existsByStudentIdAndOfferId(studentId, offerId)) {
            throw new RuntimeException("Vous avez déjà postulé à cette offre");
        }

        Application application = new Application();
        application.setStudentId(studentId);
        application.setOfferId(offerId);
        application.setDateCandidature(LocalDateTime.now());
        return applicationRepository.save(application);
    }

    public Application updateApplicationStatus(Long applicationId, String status) {
        Optional<Application> optionalApplication = applicationRepository.findById(applicationId);
        if (optionalApplication.isPresent()) {
            Application application = optionalApplication.get();
            application.setStatut(status);
            return applicationRepository.save(application);
        }
        return null;
    }

    public Long countApplicationsByOffer(Long offerId) {
        return applicationRepository.countByOfferId(offerId);
    }

    public Long countApplicationsByStudent(Long studentId) {
        return applicationRepository.countByStudentId(studentId);
    }

    public boolean hasApplied(Long studentId, Long offerId) {
        return applicationRepository.existsByStudentIdAndOfferId(studentId, offerId);
    }

    public Optional<Application> getApplicationDetails(Long studentId, Long offerId) {
        return applicationRepository.findByStudentIdAndOfferId(studentId, offerId);
    }
}