package com.example.qrquest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AddPhoto extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public void takePicture(Activity activity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public Bitmap getCapturedImage(Intent data) {
        Bundle extras = data.getExtras();
        return (Bitmap) extras.get("data");
    }
}
