package com.example.bubblewash.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Printer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bubblewash.R;
import com.example.bubblewash.databases.BubbleWashDatabase;
import com.example.bubblewash.model.Booking;
import com.example.bubblewash.model.TimeDuration;
import com.example.bubblewash.model.UsageHistory;
import com.example.bubblewash.model.User;
import com.example.bubblewash.utils.BookingStatus;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ImageView imageViewUser;
    private ImageView imgViewShowCalendar;
    private TextView txtSelectedDate;
    private TextView txtViewWelcome;
    private CheckBox chkWasher;
    private CheckBox chkDryer;
    private CheckBox chkPick;
    private CheckBox chkDeliver;
    private Spinner spinnerWasher;
    private Spinner spinnerDryer;
    private TextView txtPickDate;
    private TextView txtPickTime;
    private TextView txtDeliverDate;
    private TextView txtDeliverTime;
    private EditText txtRemarks;
    private String userId;
    BubbleWashDatabase bwd;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleNavigation();

        imgViewShowCalendar = findViewById(R.id.imageViewShowCalendar);
        txtSelectedDate = findViewById(R.id.textViewSelectedDate);
        txtViewWelcome = findViewById(R.id.textViewWelcome);
        chkWasher = findViewById(R.id.checkBoxWasher);
        chkDryer = findViewById(R.id.checkBoxDryer);
        chkPick = findViewById(R.id.checkBoxPickup);
        chkDeliver = findViewById(R.id.checkBoxDeliver);
        Button btnReserve = findViewById(R.id.buttonReserve);

        spinnerWasher = findViewById(R.id.spinnerWasherTimes);
        spinnerDryer = findViewById(R.id.spinnerDryerTimes);
        TextView txtWasher =  findViewById(R.id.textViewSelectTimeSlotDryer);
        spinnerDryer.setVisibility(View.GONE);
        txtWasher.setVisibility(View.GONE);
        txtPickTime =  findViewById(R.id.textViewPickupTime);
        txtDeliverTime =  findViewById(R.id.textViewDeliverTime);
        txtRemarks = findViewById(R.id.editTextRemarks);
        displayUserName();
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String formattedDate = df.format(c);

        txtSelectedDate.setText(formattedDate);

        imgViewShowCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendarDialog();
            }
        });
        getTimeDurationLists();

        chkDryer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int visibility = isChecked ? View.VISIBLE : View.GONE;

                spinnerDryer.setVisibility(visibility);
                txtWasher.setVisibility(visibility);

            }
        });

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBooking();
            }
        });

        chkPick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //int visibility = isChecked ? View.VISIBLE : View.GONE;
                if (isChecked) {
                    int washerTime = chkWasher.isChecked() ? Integer.parseInt(spinnerWasher.getSelectedItem().toString().substring(0, 2)) : 0;
                    txtPickTime.setText(String.format("%02d", washerTime - 2 )+ ":00");
                } else {
                    txtPickTime.setText("");
                }
            }
        });

        chkDeliver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    int washerTime = chkWasher.isChecked() ? Integer.parseInt(spinnerWasher.getSelectedItem().toString().substring(0, 2)) : 0;
                    int dryerTime = chkDryer.isChecked() ? Integer.parseInt(spinnerDryer.getSelectedItem().toString().substring(0, 2)) : 0;

                    if (chkDryer.isChecked()){
                        txtDeliverTime.setText(String.format("%02d", dryerTime + 2 )+ ":00");
                    }else {
                        txtDeliverTime.setText(String.format("%02d", washerTime - 2 )+ ":00");

                    }
                } else {
                    txtDeliverTime.setText("");
                }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createBooking(){

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Booking booking = new Booking();
                booking.setUserId(userId);
                booking.setDate(txtSelectedDate.getText().toString());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd"); //"MM" is month number "mm" is minutes "YYYY" year based on week number
                LocalDate bookingMonth = LocalDate.parse(txtSelectedDate.getText().toString(), formatter);
                booking.setMonth(bookingMonth.getMonth().toString().substring(0,3));
                booking.setWash(chkWasher.isChecked());
                booking.setDry(chkDryer.isChecked());
                booking.setWashCost(chkWasher.isChecked()? 3 : 0);
                booking.setDryCost(chkDryer.isChecked()? 2 : 0);
                float totalCost = (chkWasher.isChecked()? 3 : 1) * (chkDryer.isChecked()? 2 : 1);
                booking.setTotalCost(totalCost == 1 ? 0 : totalCost);
                booking.setWashTime(chkWasher.isChecked() ? Integer.parseInt(spinnerWasher.getSelectedItem().toString().substring(0,2)) : 0);
                booking.setDryTime(chkDryer.isChecked() ? Integer.parseInt(spinnerDryer.getSelectedItem().toString().substring(0,2)) : 0);
                booking.setPick(chkPick.isChecked());
                booking.setDeliver(chkDeliver.isChecked());
                booking.setPickTime(chkPick.isChecked() ? Integer.parseInt(txtPickTime.getText().toString().substring(0,2)) : 0);
                booking.setDeliverTime(chkDeliver.isChecked() ? Integer.parseInt(txtDeliverTime.getText().toString().substring(0,2)) : 0);
                booking.setStatus(BookingStatus.CONFIRM);
                booking.setRemarks(txtRemarks.getText().toString());
                try{
                    long bookingId = bwd.bookingDao().insertOneBooking(booking);
                    getTimeDurationLists();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Create a bundle
                            Bundle bundle = new Bundle();
                            //put data in the bundle
                            bundle.putLong("BOOKINGID", bookingId);
                            //create an intent and put the bundle
                            Intent intent = new Intent(MainActivity.this, BookingSummaryActivity.class);
                            intent.putExtras(bundle);
                            //start activity with ths intent
                            startActivity(intent);
                        }
                    });
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    private void showMessage(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void getTimeDurationLists(){
        bwd = Room.databaseBuilder(getApplicationContext(), BubbleWashDatabase.class, "bubblewash.db").build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<TimeDuration> times = bwd.timeDurationDao().getWasherTimeDurationList(txtSelectedDate.getText().toString());
                Log.d("DB", times.size() + " duration count for: " + txtSelectedDate.getText().toString());

                List<TimeDuration> dryerTimes = bwd.timeDurationDao().getDryerTimeDurationList(txtSelectedDate.getText().toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Set the retrieved times to spinnerWasherTimes
                        Spinner spinner = findViewById(R.id.spinnerWasherTimes); // Assuming you have a Spinner in your layout XML with id 'spinner'
                        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        for (TimeDuration value : times) {
                            adapter.add(String.format("%02d", value.getStartTime() )+ ":00");
                        }
                        spinner.setAdapter(adapter);

                        Spinner spinnerDryer = findViewById(R.id.spinnerDryerTimes); // Assuming you have a Spinner in your layout XML with id 'spinner'
                        ArrayAdapter<CharSequence> adapterDryer = new ArrayAdapter<CharSequence>(getApplicationContext(), android.R.layout.simple_spinner_item);
                        adapterDryer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        for (TimeDuration value : dryerTimes) {
                            if (value.getStartTime() > times.get(0).getStartTime()) {
                                adapterDryer.add(String.format("%02d", value.getStartTime()) + ":00");
                            }
                        }
                        spinnerDryer.setAdapter(adapterDryer);
                    }
                });
            }
        });
    }

    private  void displayUserName(){
        String userName = "";
        String userImage = "";

        SharedPreferences settings = getSharedPreferences("PREFS_BBW", 0);
        userName = settings.getString("USERNAME", "");
        userId = settings.getString("USERID", "");
        userImage = settings.getString("USERIMAGE", "");
        txtViewWelcome.setText("Hello, " + userName);
        Log.d("BUBBLEWASH", "image : " + userImage);
        if(!userImage.equals("")){
            imageViewUser = findViewById(R.id.imageViewUser);
            imageViewUser.setImageURI(Uri.parse(userImage));
        }
    }

    private void openCalendarDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                txtSelectedDate.setText(String.valueOf(year) + "/" + String.format("%02d", month) + "/" + String.format("%02d", dayOfMonth) );
                getTimeDurationLists();
            }
        }, 2024,4,9);

        datePickerDialog.show();
    }

    private void handleNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
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