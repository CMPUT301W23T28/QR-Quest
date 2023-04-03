package com.example.qr_quest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

/**
 * This class is an activity for opening the device camera to add a photo. It handles
 * taking a picture, converting the captured image to a base64 string, and storing the image in the
 * scanned QR object.
 */
public class AddPhotoActivity extends AppCompatActivity {

    private QR scannedQR;

    /**
     * The method to initiate the activity and open the camera using intent
     *
     *  @param savedInstanceState
     *      the last saved instance state of the Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo);
        scannedQR = (QR) getIntent().getSerializableExtra("scannedQR");

        // Creating an intent to start the camera app to take a picture
        Intent takePhotoIntent = new Intent("android.media.action.IMAGE_CAPTURE");

        // Launching the camera app with the intent and registering a result launcher to handle the result
        takePictureLauncher.launch(takePhotoIntent);
    }

    /**
     * Result launcher for taking a picture with the camera app. It handles the result
     * and stores the captured image in the scanned QR object.
     */
    ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData()!= null) {
                    Intent data = result.getData();
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    // To convert the photo captured to string
                    String capturedImage = toBase64(photo);
                    scannedQR.setImgString(capturedImage);
                    new GeoLocationFragment(scannedQR).show(getSupportFragmentManager(), "Ask for photo");
                    //add the photo to database

                } else {
                    new GeoLocationFragment(scannedQR).show(getSupportFragmentManager(), "Ask for photo");
                }
            });

    /**
     * The function converts the Bitmap image captured into string
     *
     * @param bm
     *      Bitmap which contains the photo
     * @return
     *      Returns the string which contains image
     */
    public String toBase64(Bitmap bm) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        int sizeInBytes = array.size();
        int sizeInKiloBytes = sizeInBytes/1024;
        // if the size of image is greater than 100kb, than it is compress to 100kb
        if (sizeInKiloBytes > 100){
            int newResolution =  100*100/sizeInKiloBytes;
            bm.compress(Bitmap.CompressFormat.PNG, newResolution, array);
        }
        bm.compress(Bitmap.CompressFormat.PNG, 100, array);
        byte[] b = array.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }
}
