package com.example.pharmacie;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pharmacie.models.Medicament;
import com.example.pharmacie.retrofit.MedicamentApi;
import com.example.pharmacie.retrofit.ApiClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditMedicamentActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText codeMedEditText, libelleEditText, dateExpirationEditText, prixUnitaireEditText,
            stockMinEditText, qteStockEditText, familleMedEditText;
    private ImageView medicamentImageView;
    private Button selectImageButton, saveButton;
    private Uri imageUri;
    private Medicament medicamentToEdit;
    private Calendar expirationCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_medicament);

        initializeViews();
        setupDatePicker();
        setupImageSelection();
        checkForEditMode();
        setupSaveButton();
    }

    private void initializeViews() {
        codeMedEditText = findViewById(R.id.codeMedEditText);
        libelleEditText = findViewById(R.id.libelleEditText);
        dateExpirationEditText = findViewById(R.id.dateExpirationEditText);
        prixUnitaireEditText = findViewById(R.id.prixUnitaireEditText);
        stockMinEditText = findViewById(R.id.stockMinEditText);
        qteStockEditText = findViewById(R.id.qte_stockEditText);
        familleMedEditText = findViewById(R.id.familleMedEditText);
        medicamentImageView = findViewById(R.id.medicamentImageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        saveButton = findViewById(R.id.saveButton);
    }

    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                expirationCalendar.set(Calendar.YEAR, year);
                expirationCalendar.set(Calendar.MONTH, month);
                expirationCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };

        dateExpirationEditText.setOnClickListener(v -> new DatePickerDialog(
                AddEditMedicamentActivity.this,
                dateSetListener,
                expirationCalendar.get(Calendar.YEAR),
                expirationCalendar.get(Calendar.MONTH),
                expirationCalendar.get(Calendar.DAY_OF_MONTH)
        ).show());
    }

    private void updateDateLabel() {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        dateExpirationEditText.setText(sdf.format(expirationCalendar.getTime()));
    }

    private void setupImageSelection() {
        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            medicamentImageView.setImageURI(imageUri);
        }
    }

    private void checkForEditMode() {
        try {
            if (getIntent().hasExtra("MEDICAMENT")) {
                medicamentToEdit = (Medicament) getIntent().getSerializableExtra("MEDICAMENT");
                if (medicamentToEdit != null) {
                    Log.d("EditMode", "Médicament reçu - ID: " + medicamentToEdit.getId());
                    populateFields();
                    saveButton.setText("Modifier");
                }
            }
        } catch (Exception e) {
            Log.e("EditMode", "Erreur lors de la récupération du médicament", e);
        }
    }

    private void populateFields() {
        if (medicamentToEdit == null) {
            Log.e("EditMode", "medicamentToEdit est null");
            return;
        }

        try {
            codeMedEditText.setText(medicamentToEdit.getCodeMed() != null ? medicamentToEdit.getCodeMed() : "");
            libelleEditText.setText(medicamentToEdit.getLibelle() != null ? medicamentToEdit.getLibelle() : "");
            dateExpirationEditText.setText(medicamentToEdit.getDateExpiration() != null ? medicamentToEdit.getDateExpiration() : "");

            if (medicamentToEdit.getPrixUnitaire() != null) {
                prixUnitaireEditText.setText(String.valueOf(medicamentToEdit.getPrixUnitaire()));
            }

            if (medicamentToEdit.getStockMin() != null) {
                stockMinEditText.setText(String.valueOf(medicamentToEdit.getStockMin()));
            }

            if (medicamentToEdit.getQteStock() != null) {
                qteStockEditText.setText(String.valueOf(medicamentToEdit.getQteStock()));
            }

            familleMedEditText.setText(medicamentToEdit.getFamilleMed() != null ? medicamentToEdit.getFamilleMed() : "");

        } catch (Exception e) {
            Log.e("EditMode", "Erreur lors du remplissage des champs", e);
        }
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> {
            if (validateFields()) {
                if (medicamentToEdit != null) {
                    updateMedicament();
                } else {
                    if (imageUri != null) {
                        uploadMedicamentWithImage();
                    } else {
                        uploadMedicamentWithoutImage();
                    }
                }
            }
        });
    }

    private boolean validateFields() {
        if (codeMedEditText.getText().toString().trim().isEmpty()) {
            codeMedEditText.setError("Code médicament requis");
            return false;
        }
        if (libelleEditText.getText().toString().trim().isEmpty()) {
            libelleEditText.setError("Libellé requis");
            return false;
        }
        if (dateExpirationEditText.getText().toString().trim().isEmpty()) {
            dateExpirationEditText.setError("Date d'expiration requise");
            return false;
        }
        if (familleMedEditText.getText().toString().trim().isEmpty()) {
            familleMedEditText.setError("Famille médicament requise");
            return false;
        }
        return true;
    }

    private void uploadMedicamentWithImage() {
        try {
            String filePath = FileUtils.getPath(this, imageUri);
            if (filePath == null) {
                Toast.makeText(this, "Impossible de récupérer le fichier", Toast.LENGTH_SHORT).show();
                return;
            }
            File file = new File(filePath);

            RequestBody codeMedPart = RequestBody.create(MediaType.parse("text/plain"), codeMedEditText.getText().toString());
            RequestBody libellePart = RequestBody.create(MediaType.parse("text/plain"), libelleEditText.getText().toString());
            RequestBody dateExpPart = RequestBody.create(MediaType.parse("text/plain"), dateExpirationEditText.getText().toString());
            RequestBody prixUnitPart = RequestBody.create(MediaType.parse("text/plain"), prixUnitaireEditText.getText().toString());
            RequestBody stockMinPart = RequestBody.create(MediaType.parse("text/plain"), stockMinEditText.getText().toString());
            RequestBody qteStockPart = RequestBody.create(MediaType.parse("text/plain"), qteStockEditText.getText().toString());
            RequestBody famillePart = RequestBody.create(MediaType.parse("text/plain"), familleMedEditText.getText().toString());

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            MedicamentApi api = ApiClient.getClient().create(MedicamentApi.class);
            Call<Medicament> call = api.uploadMedicament(
                    codeMedPart, libellePart, dateExpPart, prixUnitPart,
                    stockMinPart, qteStockPart, famillePart, imagePart
            );

            call.enqueue(new Callback<Medicament>() {
                @Override
                public void onResponse(Call<Medicament> call, Response<Medicament> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddEditMedicamentActivity.this, "Ajouté avec succès", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        try {
                            String error = response.errorBody().string();
                            Log.e("API ERROR", error);
                            Toast.makeText(AddEditMedicamentActivity.this, "Erreur: " + error, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Toast.makeText(AddEditMedicamentActivity.this, "Erreur inconnue", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Medicament> call, Throwable t) {
                    Toast.makeText(AddEditMedicamentActivity.this, "Échec: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Erreur interne: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    private void uploadMedicamentWithoutImage() {
        Medicament medicament = new Medicament();
        medicament.setCodeMed(codeMedEditText.getText().toString());
        medicament.setLibelle(libelleEditText.getText().toString());
        medicament.setDateExpiration(dateExpirationEditText.getText().toString());
        medicament.setPrixUnitaire(Double.parseDouble(prixUnitaireEditText.getText().toString()));
        medicament.setStockMin(Integer.parseInt(stockMinEditText.getText().toString()));
        medicament.setQteStock(Integer.parseInt(qteStockEditText.getText().toString()));
        medicament.setFamilleMed(familleMedEditText.getText().toString());

        MedicamentApi api = ApiClient.getClient().create(MedicamentApi.class);
        Call<Medicament> call = api.createMedicament(medicament);

        call.enqueue(new Callback<Medicament>() {
            @Override
            public void onResponse(Call<Medicament> call, Response<Medicament> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddEditMedicamentActivity.this, "Médicament ajouté avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddEditMedicamentActivity.this, "Erreur: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Medicament> call, Throwable t) {
                Toast.makeText(AddEditMedicamentActivity.this, "Échec: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMedicament() {
        Medicament medicament = new Medicament();
        medicament.setId(medicamentToEdit.getId());
        medicament.setCodeMed(codeMedEditText.getText().toString());
        medicament.setLibelle(libelleEditText.getText().toString());
        medicament.setDateExpiration(dateExpirationEditText.getText().toString());
        medicament.setPrixUnitaire(Double.parseDouble(prixUnitaireEditText.getText().toString()));
        medicament.setStockMin(Integer.parseInt(stockMinEditText.getText().toString()));
        medicament.setQteStock(Integer.parseInt(qteStockEditText.getText().toString()));
        medicament.setFamilleMed(familleMedEditText.getText().toString());

        // Conserver l'image existante si aucune nouvelle image n'est sélectionnée
        if (imageUri != null) {
            // TODO: Implémenter la mise à jour de l'image
            // Pour l'instant, on garde l'ancienne image
            medicament.setImage(medicamentToEdit.getImage());
        } else {
            medicament.setImage(medicamentToEdit.getImage());
        }

        MedicamentApi api = ApiClient.getClient().create(MedicamentApi.class);
        Call<Medicament> call = api.updateMedicament(medicamentToEdit.getId(), medicament);

        call.enqueue(new Callback<Medicament>() {
            @Override
            public void onResponse(Call<Medicament> call, Response<Medicament> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddEditMedicamentActivity.this, "Médicament modifié avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddEditMedicamentActivity.this, "Erreur: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Medicament> call, Throwable t) {
                Toast.makeText(AddEditMedicamentActivity.this, "Échec: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}