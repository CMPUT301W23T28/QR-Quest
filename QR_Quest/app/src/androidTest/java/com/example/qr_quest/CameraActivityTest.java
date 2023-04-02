package com.example.qr_quest;
import android.app.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.robotium.solo.Solo;

import android.provider.Settings;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.fragment.app.FragmentActivity;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

@RunWith(AndroidJUnit4.class)
public class CameraActivityTest {

    private Solo solo;
    private String sha_256_string;

    private static final int PERMISSION_REQUEST_CODE = 100;

    @Rule
    public ActivityTestRule<CameraActivity> rule =
            new ActivityTestRule<>(CameraActivity.class, true, true);


    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), (FragmentActivity) rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkQRScan(){
        solo.assertCurrentActivity("Wrong Activity", CameraActivity.class);
        sha_256_string = "hello";
        QR QR_code = new QR(sha_256_string);
        rule.getActivity().runOnUiThread(() -> {
            Toast.makeText(rule.getActivity(), QR_code.getHashValue(), Toast.LENGTH_SHORT).show();
        });
        new QRFragment(QR_code).show(rule.getActivity().getSupportFragmentManager(), "Ask for photo");
    }

    @Test
    public void testOnRequestPermissionsResult() {
        // Grant permission
        rule.getActivity().runOnUiThread(() -> rule.getActivity().onRequestPermissionsResult(
                PERMISSION_REQUEST_CODE,
                new String[]{android.Manifest.permission.CAMERA},
                new int[]{PackageManager.PERMISSION_GRANTED}));

//        assertTrue(solo.waitForText("Permission granted!", 1, 2000));

        // Deny permission
        rule.getActivity().runOnUiThread(() -> rule.getActivity().onRequestPermissionsResult(
                PERMISSION_REQUEST_CODE,
                new String[]{android.Manifest.permission.CAMERA},
                new int[]{PackageManager.PERMISSION_DENIED}));

//        assertTrue(solo.waitForText("Permission denied", 1, 2000));
        assertTrue(solo.waitForText("Camera permission is required to scan QR codes. Grant the permission manually from the app settings.", 1, 2000));

        solo.clickOnButton("Open Settings");
//        assertTrue(solo.waitForActivity(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, 2000));
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }



}