package com.example.qr_quest.unitTests;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import static org.junit.Assert.assertEquals;

import com.example.qr_quest.GeoLocation;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoLocationTest {

    private Context context;
    private Location location;

    @Test
    public void testConstructor(){
        GeoLocation geoLocation = new GeoLocation(context, location);
        assertEquals(geoLocation.getContext(),context);
        assertEquals(geoLocation.getLocation(), location);
    }

    @Test
    public void testGetLatitude(){
        Location location = new Location("");
        location.setLatitude(37.422);
        location.setLongitude(-122.084);
        GeoLocation geoLocation = new GeoLocation(context, location);
        assertEquals(geoLocation.getLatitude(), location.getLatitude(), 0.001);
    }

    @Test
    public void testGetLongitude(){
        Location location = new Location("");
        location.setLatitude(37.422);
        location.setLongitude(-122.084);
        GeoLocation geoLocation = new GeoLocation(context, location);
        assertEquals(geoLocation.getLongitude(), location.getLongitude(), 0.001);
    }

    @Test
    public void testGetCity() throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(48.8566, 2.3522, 1);
        if (addresses != null && addresses.size() > 0) {
            assertEquals("Paris", addresses.get(0).getLocality());
        }
    }
}
