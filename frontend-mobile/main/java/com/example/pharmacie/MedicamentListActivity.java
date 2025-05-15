package com.example.pharmacie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pharmacie.models.Medicament;
import com.example.pharmacie.retrofit.ApiClient;
import com.example.pharmacie.retrofit.MedicamentApi;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicamentListActivity extends AppCompatActivity {
    private ListView listView;
    private List<Medicament> medicaments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament_list);

        listView = findViewById(R.id.medicamentListView);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Medicament selectedMedicament = medicaments.get(position);
            Intent intent = new Intent(MedicamentListActivity.this, MedicamentDetailActivity.class);
            intent.putExtra("MEDICAMENT_ID", selectedMedicament.getId());
            startActivity(intent);
        });

        loadMedicaments();
    }

    private void loadMedicaments() {
        MedicamentApi api = ApiClient.getClient().create(MedicamentApi.class);
        Call<List<Medicament>> call = api.getAllMedicaments();

        call.enqueue(new Callback<List<Medicament>>() {
            @Override
            public void onResponse(Call<List<Medicament>> call, Response<List<Medicament>> response) {
                if (response.isSuccessful()) {
                    medicaments = response.body();
                    displayMedicaments(medicaments);
                } else {
                    Toast.makeText(MedicamentListActivity.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Medicament>> call, Throwable t) {
                Toast.makeText(MedicamentListActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayMedicaments(List<Medicament> medicaments) {
        MedicamentListAdapter adapter = new MedicamentListAdapter(this, medicaments);
        listView.setAdapter(adapter);
    }

    public void onAddMedicamentClick(View view) {
        startActivity(new Intent(this, AddEditMedicamentActivity.class));
    }
}