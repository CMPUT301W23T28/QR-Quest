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

    @SuppressLint("InflateParams")
    public CustomShowInfoWindowAdapter(Context context, String name, String avatar ) {
        this.mContext = context;
        this.QRname = name;
        this.avatar = avatar;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_showinfo_adapter, null);
    }

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
