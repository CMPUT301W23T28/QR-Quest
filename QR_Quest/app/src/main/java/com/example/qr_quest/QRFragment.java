package com.example.qr_quest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Class is used to create QRFragment which ask the user to add photo for QRcode.
 */
public class QRFragment extends DialogFragment {

    private QR scannedQR;

    /**
     * Constructor for the class with specified QRcode.
     * @param scannedQR
     *      the QR code that was scanned by the user.
     */
    public QRFragment( QR scannedQR){
        this.scannedQR = scannedQR;

    }

    /**
     * Called when the Fragment's dialog is being created.
     * @param
     *      savedInstanceState The last saved instance state of the Fragment,
     *      or null if this is a freshly created Fragment.
     * @return
     *      a new AlertDialog with the appropriate layout and buttons set up.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_photo_fragment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Button noButton = view.findViewById(R.id.no_button);
        Button yesButton = view.findViewById(R.id.yes_button);

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "No Selected!", Toast.LENGTH_SHORT).show();
                new GeoLocationFragment(scannedQR).show(getChildFragmentManager(), "Ask for photo");
            }
        });
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddPhotoActivity.class);
                intent.putExtra("scannedQR", scannedQR);
                startActivity(intent);
            }
        });

        return builder
                .setView(view)
                .create();
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * Sets the background color and dim amount of the fragment's dialog window.
     */
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#CB8932")));
            getDialog().getWindow().setDimAmount(0);
        }
    }
}