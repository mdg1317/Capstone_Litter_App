package com.example.litterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LabelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);

        final ConstraintLayout layout = findViewById(R.id.leaderboards_text);
        Button buttonBack = findViewById(R.id.button_back);

        // Go back to main activity when back button is pressed
        //buttonBack.setOnClickListener(view -> setContentView(R.layout.activity_main));

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
<<<<<<< HEAD
            public void onClick(View v) {
                finish();
=======
            public void onClick( View view){
                setContentView(R.layout.activity_menu);
>>>>>>> parent of 042f219 (Renamed Resource)
            }
        });
    }
}
