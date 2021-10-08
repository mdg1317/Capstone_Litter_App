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

        final ConstraintLayout layout = findViewById(R.id.layout);
        Button buttonBack = (Button) findViewById(R.id.button_back);
        Button buttonFinish = (Button) findViewById(R.id.button_finish);

        // Opens camera app when clicking camera button
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view){
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
        });

        // transition to menu screen when clicking on menu button
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view){
                setContentView(R.layout.activity_main);
            }
        });
    }
}
