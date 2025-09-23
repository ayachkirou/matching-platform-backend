package net.java.guides.matching.services;

import net.java.guides.matching.entity.Offres;
import net.java.guides.matching.repositories.OffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class OffresService {
    @Autowired
    private OffreRepository offreRepository;
    public List<Offres> getllOffres(){
        return offreRepository.findAll();
    }
    public Optional<Offres> getOffreById(Long id) {
        return offreRepository.findById(id);
    }
}
