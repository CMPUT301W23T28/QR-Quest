package com.example.qrquest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

//    private Button cameraButton;
//    private AddPhoto photoHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        cameraButton = (Button) findViewById(R.id.camera_button);
//        photoHelper = new AddPhoto();
//
//        cameraButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                photoHelper.takePicture(MainActivity.this);
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == AddPhoto.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bitmap capturedImage = photoHelper.getCapturedImage(data);
//
//            // Do whatever you want with the captured image
//        }
//    }

//
//    private static final int pic_id = 123;
//    private static final int REQUEST_CAMERA_PERMISSION = 1;
//    // Define the button and imageview type variable
//    Button camera_open_id;
//    ImageView click_image_id;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // By ID we can get each component which id is assigned in XML file get Buttons and imageview.
//        camera_open_id = findViewById(R.id.camera_button);
//        click_image_id = findViewById(R.id.click_image);
//
//        // Camera_open button is for open the camera and add the setOnClickListener in this button
//        camera_open_id.setOnClickListener(v -> {
//            // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
//            askCameraPermission();
//
//        });
//    }
//
//    // This method will help to retrieve the image
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // Match the request 'pic id with requestCode
//        if (requestCode == pic_id) {
//            // BitMap is data structure of image file which store the image in memory
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            // Set the image in imageview for display
//            click_image_id.setImageBitmap(photo);
//        }
//    }
//
//    private void askCameraPermission(){
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
//        }else{
//            openCamera();
//        }
//    }
//
//    private void openCamera() {
//        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Start the activity with camera_intent, and request pic id
//        startActivityForResult(camera_intent, pic_id);
//    }


    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Button takePictureButton;
    private ImageView imageView;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePictureButton = findViewById(R.id.camera_button);
        imageView = findViewById(R.id.click_image);

        // Set up the permission launcher
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        launchTakePicture();
                    } else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                });

        // Set up the take picture launcher


        takePictureButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                // Permission is already granted, launch the camera
                launchTakePicture();
            } else {
                // Request the permission
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });
    }

    private void launchTakePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureLauncher.launch(takePictureIntent);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == RESULT_OK && result.getData()!= null) {
            // Image captured and saved to fileUri specified in the Intent
            Uri imageUri = result.getData().getData();
            imageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Picture not taken", Toast.LENGTH_SHORT).show();
        }
    });
}