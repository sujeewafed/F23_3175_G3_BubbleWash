package com.example.bubblewash.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.bubblewash.R;
import com.example.bubblewash.databases.BubbleWashDatabase;
import com.example.bubblewash.databinding.ActivityHistoryBinding;
import com.example.bubblewash.databinding.ActivityTrackBinding;
import com.example.bubblewash.model.Booking;
import com.example.bubblewash.model.UsageHistory;
import com.example.bubblewash.utils.BookingStatus;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrackActivity extends AppCompatActivity {

    ActivityTrackBinding binding;
    BubbleWashDatabase bwd;
    boolean isCurrentBooking = true;
    List<Booking> bookings;
    List<String> currentDataList;
    Booking currentBooking;
    float selectedRating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        handleNavigation();

        bwd = Room.databaseBuilder(getApplicationContext(), BubbleWashDatabase.class, "bubblewash.db").build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                SharedPreferences settings = getSharedPreferences("PREFS_BBW", 0);
                String userId = settings.getString("USERID", "");

                bookings = bwd.bookingDao().getCurrentUserBookings(userId);
                Log.d("BUBBLE_WASH", "Current Bookings : " + bookings.size());


                List<String> currentDataList = new ArrayList<>();
                for(int i=0; i<bookings.size(); i++){
                    String txtValue = bookings.get(i).getDate().toString() + " - $" + Float.toString(bookings.get(i).getTotalCost());
                    currentDataList.add(txtValue);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, currentDataList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(bookings.size() == 0){
                            isCurrentBooking = false;
                        }
                        else{
                            isCurrentBooking = true;
                            currentBooking = bookings.get(0);
                            setData();
                            binding.spinnerCurrentBookings.setAdapter(adapter);
                        }
                        handleVisibility();
                    }
                });
            }
        });

        binding.spinnerCurrentBookings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentBooking = bookings.get(position);
                setData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                selectedRating = rating;
            }
        });

        binding.btnRateSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCurrentBooking){
                    currentBooking.setRemarks(binding.editTextComment.getText().toString());
                    currentBooking.setRating(selectedRating);
                    ExecutorService executorService = Executors.newSingleThreadExecutor();

                    // triggerSms();

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            bwd.bookingDao().insertOneBooking(currentBooking);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showMessage("Successfully added the rating !");
                                    binding.editTextComment.setText("");
                                }
                            });
                        }
                    });
                }
            }
        });

    }

    private void triggerSms(){
        if(ContextCompat.checkSelfPermission(TrackActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            sendSms();
        }
        else {
            ActivityCompat.requestPermissions(TrackActivity.this, new String[]{Manifest.permission.SEND_SMS}, 100);
        }
    }

    private void sendSms(){
        SmsManager SmsManager = android.telephony.SmsManager.getDefault();
        SmsManager.sendTextMessage("7788825994", null, "Test Msg", null, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            sendSms();
        }
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setData(){
        binding.txtViewSummaryDate.setText("Date : " + currentBooking.getDate());
        binding.txtViewSummaryCost.setText("Cost : $" + Float.toString(currentBooking.getTotalCost()));
        binding.txtViewSummaryWasherTime.setText("Wash Time : " + Integer.toString(currentBooking.getWashTime()) + ":00");
        binding.txtViewSummaryDryerTime.setText("Dry Time : " + Integer.toString(currentBooking.getDryTime()) + ":00");
        binding.editTextComment.setText(currentBooking.getRemarks());
        binding.ratingBar.setRating(currentBooking.getRating());
        setProgressData();
    }

    private void setProgressData(){
        binding.chkBxTrackConfirmOrder.setChecked(false);
        binding.chkBxTrackPickOrder.setChecked(false);
        binding.chkBoxTrackWashOrder.setChecked(false);
        binding.chkBoxTrackDryOrder.setChecked(false);
        binding.chkBoxTrackDeliverOrder.setChecked(false);

        binding.chkBxTrackConfirmOrder.setEnabled(false);
        binding.chkBxTrackPickOrder.setEnabled(false);
        binding.chkBoxTrackWashOrder.setEnabled(false);
        binding.chkBoxTrackDryOrder.setEnabled(false);
        binding.chkBoxTrackDeliverOrder.setEnabled(false);
        BookingStatus status = currentBooking.getStatus();
        if(BookingStatus.valueOf("CONFIRM") == status){
            setConfirmProgressData();
        } else if (BookingStatus.valueOf("PICK") == status) {
            setConfirmProgressData();
            setPickProgressData();
        } else if (BookingStatus.valueOf("WASH") == status) {
            setConfirmProgressData();
            setPickProgressData();
            setWashProgressData();
        } else if (BookingStatus.valueOf("DRY") == status) {
            setConfirmProgressData();
            setPickProgressData();
            setWashProgressData();
            setDryProgressData();
        } else if (BookingStatus.valueOf("DELIVER") == status) {
            setConfirmProgressData();
            setPickProgressData();
            setWashProgressData();
            setDryProgressData();
            setDeliverProgressData();
        }
    }

    private void setConfirmProgressData(){
        binding.chkBxTrackConfirmOrder.setChecked(true);
        binding.chkBxTrackConfirmOrder.setTextColor(Color.BLACK);
        binding.chkBxTrackConfirmOrder.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#097969")));
    }

    private void setPickProgressData(){
        binding.chkBxTrackPickOrder.setChecked(true);
        binding.chkBxTrackPickOrder.setTextColor(Color.BLACK);
        binding.chkBxTrackPickOrder.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#097969")));
    }

    private void setWashProgressData(){
        binding.chkBoxTrackWashOrder.setChecked(true);
        binding.chkBoxTrackWashOrder.setTextColor(Color.BLACK);
        binding.chkBoxTrackWashOrder.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#097969")));
    }

    private void setDryProgressData(){
        binding.chkBoxTrackDryOrder.setChecked(true);
        binding.chkBoxTrackDryOrder.setTextColor(Color.BLACK);
        binding.chkBoxTrackDryOrder.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#097969")));
    }

    private void setDeliverProgressData(){
        binding.chkBoxTrackDeliverOrder.setChecked(true);
        binding.chkBoxTrackDeliverOrder.setTextColor(Color.BLACK);
        binding.chkBoxTrackDeliverOrder.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#097969")));
    }

    private void handleVisibility(){
        if(isCurrentBooking){
            binding.txtViewNoBookingMsg.setVisibility(View.GONE);
            binding.txtViewTitleSelectCurrentBooking.setVisibility(View.VISIBLE);
            binding.spinnerCurrentBookings.setVisibility(View.VISIBLE);
            binding.txtViewSummaryDate.setVisibility(View.VISIBLE);
            binding.txtViewSummaryCost.setVisibility(View.VISIBLE);
            binding.txtViewSummaryWasherTime.setVisibility(View.VISIBLE);
            binding.txtViewSummaryDryerTime.setVisibility(View.VISIBLE);
            binding.viewLine1.setVisibility(View.VISIBLE);
            binding.chkBxTrackConfirmOrder.setVisibility(View.VISIBLE);
            binding.chkBxTrackPickOrder.setVisibility(View.VISIBLE);
            binding.txtViewTrackPickTime.setVisibility(View.VISIBLE);
            binding.chkBoxTrackWashOrder.setVisibility(View.VISIBLE);
            binding.chkBoxTrackDryOrder.setVisibility(View.VISIBLE);
            binding.chkBoxTrackDeliverOrder.setVisibility(View.VISIBLE);
            binding.txtViewTrackDeliverTime.setVisibility(View.VISIBLE);
            binding.viewLine2.setVisibility(View.VISIBLE);
            binding.txtViewTitleRate.setVisibility(View.VISIBLE);
            binding.ratingBar.setVisibility(View.VISIBLE);
            binding.ratingBar.setVisibility(View.VISIBLE);
            binding.editTextComment.setVisibility(View.VISIBLE);
            binding.btnRateSubmit.setVisibility(View.VISIBLE);
        }
        else {
            binding.txtViewNoBookingMsg.setVisibility(View.VISIBLE);
            binding.txtViewTitleSelectCurrentBooking.setVisibility(View.GONE);
            binding.spinnerCurrentBookings.setVisibility(View.GONE);
            binding.txtViewSummaryDate.setVisibility(View.GONE);
            binding.txtViewSummaryCost.setVisibility(View.GONE);
            binding.txtViewSummaryWasherTime.setVisibility(View.GONE);
            binding.txtViewSummaryDryerTime.setVisibility(View.GONE);
            binding.viewLine1.setVisibility(View.GONE);
            binding.chkBxTrackConfirmOrder.setVisibility(View.GONE);
            binding.chkBxTrackPickOrder.setVisibility(View.GONE);
            binding.txtViewTrackPickTime.setVisibility(View.GONE);
            binding.chkBoxTrackWashOrder.setVisibility(View.GONE);
            binding.chkBoxTrackDryOrder.setVisibility(View.GONE);
            binding.chkBoxTrackDeliverOrder.setVisibility(View.GONE);
            binding.txtViewTrackDeliverTime.setVisibility(View.GONE);
            binding.viewLine2.setVisibility(View.GONE);
            binding.txtViewTitleRate.setVisibility(View.GONE);
            binding.ratingBar.setVisibility(View.GONE);
            binding.ratingBar.setVisibility(View.GONE);
            binding.editTextComment.setVisibility(View.GONE);
            binding.btnRateSubmit.setVisibility(View.GONE);
        }
    }

    private void handleNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_track);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.bottom_home){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if(item.getItemId() == R.id.bottom_track){
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