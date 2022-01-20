package com.example.litterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText username = findViewById(R.id.editTextUsername);
        Button loginButton = findViewById(R.id.buttonLogin);
        TextView usernameTest = findViewById(R.id.test_username);

        // Opens camera app when clicking camera button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view){
                String myUsername = username.getText().toString();
                usernameTest.setText(myUsername);
            }
        });
    }
}