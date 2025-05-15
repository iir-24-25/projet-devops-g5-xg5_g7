package com.example.pharmacie;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class StatsAdapter extends BaseAdapter {
    private Context context;
    private List<StatItem> stats;

    public StatsAdapter(Context context, List<StatItem> stats) {
        this.context = context;
        this.stats = stats;
    }

    @Override
    public int getCount() {
        return stats.size();
    }

    @Override
    public Object getItem(int position) {
        return stats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.card_stat_item, parent, false);
        }

        StatItem stat = stats.get(position);

        ImageView icon = convertView.findViewById(R.id.iconStat);
        TextView title = convertView.findViewById(R.id.titleStat);
        TextView value = convertView.findViewById(R.id.valueStat);

        icon.setImageResource(stat.getIconRes());
        icon.setColorFilter(ContextCompat.getColor(context, stat.getColor()));
        title.setText(stat.getTitle());
        value.setText(stat.getValue());

        return convertView;
    }
}
