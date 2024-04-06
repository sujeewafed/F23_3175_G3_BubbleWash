package com.example.bubblewash.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.bubblewash.R;
import com.example.bubblewash.databases.BubbleWashDatabase;
import com.example.bubblewash.databinding.ActivityChartsBinding;
import com.example.bubblewash.databinding.ActivityHistoryBinding;
import com.example.bubblewash.model.Booking;
import com.example.bubblewash.model.MonthCostTuple;
import com.example.bubblewash.model.UsageHistory;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChartsActivity extends AppCompatActivity {

    ArrayList barArraylist;
    BubbleWashDatabase bwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityChartsBinding binding = ActivityChartsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBackNavigation();

        bwd = Room.databaseBuilder(getApplicationContext(), BubbleWashDatabase.class, "bubblewash.db").build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                SharedPreferences settings = getSharedPreferences("PREFS_BBW", 0);
                String userId = settings.getString("USERID", "");

                List<MonthCostTuple> monthyData = bwd.bookingDao().getMonthlyUsage(userId);
                String[] months = new String[monthyData.size()];
                barArraylist = new ArrayList();

                for(int i=0; i<monthyData.size(); i++){
                    months[i] = monthyData.get(i).getBookingMonth();
                    barArraylist.add(new BarEntry(i, monthyData.get(i).getCost()));
                }

                BarChart barChart = findViewById(R.id.barChartUsage);
                BarDataSet barDataSet = new BarDataSet(barArraylist,"Monthly Usage History");
                BarData barData = new BarData(barDataSet);
                barChart.setData(barData);
                barChart.setFitBars(true);
                barChart.invalidate();

                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);

                ValueFormatter formatter = new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        return months[(int) value];
                    }
                };

                xAxis.setGranularity(1f);
                xAxis.setValueFormatter(formatter);
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(14f);
                barChart.getDescription().setEnabled(true);
            }
        });
    }

    private void setBackNavigation(){
        Button btnBack = findViewById(R.id.btnBackFromCharts);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MoreActivity.class));
            }
        });
    }
}