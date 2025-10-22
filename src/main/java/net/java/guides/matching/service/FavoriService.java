package net.java.guides.matching.service;

import net.java.guides.matching.entity.Favori;
import net.java.guides.matching.repository.FavoriRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriService {
    
    @Autowired
    private FavoriRepository favoriRepository;
    
    public boolean toggleFavori(Long studentId, Long offerId) {
        Optional<Favori> existingFavori = favoriRepository.findByStudentIdAndOfferId(studentId, offerId);
        
        if (existingFavori.isPresent()) {
            // Si existe, on supprime (retirer des favoris)
            favoriRepository.delete(existingFavori.get());
            return false; // Favori retiré
        } else {
            // Si n'existe pas, on ajoute
            Favori newFavori = new Favori(studentId, offerId);
            favoriRepository.save(newFavori);
            return true; // Favori ajouté
        }
    }
    
    public boolean isFavori(Long studentId, Long offerId) {
        return favoriRepository.existsByStudentIdAndOfferId(studentId, offerId);
    }
    
    public List<Favori> getFavorisByStudent(Long studentId) {
        return favoriRepository.findByStudentId(studentId);
    }
}