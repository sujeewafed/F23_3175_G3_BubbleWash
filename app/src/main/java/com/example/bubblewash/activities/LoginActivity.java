package com.example.bubblewash.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bubblewash.R;
import com.example.bubblewash.databases.BubbleWashDatabase;
import com.example.bubblewash.model.Booking;
import com.example.bubblewash.model.TimeDuration;
import com.example.bubblewash.model.User;
import com.example.bubblewash.utils.BookingStatus;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    List<User> users = new ArrayList<>();
    List<Booking> bookings = new ArrayList<>();

    List<TimeDuration> timeDurations = new ArrayList<>();
    BubbleWashDatabase bubbleWashDatabase;

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    private static final int REQ_ONE_TAP = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupInitialData();

        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId("331532376916-m9b48d0245r8sbjf1r79ddq2483f3nsa.apps.googleusercontent.com")
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();

        Button btnSignIn = findViewById(R.id.buttonSignIn);
        Button btnRegister = findViewById(R.id.buttonRegister);

        EditText txtUserName = findViewById(R.id.editTextUserName);
        EditText txtPassword = findViewById(R.id.editTextPassword);

        bubbleWashDatabase = Room.databaseBuilder(getApplicationContext(), BubbleWashDatabase.class, "bubblewash.db").build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                bubbleWashDatabase.userDAO().insertUsersFromList(users);
                Log.d("DB", users.size() + " users added");

                bubbleWashDatabase.bookingDao().insertBookingsFromList(bookings);
                Log.d("DB", bookings.size() + " bookings added");

                bubbleWashDatabase.timeDurationDao().insertTimesFromList(timeDurations);
                Log.d("DB", timeDurations.size() + " time durations added");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //binding.listViewStudents.setAdapter(new StudentAdapter(studentDb));
                    }
                });
            }
        });

        //Linking "Register" button with the registration activity intent
        btnRegister.setMovementMethod(LinkMovementMethod.getInstance());

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtUserName.getText().toString().isEmpty() && !txtPassword.getText().toString().isEmpty()) {
                    validateUser(txtUserName.getText().toString(), txtPassword.getText().toString());
                }
                else if (txtUserName.getText().toString().isEmpty()){
                    showMessage("Please enter your user name");
                }else if (txtPassword.getText().toString().isEmpty()){
                    showMessage("Please enter your password");
                }
                else{
                    showMessage("Lost");
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void setupInitialData(){

        boolean isFirstRun = false;
        SharedPreferences settings = getSharedPreferences("PREFS_BBW", 0);
        isFirstRun = settings.getBoolean("FIRST_RUN", false);
        if (!isFirstRun) {
            // do the thing for the first time
            Log.d("BBL", " FIRST RUN");
            settings = getSharedPreferences("PREFS_BBW", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.commit();
            readUsersFromCSV();
            readBookingsFromCSV();
            readTimeDurationsFromCSV();
        } else {
            // other time your app loads
            Log.d("BBL", " NOT THE FIRST RUN");
        }
    }
    private void validateUser(String userName, String password){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                User user = bubbleWashDatabase.userDAO().getUser(userName,password);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (user!=null) {
                            if(user.isAdmin()){

                                SharedPreferences settings = getSharedPreferences("PREFS_BBW", 0);
                                settings = getSharedPreferences("PREFS_BBW", 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("USERNAME", user.getUserName());
                                editor.putString("PASSWORD", user.getPassword());
                                editor.putString("USERID", user.getId());
                                editor.putBoolean("IS_LOGGED", true);
                                editor.commit();

                                startActivity(new Intent(LoginActivity.this, ManageBookingActivity.class));
                            }else {
                                Log.d("DB User found : ", user.getUserName());

                                // save current user info
                                SharedPreferences settings = getSharedPreferences("PREFS_BBW", 0);
                                settings = getSharedPreferences("PREFS_BBW", 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("USERNAME", user.getUserName());
                                editor.putString("PASSWORD", user.getPassword());
                                editor.putString("USERID", user.getId());
                                editor.putBoolean("IS_LOGGED", true);
                                editor.commit();

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }

                        }
                        else{
                            EditText txtUserName = findViewById(R.id.editTextUserName);
                            EditText txtPassword = findViewById(R.id.editTextPassword);
                            txtUserName.setText("");
                            txtPassword.setText("");
                            txtUserName.requestFocus();
                            showMessage("Incorrect username or password. Try again.");
                        }

                    }
                });

            }
        });

    }

    private void validateGoogleSignInUser(String userName){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                User user = bubbleWashDatabase.userDAO().getUserByUserName(userName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (user!=null) {
                            Log.d("DB User found : ", user.getUserName());

                            // save current user info
                            SharedPreferences settings = getSharedPreferences("PREFS_BBW", 0);
                            settings = getSharedPreferences("PREFS_BBW", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("USERNAME", user.getUserName());
                            //editor.putString("PASSWORD", user.getPassword());
                            editor.putString("USERID", user.getId());
                            editor.putBoolean("IS_LOGGED", true);
                            editor.commit();

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        else{
                            EditText txtUserName = findViewById(R.id.editTextUserName);
                            EditText txtPassword = findViewById(R.id.editTextPassword);
                            txtUserName.setText("");
                            txtPassword.setText("");
                            txtUserName.requestFocus();
                            showMessage("Incorrect username or password. Try again.");
                        }

                    }
                });

            }
        });

    }

    private void showMessage(String message){
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void readUsersFromCSV(){
        //read users from users.csv
        users = new ArrayList<>();
        String inputLine;
        InputStream inputStream = getResources().openRawResource(R.raw.users);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        //rest of the file read logic next class

        try{
            if((inputLine = reader.readLine()) !=null){
                // header lin is contained in inputLine
            }
            while ((inputLine = reader.readLine()) != null){
                String[] eachUserFields = inputLine.split(",");
                User eachUser = new User(eachUserFields[0],eachUserFields[1],eachUserFields[2], Boolean.parseBoolean(eachUserFields[3]));
                users.add(eachUser);
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        } finally {
            try{
                inputStream.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private void readBookingsFromCSV(){
        bookings = new ArrayList<>();
        String inputLine;
        InputStream inputStream = getResources().openRawResource(R.raw.bookings);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try{
            if((inputLine = reader.readLine()) !=null){
                // header lin is contained in inputLine
            }
            while ((inputLine = reader.readLine()) != null){
                String[] eachBookingFields = inputLine.split(",");
                Log.d("DB", eachBookingFields + " Captured bookings");

                Booking eachBooking = new Booking(
                        // UserId - 00001
                        eachBookingFields[1],
                        // Date - 2024/01/10
                        eachBookingFields[2],
                        // Month - Jan
                        eachBookingFields[3],
                        // Wash - true
                        Boolean.parseBoolean(eachBookingFields[4]),
                        // Dry - true
                        Boolean.parseBoolean(eachBookingFields[5]),
                        // WashCost - 8
                        Float.parseFloat(eachBookingFields[6]),
                        // DryCost - 0
                        Float.parseFloat(eachBookingFields[7]),
                        // TotalCost - 8
                        Float.parseFloat(eachBookingFields[8]),
                        // WashTime - 9
                        Integer.parseInt(eachBookingFields[9]),
                        // DryTime - 10
                        Integer.parseInt(eachBookingFields[10]),
                        // Pick - true
                        Boolean.parseBoolean(eachBookingFields[11]),
                        // pick time - 10
                        Integer.parseInt(eachBookingFields[12]),
                        // Deliver - true
                        Boolean.parseBoolean(eachBookingFields[13]),
                        // deliver time - 10
                        Integer.parseInt(eachBookingFields[14]),
                        // Status - CONFIRM
                        BookingStatus.valueOf(eachBookingFields[15]),
                        // Rating - 0
                        Float.parseFloat(eachBookingFields[16]),
                        // Remarks - test
                        eachBookingFields[17]
                );
                bookings.add(eachBooking);
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        } finally {
            try{
                inputStream.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private void readTimeDurationsFromCSV(){
        //read users from users.csv
        timeDurations = new ArrayList<>();
        String inputLine;
        InputStream inputStream = getResources().openRawResource(R.raw.times);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try{
            if((inputLine = reader.readLine()) !=null){
                // header lin is contained in inputLine
            }
            while ((inputLine = reader.readLine()) != null){
                String timeField = inputLine;
                TimeDuration eachTime = new TimeDuration(Integer.parseInt(timeField));
                timeDurations.add(eachTime);
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        } finally {
            try{
                inputStream.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public void googleSignIn(View view){
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                    null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e("BBL", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        Log.d("BBL", "onFailure " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    String username = credential.getId();
                    String password = credential.getPassword();
                    showMessage("Google User authenticated " + username );
                    validateGoogleSignInUser(username);
                    if (idToken !=  null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with your backend.
                        Log.d("BBL", "Got ID token.");
                    } else if (password != null) {
                        // Got a saved username and password. Use them to authenticate
                        // with your backend.
                        Log.d("BBL", "Got password.");
                    }
                } catch (ApiException e) {
                    showMessage("Google sign in failed " + e.getMessage() );
                    //showMessage("Google sign in failed " + e.getMessage() );
                }
                break;
        }
    }
}