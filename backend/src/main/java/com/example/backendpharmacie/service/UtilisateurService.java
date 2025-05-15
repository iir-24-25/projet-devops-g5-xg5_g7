package com.example.backendpharmacie.service;

import com.example.backendpharmacie.model.Utilisateur;
import com.example.backendpharmacie.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // ✅ Injection réussie via Spring

    public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
        // 🔐 Hacher le mot de passe avant sauvegarde
        String rawPassword = utilisateur.getMotDePasse();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        utilisateur.setMotDePasse(encodedPassword);

        return utilisateurRepository.save(utilisateur);
    }

    // Récupérer tous les utilisateurs
    public List<Utilisateur> listerUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    // Récupérer un utilisateur par son ID
    public Optional<Utilisateur> trouverUtilisateurParId(Long id) {
        return utilisateurRepository.findById(id);
    }

    // Mettre à jour un utilisateur
    public Utilisateur mettreAJourUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    // Supprimer un utilisateur
    public void supprimerUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }

    public List<Utilisateur> rechercherUtilisateurs(String searchTerm) {
        return utilisateurRepository.findByNomOrPrenomContainingIgnoreCase(searchTerm);
    }
}