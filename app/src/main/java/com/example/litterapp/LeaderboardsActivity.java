package com.example.litterapp;

import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LeaderboardsActivity extends AppCompatActivity {

    Button showMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);

        Button leaderboardsButtonBack = findViewById(R.id.leaderboardsButtonBack);

        leaderboardsButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //get spinner from xml file.
        Spinner dropdown = findViewById(R.id.spinner1);
        //create list of regions for spinner
        String[] items = new String[]{"Americas", "Asia", "Europe", "Global", "United States"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
    }
}
