package com.example.litterapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class LeaderboardsActivity extends AppCompatActivity {

    Button showMenu;
    TableLayout table;
    TextView name1;

    // JSON variables
    private JSONArray jArray;
    private String userTableString; // variable for storing the returned user table JSON as a string
    private TreeMap<String, Integer> userData = new TreeMap<>();

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
        //Spinner dropdown = findViewById(R.id.spinner1);
        //create list of regions for spinner
        String[] items = new String[]{"Americas", "Asia", "Europe", "Global", "United States"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        //dropdown.setAdapter(adapter);

        String myUsername = "Hey! This is pointless";
        postRequest(myUsername, URL);
    }

    ConnectionInfo connectInfo = new ConnectionInfo();
    String address = connectInfo.getAddress();
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
                        try {
                            //Toast.makeText(LeaderboardsActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                            userTableString = response.body().string();
                            jArray = new JSONArray(userTableString); // entire user table is a JSONArray (each user is an individual jsonObject)

                            // populate leaderboard
                            for (int i = 0; i < jArray.length(); i++){
                                JSONObject jObject = jArray.getJSONObject(i); // gets individual user objects
                                userData.put(jObject.getString("username"), jObject.getInt("score"));
                            }

                            Map sortedData = valueSort(userData);
                            Set set = sortedData.entrySet();
                            Iterator i = set.iterator();
                            // Go through sorted data and fill in leaderboard

                            TextView [] nameViews = new TextView[10];
                            TextView name1 = findViewById(R.id.name1);
                            TextView name2 = findViewById(R.id.name2);
                            TextView name3 = findViewById(R.id.name3);
                            TextView name4 = findViewById(R.id.name4);
                            TextView name5 = findViewById(R.id.name5);
                            TextView name6 = findViewById(R.id.name6);
                            TextView name7 = findViewById(R.id.name7);
                            TextView name8 = findViewById(R.id.name8);
                            TextView name9 = findViewById(R.id.name9);
                            TextView name10 = findViewById(R.id.name10);
                            TextView [] scoreViews = new TextView[10];
                            TextView score1 = findViewById(R.id.points1);
                            TextView score2 = findViewById(R.id.points2);
                            TextView score3 = findViewById(R.id.points3);
                            TextView score4 = findViewById(R.id.points4);
                            TextView score5 = findViewById(R.id.points5);
                            TextView score6 = findViewById(R.id.points6);
                            TextView score7 = findViewById(R.id.points7);
                            TextView score8 = findViewById(R.id.points8);
                            TextView score9 = findViewById(R.id.points9);
                            TextView score10 = findViewById(R.id.points10);

                            // username TextViews
                            nameViews[0] = name1;
                            nameViews[1] = name2;
                            nameViews[2] = name3;
                            nameViews[3] = name4;
                            nameViews[4] = name5;
                            nameViews[5] = name6;
                            nameViews[6] = name7;
                            nameViews[7] = name8;
                            nameViews[8] = name9;
                            nameViews[9] = name10;
                            // point TextViews
                            scoreViews[0] = score1;
                            scoreViews[1] = score2;
                            scoreViews[2] = score3;
                            scoreViews[3] = score4;
                            scoreViews[4] = score5;
                            scoreViews[5] = score6;
                            scoreViews[6] = score7;
                            scoreViews[7] = score8;
                            scoreViews[8] = score9;
                            scoreViews[9] = score10;

                            int counter = 0;
                            while (i.hasNext()) {

                                Map.Entry mp = (Map.Entry)i.next();
                                nameViews[counter].setText(mp.getKey().toString());
                                scoreViews[counter].setText(mp.getValue().toString());
                                counter++;
                            }

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /*
    valueSort(): function to sort the user data by score
    Resources: https://www.geeksforgeeks.org/how-to-sort-a-treemap-by-value-in-java/
     */
    public static <K, V extends Comparable<V> > Map<K, V>
    valueSort(final Map<K, V> map)
    {
        // Static Method with return type Map and
        // extending comparator class which compares values
        // associated with two keys
        Comparator<K> valueComparator = new Comparator<K>() {

            // return comparison results of values of
            // two keys
            public int compare(K k1, K k2)
            {
                int comp = map.get(k1).compareTo(map.get(k2));
                if (comp == 0)
                    return 1;
                else
                    return -comp;
            }

        };

        // SortedMap created using the comparator
        Map<K, V> sorted = new TreeMap<K, V>(valueComparator);

        sorted.putAll(map);

        return sorted;
    }
}
