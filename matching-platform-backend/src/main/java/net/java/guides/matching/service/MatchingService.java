package net.java.guides.matching.service;

import net.java.guides.matching.entity.Company;
import net.java.guides.matching.entity.Offre;
import net.java.guides.matching.entity.Student;
import net.java.guides.matching.repository.CompanyRepository;
import net.java.guides.matching.repository.OffreRepository;
import net.java.guides.matching.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    @Autowired
    private OffreRepository offreRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public List<OffreMatchDTO> findMatchingOffersForStudent(Long studentId, double minMatchScore) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Student student = studentOpt.get();
        List<String> studentCompetences = student.getCompetencesList();

        List<Offre> allOffres = offreRepository.findAll();

        return allOffres.stream()
                .map(offre -> {
                    double matchScore = calculateCosineSimilarity(studentCompetences, offre.getCompetencesList());
                    return new OffreMatchDTO(offre, matchScore);
                })
                .filter(offreMatch -> offreMatch.getMatchScore() >= minMatchScore)
                .sorted((o1, o2) -> Double.compare(o2.getMatchScore(), o1.getMatchScore()))
                .collect(Collectors.toList());
    }

    public List<OffreResponseDTO> getAllOffresForDisplay() {
        List<Offre> offres = offreRepository.findAll();

        return offres.stream()
                .map(offre -> {
                    Company company = companyRepository.findById(offre.getCompanyId()).orElse(null);
                    return new OffreResponseDTO(
                            offre.getId(),
                            company != null ? company.getInitials() : "CO",
                            offre.getTitre(),
                            company != null ? company.getNomEntreprise() : "Entreprise",
                            offre.getTypeOffre() != null ? offre.getTypeOffre().toString() : "Stage",
                            "4000-6000 MAD", // À adapter selon vos données réelles
                            offre.getLocalisation(),
                            calculateTimeAgo(offre.getDatePublication()),
                            offre.getDescription(),
                            offre.getCompetencesList(),
                            false // saved par défaut
                    );
                })
                .collect(Collectors.toList());
    }

    private String calculateTimeAgo(Timestamp date) {
        if (date == null) {
            return "Date inconnue";
        }

        LocalDateTime publicationDate = date.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(publicationDate, now);

        long days = duration.toDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes();

        if (days > 0) {
            return "Il y a " + days + " jour" + (days > 1 ? "s" : "");
        } else if (hours > 0) {
            return "Il y a " + hours + " heure" + (hours > 1 ? "s" : "");
        } else {
            return "Il y a " + minutes + " minute" + (minutes > 1 ? "s" : "");
        }
    }

    public double calculateCosineSimilarity(List<String> competences1, List<String> competences2) {
        if (competences1 == null || competences2 == null ||
                competences1.isEmpty() || competences2.isEmpty()) {
            return 0.0;
        }

        Set<String> allCompetences = new HashSet<>();
        allCompetences.addAll(competences1);
        allCompetences.addAll(competences2);

        List<String> competencesList = new ArrayList<>(allCompetences);
        double[] vector1 = createVector(competences1, competencesList);
        double[] vector2 = createVector(competences2, competencesList);

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            norm1 += Math.pow(vector1[i], 2);
            norm2 += Math.pow(vector2[i], 2);
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private double[] createVector(List<String> competences, List<String> allCompetences) {
        double[] vector = new double[allCompetences.size()];

        for (int i = 0; i < allCompetences.size(); i++) {
            String competence = allCompetences.get(i);
            long count = competences.stream()
                    .filter(c -> c.equalsIgnoreCase(competence))
                    .count();
            vector[i] = count;
        }

        return vector;
    }

    // DTOs (inchangés)
    public static class OffreMatchDTO {
        private Offre offre;
        private double matchScore;
        private String matchPercentage;

        public OffreMatchDTO(Offre offre, double matchScore) {
            this.offre = offre;
            this.matchScore = matchScore;
            this.matchPercentage = String.format("%.0f%%", matchScore * 100);
        }

        public Offre getOffre() { return offre; }
        public double getMatchScore() { return matchScore; }
        public String getMatchPercentage() { return matchPercentage; }
    }

    public static class OffreResponseDTO {
        private Long id;
        private String companyInitials;
        private String title;
        private String company;
        private String type;
        private String salary;
        private String location;
        private String time;
        private String description;
        private List<String> skills;
        private boolean saved;

        public OffreResponseDTO(Long id, String companyInitials, String title, String company,
                                String type, String salary, String location, String time,
                                String description, List<String> skills, boolean saved) {
            this.id = id;
            this.companyInitials = companyInitials;
            this.title = title;
            this.company = company;
            this.type = type;
            this.salary = salary;
            this.location = location;
            this.time = time;
            this.description = description;
            this.skills = skills;
            this.saved = saved;
        }

        // Getters
        public Long getId() { return id; }
        public String getCompanyInitials() { return companyInitials; }
        public String getTitle() { return title; }
        public String getCompany() { return company; }
        public String getType() { return type; }
        public String getSalary() { return salary; }
        public String getLocation() { return location; }
        public String getTime() { return time; }
        public String getDescription() { return description; }
        public List<String> getSkills() { return skills; }
        public boolean isSaved() { return saved; }
    }
}