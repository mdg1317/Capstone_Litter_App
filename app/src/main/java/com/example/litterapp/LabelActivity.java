package com.example.litterapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
//import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LabelActivity extends AppCompatActivity {

    private static final int CAMERA_PERM_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    ImageView selectedImage;
    ScoreRequests scoreRequestObject = new ScoreRequests();
    private int s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);

        Button buttonBack = findViewById(R.id.button_back);
        Button buttonFinish = findViewById(R.id.button_finish);
        selectedImage = findViewById(R.id.image_display);

        // Open camera when label screen is reached, or when the back button is pressed
        openCamera();
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addScore("1");
                finish();
            }
        });
    }

    // https://www.youtube.com/watch?v=s1aOlr3vbbk
    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            selectedImage.setImageBitmap(image);
        }
    }

    // Score methods (set score and get score from database)
    // Post parameters: score (how many points to add), URL (where to send the request)

    // setScore method: adds the arg value to the score and current pet food count
    // Arguments: score to add (in String format, for example "1")
    // Returns: NA
    public void addScore(String score) {
        postRequest(score, postURL);
        postRequestUpdatePetFood(score, postURLUpdatePetFood);
    }

    // getScore method:
    // Arguments: NA
    // Returns: Currently logged in user's score
    public int getScore() {
        postRequest("0", getURL);
        return s;
    }

    ConnectionInfo connectInfo = new ConnectionInfo();
    String address = connectInfo.getAddress();
    String postRoute = "addscore";
    String postRouteUpdatePetFood = "updatepetfood";
    String getRoute = "getscore";
    String postURL = address + postRoute;
    String getURL = address + getRoute;
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
                            s = Integer.parseInt(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    // Send post request to update/add to the score
    private void postRequestUpdatePetFood(String value, String URL) {
        RequestBody requestBody = buildRequestBody(value);
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

}


