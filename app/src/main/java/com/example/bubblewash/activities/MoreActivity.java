package com.example.bubblewash.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bubblewash.R;
import com.example.bubblewash.adapters.MoreOptionAdapter;
import com.example.bubblewash.model.MoreOption;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoreActivity extends AppCompatActivity {

    List<String> optionNames = new ArrayList<>(Arrays.asList("History", "My Account", "Manage Cards", "Charts", "About Us", "Sign out"));
    List<Integer> optionIcons = new ArrayList<>(Arrays.asList(R.drawable.baseline_history_24, R.drawable.baseline_manage_accounts_24, R.drawable.baseline_credit_card_24, R.drawable.baseline_insert_chart_outlined_24, R.drawable.baseline_supervised_user_circle_24, R.drawable.baseline_logout_24));
    List<MoreOption> optionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        handleNavigation();
        loadOptionData();

        ListView listViewMoreOptions = findViewById(R.id.listViewMoreOptions);
        MoreOptionAdapter moreOptionAdapter = new MoreOptionAdapter(optionList);
        listViewMoreOptions.setAdapter(moreOptionAdapter);

        listViewMoreOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(), MyAccountActivity.class));
                        break;
                    case 2:
                        Toast.makeText(MoreActivity.this, "Clicked on Manage Cards", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), AddCardActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(), ChartsActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
                        break;
                    case 5:
                        signout();
                        break;

                }
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

    private void aboutUs(){
        SharedPreferences settings = getSharedPreferences("PREFS_BBW", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("IS_LOGGED", false);
        editor.putString("USERID", "");
        editor.putString("USERNAME", "");
        editor.putString("PASSWORD", "");
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    private void handleNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_more);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.bottom_home){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if(item.getItemId() == R.id.bottom_track){
                startActivity(new Intent(getApplicationContext(), TrackActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if(item.getItemId() == R.id.bottom_more){
                return true;
            }
            return false;
        });
    }

    private void loadOptionData(){
        for(int i=0; i<optionNames.size(); i++){
            MoreOption option = new MoreOption(optionNames.get(i), optionIcons.get(i));
            optionList.add(option);
        }
    }
}