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

/**
 * Class that represents the fragment for the home page.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
    }
    
    /**
     * Initializes the user view. This will return the view for the home page. If the user can be
     * found on the user database, then the home page will be displayed. It will display a welcome
     * message and the user information: user name, total score, global rank, and number of QR codes
     * scanned.
     * @param inflater
     *      The LayoutInflater object that can be used to inflate
     *      any views in the fragment,
     * @param container
     *      If non-null, this is the parent view that the fragment's
     *      UI should be attached to.  The fragment should not add the view itself,
     *      but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState
     *      If non-null, this fragment is being re-constructed
     *      from a previous saved state as given here.
     * @return
     *      Returns a view that is associated with HomeFragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        UserDatabase.getCurrentUser(UserDatabase.getDevice(getContext()), userDoc ->  {
            // Set the TextViews to the values retrieved from the Firestore database
            TextView welcomeTextView = view.findViewById(R.id.welcome_text);
            String username = userDoc.getString("user_name");
            welcomeTextView.setText("Welcome " + username + ",\n your current score:");

            TextView scoreTextView = view.findViewById(R.id.main_score);
            long score = userDoc.getLong("score");
            scoreTextView.setText(String.valueOf(score));

            TextView rankTextView = view.findViewById(R.id.world_rank);

            UserDatabase.getUserRank(username, rank ->
                    rankTextView.setText("World Rank:  " + rank));

            TextView qr_numTextView = view.findViewById(R.id.qr_number);
            ArrayList<String> scannedQRCodes = (ArrayList<String>) userDoc.get("qr_code_list");
            qr_numTextView.setText("QR's Collected:  " + scannedQRCodes.size());
        });

        return view;
    }
}
