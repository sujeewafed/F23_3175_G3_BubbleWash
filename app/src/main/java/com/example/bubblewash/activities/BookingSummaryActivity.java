package com.example.bubblewash.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bubblewash.R;
import com.example.bubblewash.databases.BubbleWashDatabase;
import com.example.bubblewash.databinding.ActivityBookingSummaryBinding;
import com.example.bubblewash.databinding.ActivityTrackBinding;
import com.example.bubblewash.model.Booking;
import com.example.bubblewash.model.TimeDuration;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookingSummaryActivity extends AppCompatActivity {
    BubbleWashDatabase bwd;
    ActivityBookingSummaryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_booking_summary);
        binding = ActivityBookingSummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        handleNavigation();
        try{
            int bookingId = 0;
            Bundle bundle = getIntent().getExtras();
            bookingId = Integer.parseInt(String.valueOf(getIntent().getExtras().getLong("BOOKINGID",0)));
            getBookingData(bookingId);
            //txtViewResults.setGravity(Gravity.CENTER);
            //txtViewResults.setGravity(Gravity.TOP | Gravity.CLIP_HORIZONTAL);

        }catch (Exception ex){
            ex.printStackTrace();
            Log.d("CONCERTDEMO", ex.getMessage());
        }
    }

    private void getBookingData(int bookingId){
        bwd = Room.databaseBuilder(getApplicationContext(), BubbleWashDatabase.class, "bubblewash.db").build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Booking booking = bwd.bookingDao().getBookingById(bookingId);
                //Log.d("DB", times.size() + " duration count for: " + txtSelectedDate.getText().toString());

                //List<TimeDuration> dryerTimes = bwd.timeDurationDao().getDryerTimeDurationList(txtSelectedDate.getText().toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setData(booking);
                    }
                });
            }
        });
    }

    private void setData(Booking currentBooking){
        binding.txtViewSummaryDate.setText("Date : " + currentBooking.getDate());
        binding.txtViewSummaryCost.setText("Cost : $" + Float.toString(currentBooking.getTotalCost()));
        binding.txtViewSummaryWasherTime.setText("Wash Time : " + Integer.toString(currentBooking.getWashTime()) + ":00");
        binding.txtViewSummaryDryerTime.setText("Dry Time : " + Integer.toString(currentBooking.getDryTime()) + ":00");
        binding.txtViewSummaryWasherTime.setText("Pickup Time : " + Integer.toString(currentBooking.getPickTime()) + ":00");
        binding.txtViewSummaryDryerTime.setText("Delivery Time : " + Integer.toString(currentBooking.getDeliverTime()) + ":00");

    }
    private void showMessage(String message){
        Toast.makeText(BookingSummaryActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void handleNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationBookigSummary);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.bottom_home){
                return true;
            }
            else if(item.getItemId() == R.id.bottom_track){
                startActivity(new Intent(getApplicationContext(), TrackActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if(item.getItemId() == R.id.bottom_more){
                startActivity(new Intent(getApplicationContext(), MoreActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }

}