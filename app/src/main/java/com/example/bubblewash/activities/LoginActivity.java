package com.example.bubblewash.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bubblewash.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnSignIn = findViewById(R.id.buttonSignIn);
        Button btnRegister = findViewById(R.id.buttonRegister);

        EditText txtUserName = findViewById(R.id.editTextUserName);
        EditText txtPassword = findViewById(R.id.editTextPassword);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUserName.getText().equals("") && txtPassword.getText().equals("")) {
                    startActivity(new Intent(LoginActivity.this, BookingActivity.class));
                }
                else if (txtUserName.getText().equals("")){
                    Toast.makeText(LoginActivity.this, "Please enter your user name", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
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
}