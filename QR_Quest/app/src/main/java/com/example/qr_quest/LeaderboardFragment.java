package com.example.qr_quest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderboardFragment extends Fragment  {

    String username;
    RecyclerView recyclerView;
    LeaderboardPointsAdapter pointsAdapter;
    LeaderboardQRCollectedAdapter qrCollectedAdapter;
    LeaderboardTopQRAdapter topQRAdapter;

    EditText searchBox;
    TextView optionPoints,optionQRCollected,optionTopQR,regionBtn;
    TextView regionTextView;

    /**
     *  Required empty public constructor
     */
    public LeaderboardFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LeaderboardFragment.
     */
    public static LeaderboardFragment newInstance() {
        LeaderboardFragment fragment = new LeaderboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container The parent view that the fragment's UI should be attached to
     * @param savedInstanceState This fragment is being re-constructed from a previous saved state as given here
     * @return The View for the fragment's UI, or null
     */

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_leaderboard);
        optionPoints = view.findViewById(R.id.txtview_leaderboard_points_option);
        optionQRCollected = view.findViewById(R.id.txtview_leaderboard_qr_collected_option);
        optionTopQR = view.findViewById(R.id.txtview_leaderboard_top_qr_option);
        searchBox = view.findViewById(R.id.edittext_leaderboard_search);
        regionBtn = view.findViewById(R.id.txtview_leaderboard_filter);
        regionTextView = view.findViewById(R.id.txtview_leaderboard_region_view);

        UserDatabase.getCurrentUser(UserDatabase.getDevice(getContext()), userDoc ->  {
            if (userDoc != null && userDoc.exists()) {
                // Set the TextViews to the values retrieved from the Firestore database
                username = userDoc.getString("user_name");
            }});

        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setLists(success -> {
            pointsAdapter = new LeaderboardPointsAdapter(username, leaderboard.getUsersSortedByPoints(),
                    user -> navigateToUserActivity(user));

            qrCollectedAdapter = new LeaderboardQRCollectedAdapter(username, leaderboard.getUsersSortedByQRsCollected(),
                    user -> navigateToUserActivity(user));

            User currUser = new User();
            UserDatabase.getCurrentUser(UserDatabase.getDevice(getContext()), userDoc -> {
                currUser.setQRCodeList((ArrayList<String>) userDoc.get("qr_code_list"));
                topQRAdapter = new LeaderboardTopQRAdapter(currUser, leaderboard.getQrsSortedByPoints());
            });

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            //the default list is the points list.
            recyclerView.setAdapter(pointsAdapter);
        });

        optionPoints.setOnClickListener(view15 -> {
            if (regionBtn.getVisibility() == View.VISIBLE) {
                regionBtn.setVisibility(View.GONE);
                regionTextView.setVisibility(View.GONE);
            }
            recyclerView.setAdapter(pointsAdapter);
            optionPoints.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview));
            optionQRCollected.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview1));
            optionTopQR.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview1));
        });

        optionQRCollected.setOnClickListener(view14 -> {
            if (regionBtn.getVisibility() == View.VISIBLE) {
                regionBtn.setVisibility(View.GONE);
                regionTextView.setVisibility(View.GONE);
            }
            recyclerView.setAdapter(qrCollectedAdapter);
            optionQRCollected.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview));
            optionPoints.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview1));
            optionTopQR.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview1));
        });

        optionTopQR.setOnClickListener(view13 -> {
            optionTopQR.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview));
            optionPoints.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview1));
            optionQRCollected.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview1));
            regionBtn.setVisibility(View.VISIBLE);
            regionTextView.setVisibility((view13.VISIBLE));

            regionBtn.setOnClickListener(view12 -> {
                LayoutInflater inflater1 = LayoutInflater.from(getContext());
                View view1 = inflater1.inflate(R.layout.location_dialog, null);

                EditText locationFilterEditText = view1.findViewById(R.id.edittext_location_filter);
                Button filterBtn = view1.findViewById(R.id.btn_location_filter);
                locationFilterEditText.setText(leaderboard.getRegion());

                final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setView(view1)
                        .create();
                alertDialog.show();

                filterBtn.setOnClickListener(v -> {
                    String region = locationFilterEditText.getText().toString();
                    leaderboard.filterByRegion(region);
                    regionTextView.setText(leaderboard.getRegion());
                    alertDialog.dismiss();
                });
            });
            recyclerView.setAdapter(topQRAdapter);
        });

        searchBox.addTextChangedListener(new TextWatcher() {

            /**
             * This method is called when the text in the searchBox is about to be changed.
             * @param charSequence the sequence of characters about to be changed
             * @param i the position of the beginning of the changed part in the text
             * @param i1 the length of the changed part in the text
             * @param i2 the length of the new text after the change
             */
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            /**
             * This method is called when the text in the searchBox is changed.
             * It filters the leaderboard data based on the entered query and updates the displayed list.
             * @param charSequence the new text in the searchBox
             * @param start the position of the beginning of the changed part in the text
             * @param i1 the length of the changed part in the text
             * @param i2 the length of the new text after the change
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int i1, int i2) {
                String query = charSequence.toString().toLowerCase();
                leaderboard.filter(query);
                pointsAdapter.filterList(leaderboard.getUsersSortedByPoints());
                qrCollectedAdapter.filterList(leaderboard.getUsersSortedByQRsCollected());
                topQRAdapter.filterList(leaderboard.getQrsSortedByPoints());
            }

            /**
             * This method is called after the text in the searchBox has been changed.
             * It does not perform any action in this implementation.
             * @param editable the new text in the searchBox
             */
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        return view;
    }

    /**
     *
     * Navigates to the UserActivity when a user item is clicked.
     * @param user The User object representing the clicked item.
     */
    private void navigateToUserActivity(User user) {
        // handle item click here
        Intent intent = new Intent(getActivity(), UserActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}