package com.example.bubblewash.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.bubblewash.R;
import com.example.bubblewash.adapters.MoreOptionAdapter;
import com.example.bubblewash.adapters.UsageHistoryAdapter;
import com.example.bubblewash.databases.BubbleWashDatabase;
import com.example.bubblewash.databinding.ActivityHistoryBinding;
import com.example.bubblewash.databinding.ActivityMainBinding;
import com.example.bubblewash.model.Booking;
import com.example.bubblewash.model.MoreOption;
import com.example.bubblewash.model.UsageHistory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryActivity extends AppCompatActivity {

    BubbleWashDatabase bwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHistoryBinding binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBackNavigation();

        // UsageHistoryAdapter usageHistoryAdapter;
        bwd = Room.databaseBuilder(getApplicationContext(), BubbleWashDatabase.class, "bubblewash.db").build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ListView listViewPastBookings = findViewById(R.id.listViewPastBookings);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Booking> bookings = bwd.bookingDao().GetAllBookings();
                Log.d("DB", bookings.size() + " bookings count");

                List<UsageHistory> histories = new ArrayList<>();
                for(int i=0; i<bookings.size(); i++){
                    UsageHistory historyItem = new UsageHistory(bookings.get(i).getDate(), bookings.get(i).getTotalCost());
                    histories.add(historyItem);
                }
                Log.d("DB", histories.size() + " histories count");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.listViewPastBookings.setAdapter(new UsageHistoryAdapter(histories));
                    }
                });
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