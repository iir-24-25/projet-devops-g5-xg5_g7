package com.example.backendpharmacie.repository;

import com.example.backendpharmacie.model.BonDeCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonDeCommandeRepository extends JpaRepository<BonDeCommande, Long> {
}