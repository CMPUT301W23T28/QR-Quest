package com.example.qr_quest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity implements ItemClickListener{

    private User user;

    private WalletAdapter walletAdapter;

    private QR[] qrs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        user = (User) getIntent().getSerializableExtra("user");

        // Set the TextViews to the values retrieved from the user object passed database
        TextView usernameTextView = findViewById(R.id.user_name);
        usernameTextView.setText(user.getUsername());

        TextView nameTextView = findViewById(R.id.full_name);
        String f_name = user.getFirstName();
        String l_name = user.getLastName();
        nameTextView.setText(f_name + " " + l_name);

        TextView emailTextView = findViewById(R.id.email);
        emailTextView.setText(user.getEmail());

        TextView statsTextView = findViewById(R.id.userStats);
        statsTextView.setText(user.getScore() + "pts       " +
                        user.getQRCodes().size() + " QR's Collected       Rank: " + 10);

//        walletAdapter = new WalletAdapter(qrs);

        ImageButton backBtn = findViewById(R.id.backBtn);
        androidx.cardview.widget.CardView highest_Card = findViewById(R.id.highest_card);
        androidx.cardview.widget.CardView lowest_Card = findViewById(R.id.lowest_card);
        RecyclerView recyclerView = findViewById(R.id.wallet_recyclerview);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
//        recyclerView.setAdapter(walletAdapter);
//        walletAdapter.setClickListener(this);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        highest_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserActivity.this,QRActivity.class));
            }
        });

        lowest_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserActivity.this,QRActivity.class));
            }
        });
    }
    @Override
    public void onClick(View view, int position) {
        Intent i = new Intent(UserActivity.this, QRActivity.class);
        startActivity(i);
    }
}