package com.example.backendpharmacie.service;

import com.example.backendpharmacie.model.BonDeCommande;
import com.example.backendpharmacie.model.Fournisseur;
import com.example.backendpharmacie.model.Medicament;
import com.example.backendpharmacie.repository.BonDeCommandeRepository;
import com.example.backendpharmacie.repository.FournisseurRepository;
import com.example.backendpharmacie.repository.MedicamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BonDeCommandeService {

    @Autowired
    private BonDeCommandeRepository bonDeCommandeRepo;

    @Autowired
    private MedicamentRepository medicamentRepo;

    @Autowired
    private FournisseurRepository fournisseurRepo;

    public BonDeCommande saveBonDeCommande(BonDeCommande bon) {
        return bonDeCommandeRepo.save(bon);
    }

    public List<BonDeCommande> getAllBonsDeCommande() {
        return bonDeCommandeRepo.findAll();
    }

    public BonDeCommande getBonDeCommandeById(Long id) {
        return bonDeCommandeRepo.findById(id).orElse(null);
    }

    public void deleteBonDeCommande(Long id) {
        bonDeCommandeRepo.deleteById(id);
    }

    public List<Medicament> getAllMedicaments() {
        return medicamentRepo.findAll();
    }

    public List<Fournisseur> getAllFournisseurs() {
        return fournisseurRepo.findAll();
    }
}