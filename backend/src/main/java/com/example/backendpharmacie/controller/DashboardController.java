package com.example.backendpharmacie.controller;

import com.example.backendpharmacie.model.Medicament;
import com.example.backendpharmacie.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private BonDeCommandeRepository bonDeCommandeRepository;

    // Compter les médicaments
    @GetMapping("/medicaments/count")
    public Long countMedicaments() {
        return medicamentRepository.count();
    }

    // Compter les fournisseurs
    @GetMapping("/fournisseurs/count")
    public Long countFournisseurs() {
        return fournisseurRepository.count();
    }

    // Compter les utilisateurs (ajuster selon votre entité)
    @GetMapping("/utilisateurs/count")
    public Long countUtilisateurs() {
        return utilisateurRepository.count();
    }

    // Compter les bons de commande
    @GetMapping("/bon-de-commande/count")
    public Long countBonsDeCommande() {
        return bonDeCommandeRepository.count();
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<Medicament>> getMedicamentAlerts() {
        LocalDate aujourdhui = LocalDate.now();
        List<Medicament> alertes = medicamentRepository.findAlerts(aujourdhui);
        return ResponseEntity.ok(alertes);
    }

}