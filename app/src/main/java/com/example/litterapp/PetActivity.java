package com.example.litterapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PetActivity extends AppCompatActivity {

    private int foodCount = 0;
    private int level = 1;
    private int currentExp = 0;
    private int requiredExp = level * 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        Button feedButton = findViewById(R.id.feed_button);
        ProgressBar expBar = (ProgressBar) findViewById(R.id.expBar);

        // get pet data from database and store in variables
        updateAllValues();

        this.requiredExp = level * 100;

        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodCount <= 0){
                    // do nothing
                } else if (foodCount > 0) {
                    //foodCount--;
                    postRequestPetFood("-1", postURLUpdatePetFood);
                    setFoodCount(foodCount);

                    // add exp
                    //currentExp += 10;
                    postRequestPetExp("10", postURLUpdatePetExp);
                    expBar.setProgress(currentExp);

                    // if max exp, increase level
                    if (currentExp >= requiredExp) {
                        //currentExp = currentExp - requiredExp; // reset curr exp
                        postRequestPetExp("-" + currentExp, postURLUpdatePetExp);
                        //level++;
                        postRequestPetLevel("1", postURLUpdatePetLevel);
                        updateAllValues();
                        requiredExp = level * 100;
                        // update progress bar
                        setLevel(level);
                        expBar.setMax(requiredExp);
                        expBar.setProgress(currentExp);
                    }
                }
            }
        });

        Button backButton = findViewById(R.id.backButton);
        // return to menu when back button is clicked
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setLevel(int lvl){
        TextView levelDisplay = findViewById(R.id.levelView);
        levelDisplay.setText("LVL " + lvl);
    }

    private void setFoodCount(int food){
        TextView foodDisplay = findViewById(R.id.food_count);
        foodDisplay.setText("Remaining food: " + food);
    }

    public void updateAllValues(){
        ProgressBar expBar = (ProgressBar) findViewById(R.id.expBar);

        postRequestPetLevel("0", postURLPetLevel);
        postRequestPetExp("0", postURLPetExp);
        postRequestPetFood("0", postURLPetFood);
        this.requiredExp = level * 100;
        setFoodCount(foodCount);
        setLevel(level);
        expBar.setMax(requiredExp);
        expBar.setProgress(currentExp);
    }

    private void updateLevel(int value){
        this.level = value;
        this.requiredExp = this.level * 100;
    }

    private void updateExp(int value){
        this.currentExp = value;

    }

    private void updateFood(int value){
        this.foodCount = value;
    }


    ConnectionInfo connectInfo = new ConnectionInfo();
    String address = connectInfo.getAddress();
    String postRoutePetLevel = "getpetlevel";
    String postRoutePetExp = "getpetcurrentexp";
    String postRoutePetFood = "getpetcurrentfood";
    String postRouteUpdatePetLevel = "updatepetlevel";
    String postRouteUpdatePetExp = "updatepetexp";
    String postRouteUpdatePetFood = "updatepetfood";
    String postURLPetLevel = address + postRoutePetLevel;
    String postURLPetExp = address + postRoutePetExp;
    String postURLPetFood = address + postRoutePetFood;
    String postURLUpdatePetLevel = address + postRouteUpdatePetLevel;
    String postURLUpdatePetExp = address + postRouteUpdatePetExp;
    String postURLUpdatePetFood = address + postRouteUpdatePetFood;
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;
    private RequestBody buildRequestBody(String msg) {
        postBodyString = msg;
        mediaType = MediaType.parse("text/plain");
        requestBody = RequestBody.create(postBodyString, mediaType);
        return requestBody;
    }
    // Send post request for pet level
    private void postRequestPetLevel(String value, String URL) {
        RequestBody requestBody = buildRequestBody(value);
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
                        call.cancel();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // set values from database
                        try {
                            updateLevel(Integer.parseInt(response.body().string()));
                            setLevel(level);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    // Send post request for pet exp
    private void postRequestPetExp(String value, String URL) {
        RequestBody requestBody = buildRequestBody(value);
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
                        call.cancel();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // set values from database
                        try {
                            updateExp(Integer.parseInt(response.body().string()));
                            ProgressBar expBar = (ProgressBar) findViewById(R.id.expBar);
                            requiredExp = level * 100;
                            expBar.setMax(requiredExp);
                            expBar.setProgress(currentExp);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    // Send post request for pet food
    private void postRequestPetFood(String value, String URL) {
        RequestBody requestBody = buildRequestBody(value);
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
                        call.cancel();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // set values from database
                        try {
                            updateFood(Integer.parseInt(response.body().string()));
                            setFoodCount(foodCount);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

}
