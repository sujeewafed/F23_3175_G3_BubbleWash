package com.example.bubblewash.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bubblewash.R;
import com.example.bubblewash.databases.BubbleWashDatabase;
import com.example.bubblewash.model.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    BubbleWashDatabase bwd;
    User user;
    String imagePath;
    ImageView imageView;
    FloatingActionButton button;

    private static final String CHARACTERS = "0123456789";
    private static final int ID_LENGTH = 5;
    private static final SecureRandom random = new SecureRandom();

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
        imageView = findViewById(R.id.imgViewUserProfile);
        button = findViewById(R.id.floatingActionButtonPick);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(RegisterActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        bwd = Room.databaseBuilder(getApplicationContext(), BubbleWashDatabase.class, "bubblewash.db").build();
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    user = new User(generateID(), username.getText().toString(), password.getText().toString(), firstname.getText().toString(),
                            lastname.getText().toString(), mobile.getText().toString(), streetaddress.getText().toString(), imagePath);

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

    public static String generateID() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        Log.d("BUBBLE_WASH", "Image path : " + uri);
        imagePath = String.valueOf(uri);
        imageView.setImageURI(uri);
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