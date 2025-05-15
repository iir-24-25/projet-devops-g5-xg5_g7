package com.example.backendpharmacie.controller;

import com.example.backendpharmacie.model.Fournisseur;
import com.example.backendpharmacie.service.FournisseurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fournisseurs")
public class FournisseurController {

    @Autowired
    private FournisseurService fournisseurService;

    // Endpoint pour ajouter un fournisseur
    @PostMapping
    public ResponseEntity<Fournisseur> ajouterFournisseur(@Valid @RequestBody Fournisseur fournisseur) {
        Fournisseur nouveauFournisseur = fournisseurService.ajouterFournisseur(fournisseur);
        return ResponseEntity.ok(nouveauFournisseur);
    }

    // Endpoint pour récupérer tous les fournisseurs
    @GetMapping
    public ResponseEntity<List<Fournisseur>> listerFournisseurs() {
        List<Fournisseur> fournisseurs = fournisseurService.listerFournisseurs();
        return ResponseEntity.ok(fournisseurs);
    }

    // Endpoint pour récupérer un fournisseur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Fournisseur> trouverFournisseurParId(@PathVariable Long id) {
        Optional<Fournisseur> optionalFournisseur = fournisseurService.trouverFournisseurParId(id);
        if (optionalFournisseur.isPresent()) {
            return ResponseEntity.ok(optionalFournisseur.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint pour mettre à jour un fournisseur
    @PutMapping("/{id}")
    public ResponseEntity<Fournisseur> mettreAJourFournisseur(@PathVariable Long id, @Valid @RequestBody Fournisseur fournisseurDetails) {
        Optional<Fournisseur> optionalFournisseur = fournisseurService.trouverFournisseurParId(id);
        if (optionalFournisseur.isPresent()) {
            Fournisseur fournisseur = optionalFournisseur.get();
            fournisseur.setNom(fournisseurDetails.getNom());
            fournisseur.setPrenom(fournisseurDetails.getPrenom());
            fournisseur.setEmail(fournisseurDetails.getEmail());
            fournisseur.setTelephone(fournisseurDetails.getTelephone());
            fournisseur.setAdresse(fournisseurDetails.getAdresse());

            Fournisseur fournisseurMiseAJour = fournisseurService.mettreAJourFournisseur(fournisseur);
            return ResponseEntity.ok(fournisseurMiseAJour);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint pour supprimer un fournisseur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerFournisseur(@PathVariable Long id) {
        Optional<Fournisseur> optionalFournisseur = fournisseurService.trouverFournisseurParId(id);
        if (optionalFournisseur.isPresent()) {
            fournisseurService.supprimerFournisseur(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint pour rechercher des fournisseurs par nom ou prénom
    @GetMapping(params = "search")
    public ResponseEntity<List<Fournisseur>> rechercherFournisseurs(@RequestParam String search) {
        List<Fournisseur> fournisseurs = fournisseurService.rechercherFournisseurs(search);
        return ResponseEntity.ok(fournisseurs);
    }
}