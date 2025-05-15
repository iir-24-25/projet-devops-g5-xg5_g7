package com.example.backendpharmacie.model;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bon_de_commande")
public class BonDeCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medicament_id", nullable = false)
    private Medicament medicament;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private Fournisseur fournisseur;

    @Column(name = "date_commande", nullable = false)
    private Date dateCommande;

    @Column(name = "quantite_commandee", nullable = false)
    private Integer quantiteCommandee;

    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;

    @Column(name = "statut_commande", nullable = false)
    private String statutCommande;

    // Constructeurs
    public BonDeCommande() {}

    public BonDeCommande(Medicament medicament, Fournisseur fournisseur, Date dateCommande,
                         Integer quantiteCommandee, Double prixUnitaire, String statutCommande) {
        this.medicament = medicament;
        this.fournisseur = fournisseur;
        this.dateCommande = dateCommande;
        this.quantiteCommandee = quantiteCommandee;
        this.prixUnitaire = prixUnitaire;
        this.statutCommande = statutCommande;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Integer getQuantiteCommandee() {
        return quantiteCommandee;
    }

    public void setQuantiteCommandee(Integer quantiteCommandee) {
        this.quantiteCommandee = quantiteCommandee;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public String getStatutCommande() {
        return statutCommande;
    }

    public void setStatutCommande(String statutCommande) {
        this.statutCommande = statutCommande;
    }
}