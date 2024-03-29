package com.example.qr_quest;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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
 * This Class is used to create a dialog fragment which is used to add comment and geolocation of
 * the scanned QR.
 */
public class GeoLocationFragment extends DialogFragment {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    QR scannedQR;

    /**
     * Constructor for the class
     *
     * @param scannedQR
     *      The Qr code that has been scanned by the user
     */
    public GeoLocationFragment(QR scannedQR){
        this.scannedQR = scannedQR;
    }

    /**
     * Used to create the dialog fragment to store location and add caption
     *
     * @param savedInstanceState
     *      The last saved instance state of the Fragment,
     *      or null if this is a freshly created Fragment.
     * @return
     *      Returns the dialog fragment
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_geolocation, null);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        TextView pointsTitle = view.findViewById(R.id.txtview_geo_points);
        CheckBox addGeoLocation = view.findViewById(R.id.chkbox_geo_addloc);
        EditText captionAdded = view.findViewById(R.id.edittext_geo_comment);
        Button saveButton = view.findViewById(R.id.btn_geo_save);

        pointsTitle.setText("You just scanned " + scannedQR.getQRName() + " for " + scannedQR.getScore() + " pts!");

        addGeoLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                getLocation();
            } else {
                deleteLocation();
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

        saveButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), QRActivity.class);

            if(!addGeoLocation.isChecked()) {
                deleteLocation();
            }

            UserDatabase userDatabase = new UserDatabase();
            scannedQR.setCaption(captionAdded.getText().toString());
            userDatabase.addQRCodeToUser(getContext(), scannedQR, success -> {
                if (success){
                    // If there was an error updating the QR code document, show an error message
                    intent.putExtra("scannedQR", scannedQR);
                    intent.putExtra("comingFromGeoLocation",true);
                    startActivity(intent);
                }
            });
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view).create();
    }

    /**
     * Function to ask for location permission to the user
     *
     * @return Return True if permission is granted, otherwise returns False
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
    private void getLocation() {
        // If permission is already granted, than get the present location else request for permission
        if (checkLocationPermission()) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            GeoLocation geolocation = new GeoLocation(getContext(), location);
                            double latitude = geolocation.getLatitude();
                            double longitude = geolocation.getLongitude();
                            String city;
                            try {
                                city = geolocation.getCity();
                            } catch (IOException e) {
                                Toast.makeText(requireContext(), "Location not added", Toast.LENGTH_SHORT).show();
                                throw new RuntimeException(e);
                            }
                            scannedQR.setLocation(latitude,longitude,city);
                        }
                    });
        } else {
            requestLocationPermission();
        }
    }

    private void deleteLocation() {
        scannedQR.setLocation(-999,-999, "");
    }
}