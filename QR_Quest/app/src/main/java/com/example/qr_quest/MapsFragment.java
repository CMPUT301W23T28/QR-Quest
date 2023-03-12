package com.example.qr_quest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import androidx.appcompat.widget.SearchView;

import java.io.IOException;
import java.util.List;


public class MapsFragment extends Fragment {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    Location currentLocation;
//    Marker marker;

    SearchView searchView;


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
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
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

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // on below line we are getting the
                    // location name from search view.
                    String location = searchView.getQuery().toString();

                    // below line is to create a list of address
                    // where we will store the list of all address.
                    List<Address> addressList = null;

                    // checking if the entered location is null or not.
                    if (location != null || location.equals("")) {
                        // on below line we are creating and initializing a geo coder.
//                        Geocoder geocoder = new Geocoder(getContext());
//                        try {
//                            // on below line we are getting location from the
//                            // location name and adding that location to address list.
//                            addressList = geocoder.getFromLocationName(location, 1);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        // on below line we are getting the location
//                        // from our list a first position.
//                        Address address = addressList.get(0);
                        LatLng latLng;
                        if(location.equals("CrazyEightGlowStrongRockyMonster")){
                            latLng = new LatLng(53.523220, -113.526321);
                            // on below line we are adding marker to that position.
                            googleMap.addMarker(new MarkerOptions().position(latLng).title(location));

                            // below line is to animate camera to that position.
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        }else{
                            // on below line we are creating a variable for our location
                            // where we will add our locations latitude and longitude.
//                            latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            Toast.makeText(getContext(), "Enter a valid QR name", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        searchView = (SearchView) view.findViewById(R.id.idSearchView);
        return view;
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
