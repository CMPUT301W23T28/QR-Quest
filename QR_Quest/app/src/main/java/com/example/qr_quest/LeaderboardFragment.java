package com.example.qr_quest;

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
public class LeaderboardFragment extends Fragment {
    private final Leaderboard leaderboard = new Leaderboard();

    RecyclerView recyclerView;
    LeaderboardPointsAdapter pointsAdapter;
    LeaderboardQRCollectedAdapter qrCollectedAdapter;
    LeaderboardTopQRAdapter topQRAdapter;

    EditText searchBox;
    TextView optionPoints,optionQRCollected,optionTopQR;


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
                recyclerView.setAdapter(pointsAdapter);
                optionPoints.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview));
                optionQRCollected.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));
                optionTopQR.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));
            }
        });

        optionQRCollected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.top_qr_filter_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.filter1:
                                // Handle filter 1 selection
                                return true;
                            case R.id.filter2:
                                // Handle filter 2 selection
                                return true;
                            case R.id.filter3:
                                // Handle filter 3 selection
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popupMenu.show();
                recyclerView.setAdapter(topQRAdapter);

            }
        });


//        searchBox.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int i1, int i2) {
//                String s = Objects.requireNonNull(searchBox.getText()).toString().trim().toLowerCase();
//                sort_users.clear();
//
//                for (int i = 0; i < users.length; i++) {
//                    if (s.length() <= users[i].getUsername().length()) {
//                        if (users[i].getUsername().toLowerCase().trim().contains(
//                                s.trim())) {
////                            sort_users.add(new user(users[i].getName(),users[i].getTopQr(),users[i].getCollectedQr()
////                                    ,users[i].getRegionQr()));
//                        }
//                    }
//                }
//                adapter = new LeaderboardPointsAdapter(sort_users);
//                adapter1 = new LeaderboardQRCollectedAdapter(sort_users);
//                adapter2 = new LeaderboardQRCollectedAdapter(sort_users);
//                recyclerView.setHasFixedSize(true);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                recyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });

        return view;
    }
}