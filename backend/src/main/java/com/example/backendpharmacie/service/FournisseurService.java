package com.example.backendpharmacie.service;

import com.example.backendpharmacie.model.Fournisseur;
import com.example.backendpharmacie.repository.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FournisseurService {

    @Autowired
    private FournisseurRepository fournisseurRepository;

    // Ajouter un fournisseur
    public Fournisseur ajouterFournisseur(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    // Récupérer tous les fournisseurs
    public List<Fournisseur> listerFournisseurs() {
        return fournisseurRepository.findAll();
    }

    // Récupérer un fournisseur par son ID
    public Optional<Fournisseur> trouverFournisseurParId(Long id) {
        return fournisseurRepository.findById(id);
    }

    // Mettre à jour un fournisseur
    public Fournisseur mettreAJourFournisseur(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    // Supprimer un fournisseur
    public void supprimerFournisseur(Long id) {
        fournisseurRepository.deleteById(id);
    }

    // Rechercher des fournisseurs par nom ou prénom
    public List<Fournisseur> rechercherFournisseurs(String searchTerm) {
        return fournisseurRepository.findByNomOrPrenomContainingIgnoreCase(searchTerm);
    }


}