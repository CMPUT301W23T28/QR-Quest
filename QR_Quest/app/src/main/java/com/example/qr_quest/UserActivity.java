//package com.example.qr_quest;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageButton;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class UserActivity extends AppCompatActivity implements ItemClickListener{
//
//    private User user;
//
//    private androidx.cardview.widget.CardView highest_Card, lowest_Card;
//    private WalletAdapter walletAdapter;
//    private RecyclerView recyclerView;
//    private ImageButton backBtn;
//
//
//    Wallet[] myQrData = new Wallet[] {
//            new Wallet("---   --- \n    ||   \n  `---`   ", "iAmG", "168pts"),
//            new Wallet("---   --- \n    ||   \n  `---`   ", "chubs", "450pts"),
//            new Wallet("---   --- \n    ||   \n  `---`   ", "wall-e", "80pts"),
//            new Wallet("---   --- \n    ||   \n  `---`   ", "wall-e", "80pts"),
//            new Wallet("---   --- \n    ||   \n  `---`   ", "wall-e", "80pts"),
//            new Wallet("---   --- \n    ||   \n  `---`   ", "wall-e", "80pts"),
//            new Wallet("---   --- \n    ||   \n  `---`   ", "wall-e", "80pts"),
//            new Wallet("---   --- \n    ||   \n  `---`   ", "wall-e", "80pts"),
//            new Wallet("---   --- \n    ||   \n  `---`   ", "wall-e", "80pts"),
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user);
//
//        user = (User) getIntent().getSerializableExtra("user");
//        Log.d("MainActivity", "Received user: " + user.getUsername());
//
//        walletAdapter = new WalletAdapter(myQrData);
//
//        backBtn = findViewById(R.id.backBtn);
//        highest_Card = findViewById(R.id.highest_card);
//        lowest_Card = findViewById(R.id.lowest_card);
//        recyclerView = findViewById(R.id.wallet_recyclerview);
//        recyclerView.setHasFixedSize(true);
//
//        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(horizontalLayoutManager);
//        recyclerView.setAdapter(walletAdapter);
//        walletAdapter.setClickListener(this);
//
//
//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
//
//        highest_Card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(UserActivity.this,QRActivity.class));
//            }
//        });
//
//        lowest_Card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(UserActivity.this,QRActivity.class));
//            }
//        });
//    }
//    @Override
//    public void onClick(View view, int position) {
//        Intent i = new Intent(UserActivity.this, QRActivity.class);
//        startActivity(i);
//    }
//}