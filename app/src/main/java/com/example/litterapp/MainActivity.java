package com.example.litterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ConstraintLayout layout = findViewById(R.id.leaderboards_text);
        Button buttonMenu = findViewById(R.id.buttonMenu);
        ImageButton buttonCamera = findViewById(R.id.buttonCamera);

        // transition to menu screen when clicking on menu button
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view){
                //show Popup menu
                showMenu(view);
            }
        });

        // Opens camera app when clicking camera button
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view){
                //setContentView(R.layout.activity_label);
                Intent label = new Intent(MainActivity.this, LabelActivity.class);
                startActivity(label);
            }
        });

        // change background color to white when clicking on main screen
        // TEMPORARY
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view){
                layout.setBackgroundColor(Color.WHITE);
            }
        });
    }

    // showMenu: displays a popup menu when the menu button is tapped
    private void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            // Create intents in order to run new activities
            //Intent intent1 = new Intent(MainActivity.this, RewardsActivity.class);
            //Intent intent2 = new Intent(MainActivity.this, LeaderboardsActivity.class);
            //Intent intent3 = new Intent(MainActivity.this, SettingsActivity.class);
            Intent intent4 = new Intent(MainActivity.this, LabelActivity.class);

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.buttonRewards)
                    //setContentView(R.layout.activity_rewards);
                    //return true;
                if (item.getItemId() == R.id.buttonLeaderboards)
                    //startActivity(intent2);
                    //return true;
                if (item.getItemId() == R.id.buttonSettings)
                    //startActivity(intent3);
                    return true;

                return true;
            }
        });

        popupMenu.show();
    }
}
