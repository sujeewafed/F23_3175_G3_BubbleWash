package com.example.bubblewash.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.bubblewash.R;
import com.example.bubblewash.adapters.ManageBookingAdapter;
import com.example.bubblewash.databases.BubbleWashDatabase;
import com.example.bubblewash.databinding.ActivityManageBookingBinding;
import com.example.bubblewash.model.Booking;
import com.example.bubblewash.model.ManageBookingAdminPanel;
import com.example.bubblewash.utils.BookingStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManageBookingActivity extends AppCompatActivity {

    BubbleWashDatabase bwd;
    List<Booking> bookings;
    Booking currentItem;
    BookingStatus selectedStatus;
    ActivityManageBookingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bwd = Room.databaseBuilder(getApplicationContext(), BubbleWashDatabase.class, "bubblewash.db").build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // set spinner data
        BookingStatus[] statuses = BookingStatus.values();
        String[] statusList = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            statusList[i] = statuses[i].name();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, statusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setData();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.spinnerChangeBookingStatus.setAdapter(adapter);
                    }
                });
            }
        });

        binding.spinnerChangeBookingStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = BookingStatus.valueOf(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnChangeBookingStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem.setStatus(selectedStatus);
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        bwd.bookingDao().insertOneBooking(currentItem);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Successfully changed the status !", Toast.LENGTH_SHORT).show();
                                setData();
                            }
                        });
                    }
                });
            }
        });

        binding.listViewCurrentBooking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentItem = bookings.get(position);
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

    private void setData(){
        bwd = Room.databaseBuilder(getApplicationContext(), BubbleWashDatabase.class, "bubblewash.db").build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                bookings = bwd.bookingDao().getCurrentBookingList();
                List<ManageBookingAdminPanel> manageBookingAdminPanelList = new ArrayList<>();
                for (int i =0; i <bookings.size();i++){
                    ManageBookingAdminPanel items = new ManageBookingAdminPanel
                            (bookings.get(i).getDate(),bookings.get(i).getMonth(),
                                    bookings.get(i).getPickTime(), bookings.get(i).getStatus());

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