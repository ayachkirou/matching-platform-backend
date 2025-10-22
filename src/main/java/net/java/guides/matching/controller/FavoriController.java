package net.java.guides.matching.controller;

import net.java.guides.matching.service.FavoriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/favoris")
@CrossOrigin(origins = "http://localhost:3000")
public class FavoriController {
    
    @Autowired
    private FavoriService favoriService;
    
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleFavori(
            @RequestParam Long studentId, 
            @RequestParam Long offerId) {
        
        boolean isFavori = favoriService.toggleFavori(studentId, offerId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("isFavori", isFavori);
        response.put("message", isFavori ? "Ajouté aux favoris" : "Retiré des favoris");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkFavori(
            @RequestParam Long studentId, 
            @RequestParam Long offerId) {
        
        boolean isFavori = favoriService.isFavori(studentId, offerId);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("isFavori", isFavori);
        
        return ResponseEntity.ok(response);
    }
}