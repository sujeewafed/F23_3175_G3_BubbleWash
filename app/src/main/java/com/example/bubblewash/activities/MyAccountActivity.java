package com.example.bubblewash.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bubblewash.R;
import com.example.bubblewash.databases.BubbleWashDatabase;
import com.example.bubblewash.databinding.ActivityChartsBinding;
import com.example.bubblewash.databinding.ActivityMyAccountBinding;
import com.example.bubblewash.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyAccountActivity extends AppCompatActivity {

    ActivityMyAccountBinding binding;
    BubbleWashDatabase bwd;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        binding = ActivityMyAccountBinding.inflate(getLayoutInflater());
        bwd = Room.databaseBuilder(getApplicationContext(), BubbleWashDatabase.class, "bubblewash.db").build();
        setContentView(binding.getRoot());
        setUserData();
        setBackNavigation();

        binding.btnMyAccountUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        user.setFirstName(binding.editTextMyAccountFirstName.getText().toString());
                        user.setLastName(binding.editTextMyAccountLastName.getText().toString());
                        user.setMobileNumber(binding.editTextMyAccountMobile.getText().toString());
                        user.setAddress(binding.editTextMyAccountAddress.getText().toString());
                        user.setPassword(binding.editTextMyAccountPassword.getText().toString());
                        bwd.userDAO().insertOneUser(user);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences settings = getSharedPreferences("PREFS_BBW", 0);
                                String password = settings.getString("PASSWORD", "");
                                Toast.makeText(getApplicationContext(), "Successfully updated the password !", Toast.LENGTH_SHORT).show();
                                if(!binding.editTextMyAccountPassword.getText().toString().equals(password)){
                                    signout();
                                }
                            }
                        });
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

    private void setUserData(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                SharedPreferences settings = getSharedPreferences("PREFS_BBW", 0);
                String userid = settings.getString("USERID", "");
                user = bwd.userDAO().getUserById(userid);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.editTextMyAccountUsername.setText(user.getUserName());
                        binding.editTextMyAccountPassword.setText(user.getPassword());
                        binding.editTextMyAccountFirstName.setText(user.getFirstName());
                        binding.editTextMyAccountLastName.setText(user.getLastName());
                        binding.editTextMyAccountAddress.setText(user.getAddress());
                        binding.editTextMyAccountMobile.setText(user.getMobileNumber());
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