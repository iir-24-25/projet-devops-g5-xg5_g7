package com.example.pharmacie;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pharmacie.models.Medicament;
import com.example.pharmacie.retrofit.ApiClient;
import com.example.pharmacie.retrofit.MedicamentApi;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        setupStatsGrid();
        setupMedicamentsChart();
    }

    private void setupStatsGrid() {
        MedicamentApi api = ApiClient.getClient().create(MedicamentApi.class);

        Call<List<Medicament>> call = api.getAllMedicaments();
        call.enqueue(new Callback<List<Medicament>>() {
            @Override
            public void onResponse(Call<List<Medicament>> call, Response<List<Medicament>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Medicament> medicaments = response.body();
                    updateStatsGrid(medicaments);
                }
            }

            @Override
            public void onFailure(Call<List<Medicament>> call, Throwable t) {
                showError("Erreur lors du chargement des statistiques");
            }
        });
    }

    private void updateStatsGrid(List<Medicament> medicaments) {
        int totalMedicaments = medicaments.size();
        int lowStockCount = 0;

        for (Medicament med : medicaments) {
            if (med.getQteStock() < med.getStockMin()) {
                lowStockCount++;
            }
        }

        List<StatItem> stats = new ArrayList<>();
        stats.add(new StatItem("Médicaments", String.valueOf(totalMedicaments), R.drawable.ic_medicament, R.color.purple_500));
        stats.add(new StatItem("Stocks Faibles", String.valueOf(lowStockCount), R.drawable.ic_alerte, R.color.red_500));

        GridView gridView = findViewById(R.id.gridStats);
        gridView.setAdapter(new StatsAdapter(DashboardActivity.this, stats));
    }

    private void setupMedicamentsChart() {
        PieChart chart = findViewById(R.id.chartMedicaments);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setCenterText("Répartition des stocks");
        chart.setEntryLabelColor(Color.BLACK);

        MedicamentApi api = ApiClient.getClient().create(MedicamentApi.class);
        Call<List<Medicament>> call = api.getAllMedicaments();

        call.enqueue(new Callback<List<Medicament>>() {
            @Override
            public void onResponse(Call<List<Medicament>> call, Response<List<Medicament>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateMedicamentsChart(chart, response.body());
                } else {
                    showEmptyChart(chart);
                }
            }

            @Override
            public void onFailure(Call<List<Medicament>> call, Throwable t) {
                showError("Erreur lors du chargement du graphique");
                showEmptyChart(chart);
            }
        });
    }

    private void updateMedicamentsChart(PieChart chart, List<Medicament> medicaments) {
        // 1. Compter la quantité totale par famille
        Map<String, Float> familleQuantities = new HashMap<>();
        for (Medicament med : medicaments) {
            String famille = med.getFamilleMed() != null ? med.getFamilleMed() : "Non classé";
            float quantity = med.getQteStock() != null ? med.getQteStock() : 0;
            familleQuantities.put(famille, familleQuantities.getOrDefault(famille, 0f) + quantity);
        }

        // 2. Créer les entrées en regroupant les petites familles sous "Autres"
        List<PieEntry> entries = new ArrayList<>();
        float othersQuantity = 0;
        final float THRESHOLD = 5; // Seuil minimum pour afficher une famille séparément

        for (Map.Entry<String, Float> entry : familleQuantities.entrySet()) {
            if (entry.getValue() > THRESHOLD) {
                entries.add(new PieEntry(entry.getValue(), entry.getKey()));
            } else {
                othersQuantity += entry.getValue();
            }
        }

        if (othersQuantity > 0) {
            entries.add(new PieEntry(othersQuantity, "Autres"));
        }

        // 3. Si aucune donnée, afficher un message
        if (entries.isEmpty()) {
            showEmptyChart(chart);
            return;
        }

        // 4. Configurer le dataset
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueFormatter(new PercentFormatter(chart));

        // 5. Configurer le graphique
        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.setCenterTextSize(14f);
        chart.animateY(1000);
        chart.invalidate();
    }

    private void showEmptyChart(PieChart chart) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(1f, "Aucune donnée"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(Color.GRAY);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.invalidate();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}