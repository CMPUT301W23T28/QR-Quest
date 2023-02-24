package com.example.qr_quest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class QRFragment extends DialogFragment {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_CONTENT_RESOLVER = 100;
    private Uri mPhotoUri;


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
                Toast.makeText(getContext(), "No Selected!", Toast.LENGTH_SHORT).show();
            }
        });
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddPhoto.class);
                startActivity(intent);
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

//    ActivityResultLauncher<Intent> mTakePictureLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    Intent intent = new Intent()
//
//                }
//            }
//                // handle the result here
//                if (result.getResultCode() == Activity.RESULT_OK) {
//                    if(result.getData()!= null){
//                        Toast.makeText(getActivity(), "pressed ok", Toast.LENGTH_SHORT).show();
//
//                        Fragment someFragment = new GeoLocationFragment();
//                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//                        transaction.replace(R.id.geo_location_and_comment_fragment, someFragment);
//                        transaction.addToBackStack(null);
//                        transaction.commit();
//                    }
//
//                } else {
//                    // The user cancelled or the picture could not be taken
//                    Toast.makeText(getContext(), "Failed to take picture", Toast.LENGTH_SHORT).show();
//                }
//            });
}
