package com.example.bubblewash.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
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

        bwd = Room.databaseBuilder(getApplicationContext(), BubbleWashDatabase.class, "bubblewash.db").build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                SharedPreferences settings = getSharedPreferences("APP", 0);
                String userId = settings.getString("USER_ID", "");

                List<Booking> bookings = bwd.bookingDao().getAllPastBookingsForUser(userId);
                List<UsageHistory> histories = new ArrayList<>();
                for(int i=0; i<bookings.size(); i++){
                    UsageHistory historyItem = new UsageHistory(bookings.get(i).getDate(), bookings.get(i).getTotalCost());
                    histories.add(historyItem);
                }

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