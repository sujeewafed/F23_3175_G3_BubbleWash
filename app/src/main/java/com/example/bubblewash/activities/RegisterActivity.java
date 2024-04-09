package com.example.bubblewash.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bubblewash.R;
import com.example.bubblewash.databases.BubbleWashDatabase;
import com.example.bubblewash.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    //ActivityRegisterBinding binding;
    BubbleWashDatabase bwd;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText firstname =  findViewById(R.id.editTxtFirstName);
        EditText lastname = findViewById(R.id.editTxtLastName);
        EditText username = findViewById(R.id.editTxtUserName);
        EditText password = findViewById(R.id.editTxtPassword);
        EditText mobile =findViewById(R.id.editTxtMobile);
        EditText streetaddress = findViewById(R.id.editTxtAddress);


        bwd = Room.databaseBuilder(getApplicationContext(), BubbleWashDatabase.class, "bubblewash.db").build();

        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {



                    user.setFirstName(firstname.getText().toString());
                    user.setLastName(lastname.getText().toString());
                    user.setMobileNumber(mobile.getText().toString());
                    user.setUserName(username.getText().toString());
                    user.setAddress(streetaddress.getText().toString());
                    user.setPassword(password.getText().toString());

                    if(validateInput(user)){
                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {


                                bwd.userDAO().insertOneUser(user);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(RegisterActivity.this,
                                                "Thank you for registering with BubbleWash !", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                                    }
                                });

                            }
                        });
                    }else{
                        Toast.makeText(RegisterActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                    }



                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        });


    }
    private Boolean validateInput(User user){
        if(user.getFirstName().isEmpty()||
                user.getLastName().isEmpty()||
                user.getMobileNumber().isEmpty()||
                user.getUserName().isEmpty()||
                user.getAddress().isEmpty()||
                user.getPassword().isEmpty()
        ){
            return false;
        }

        return true;

    }
}