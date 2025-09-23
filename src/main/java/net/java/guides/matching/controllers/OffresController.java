package net.java.guides.matching.controllers;

import net.java.guides.matching.entity.Offres;
import net.java.guides.matching.services.OffresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RequestMapping("/api/offres")

@RestController
public class OffresController {
    @Autowired
    private OffresService offresServices;
    @GetMapping
    public List<Offres>getoffres(){
        System.out.println("Endpoint /public/getoffres appelé !");

        return offresServices.getllOffres();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Offres> getOffreById(@PathVariable Long id) {
        return offresServices.getOffreById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
