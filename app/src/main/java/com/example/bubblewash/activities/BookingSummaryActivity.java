package com.example.bubblewash.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bubblewash.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;

public class BookingSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);
        handleNavigation();
        try{
            long bookingId = 0;
            Bundle bundle = getIntent().getExtras();
            double cost = 0; //bundle.getDouble("BOOKINGID",0);
            bookingId = getIntent().getExtras().getLong("BOOKINGID",0);
            //String concertType = bundle.getString("TYPE", "NOTHING");
            DecimalFormat df = new DecimalFormat("$#.##");
            String outputStr = "Booking ID " + bookingId + "\n" +
                    "Total cost " + df.format(cost);
            showMessage(outputStr);
            TextView txtViewResults = findViewById(R.id.textViewSummarySuccess);
            txtViewResults.setText(outputStr);
            //txtViewResults.setGravity(Gravity.CENTER);
            //txtViewResults.setGravity(Gravity.TOP | Gravity.CLIP_HORIZONTAL);

        }catch (Exception ex){
            ex.printStackTrace();
            Log.d("CONCERTDEMO", ex.getMessage());
        }
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