package com.example.qr_quest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

    User user = new User();

    RecyclerView recyclerView;
    LeaderboardPointsAdapter pointsAdapter;
    LeaderboardQRCollectedAdapter qrCollectedAdapter;
    LeaderboardTopQRAdapter topQRAdapter;

    EditText searchBox;
    TextView optionPoints,optionQRCollected,optionTopQR,regionBtn;
    TextView regionTextView, persistentRank, persistentUsername, persistentInfo;
    CardView persistentCardView;

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

        persistentCardView = view.findViewById(R.id.leader_board_persistent_card);
        persistentRank = view.findViewById(R.id.leaderboard_persistent_rank);
        persistentUsername = view.findViewById(R.id.leaderboard_persistent_username);
        persistentInfo = view.findViewById(R.id.leaderboard_persistent_info);

        UserDatabase.getCurrentUser(UserDatabase.getDevice(getContext()), userDoc -> {
            if (userDoc != null && userDoc.exists()) {
                // Set the User object
                user.setUsername(userDoc.getString("user_name"));
                user.setEmail(userDoc.getString("email"));
                user.setFirstName(userDoc.getString("first_name"));
                user.setLastName(userDoc.getString("last_name"));
                user.setPhoneNumber(userDoc.getString("phone"));
                user.setScore(userDoc.getLong("score"));
                user.setQRCodeList((ArrayList<String>) userDoc.get("qr_code_list"));

                // Set the TextViews to the values retrieved from the Firestore database
                persistentUsername.setText(user.getUsername());
                persistentInfo.setText(user.getScore() + " pts");
            }
        });


        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setLists(success -> {
            pointsAdapter = new LeaderboardPointsAdapter(leaderboard.getUsersSortedByPoints(), new LeaderboardPointsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(User user) {
                    navigateToUserActivity(user);
                }
            });
            qrCollectedAdapter = new LeaderboardQRCollectedAdapter(leaderboard.getUsersSortedByQRsCollected(), new LeaderboardQRCollectedAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(User user) {
                    navigateToUserActivity(user);
                }
            });
            topQRAdapter = new LeaderboardTopQRAdapter(leaderboard.getQrsSortedByPoints());

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            //the default list is the points list.
            recyclerView.setAdapter(pointsAdapter);
        });

        optionPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                persistentInfo.setText(user.getScore() + " pts");
                persistentCardView.setVisibility(View.VISIBLE);

                if (regionBtn.getVisibility() == View.VISIBLE) {
                    regionBtn.setVisibility(View.GONE);
                    regionTextView.setVisibility(View.GONE);
                }
                recyclerView.setAdapter(pointsAdapter);
                optionPoints.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview));
                optionQRCollected.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview1));
                optionTopQR.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview1));
            }
        });

        optionQRCollected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                persistentInfo.setText(user.getQRCodes().size() + " qrs");
                persistentCardView.setVisibility(View.VISIBLE);

                if (regionBtn.getVisibility() == View.VISIBLE) {
                    regionBtn.setVisibility(View.GONE);
                    regionTextView.setVisibility(View.GONE);
                }
                recyclerView.setAdapter(qrCollectedAdapter);
                optionQRCollected.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview));
                optionPoints.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview1));
                optionTopQR.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview1));
            }
        });

        optionTopQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                persistentCardView.setVisibility(View.GONE);
                optionTopQR.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview));
                optionPoints.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview1));
                optionQRCollected.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.textview1));
                regionBtn.setVisibility(View.VISIBLE);
                regionTextView.setVisibility((view.VISIBLE));

                regionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        View view1 = inflater.inflate(R.layout.location_dialog, null);

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
                    }
                });
                recyclerView.setAdapter(topQRAdapter);
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int i1, int i2) {
                String query = charSequence.toString().toLowerCase();
                leaderboard.filter(query);
                pointsAdapter.filterList(leaderboard.getUsersSortedByPoints());
                qrCollectedAdapter.filterList(leaderboard.getUsersSortedByQRsCollected());
                topQRAdapter.filterList(leaderboard.getQrsSortedByPoints());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        return view;
    }

    private void navigateToUserActivity(User user) {
        // handle item click here
        Intent intent = new Intent(getActivity(), UserActivity.class);
        intent.putExtra("user", user);
        Log.d("test", String.valueOf(user.getPointsRank()));
        startActivity(intent);
    }
}