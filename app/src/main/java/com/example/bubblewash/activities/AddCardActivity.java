package com.example.bubblewash.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bubblewash.R;
import com.example.bubblewash.databases.BubbleWashDatabase;
import com.example.bubblewash.model.CardDetails;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddCardActivity extends AppCompatActivity {

    BubbleWashDatabase bubbleWashDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        EditText txtCardNumber = findViewById(R.id.editTxtCrdNumber);
        EditText txtExpDate = findViewById(R.id.ediTxtExpDate);
        EditText txtCvv = findViewById(R.id.editTxtCvv);
        EditText txtCardHolderName = findViewById(R.id.editTxtName);
        Button btnSubmitCard = findViewById(R.id.btnSubmitCard);

        btnSubmitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences settings = getSharedPreferences("PREFS_BBW", 0);
                String userId = settings.getString("USERID", "");

                CardDetails card = new CardDetails(txtCardNumber.getText().toString(), txtExpDate.getText().toString()
                                                    , txtCvv.getText().toString(), txtCardHolderName.getText().toString(), userId);

                bubbleWashDatabase = Room.databaseBuilder(getApplicationContext(), BubbleWashDatabase.class, "bubblewash.db").build();
                ExecutorService executorService = Executors.newSingleThreadExecutor();

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        bubbleWashDatabase.cardDetailsDao().insertCardDetails(card);
                        Log.d("DB",  " card added");

                    }
                });

                Toast.makeText(AddCardActivity.this, "Successfully added your card", Toast.LENGTH_SHORT).show();

                txtCardNumber.setText("");
                txtExpDate.setText("");
                txtCvv.setText("");
                txtCardHolderName.setText("");

            }


        });







    }
}