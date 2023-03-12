package com.example.qr_quest;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;

/**
 * This Class is used to create a dialog fragment which is used to add comment and geolocation of the scanned QR
 */
public class GeoLocationFragment extends DialogFragment {

    private static final int PERMISSION_REQUEST_CODE = 123;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    String caption;

    QR scannedQR;

    /**
     * Constructor for the class
     * @param scannedQR
     *      The Qr code that has been scanned by the user
     */
    public GeoLocationFragment(QR scannedQR){
        this.scannedQR = scannedQR;
    }

    /**
     * Used to create the dialog fragment to store location and add caption
     * @param savedInstanceState
     *      The last saved instance state of the Fragment,
     *      or null if this is a freshly created Fragment.
     * @return
     *      Returns the dialog fragment
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.add_geo_location_and_comment_fragment, null);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        Button addGeoLocation = view.findViewById(R.id.add_geo_location);
        EditText captionAdded = view.findViewById(R.id.comment_on_QR);
        caption = captionAdded.getText().toString();
        scannedQR.setCaption(caption);

        addGeoLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });

        // RequestPermissionLauncher is initialized
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if(isGranted) {
                        getLocation();
                    } else {
                        Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Saved Clicked", Toast.LENGTH_SHORT).show();

                        //add to QR page
                        // Check if the username is already taken by querying the "Users" collection
//                        UserDatabase userDatabaseRegister = new UserDatabase(getApplicationContext(), new_player);
//                        userDatabaseRegister.setRegistrationCallback(() -> {
//                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
//                            startActivity(intent);
//                        });
                    }
                }).create();
    }

    /**
     * Function to ask for location permission to the user
     * @return
     *      Return True if permission is granted, otherwise returns False
     */
    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Function used to launch the permission launcher
     */
    private void requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * Function used to get the location of the user and store it in the database
     */
    private void getLocation(){
        // If permission is already granted, than get the present location else request for permission
        if (checkLocationPermission()) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                GeoLocation geolocation = new GeoLocation(getContext(), location);
                                double latitude = geolocation.getLatitude();
                                double longitude = geolocation.getLongitude();
                                try {
                                    String city = geolocation.getCity();
                                    scannedQR.setLocation(latitude,longitude,city);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
        } else {
            requestLocationPermission();
        }
    }
}