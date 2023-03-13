package com.example.qr_quest;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        UserDatabase.getCurrentUser(UserDatabase.getDevice(getContext()), userDoc ->  {
            if (userDoc != null && userDoc.exists()) {
                // Set the TextViews to the values retrieved from the Firestore database
                TextView welcomeTextView = view.findViewById(R.id.welcome_text);
                String username = userDoc.getString("user_name");
                welcomeTextView.setText("Welcome " + username + ",\n your current score:");

                TextView scoreTextView = view.findViewById(R.id.main_score);
                long score = userDoc.getLong("score");
                scoreTextView.setText(String.valueOf(score));

                TextView rankTextView = view.findViewById(R.id.world_rank);

                // device context
                UserDatabase.getRank(UserDatabase.getDevice(getContext()), rank ->
                        rankTextView.setText("World Rank:  " + rank));

                TextView qr_numTextView = view.findViewById(R.id.qr_number);
                ArrayList<String> scannedQRCodes = (ArrayList<String>) userDoc.get("qr_code_list");
                qr_numTextView.setText("QR's Collected:  " + scannedQRCodes.size());
            } else {
                Log.e(TAG, "User document not found");
            }
        });

        return view;
    }
}