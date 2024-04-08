package com.example.bubblewash.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bubblewash.R;
import com.example.bubblewash.adapters.ManageBookingAdapter;
import com.example.bubblewash.databases.BubbleWashDatabase;
import com.example.bubblewash.databinding.ActivityManageBookingBinding;
import com.example.bubblewash.model.Booking;
import com.example.bubblewash.model.ManageBookingAdminPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManageBookingActivity extends AppCompatActivity {

    BubbleWashDatabase bwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_manage_booking);

        ActivityManageBookingBinding binding = ActivityManageBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bwd = Room.databaseBuilder(getApplicationContext(), BubbleWashDatabase.class, "bubblewash.db").build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Booking> bookings = bwd.bookingDao().getCurrentBookingList();
                List<ManageBookingAdminPanel> manageBookingAdminPanelList = new ArrayList<>();

                for (int i =0; i <bookings.size();i++){
                    ManageBookingAdminPanel items = new ManageBookingAdminPanel
                            (bookings.get(i).getDate(),bookings.get(i).getMonth()
                                    ,bookings.get(i).getPickDate(),bookings.get(i).getPickTime(), bookings.get(i).getStatus());

                    manageBookingAdminPanelList.add(items);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.listViewCurrentBooking.setAdapter(new ManageBookingAdapter(manageBookingAdminPanelList));
                    }
                });
            }
        });

        Button btnsignout = findViewById(R.id.btnManageBookingSignout);

        btnsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signout();
            }
        });
    }

    private void signout(){
        SharedPreferences settings = getSharedPreferences("PREFS_BBW", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("IS_LOGGED", false);
        editor.putString("USERID", "");
        editor.putString("USERNAME", "");
        editor.putString("PASSWORD", "");
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}