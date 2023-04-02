package com.example.qr_quest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * This class is used to make custom info window for the markers on the map
 */
public class CustomShowInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private View mWindow;
    private Context mContext;

    private String QRname;

    private String avatar;

    public void setQRName(String name) {
        this.QRname = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * Constructor for creating a custom InfoWindow Adapter that displays a custom layout.
     * @param context The Context in which the adapter will be used.
     * @param name The name of the QR code associated with the InfoWindow.
     * @param avatar The avatar of the QR code associated with the InfoWindow.
     *
     * */
    @SuppressLint("InflateParams")
    public CustomShowInfoWindowAdapter(Context context, String name, String avatar ) {
        this.mContext = context;
        this.QRname = name;
        this.avatar = avatar;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_showinfo_adapter, null);
    }

    /**
     * Returns the custom contents of the InfoWindow for a given Marker.
     * @param marker The Marker for which the InfoWindow contents are being retrieved.
     * @return A View containing the custom contents of the InfoWindow for the given Marker, or null if
     */
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        TextView nameTextView = mWindow.findViewById(R.id.txtview_showinfo_qrname);
        TextView ageTextView = mWindow.findViewById(R.id.txtview_showinfo_qravatar);

        nameTextView.setText(QRname);
        ageTextView.setText(avatar);

        return mWindow;
    }

    /**
     * Returns the custom info window for the specified marker.
     * @param marker
     *   The marker for which to create or modify the info window.
     * @return
     *   The custom info window view for the specified marker, or null to use the default
     */
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        TextView nameTextView = mWindow.findViewById(R.id.txtview_showinfo_qrname);
        TextView ageTextView = mWindow.findViewById(R.id.txtview_showinfo_qravatar);

        nameTextView.setText(QRname);
        ageTextView.setText(avatar);
        return mWindow;
    }
}
