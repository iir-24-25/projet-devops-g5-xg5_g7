package com.example.backendpharmacie.controller;

import com.example.backendpharmacie.model.BonDeCommande;
import com.example.backendpharmacie.model.Fournisseur;
import com.example.backendpharmacie.model.Medicament;
import com.example.backendpharmacie.service.BonDeCommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bon-de-commande")
public class BonDeCommandeController {

    @Autowired
    private BonDeCommandeService bonDeCommandeService;

    // Créer un bon de commande
    @PostMapping
    public ResponseEntity<?> createBonDeCommande(@RequestBody BonDeCommande bonDeCommande) {
        if (bonDeCommande.getMedicament() == null || bonDeCommande.getFournisseur() == null) {
            return ResponseEntity.badRequest()
                    .body("Erreur : Médicament et Fournisseur ne peuvent pas être vides.");
        }
        BonDeCommande saved = bonDeCommandeService.saveBonDeCommande(bonDeCommande);
        return ResponseEntity.ok(saved);
    }

    // Récupérer tous les bons de commande
    @GetMapping
    public ResponseEntity<List<BonDeCommande>> getAllBonsDeCommande() {
        return ResponseEntity.ok(bonDeCommandeService.getAllBonsDeCommande());
    }

    // Récupérer un bon par ID
    @GetMapping("/{id}")
    public ResponseEntity<BonDeCommande> getBonById(@PathVariable Long id) {
        BonDeCommande bon = bonDeCommandeService.getBonDeCommandeById(id);
        return bon != null ? ResponseEntity.ok(bon) : ResponseEntity.notFound().build();
    }

    // Supprimer un bon
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBon(@PathVariable Long id) {
        bonDeCommandeService.deleteBonDeCommande(id);
        return ResponseEntity.noContent().build();
    }

    // Récupérer tous les médicaments (pour liste déroulante)
    @GetMapping("/medicaments")
    public ResponseEntity<List<Medicament>> getAllMedicaments() {
        return ResponseEntity.ok(bonDeCommandeService.getAllMedicaments());
    }

    // Récupérer tous les fournisseurs (pour liste déroulante)
    @GetMapping("/fournisseurs")
    public ResponseEntity<List<Fournisseur>> getAllFournisseurs() {
        return ResponseEntity.ok(bonDeCommandeService.getAllFournisseurs());
    }
}