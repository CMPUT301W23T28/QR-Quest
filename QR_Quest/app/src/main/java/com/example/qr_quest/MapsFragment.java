package com.example.qr_quest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
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

import androidx.appcompat.widget.SearchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * This class defines the google map and add a custom marker which points to the qr code scanned in user geolocation vicinity
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    private QR searchedQR;

    private Location currentLocation;
    private SearchView searchView;
    private Button filter;


    Marker selectedMarker;
    private String filterType = "By Name";

    public MapsFragment(){}

    /**
     * Creates a new instance of the MapsFragment with the specified QR object to be searched.
     *
     * @param searchedQR
     *      the QR object to be searched
     */
    public MapsFragment(QR searchedQR){
        this.searchedQR = searchedQR;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Disable all the inbuilt markers
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

        // Added the Qr marker
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.qr);

        List<QR> allQR = new ArrayList<>();
        List<Marker> allMarkers = new ArrayList<>();

        // Taking all the QRs from the database saved with geolocation and adding markers for them in map
        QRDatabase.getAllQRs(qrs -> {
            for (int i = 0; i < qrs.size(); i++ ) {
                QR qrToAdd = qrs.get(i);
                if (qrToAdd.getLatitude() != -999 && qrToAdd.getLongitude() != -999) {
                    LatLng qrLocation = new LatLng(qrToAdd.getLatitude(), qrToAdd.getLongitude());

                    String qrName = qrToAdd.getQRName();
                    MarkerOptions markerOptions1 = new MarkerOptions()
                            .position(qrLocation)
                            .icon(markerIcon)
                            .anchor(0.5f, 0.5f)
                            .title(qrName);

                    googleMap.addMarker(markerOptions1);
                    allMarkers.add(googleMap.addMarker(markerOptions1));
                    allQR.add(qrs.get(i));
                }
            }
            if (searchedQR != null){
                for (int i = 0; i < allMarkers.size(); i++) {
                    LatLng latLng = new LatLng(searchedQR.getLatitude(), searchedQR.getLongitude());
                    if (Objects.equals(allMarkers.get(i).getPosition(), latLng)){
                        selectedMarker = allMarkers.get(i);
                        selectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.highlightedqr));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                        markerClick(searchedQR, googleMap, selectedMarker);
                    }
                }
            }

        });

        // Setting up a onClickListener for the marker which opens up a CustomShowInfo to display name and QR
        googleMap.setOnMarkerClickListener(marker -> {
            if (selectedMarker != null) {
                selectedMarker.setIcon(markerIcon);
            }
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.highlightedqr));
            for (int i = 0; i < allQR.size(); i++) {
                LatLng latLng = new LatLng(allQR.get(i).getLatitude(), allQR.get(i).getLongitude());
                if (Objects.equals(marker.getPosition(), latLng)){
                    QR scannedQR = allQR.get(i);
                    markerClick(scannedQR, googleMap, marker);
                    return true;
                }
            }
            return false;
        });

        googleMap.setOnInfoWindowClickListener(marker -> {
            for (int i = 0; i < allQR.size(); i++) {
                LatLng latLng = new LatLng(allQR.get(i).getLatitude(), allQR.get(i).getLongitude());
                if (Objects.equals(marker.getPosition(), latLng)){
                    QR selectedQR = allQR.get(i);
                    openQRActivity(selectedQR);
                    return;
                }
            }
        });

        if (checkLocationPermission()){
            googleMap.setMyLocationEnabled(true);
        }

        // Enabling the option to zoom in the map using controls and gestures
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Setting up a onclicklistener for the filter which will show the pop-up menu
        filter.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), filter);
            popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.search_by_city:
                        filterType = "By City";
                        break;
                    case R.id.search_by_name:
                        filterType = "By Name";
                        break;
                }
                filter.setText(filterType);
                return true;
            });
            popupMenu.show();
        });

        // This the search view which is used to enable the searching of the map using qr name and city
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            QR targetQR = null;
            Marker selectedMarker = null;
            /**
             * This method is called when the user submits a search query.
             *
             * @param query
             *      the text entered by the user in the searchView
             * @return true if the search was successful, false otherwise
             */
            @Override
            public boolean onQueryTextSubmit(String query) {
                String enteredText = searchView.getQuery().toString();
                if (Objects.equals(filterType, "By Name")){

                    for (int i = 0; i < allQR.size(); i++) {
                        if (enteredText.equalsIgnoreCase(allQR.get(i).getQRName())) {
                            targetQR = allQR.get(i);
                            selectedMarker = allMarkers.get(i);
                        }
                    }
                    if (targetQR != null){
                        LatLng latLng = new LatLng(targetQR.getLatitude(), targetQR.getLongitude());
                        selectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.highlightedqr));
                        markerClick(targetQR, googleMap, selectedMarker);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                    }else{
                        Toast.makeText(getContext(), "QR not found!", Toast.LENGTH_SHORT).show();
                    }
                } else if (Objects.equals(filterType, "By City")) {
                    Geocoder geocoder = new Geocoder(getContext());
                    List<Address> addresses;
                    try {
                        addresses = geocoder.getFromLocationName(enteredText, 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                        } else {
                            Toast.makeText(getContext(), "Location not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(getContext(), "Geocoding failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }

            /**
             * This method is called when the text in the searchView is changed.
             *
             * @param newText
             *      the new text entered by the user in the searchView
             * @return false, as the search query is not submitted until the user presses "enter"
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                if (selectedMarker != null) {
                    selectedMarker.setIcon(markerIcon);
                }
                return false;
            }
        });
    }

    /**
     * A fragment that displays a map and allows the user to search for locations using a search bar.
     *
     * @param inflater
     *      The LayoutInflater object that can be used to inflate
     *      any views in the fragment,
     * @param container
     *      If non-null, this is the parent view that the fragment's
     *      UI should be attached to.  The fragment should not add the view itself,
     *      but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState
     *      If non-null, this fragment is being re-constructed
     *      from a previous saved state as given here.
     * @return Returns the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        View locationButton = ((View) view.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        rlp.setMargins(0, 180, 180, 0);
        searchView = view.findViewById(R.id.searchview_id);
        filter = view.findViewById(R.id.btn_location_filter);
        return view;
    }

    /**
     * Called immediately after the map fragment display has returned something. To help setting up
     * the map fragment itself.
     *
     * @param view
     *      The View returned by the fragment.
     * @param savedInstanceState
     *      If non-null, this fragment is being re-constructed
     *      from a previous saved state as given here.
     */
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

    /**
     * Function to ask for location permission to the user
     *
     * @return Return True if permission is granted, otherwise returns False
     */
    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Function used to launch the permission launcher
     */
    private void requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * Gets the current location permission and if it is not granted, requests it. If it is granted,
     * it gets the last known location and initializes the map using a callback.
     */
    private void getCurrentLocationPermission(){
        if (!checkLocationPermission()) {
            requestLocationPermission();
        } else {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            currentLocation = location;
                            SupportMapFragment mapFragment =
                                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                            if (mapFragment != null) {
                                mapFragment.getMapAsync(this);
                            }
                        }
                    });
        }
    }

    /**
     * This method is called when a marker is clicked on the map.
     *
     * @param scannedQR
     *      the QR code associated with the clicked marker
     * @param googleMap
     *      the GoogleMap object on which the marker was clicked
     * @param marker
     *      the Marker object that was clicked
     */
    private void markerClick(QR scannedQR, GoogleMap googleMap, Marker marker) {
        CustomShowInfoWindowAdapter adapter = new CustomShowInfoWindowAdapter(getContext(), scannedQR.getQRName(), scannedQR.getQRIcon());
        googleMap.setInfoWindowAdapter(adapter);
        marker.showInfoWindow();
    }

    private void openQRActivity(QR qr) {
        User user = new User();
        UserDatabase.getCurrentUser(UserDatabase.getDevice(getContext()), userDoc -> {
            Intent intent = new Intent(getContext(), QRActivity.class);
            intent.putExtra("comingFromMapsFragment", true);
            intent.putExtra("scannedQR", qr);
            user.setUsername(userDoc.getString("user_name"));
            intent.putExtra("user", user);
            startActivity(intent);
        });
    }
}