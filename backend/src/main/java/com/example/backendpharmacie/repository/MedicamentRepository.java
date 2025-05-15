package com.example.backendpharmacie.repository;

import com.example.backendpharmacie.model.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long> {

    @Query("SELECT m FROM Medicament m WHERE LOWER(m.libelle) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Medicament> findByLibelleContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    // Trouver un médicament par son code
    Medicament findByCodeMed(String codeMed);

    // Méthode pour les alertes (date expiration proche ou stock insuffisant)
    @Query("SELECT m FROM Medicament m WHERE " +
            "CAST(m.dateExpiration AS localdate) < :currentDate OR " +
            "m.qteStock <= m.stockMin")
    List<Medicament> findAlerts(@Param("currentDate") LocalDate currentDate);
}