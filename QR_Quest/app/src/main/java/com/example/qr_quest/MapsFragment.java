package com.example.qr_quest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;



public class MapsFragment extends Fragment {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    Location currentLocation;
//    Marker marker;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        //        /**
//         * Manipulates the map once available.
//         * This callback is triggered when the map is ready to be used.
//         * This is where we can add markers or lines, add listeners or move the camera.
//         * In this case, we just add a marker near Sydney, Australia.
//         * If Google Play services is not installed on the device, the user will be prompted to
//         * install it inside the SupportMapFragment. This method will only be triggered once the
//         * user has installed Google Play services and returned to the app.
//         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            // Disable all the inbuilt markers (Chatgpt)
            googleMap.setMapStyle(new MapStyleOptions("[\n" +
                    "  {\n" +
                    "    \"featureType\": \"poi\",\n" +
                    "    \"elementType\": \"labels\",\n" +
                    "    \"stylers\": [\n" +
                    "      { \"visibility\": \"off\" }\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"featureType\": \"transit\",\n" +
                    "    \"stylers\": [\n" +
                    "      { \"visibility\": \"off\" }\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"featureType\": \"road\",\n" +
                    "    \"elementType\": \"labels.icon\",\n" +
                    "    \"stylers\": [\n" +
                    "      { \"visibility\": \"off\" }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "]"));
            if (currentLocation == null) {
                Toast.makeText(requireContext(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a LatLng object for the current location
            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

            // Move the camera to the user's current location and zoom in
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentLatLng)
                    .zoom(11.0f)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.moveCamera(cameraUpdate);
            BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.qr);

            LatLng location = new LatLng(53.523220, -113.526321);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .icon(markerIcon)
                    .anchor(0.5f,0.5f);

            googleMap.addMarker(markerOptions);

            googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    // TODO Auto-generated method stub
                    if(marker.equals(marker)){
                        new QRDialogFragment().show(getChildFragmentManager(), "QR Dialog");
                        return true;
                    }
                    return false;

                }
            });

            if (checkLocationPermission()){
                googleMap.setMyLocationEnabled(true);
            }
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if(isGranted) {
                        getCurrentLocationPermission();
                    } else {
                        Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
                    }
                });
        getCurrentLocationPermission();
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void getCurrentLocationPermission(){
        if (!checkLocationPermission()) {
            requestLocationPermission();
        }else{
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            currentLocation = location;
                            SupportMapFragment mapFragment =
                                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                            if (mapFragment != null) {
                                mapFragment.getMapAsync(callback);
                            }
                        }
                    });
        }
    }
}

//class="com.google.android.gms.maps.SupportMapFragment"