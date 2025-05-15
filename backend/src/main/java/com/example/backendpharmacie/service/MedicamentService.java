package com.example.backendpharmacie.service;

import com.example.backendpharmacie.model.Medicament;
import com.example.backendpharmacie.repository.MedicamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MedicamentService {

    @Autowired
    private MedicamentRepository medicamentRepository;

    // Ajouter un médicament
    public Medicament ajouterMedicament(Medicament medicament) {
        return medicamentRepository.save(medicament);
    }

    // Récupérer tous les médicaments
    public List<Medicament> listerMedicaments() {
        return medicamentRepository.findAll();
    }

    // Récupérer un médicament par son ID
    public Optional<Medicament> trouverMedicamentParId(Long id) {
        return medicamentRepository.findById(id);
    }

    // Mettre à jour un médicament
    public Medicament mettreAJourMedicament(Medicament medicament) {
        return medicamentRepository.save(medicament);
    }

    // Supprimer un médicament
    public void supprimerMedicament(Long id) {
        medicamentRepository.deleteById(id);
    }

    public List<Medicament> rechercherMedicaments(String searchTerm) {
        return medicamentRepository.findByLibelleContainingIgnoreCase(searchTerm);
    }

}