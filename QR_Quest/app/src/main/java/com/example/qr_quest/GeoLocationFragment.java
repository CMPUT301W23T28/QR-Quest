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
        View view = getLayoutInflater().inflate(R.layout.geoloc_fragment, null);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        TextView pointsTitle = view.findViewById(R.id.pointsTitle);
        Button addGeoLocation = view.findViewById(R.id.loc_button);
        EditText captionAdded = view.findViewById(R.id.commentQR);
        Button saveButton = view.findViewById(R.id.save_button);

        pointsTitle.setText("You just scanned " + scannedQR.getQRName() + " for " + scannedQR.getScore() + " pts!");
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                Intent intent = new Intent(getActivity(), QRActivity.class);
                intent.putExtra("scannedQR", scannedQR);
                startActivity(intent);
                Toast.makeText(getContext(), "Saved Caption", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view).create();
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
                                    Toast.makeText(requireContext(), "Location added", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    Toast.makeText(requireContext(), "Location not added", Toast.LENGTH_SHORT).show();
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