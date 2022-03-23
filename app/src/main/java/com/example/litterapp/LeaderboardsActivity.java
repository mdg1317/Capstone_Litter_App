package com.example.litterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LeaderboardsActivity extends AppCompatActivity {

    Button showMenu;
    TableLayout table;
    TextView name1;

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

        name1 = findViewById(R.id.name1);
        table = findViewById(R.id.leaderboardTable);
        //get spinner from xml file.
        Spinner dropdown = findViewById(R.id.spinner1);
        //create list of regions for spinner
        String[] items = new String[]{"Americas", "Asia", "Europe", "Global", "United States"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        String myUsername = "Hey! This is pointless";
        postRequest(myUsername, URL);
    }

    String address = "http://10.17.6.221:5000/";
    String route = "getleaderboard";
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
                .post(requestBody)
                .url(URL)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LeaderboardsActivity.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        call.cancel();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*for(int i = 0; i < table.getChildCount(); i++) {
                            View view = table.getChildAt(i);
                            if (view instanceof TableRow) {

                            }
                        }*/
                        /*try {
                            name1.setText(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                    }
                });
            }
        });
    }
}
