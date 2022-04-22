package com.example.litterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private String myUsername;
    TextView usernameTest;
    ScoreRequests scoreRequestObject = new ScoreRequests();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText username = findViewById(R.id.editTextUsername);
        Button loginButton = findViewById(R.id.buttonLogin);
        Button guestButton = findViewById(R.id.buttonGuest);
        Button registerButton = findViewById(R.id.buttonCreateUser);
        usernameTest = findViewById(R.id.test_username);

        // Checks database for login info
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view){
                myUsername = username.getText().toString();
                //usernameTest.setText(myUsername);

                postRequest(myUsername, URL);
            }
        });

        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view){
                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent1);
                //finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view){
                Intent intent2 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent2);
            }
        });

    }

    ConnectionInfo connectInfo = new ConnectionInfo();
    String address = connectInfo.getAddress();
    String route = "login";
    String URL = address + route;
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;

    private RequestBody buildRequestBody(String msg) {
        postBodyString = msg;
        mediaType = MediaType.parse("text/plain");
        requestBody = RequestBody.create(postBodyString, mediaType);
        return requestBody;
    }

    private void postRequest(String message, String URL) {
        RequestBody requestBody = buildRequestBody(message);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .header("Content-Type", "text/plain")
                .header("Accept", "*/*")
                .header("Connection", "keep-alive")
                .post(requestBody)
                .url(URL)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        call.cancel();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //try {
                            //Toast.makeText(LoginActivity.this, response.body().string(), Toast.LENGTH_LONG).show();
                        //} catch (IOException e) {
                            //e.printStackTrace();
                        //}
                        try {
                            String name = response.body().string();
                            if (name.equals(myUsername)) {
                                ScoreRequests.setUser(name);
                                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent1);
                            } else {
                                usernameTest.setText("Invalid username");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }
}