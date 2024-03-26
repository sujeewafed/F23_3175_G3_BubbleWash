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
                if (!txtUserName.getText().toString().isEmpty() && !txtPassword.getText().toString().isEmpty()) {
                    if (validateUser(txtUserName.getText().toString(), txtPassword.getText().toString())) {
                        startActivity(new Intent(LoginActivity.this, BookingActivity.class));
                    }
                    else{
                        txtUserName.setText("");
                        txtPassword.setText("");
                        txtUserName.requestFocus();
                        showMessage("Incorrect username or password. Try again.");
                    }
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

    private boolean validateUser(String userName, String password){
        if (userName.equals("admin") && password.equals("admin")) {
            return true;
        }
        else{
            return false;
        }
    }

    private void showMessage(String message){
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}