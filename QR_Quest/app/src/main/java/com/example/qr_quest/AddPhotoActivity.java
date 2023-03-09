package com.example.qr_quest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;


/**
 * Activity for adding a photo to a quest.
 */
public class AddPhotoActivity extends AppCompatActivity {

    private Intent takePhotoIntent;
    private static final int REQUEST_CODE_TAKE_PICTURE = 1;
    private QR scannedQR;
    private String capturedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo);
        Intent intent = getIntent();
        scannedQR = (QR) getIntent().getSerializableExtra("scannedQR");

        // Creating an intent to start the camera app to take a picture
        takePhotoIntent = new Intent("android.media.action.IMAGE_CAPTURE");

        // Launching the camera app with the intent and registering a result launcher to handle the result
        takePictureLauncher.launch(takePhotoIntent);
    }

    /**
     * Result launcher for taking a picture with the camera app.
     */
    ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData()!= null) {
                    Intent data = result.getData();
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    capturedImage = toBase64(photo);
                    scannedQR.setImgString(capturedImage);
                    new GeoLocationFragment(scannedQR).show(getSupportFragmentManager(), "Ask for photo");
                    //add the photo to database

                } else {
                    new GeoLocationFragment(scannedQR).show(getSupportFragmentManager(), "Ask for photo");
                }
            });

    public String toBase64(Bitmap bm) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        int sizeInBytes = array.size();
        int sizeInKiloBytes = sizeInBytes/1024;
        if (sizeInKiloBytes > 100){
            int newResolution =  100*100/sizeInKiloBytes;
            bm.compress(Bitmap.CompressFormat.PNG, newResolution, array);
        }
        bm.compress(Bitmap.CompressFormat.PNG, 100, array); //bm is the bitmap object
        byte[] b = array.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }
}
