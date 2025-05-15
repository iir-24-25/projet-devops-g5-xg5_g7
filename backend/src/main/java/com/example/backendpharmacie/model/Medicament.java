package com.example.backendpharmacie.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "medicament")
public class Medicament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_med", nullable = false, unique = true)
    private String codeMed;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "date_expiration", nullable = false)
    private Date dateExpiration;

    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;

    @Column(name = "stock_min", nullable = false)
    private Integer stockMin;

    @Column(name = "qte_stock", nullable = false)
    private Integer qteStock;


    @Column(name = "famille_med", nullable = false)
    private String familleMed;

    @Column(name = "image", nullable = false)
    private String image;


    // Constructeurs
    public Medicament() {}

    public Medicament(String codeMed, String libelle, Date dateExpiration,
                      Double prixUnitaire, Integer stockMin, String familleMed,
                      String image, Integer qteStock) {
        this.codeMed = codeMed;
        this.libelle = libelle;
        this.dateExpiration = dateExpiration;
        this.prixUnitaire = prixUnitaire;
        this.stockMin = stockMin;
        this.familleMed = familleMed;
        this.image = image;
        this.qteStock = qteStock;
    }

    // Getters et Setters
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

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
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

    // Getter et setter pour le nouvel attribut
    public Integer getQteStock() {
        return qteStock;
    }

    public void setQteStock(Integer qteStock) {
        this.qteStock = qteStock;
    }
}