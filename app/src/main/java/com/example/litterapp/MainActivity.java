package com.example.litterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.shapes.Shape;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    //private FusedLocationProviderClient fusedLocationClient;

    /*private String url = "https://flask-basic-server-test.herokuapp.com/";
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;*/
    private Button connectButton;
    private ScoreRequests scoreRequestObject = new ScoreRequests();
    private int s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ConstraintLayout layout = findViewById(R.id.leaderboards_text);
        Button buttonMenu = findViewById(R.id.buttonMenu);
        ImageButton buttonCamera = findViewById(R.id.buttonCamera);
        //TextView coordDisplay = findViewById(R.id.coordDisplay);

        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScore();
            }
        });

        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //Got last known location
                if (location != null) {
                    coordDisplay.setText("Got location!");
                } else {
                    coordDisplay.setText("Cannot get location!");
                }
            }
        });*/

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

    @Override
    public void onMapReady(GoogleMap map) {
        map.getUiSettings().setCompassEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setBuildingsEnabled(false);
        LatLng myPosition = new LatLng(45.575856, -122.730537);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myPosition)
                .zoom(120)
                .tilt(67.5f)
                .bearing(314)
                .build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        map.addMarker(new MarkerOptions()
                .position(myPosition)
                .title("My Position"));
    }

    // showMenu: displays a popup menu when the menu button is tapped
    private void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            // Create intents in order to run new activities
            Intent intent1 = new Intent(MainActivity.this, RewardsActivity.class);
            Intent intent2 = new Intent(MainActivity.this, LeaderboardsActivity.class);
            Intent intent3 = new Intent(MainActivity.this, SettingsActivity.class);
            Intent intent4 = new Intent(MainActivity.this, LoginActivity.class);
            Intent intent5 = new Intent(MainActivity.this, GoalsActivity.class);

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.buttonRewards) {
                    startActivity(intent1);
                    return true;
                } else if (item.getItemId() == R.id.buttonLeaderboards) {
                    startActivity(intent2);
                    return true;
                } else if (item.getItemId() == R.id.buttonSettings) {
                    startActivity(intent3);
                    return true;
                } else if (item.getItemId() == R.id.buttonLogout) {
                    startActivity(intent4);
                    return true;
                } else if (item.getItemId() == R.id.buttonGoals) {
                    startActivity(intent5);
                    return true;
                }
                return true;
            }
        });

        popupMenu.show();
    }

    /*private RequestBody buildRequestBody(String msg) {
        postBodyString = msg;
        mediaType = MediaType.parse("text/plain");
        requestBody = RequestBody.create(postBodyString, mediaType);
        return requestBody;
    }

    // Send a request to the server, currently doesn't send a POST request
    private void getRequest(String message, String URL) {
        RequestBody requestBody = buildRequestBody(message);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                //.post(requestBody)
                .url(URL)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(MainActivity.this, response.body().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }*/
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
        postRequest("0", getURL);
        return 0;
    }

    String address = "http://192.168.1.34:5000/";
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
                        try {
                            Toast.makeText(MainActivity.this, "Score:" + " " + response.body().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
