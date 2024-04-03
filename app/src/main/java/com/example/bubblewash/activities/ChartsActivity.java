package com.example.bubblewash.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bubblewash.R;
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

public class ChartsActivity extends AppCompatActivity {

    ArrayList barArraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        setBackNavigation();

        BarChart barChart = findViewById(R.id.barChartUsage);
        getData();
        BarDataSet barDataSet = new BarDataSet(barArraylist,"Monthly Usage History");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        final String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return months[(int) value];
            }
        };

        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);

        //color bar data set
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        //text color
        barDataSet.setValueTextColor(Color.BLACK);

        //settting text size
        barDataSet.setValueTextSize(14f);
        barChart.getDescription().setEnabled(true);
    }

    private void getData()
    {
        barArraylist = new ArrayList();
        barArraylist.add(new BarEntry(0f, 10.6F));
        barArraylist.add(new BarEntry(1f,20.3F));
        barArraylist.add(new BarEntry(2f,30.42F));
        barArraylist.add(new BarEntry(3f,40.10F));
        barArraylist.add(new BarEntry(4f,50.32F));
        barArraylist.add(new BarEntry(5f,20.12F));
        barArraylist.add(new BarEntry(6f,40.48F));
        barArraylist.add(new BarEntry(7f,30.46F));
        barArraylist.add(new BarEntry(8f,10.12F));
        barArraylist.add(new BarEntry(9f,45.78F));
        barArraylist.add(new BarEntry(10f,60.12F));
        barArraylist.add(new BarEntry(11f,30.36F));
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