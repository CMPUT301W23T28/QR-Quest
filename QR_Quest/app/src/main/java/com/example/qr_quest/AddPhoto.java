package com.example.qr_quest;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class AddPhoto extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo);
        intent = new Intent("android.media.action.IMAGE_CAPTURE");
        takePictureLauncher.launch(intent);
    }
    ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData()!= null) {
                    new GeoLocationFragment().show(getSupportFragmentManager(), "Ask for photo");
                    //add the photo to database

                } else {
                    new GeoLocationFragment().show(getSupportFragmentManager(), "Ask for photo");
                }
            });

}
