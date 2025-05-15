package com.example.pharmacie;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pharmacie.models.Medicament;
import com.example.pharmacie.retrofit.ApiClient;
import com.example.pharmacie.retrofit.MedicamentApi;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Medicament> medicamentsAlert = new ArrayList<>();
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.notificationRecyclerView);
        emptyView = findViewById(R.id.emptyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(medicamentsAlert);
        recyclerView.setAdapter(adapter);

        loadAlertMedicaments();
    }

    private void loadAlertMedicaments() {
        MedicamentApi api = ApiClient.getClient().create(MedicamentApi.class);
        Call<List<Medicament>> call = api.getMedicamentsAlerts();

        call.enqueue(new Callback<List<Medicament>>() {
            @Override
            public void onResponse(Call<List<Medicament>> call, Response<List<Medicament>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    medicamentsAlert.clear();
                    medicamentsAlert.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (medicamentsAlert.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setText("Aucune alerte pour le moment");
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Medicament>> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("Erreur de chargement des alertes");
            }
        });
    }
}