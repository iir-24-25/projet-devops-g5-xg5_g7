package com.example.backendpharmacie.controller;

import com.example.backendpharmacie.model.Medicament;
import com.example.backendpharmacie.repository.MedicamentRepository;
import com.example.backendpharmacie.service.MedicamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicaments")
@CrossOrigin(origins = "*")
@Validated
public class MedicamentController {

    @Autowired
    private MedicamentRepository medicamentRepository;

    private final Path storageDirectory = Paths.get("uploaded-images").toAbsolutePath().normalize();

    public MedicamentController() throws IOException {
        if (!Files.exists(storageDirectory)) {
            Files.createDirectories(storageDirectory);
        }
    }

    // Création avec image uploadée (multipart/form-data)
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Medicament> createWithImage(
            @RequestParam("codeMed") String codeMed,
            @RequestParam("libelle") String libelle,
            @RequestParam("dateExpiration") String dateExpirationStr,
            @RequestParam(value = "prixUnitaire", required = false) Double prixUnitaire,
            @RequestParam(value = "stockMin", required = false) Integer stockMin,
            @RequestParam("familleMed") String familleMed,
            @RequestParam("qteStock") Integer qteStock,
            @RequestParam("image") MultipartFile file) {

        try {
            // Nettoyage et sauvegarde du fichier
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path destinationFile = this.storageDirectory.resolve(filename);

            file.transferTo(destinationFile);

            // Conversion de la date
            LocalDate dateExpiration = LocalDate.parse(dateExpirationStr);

            // Création de l'entité
            Medicament medicament = new Medicament();
            medicament.setCodeMed(codeMed);
            medicament.setLibelle(libelle);
            medicament.setDateExpiration(Date.valueOf(dateExpiration));
            medicament.setPrixUnitaire(prixUnitaire);
            medicament.setStockMin(stockMin);
            medicament.setFamilleMed(familleMed);
            medicament.setQteStock(qteStock);
            medicament.setImage("/uploaded-images/" + filename);

            Medicament saved = medicamentRepository.save(medicament);
            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Création via JSON
    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Medicament> createWithJson(@Validated @RequestBody Medicament medicament) {
        Medicament saved = medicamentRepository.save(medicament);
        return ResponseEntity.ok(saved);
    }

    // Lister tous les médicaments
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Medicament>> getAllMedicaments() {
        List<Medicament> medicaments = medicamentRepository.findAll();
        return ResponseEntity.ok(medicaments);
    }

    // Obtenir un médicament par ID
    @GetMapping("/{id}")
    public ResponseEntity<Medicament> getMedicamentById(@PathVariable Long id) {
        Optional<Medicament> optional = medicamentRepository.findById(id);
        return optional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Mettre à jour un médicament
    @PutMapping(path = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Medicament> updateMedicament(
            @PathVariable Long id,
            @RequestParam(value = "codeMed", required = false) String codeMed,
            @RequestParam(value = "libelle", required = false) String libelle,
            @RequestParam(value = "dateExpiration", required = false) String dateExpirationStr,
            @RequestParam(value = "prixUnitaire", required = false) Double prixUnitaire,
            @RequestParam(value = "stockMin", required = false) Integer stockMin,
            @RequestParam(value = "familleMed", required = false) String familleMed,
            @RequestParam(value = "qteStock", required = false) Integer qteStock,
            @RequestParam(value = "image", required = false) MultipartFile file,
            @RequestParam(value = "existingImage", required = false) String existingImage) {

        try {
            Optional<Medicament> optional = medicamentRepository.findById(id);
            if (!optional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Medicament medicament = optional.get();

            // Mise à jour des champs
            if (codeMed != null) medicament.setCodeMed(codeMed);
            if (libelle != null) medicament.setLibelle(libelle);
            if (dateExpirationStr != null) {
                LocalDate dateExpiration = LocalDate.parse(dateExpirationStr);
                medicament.setDateExpiration(Date.valueOf(dateExpiration));
            }
            if (prixUnitaire != null) medicament.setPrixUnitaire(prixUnitaire);
            if (stockMin != null) medicament.setStockMin(stockMin);
            if (familleMed != null) medicament.setFamilleMed(familleMed);
            if (qteStock != null) medicament.setQteStock(qteStock);

            // Gestion de l'image
            if (file != null && !file.isEmpty()) {
                // Nouvelle image fournie
                String filename = StringUtils.cleanPath(file.getOriginalFilename());
                Path destinationFile = this.storageDirectory.resolve(filename);
                file.transferTo(destinationFile);
                medicament.setImage("/uploaded-images/" + filename);
            } else if (existingImage != null && !existingImage.isEmpty()) {
                // Conserver l'image existante
                medicament.setImage(existingImage);
            }
            // Si aucun des deux, l'image reste inchangée

            Medicament updated = medicamentRepository.save(medicament);
            return ResponseEntity.ok(updated);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Supprimer un médicament
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicament(@PathVariable Long id) {
        Optional<Medicament> optional = medicamentRepository.findById(id);
        if (optional.isPresent()) {
            medicamentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Télécharger une image
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) throws MalformedURLException {
        Path filePath = storageDirectory.resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint d'alertes (doublon supprimé)
    @GetMapping("/alerts")
    public ResponseEntity<List<Medicament>> getMedicamentAlerts() {
        LocalDate aujourdhui = LocalDate.now();
        List<Medicament> alertes = medicamentRepository.findAlerts(aujourdhui);
        return ResponseEntity.ok(alertes);
    }
}