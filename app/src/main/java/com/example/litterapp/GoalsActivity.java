package com.example.litterapp;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.StringTokenizer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.lang.Math;

public class GoalsActivity extends AppCompatActivity {

    TextView goal1;
    TextView goal2;
    TextView goal3;
    TextView progress1;
    TextView progress2;
    TextView progress3;
    TextView amount1;
    TextView amount2;
    TextView amount3;

    ConnectionInfo connectInfo = new ConnectionInfo();
    String address = connectInfo.getAddress();
    String route = "updateGoal";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        // Match items in XML
        Button goalsButtonBack = findViewById(R.id.goalsButtonBack);
        Button goalsButtonGenerate = findViewById(R.id.goalsButtonGenerate);
        goal1 = findViewById(R.id.goal1);
        goal2 = findViewById(R.id.goal2);
        goal3 = findViewById(R.id.goal3);
        progress1 = findViewById(R.id.progress1);
        progress2 = findViewById(R.id.progress2);
        progress3 = findViewById(R.id.progress3);
        amount1 = findViewById(R.id.amount1);
        amount2 = findViewById(R.id.amount2);
        amount3 = findViewById(R.id.amount3);

        postRequest( "1_get", URL);
        postRequest( "2_get", URL);
        postRequest( "3_get", URL);

        // transition to menu screen when clicking on menu button
        goalsButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // generate new set of goals (might be temporary)
        goalsButtonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Generate a new goal and send to server
                postRequest( "1_" + generateGoal(), URL);
                postRequest( "2_" + generateGoal(), URL);
                postRequest( "3_" + generateGoal(), URL);
            }
        });
    }

    private void postRequest(String message, String URL) {
        RequestBody requestBody = buildRequestBody(message);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .post(requestBody)
                .url(URL)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoalsActivity.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        call.cancel();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String reply = response.body().string();
                            // Server will always return information about specified goal, update the page with this info
                            StringTokenizer st = new StringTokenizer(reply);
                                switch (parseInt(st.nextToken("_"))) {
                                    case 1:
                                        goal1.setText(st.nextToken());
                                        progress1.setText(st.nextToken());
                                        amount1.setText("/ " + st.nextToken());
                                        break;
                                    case 2:
                                        goal2.setText(st.nextToken());
                                        progress2.setText(st.nextToken());
                                        amount2.setText("/ " + st.nextToken());
                                        break;
                                    case 3:
                                        goal3.setText(st.nextToken());
                                        progress3.setText(st.nextToken());
                                        amount3.setText("/ " + st.nextToken());
                                        break;
                                }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    private String generateGoal() {
        int amount = (int)(Math.random() * 4) + 1;
        int task = (int)(Math.random() * 5);
        switch (task) {
            case 0: // Take Pictures
                return "Take " + amount + " pictures_0_" + amount;
            case 1: // Discard Butts/ Bottles/ Wrappers/ Bag/ Can
                return "Discard " + amount + " butts_0_" + amount;
            case 2:
                return "Discard " + amount + " bottles_0_" + amount;
            case 3:
                return "Discard " + amount + " wrappers_0_" + amount;
            case 4:
                return "Discard " + amount + " bags_0_" + amount;
            case 5:
                return "Discard " + amount + " cans_0_" + amount;
//            case 6: // Feed your pet
//                return "Feed your pet " + amount + " times_0_" + amount;
            default:
                return "Error";
        }
    }
}