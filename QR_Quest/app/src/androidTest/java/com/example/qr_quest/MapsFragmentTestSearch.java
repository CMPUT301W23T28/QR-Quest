package com.example.qr_quest;

import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.robotium.solo.Solo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class MapsFragmentTestSearch {
    private Solo solo;

    @Rule
    public ActivityTestRule<HomeActivity> rule =
            new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Home Activity", HomeActivity.class);
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void testMapFilterCity() throws InterruptedException{
        new Thread(() -> {
            solo.clickOnView(solo.getView(R.id.edittext_leaderboard_search));
            solo.waitForFragmentById(R.layout.fragment_maps);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            solo.clickOnView(solo.getView(R.id.searchview_id));
            solo.enterText(0, "Paris");
            solo.sendKey(Solo.ENTER);

            MapFragment mapFragment = (MapFragment) rule.getActivity().getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    LatLngBounds bounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
                    assertTrue(bounds.contains(new LatLng(48.8566, 2.3522)));
                }
            });

            String qrName = "myQR";
            solo.clickOnView(solo.getView(R.id.edittext_leaderboard_search));
            solo.waitForFragmentById(R.layout.fragment_maps);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            solo.clickOnView(solo.getView(R.id.searchview_id));
            solo.enterText(0, qrName);
            solo.sendKey(Solo.ENTER);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                final LatLng qrLocation = new LatLng(48.8566, 2.3522);
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(qrLocation)
                            .title(qrName);
                    googleMap.addMarker(markerOptions);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(qrLocation, 15);
                    googleMap.animateCamera(cameraUpdate);
                    LatLngBounds bounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
                    assertTrue(bounds.contains(new LatLng(48.8566, 2.3522)));
                }
            });
        }).start();
        Thread.sleep(5000);
    }
}
