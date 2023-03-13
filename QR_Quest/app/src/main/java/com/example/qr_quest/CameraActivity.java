package com.example.qr_quest;

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
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

/**
 * The CameraActivity is responsible for scanning QR codes using user's device camera.
 */
public class CameraActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private String sha_256_string;

    /**
     * Called when the activity is first created. This method initializes the camera scanner,
     * requests permission to use the camera if needed, and sets a callback to handle the scanned QR code.
     * @param
     *      savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        // Initialize the CodeScanner object with the scanner view
        mCodeScanner = new CodeScanner(this, scannerView);

        // Request permission to use the camera from the user if not already granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CODE);
        }

        // Set a decode callback to handle the scanned QR code
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                sha_256_string = result.toString();
                QR QR_code = new QR(sha_256_string);
                UserDatabase userDatabase = new UserDatabase();

                // Check if the qr has been scanned by user before
                userDatabase.addQRCodeToUser(CameraActivity.this, QR_code, success -> {
                    if (success) {
                        new QRFragment(QR_code).show(getSupportFragmentManager(), "Ask for photo");
                    } else {
                        scannerView.setEnabled(false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scannerView.setEnabled(true);
                                onResume();
                            }
                        }, 1500);
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    /**
     * Called when the result of a permission request is received. This method handles the response to
     * the camera permission request and displays an alert dialog if the user denies the request.
     * @param
     *      requestCode The request code passed in
     * @param
     *      permissions The requested permissions. Never null.
     * @param
     *      grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //If user presses allow
                Toast.makeText(getApplicationContext(), "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                //If user presses deny
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Camera permission is required to scan QR codes. Grant the permission manually from the app settings.")
                        .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                // Open the app settings
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            }
        }
    }

    /**
     * Called when the activity is resumed. This method starts the camera preview if it is not already started
     */
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    /**
     * Called when the activity is paused. This method releases the resources used by the code scanner to free up memory and prevent memory leaks.
     */
    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}