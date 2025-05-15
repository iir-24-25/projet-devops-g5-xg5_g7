package com.example.pharmacie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pharmacie.models.Medicament;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MedicamentListAdapter extends ArrayAdapter<Medicament> {
    private Context context;
    private List<Medicament> medicamentList;

    public MedicamentListAdapter(Context context, List<Medicament> medicamentList) {
        super(context, 0, medicamentList);
        this.context = context;
        this.medicamentList = medicamentList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Medicament medicament = medicamentList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_medicament, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageViewMedicament);
        TextView codeTextView = convertView.findViewById(R.id.textViewCodeMed);
        TextView libelleTextView = convertView.findViewById(R.id.textViewLibelle);
        TextView expirationTextView = convertView.findViewById(R.id.textViewDateExp);

        codeTextView.setText("Code : " + medicament.getCodeMed());
        libelleTextView.setText("Nom : " + medicament.getLibelle());
        expirationTextView.setText("Expiration : " + medicament.getDateExpiration());

        if (medicament.getImage() != null && !medicament.getImage().isEmpty()) {
            String imageUrl = "http://192.168.134.23/images/" + medicament.getImage();
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background);
        }


        return convertView;
    }
}
