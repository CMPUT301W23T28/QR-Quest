package com.example.qr_quest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

/**
 * Class is used to create the QRDialogFragment which shows information about the QRmarker clicked on the map
 */
public class QRDialogFragment extends DialogFragment {

    private QR qrName;

    /**
     * Creates a new instance of the QR click dialog fragment and returns a dialog with the specified layout
     * which contains the information about the selected QR marker
     * @param savedInstanceState
     *      The last saved instance state of the Fragment,
     *      or null if this is a freshly created Fragment.
     * @return
     *        Returns a builder to build an interactive dialog box and display it
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.qr_click_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Button okButton = (Button) view.findViewById(R.id.ok_button);
        qrName = new QR("test");
        if(qrName.getQRName() != null){
            TextView qrText =  view.findViewById(R.id.qr_successful_text);
            qrText.setText(qrName.getQRName());
        }
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                view.setVisibility(View.INVISIBLE);
                Objects.requireNonNull(getDialog()).dismiss();
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


