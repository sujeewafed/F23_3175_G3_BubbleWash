package com.example.bubblewash.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bubblewash.R;
import com.example.bubblewash.databases.BubbleWashDatabase;
import com.example.bubblewash.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    List<User> users = new ArrayList<>();

    BubbleWashDatabase bubbleWashDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        readUsersFromCSV();
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
                            Log.d("DB User found : ", user.getUserName());
                            startActivity(new Intent(LoginActivity.this, BookingActivity.class));
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
                User eachUser = new User(eachUserFields[0],eachUserFields[1],eachUserFields[2]);
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
}