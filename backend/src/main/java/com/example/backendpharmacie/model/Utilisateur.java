package com.example.backendpharmacie.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "utilisateur")
@Data // Génère automatiquement getters/setters
@NoArgsConstructor // Constructeur sans arguments
@AllArgsConstructor // Constructeur avec tous les arguments
public class Utilisateur implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Email(message = "L'email doit être valide")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @NotBlank(message = "L'adresse est obligatoire")
    @Column(name = "adresse", nullable = false)
    private String adresse;

    // Implémentation des méthodes UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return motDePasse;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Le nom est obligatoire") String getNom() {
        return nom;
    }

    public void setNom(@NotBlank(message = "Le nom est obligatoire") String nom) {
        this.nom = nom;
    }

    public @NotBlank(message = "Le prénom est obligatoire") String getPrenom() {
        return prenom;
    }

    public void setPrenom(@NotBlank(message = "Le prénom est obligatoire") String prenom) {
        this.prenom = prenom;
    }

    public @Email(message = "L'email doit être valide") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "L'email doit être valide") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Le mot de passe est obligatoire") String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(@NotBlank(message = "Le mot de passe est obligatoire") String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public @NotBlank(message = "Le téléphone est obligatoire") String getTelephone() {
        return telephone;
    }

    public void setTelephone(@NotBlank(message = "Le téléphone est obligatoire") String telephone) {
        this.telephone = telephone;
    }

    public @NotBlank(message = "L'adresse est obligatoire") String getAdresse() {
        return adresse;
    }

    public void setAdresse(@NotBlank(message = "L'adresse est obligatoire") String adresse) {
        this.adresse = adresse;
    }
}