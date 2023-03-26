package com.example.qr_quest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderboardFragment extends Fragment implements ItemClickListener {
    private final Leaderboard leaderboard = new Leaderboard();

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        recyclerView=view.findViewById(R.id.recyclerView);
        optionPoints=view.findViewById(R.id.points_option);
        optionQRCollected=view.findViewById(R.id.qr_collected_option);
        optionTopQR=view.findViewById(R.id.top_qr_option);
        searchBox=view.findViewById(R.id.search);
        regionBtn=view.findViewById(R.id.regionbtn);
        regionTextView = (TextView) view.findViewById(R.id.region_view);


        pointsAdapter = new LeaderboardPointsAdapter(leaderboard.getUsersSortedByPoints());
        qrCollectedAdapter = new LeaderboardQRCollectedAdapter(leaderboard.getUsersSortedByQRsCollected());
        topQRAdapter = new LeaderboardTopQRAdapter(leaderboard.getQrsSortedByPoints());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //the default list is the points list.
        recyclerView.setAdapter(pointsAdapter);

        optionPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(regionBtn.getVisibility()==View.VISIBLE)
                {
                    regionBtn.setVisibility(View.GONE);
                    regionTextView.setVisibility(View.GONE);
                }
                recyclerView.setAdapter(pointsAdapter);
                optionPoints.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview));
                optionQRCollected.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));
                optionTopQR.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));
            }
        });

        optionQRCollected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(regionBtn.getVisibility()==View.VISIBLE)
                {
                    regionBtn.setVisibility(View.GONE);
                    regionTextView.setVisibility(View.GONE);
                }
                recyclerView.setAdapter(qrCollectedAdapter);
                optionQRCollected.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview));
                optionPoints.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));
                optionTopQR.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));
            }
        });

        optionTopQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionTopQR.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview));
                optionPoints.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));
                optionQRCollected.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));
                regionBtn.setVisibility(View.VISIBLE);
                regionTextView.setVisibility((view.VISIBLE));


                regionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        View view1 = inflater.inflate(R.layout.location_dialog, null);

                        EditText locationFilterEditText = (EditText) view1.findViewById(R.id.filter_by_location);
                        Button filterBtn = (Button) view1.findViewById(R.id.filter_button);
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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

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
    @Override
    public void onClick(View view, int position) {
        Intent i = new Intent(getContext(), UserActivity.class);
        startActivity(i);
    }
}