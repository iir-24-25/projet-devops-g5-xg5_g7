package com.example.pharmacie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pharmacie.models.Medicament;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Medicament> medicaments;

    public NotificationAdapter(List<Medicament> medicaments) {
        this.medicaments = medicaments;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Medicament medicament = medicaments.get(position);

        holder.medicamentName.setText(medicament.getLibelle());

        // Détermine le type d'alerte
        if (isExpired(medicament)) {
            holder.alertMessage.setText("Ce médicament a expiré");
            holder.alertType.setText("Expiration");
        } else if (isStockLow(medicament)) {
            holder.alertMessage.setText("Stock faible - " + medicament.getQteStock() + " unités restantes");
            holder.alertType.setText("Stock");
        }
    }

    @Override
    public int getItemCount() {
        return medicaments.size();
    }

    private boolean isExpired(Medicament medicament) {
        try {
            // Format: "2023-11-20T00:00:00.000+00:00"
            String datePart = medicament.getDateExpiration().split("T")[0];
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date expiration = sdf.parse(datePart);
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isStockLow(Medicament medicament) {
        return medicament.getQteStock() != null &&
                medicament.getStockMin() != null &&
                medicament.getQteStock() <= medicament.getStockMin();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView medicamentName, alertMessage, alertType;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            medicamentName = itemView.findViewById(R.id.medicamentName);
            alertMessage = itemView.findViewById(R.id.alertMessage);
            alertType = itemView.findViewById(R.id.alertType);
        }
    }
}