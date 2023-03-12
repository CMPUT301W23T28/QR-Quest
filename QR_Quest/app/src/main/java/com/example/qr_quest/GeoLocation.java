package com.example.qr_quest;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * This class defines a GeoLocation of the user which contains city, latitude and longitude of the user
 */
public class GeoLocation  {

    private final Context context;
    private final Location location;

    /**
     * Constructor for the class
     * @param context
     *      Context to be used
     * @param location
     *      Location class to be passed
     */
    public GeoLocation(Context context, Location location) {
        this.context = context;
        this.location = location;
    }

    /**
     * Getter for the latitude
     * @return
     *      Returns the latitude of the user device
     */
    public double getLatitude() {
        return location.getLatitude();
    }

    /**
     * Getter for the Longitude
     * @return
     *      Returns the longitude of the user device
     */
    public double getLongitude() { return location.getLongitude(); }

    /**
     * Function to get the city based on current user location
     * @return
     *      Returns the city name
     * @throws IOException
     *      If the Geocoder service is not available, or if there is no connectivity to the Geocoder service, or if the latitude and longitude values are out of range.
     */
    public String getCity() throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        return addresses.get(0).getLocality();
    }
}