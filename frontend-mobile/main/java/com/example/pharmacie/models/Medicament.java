package com.example.pharmacie.models;

import java.io.Serializable;

public class Medicament implements Serializable {
    private Long id;
    private String codeMed;
    private String libelle;
    private String dateExpiration;
    private Double prixUnitaire;
    private Integer stockMin;
    private Integer qteStock;
    private String familleMed;
    private String image;

    // Constructeur vide (nécessaire pour la désérialisation)
    public Medicament() {}

    // Constructeur avec paramètres (CORRIGÉ)
    public Medicament(String codeMed, String libelle, String dateExpiration,
                      double prixUnitaire, int stockMin, int qteStock,
                      String familleMed) {
        this.codeMed = codeMed;
        this.libelle = libelle;
        this.dateExpiration = dateExpiration;
        this.prixUnitaire = prixUnitaire;
        this.stockMin = stockMin;
        this.qteStock = qteStock;
        this.familleMed = familleMed;
    }

    // Ajoutez ce constructeur pour les mises à jour (optionnel mais utile)
    public Medicament(Long id, String codeMed, String libelle, String dateExpiration,
                      Double prixUnitaire, Integer stockMin, Integer qteStock,
                      String familleMed, String image) {
        this.id = id;
        this.codeMed = codeMed;
        this.libelle = libelle;
        this.dateExpiration = dateExpiration;
        this.prixUnitaire = prixUnitaire;
        this.stockMin = stockMin;
        this.qteStock = qteStock;
        this.familleMed = familleMed;
        this.image = image;
    }

    // Getters et setters (inchangés)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeMed() {
        return codeMed;
    }

    public void setCodeMed(String codeMed) {
        this.codeMed = codeMed;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Integer getStockMin() {
        return stockMin;
    }

    public void setStockMin(Integer stockMin) {
        this.stockMin = stockMin;
    }

    public Integer getQteStock() {
        return qteStock;
    }

    public void setQteStock(Integer qteStock) {
        this.qteStock = qteStock;
    }

    public String getFamilleMed() {
        return familleMed;
    }

    public void setFamilleMed(String familleMed) {
        this.familleMed = familleMed;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}