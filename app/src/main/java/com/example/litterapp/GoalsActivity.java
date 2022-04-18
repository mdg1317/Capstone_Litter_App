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
    Button button1;
    Button button2;
    Button button3;


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
        button1 = findViewById(R.id.button_collect1);
        button2 = findViewById(R.id.button_collect2);
        button3 = findViewById(R.id.button_collect3);

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

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Generate a new goal and send to server
                postRequest( "1_" + generateGoal(), URL);
                postRequestScore("10", connectInfo.getAddress() + "addscore");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Generate a new goal and send to server
                postRequest( "2_" + generateGoal(), URL);
                postRequestScore("10", connectInfo.getAddress() + "addscore");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Generate a new goal and send to server
                postRequest( "3_" + generateGoal(), URL);
                postRequestScore("10", connectInfo.getAddress() + "addscore");
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
                            StringTokenizer st = new StringTokenizer(reply,"_");
                            int goalNo = Integer.parseInt(st.nextToken());
                            String returnGoal = st.nextToken();
                            int returnProgress = Integer.parseInt(st.nextToken());
                            int returnAmount = Integer.parseInt(st.nextToken());
                            boolean enableButton = (returnProgress >= returnAmount);
                            switch (goalNo) {
                                case 1:
                                    goal1.setText(returnGoal);
                                    progress1.setText("" + returnProgress);
                                    amount1.setText("/ " + returnAmount);
                                    if (enableButton) {
                                        button1.setVisibility(View.VISIBLE);
                                        progress1.setVisibility(View.GONE);
                                        amount1.setVisibility(View.GONE);
                                    } else {
                                        button1.setVisibility(View.GONE);
                                        progress1.setVisibility(View.VISIBLE);
                                        amount1.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case 2:
                                    goal2.setText(returnGoal);
                                    progress2.setText("" + returnProgress);
                                    amount2.setText("/ " + returnAmount);
                                    if (enableButton) {
                                        button2.setVisibility(View.VISIBLE);
                                        progress2.setVisibility(View.GONE);
                                        amount2.setVisibility(View.GONE);
                                    } else {
                                        button2.setVisibility(View.GONE);
                                        progress2.setVisibility(View.VISIBLE);
                                        amount2.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case 3:
                                    goal3.setText(returnGoal);
                                    progress3.setText("" + returnProgress);
                                    amount3.setText("/ " + returnAmount);
                                    if (enableButton) {
                                        button3.setVisibility(View.VISIBLE);
                                        progress3.setVisibility(View.GONE);
                                        amount3.setVisibility(View.GONE);
                                    } else {
                                        button3.setVisibility(View.GONE);
                                        progress3.setVisibility(View.VISIBLE);
                                        amount3.setVisibility(View.VISIBLE);
                                    }
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

    // Send post request to update/add to the score
    private void postRequestScore(String score, String URL) {
        RequestBody requestBody = buildRequestBody(score);
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
                            int s = Integer.parseInt(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}