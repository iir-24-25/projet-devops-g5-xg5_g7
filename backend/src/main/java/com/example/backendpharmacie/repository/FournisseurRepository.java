package com.example.backendpharmacie.repository;

import com.example.backendpharmacie.model.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {

    // Recherche par nom ou prénom (insensible à la casse)
    @Query("SELECT f FROM Fournisseur f WHERE LOWER(f.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(f.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Fournisseur> findByNomOrPrenomContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    // Recherche par email
    Fournisseur findByEmail(String email);
}