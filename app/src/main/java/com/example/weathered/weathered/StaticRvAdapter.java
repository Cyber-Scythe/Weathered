package com.example.weathered.weathered;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weathered.R;

import java.util.List;


public class StaticRvAdapter extends RecyclerView.Adapter<com.example.weathered.weathered.StaticRvAdapter.ViewHolder> {

    // Context object used to inflate list_item layout
    private List<StaticRvModel> dailyItems;
    private Context context;


    // Generated constructor from members
    public StaticRvAdapter(List<StaticRvModel> dailyItems, Context context) {
        this.dailyItems = dailyItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_daily, parent, false);
        return new com.example.weathered.weathered.StaticRvAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(com.example.weathered.weathered.StaticRvAdapter.ViewHolder holder, int position) {

        StaticRvModel dailyItem = dailyItems.get(position);

        holder.dailyIcon.setImageResource(dailyItem.getImage());
        holder.dayOfWeek.setText(dailyItem.getDayOfWeek());
        holder.dailyHi.setText(dailyItem.getDailyHi());
        holder.dailyLow.setText(dailyItem.getDailyLow());

    }

    @Override
    public int getItemCount() {
        return dailyItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView dailyIcon;
        TextView dayOfWeek, dailyHi, dailyLow, percentTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            dailyIcon = (ImageView) itemView.findViewById(R.id.dailyIcon);
            dayOfWeek = (TextView) itemView.findViewById(R.id.weekdayTextView);
            dailyHi = (TextView) itemView.findViewById(R.id.dailyHigh);
            dailyLow = (TextView) itemView.findViewById(R.id.dailyLo);
            percentTextView = (TextView) itemView.findViewById(R.id.percentTextView);
        }
    }
}
