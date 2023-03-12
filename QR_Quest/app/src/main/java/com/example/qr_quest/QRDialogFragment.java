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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class QRDialogFragment extends DialogFragment {

    private QR qrName;
    public QRDialogFragment(){
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.qr_click_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Button okButton = (Button) view.findViewById(R.id.ok_button);
        qrName = new QR("test");
        if(qrName.getQRName() != null){
            TextView qrText = (TextView)view.findViewById(R.id.qr_successful_text);
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

    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#CB8932")));
            getDialog().getWindow().setDimAmount(0);
        }
    }
}


