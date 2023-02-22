package com.example.qr_quest;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetGeoLocation  {

    private final Context context;
    private final Location location;

    public GetGeoLocation(Context context, Location location) {
        this.context = context;
        this.location = location;
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public double getLongitude() {
        return location.getLongitude();

    }

    public String getCity() throws IOException {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        return addresses.get(0).getLocality();

    }
}
