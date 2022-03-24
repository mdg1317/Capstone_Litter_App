package com.example.litterapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScoreRequests extends AppCompatActivity {

    private static int score;
    private static String user; // currently logged in username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Score methods (set score and get score from database)
    // Post parameters: score (how many points to add), URL (where to send the request)

    // setScore method:
    // Arguments: score to add (in String format, for example "1")
    // Returns: NA
    public void addScore(String score) {
        postRequest(score, postURL);
    }

    // getScore method:
    // Arguments: NA
    // Returns: Currently logged in user's score
    public int getScore() {
        getRequest(getURL);
        return score;
    }

    // Setter/getter methods for username
    public static void setUser(String username) {
        user = username;
    }
    public static String getUser() {
        return user;
    }

    ConnectionInfo connectInfo = new ConnectionInfo();
    String address = connectInfo.getAddress();
    String postRoute = "addscore";
    String getRoute = "getscore";
    String postURL = address + postRoute;
    String getURL = address + getRoute;
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;

    private RequestBody buildRequestBody(String msg) {
        postBodyString = msg;
        mediaType = MediaType.parse("text/plain");
        requestBody = RequestBody.create(postBodyString, mediaType);
        return requestBody;
    }

    // Send post request to update/add to the score
    private void postRequest(String score, String URL) {
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

                    }
                });
            }
        });
    }

    // Send get request to retrieve the score
    private void getRequest(String URL){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .get()
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
                            score = Integer.parseInt(response.body().string()); // convert score to integer
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }



}
