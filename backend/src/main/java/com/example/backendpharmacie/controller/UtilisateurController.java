package com.example.backendpharmacie.controller;

import com.example.backendpharmacie.model.Utilisateur;
import com.example.backendpharmacie.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    // Endpoint pour ajouter un utilisateur
    @PostMapping
    public ResponseEntity<Utilisateur> ajouterUtilisateur(@Valid @RequestBody Utilisateur utilisateur) {
        Utilisateur nouvelUtilisateur = utilisateurService.ajouterUtilisateur(utilisateur);
        return ResponseEntity.ok(nouvelUtilisateur);
    }

    // Endpoint pour récupérer tous les utilisateurs
    @GetMapping
    public ResponseEntity<List<Utilisateur>> listerUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.listerUtilisateurs();
        return ResponseEntity.ok(utilisateurs);
    }

    // Endpoint pour récupérer un utilisateur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> trouverUtilisateurParId(@PathVariable Long id) {
        Optional<Utilisateur> optionalUtilisateur = utilisateurService.trouverUtilisateurParId(id);
        if (optionalUtilisateur.isPresent()) {
            return ResponseEntity.ok(optionalUtilisateur.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint pour mettre à jour un utilisateur
    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> mettreAJourUtilisateur(@PathVariable Long id, @Valid @RequestBody Utilisateur utilisateurDetails) {
        Optional<Utilisateur> optionalUtilisateur = utilisateurService.trouverUtilisateurParId(id);
        if (optionalUtilisateur.isPresent()) {
            Utilisateur utilisateur = optionalUtilisateur.get();
            utilisateur.setNom(utilisateurDetails.getNom());
            utilisateur.setPrenom(utilisateurDetails.getPrenom());
            utilisateur.setEmail(utilisateurDetails.getEmail());
            utilisateur.setMotDePasse(utilisateurDetails.getMotDePasse());
            utilisateur.setTelephone(utilisateurDetails.getTelephone());
            utilisateur.setAdresse(utilisateurDetails.getAdresse());

            Utilisateur utilisateurMiseAJour = utilisateurService.mettreAJourUtilisateur(utilisateur);
            return ResponseEntity.ok(utilisateurMiseAJour);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint pour supprimer un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerUtilisateur(@PathVariable Long id) {
        Optional<Utilisateur> optionalUtilisateur = utilisateurService.trouverUtilisateurParId(id);
        if (optionalUtilisateur.isPresent()) {
            utilisateurService.supprimerUtilisateur(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(params = "search")
    public ResponseEntity<List<Utilisateur>> rechercherUtilisateurs(@RequestParam String search) {
        List<Utilisateur> utilisateurs = utilisateurService.rechercherUtilisateurs(search);
        return ResponseEntity.ok(utilisateurs);
    }
}