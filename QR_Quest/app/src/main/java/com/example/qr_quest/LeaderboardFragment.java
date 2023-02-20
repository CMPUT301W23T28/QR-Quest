package com.example.qr_quest;

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
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    LeaderBoardAdapter adapter;
    LeaderBoardAdapter1 adapter1;
    LeaderBoardAdapter1 adapter2;

    EditText searchbox;
    TextView option1,option2,option3,option4;

    ArrayList<user> user_list = new ArrayList<>();
    ArrayList<user> sort_users = new ArrayList<>();


    user[] users = new user[]{
            new user("siuuuu_boy",1000,500,7777777),
            new user("mbappe_bhai",900,100,555565),
            new user("qr_quest",500,90 ,7450),
            new user("phoebe", 500,60,4589),
            new user("ishan", 345, 60, 2342),
    };

    private ArrayList<user> populateList(){

        ArrayList<user> list = new ArrayList<>();

        for(int i = 0; i < 5; i++){
             user user = new user(users[i].getName(),users[i].getTopQr(),users[i].getCollectedQr(),users[i].getRegionQr());

            list.add(user);
        }

        return list;
    }






    public LeaderboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeaderboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaderboardFragment newInstance(String param1, String param2) {
        LeaderboardFragment fragment = new LeaderboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        option1=view.findViewById(R.id.op1);
        option2=view.findViewById(R.id.op2);
        option3=view.findViewById(R.id.op3);
        option4=view.findViewById(R.id.op4);
        searchbox=view.findViewById(R.id.search);

        user_list = populateList();

        adapter = new LeaderBoardAdapter(user_list);
        adapter1 = new LeaderBoardAdapter1(user_list);
        adapter2 = new LeaderBoardAdapter1(user_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);



        sort_users = populateList();

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setAdapter(adapter1);
                option2.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview));
                option1.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));
                option3.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));
                option4.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));



            }
        });
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setAdapter(adapter);
                option1.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview));
                option2.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));
                option3.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));
                option4.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));



            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setAdapter(adapter2);
                option3.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview));
                option1.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));
                option2.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));
                option4.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));



            }
        });
        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setAdapter(adapter);
                option4.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview));
                option1.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));
                option3.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));
                option2.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.textview1));

//

            }
        });

        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int i1, int i2) {
                String s = Objects.requireNonNull(searchbox.getText()).toString().trim().toLowerCase();
                sort_users.clear();

                for (int i = 0; i < users.length; i++) {
                    if (s.length() <= users[i].getName().length()) {
                        if (users[i].getName().toLowerCase().trim().contains(
                                s.trim())) {

                            sort_users.add(new user(users[i].getName(),users[i].getTopQr(),users[i].getCollectedQr()
                                    ,users[i].getRegionQr()));

                        }
                    }
                }
                adapter = new LeaderBoardAdapter(sort_users);
                adapter1 = new LeaderBoardAdapter1(sort_users);
                adapter2 = new LeaderBoardAdapter1(sort_users);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        return view;
    }



}