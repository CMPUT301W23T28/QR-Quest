package com.example.qr_quest;

import static android.content.Intent.getIntent;
import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AddPhotoActivityTest {

    private Solo solo;
    private QR scannedQR;
    private Intent takePhotoIntent;
    private Bitmap testBitmap;

    @Rule
    public ActivityTestRule<AddPhotoActivity> rule =
            new ActivityTestRule<>(AddPhotoActivity.class, true, true);


    // Launcher for camera intent
    @Test
    public void checkCamera() {
        // Creating an intent to start the camera app to take a picture
        takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Launching the camera app with the intent and registering a result launcher to handle the result
        rule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ActivityResultLauncher<Intent> takePictureLauncher = rule.getActivity().registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                                    Intent data = result.getData();
                                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                                    String capturedImage = rule.getActivity().toBase64(photo);
                                    scannedQR.setImgString(capturedImage);
                                    new GeoLocationFragment(scannedQR).show(rule.getActivity().getSupportFragmentManager(), "Ask for photo");
                                } else {
                                    new GeoLocationFragment(scannedQR).show(rule.getActivity().getSupportFragmentManager(), "Ask for photo");
                                }
                            }
                        });

                takePictureLauncher.launch(takePhotoIntent);
            }
        });
    }

    @Test
    public void testToBase64() {
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        String base64String = rule.getActivity().toBase64(bitmap);
        rule.getActivity().runOnUiThread(() -> {
            Toast.makeText(rule.getActivity(), base64String, Toast.LENGTH_SHORT).show();
        });
    }

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), (FragmentActivity) rule.getActivity());
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}

