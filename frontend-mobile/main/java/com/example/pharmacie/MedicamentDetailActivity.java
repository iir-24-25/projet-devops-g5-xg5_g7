package com.example.pharmacie;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pharmacie.models.Medicament;
import com.example.pharmacie.retrofit.ApiClient;
import com.example.pharmacie.retrofit.MedicamentApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicamentDetailActivity extends AppCompatActivity {
    private TextView codeMedTextView, libelleTextView, dateExpirationTextView,
            prixUnitaireTextView, stockMinTextView, qteStockTextView, familleMedTextView;
    private Button editButton, deleteButton;
    private Medicament currentMedicament;
    private Long medicamentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament_detail);

        initializeViews();

        // Correction: Affectation au champ de classe
        medicamentId = getIntent().getLongExtra("MEDICAMENT_ID", -1);

        if (medicamentId == -1) {
            Toast.makeText(this, "Médicament introuvable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadMedicamentData(medicamentId);
        setupButtons();
    }


    private void initializeViews() {
        codeMedTextView = findViewById(R.id.codeMedTextView);
        libelleTextView = findViewById(R.id.libelleTextView);
        dateExpirationTextView = findViewById(R.id.dateExpirationTextView);
        prixUnitaireTextView = findViewById(R.id.prixUnitaireTextView);
        stockMinTextView = findViewById(R.id.stockMinTextView);
        qteStockTextView = findViewById(R.id.qte_stockTextView);
        familleMedTextView = findViewById(R.id.familleMedTextView);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
    }

    private void loadMedicamentData(Long id) {
        MedicamentApi api = ApiClient.getClient().create(MedicamentApi.class);
        Call<Medicament> call = api.getMedicamentById(id);

        call.enqueue(new Callback<Medicament>() {
            @Override
            public void onResponse(Call<Medicament> call, Response<Medicament> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentMedicament = response.body();
                    displayMedicamentDetails(currentMedicament);
                } else {
                    Toast.makeText(MedicamentDetailActivity.this,
                            "Erreur de chargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Medicament> call, Throwable t) {
                Toast.makeText(MedicamentDetailActivity.this,
                        "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayMedicamentDetails(Medicament medicament) {
        codeMedTextView.setText(medicament.getCodeMed());
        libelleTextView.setText(medicament.getLibelle());
        dateExpirationTextView.setText(medicament.getDateExpiration());
        prixUnitaireTextView.setText(String.valueOf(medicament.getPrixUnitaire()));
        stockMinTextView.setText(String.valueOf(medicament.getStockMin()));
        qteStockTextView.setText(String.valueOf(medicament.getQteStock()));
        familleMedTextView.setText(medicament.getFamilleMed());
    }

    private void setupButtons() {
        editButton.setOnClickListener(v -> {
            if (currentMedicament != null) {
                Intent intent = new Intent(this, AddEditMedicamentActivity.class);
                intent.putExtra("MEDICAMENT", currentMedicament);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(v -> showDeleteDialog());
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Voulez-vous vraiment supprimer ce médicament?")
                .setPositiveButton("Oui", (dialog, which) -> deleteMedicament())
                .setNegativeButton("Non", null)
                .show();
    }
    private void deleteMedicament() {
        if (medicamentId == null || medicamentId == -1) {
            Toast.makeText(this, "ID médicament invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        MedicamentApi api = ApiClient.getClient().create(MedicamentApi.class);
        Call<Void> call = api.deleteMedicament(medicamentId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MedicamentDetailActivity.this,
                            "Médicament supprimé", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(MedicamentDetailActivity.this,
                            "Erreur: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MedicamentDetailActivity.this,
                        "Échec: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (medicamentId != null && medicamentId != -1) {
            loadMedicamentData(medicamentId);
        }
    }
}