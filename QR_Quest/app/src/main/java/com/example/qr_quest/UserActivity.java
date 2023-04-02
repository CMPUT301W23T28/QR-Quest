package com.example.qr_quest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Class to display user profile and wallet information which implements ItemClickListener
 */
public class UserActivity extends AppCompatActivity implements ItemClickListener{

    private User user;
    private QR highestQR, lowestQR;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Setting the User's wallet
        RecyclerView recyclerView = findViewById(R.id.wallet_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        User currUser = new User();
        UserDatabase.getCurrentUser(UserDatabase.getDevice(getApplicationContext()), userDoc ->
                currUser.setUsername(userDoc.getString("user_name")));

        user = (User) getIntent().getSerializableExtra("user");
        UserDatabase.getUser(user, userDoc -> {
            if (userDoc != null && userDoc.exists()) {

                // Set the TextViews to the values retrieved from the user object passed database
                TextView usernameTextView = findViewById(R.id.txtview_activity_user_username);
                String username = userDoc.getString("user_name");
                usernameTextView.setText(username);

                TextView nameTextView = findViewById(R.id.txtview_activity_user_fullname);
                nameTextView.setText(userDoc.getString("first_name") + " " + userDoc.getString("last_name"));

                TextView emailTextView = findViewById(R.id.txtview_activity_user_email);
                emailTextView.setText(userDoc.getString("email"));

                int qr_num = ((ArrayList<String>) userDoc.get("qr_code_list")).size();
                TextView statsTextView = findViewById(R.id.txtview_activity_user_stats);
                UserDatabase.getUserRank(username, rank ->
                        statsTextView.setText(userDoc.getLong("score") + "pts       " +
                                qr_num + " QR's Collected       Rank: " + rank));

                QRDatabase.getHighestQR(username, qrCode -> {
                    if (qrCode != null) {
                        highestQR = qrCode;
                        TextView highestIcon = findViewById(R.id.txtview_activity_user_highest_icon);
                        TextView highestName = findViewById(R.id.txtview_activity_user_highest_name);
                        TextView highestPoint = findViewById(R.id.txtview_activity_user_highest_points);
                        TextView highestRank = findViewById(R.id.txtview_activity_user_highest_rank);

                        highestIcon.setText(highestQR.getQRIcon());
                        highestName.setText(highestQR.getQRName());
                        QRDatabase.checkIfUserHasQR(currUser, highestQR, check -> {
                            if(check) {
                                highestPoint.setText("Highest QR: " + highestQR.getScore() + " pts");
                            } else {

                                highestPoint.setText("Highest QR:");
                            }
                        });
                        QRDatabase.getQRRank(highestQR.getQRName(), rank -> highestRank.setText("Rank: " + rank));
                    }
                });

                QRDatabase.getLowestQR(username, qrCode -> {
                    if (qrCode != null) {
                        lowestQR = qrCode;
                        TextView lowestIcon = findViewById(R.id.txtview_activity_user_lowest_icon);
                        TextView lowestName = findViewById(R.id.txtview_activity_user_lowest_name);
                        TextView lowestPoint = findViewById(R.id.txtview_activity_user_lowest_points);
                        TextView lowestRank = findViewById(R.id.txtview_activity_user_lowest_rank);

                        lowestIcon.setText(lowestQR.getQRIcon());
                        lowestName.setText(lowestQR.getQRName());
                        QRDatabase.checkIfUserHasQR(currUser, lowestQR, check -> {
                            if(check) {
                                lowestPoint.setText("Lowest QR: " + lowestQR.getScore() + " pts");
                            } else {

                                lowestPoint.setText("Lowest QR:");
                            }
                        });
                        QRDatabase.getQRRank(lowestQR.getQRName(), rank -> lowestRank.setText("Rank: " + rank));
                    }
                });

                QRDatabase.getUserQRs(username, qrList -> {
                    WalletAdapter adapter = new WalletAdapter(qrList, currUser);
                    recyclerView.setAdapter(adapter);
                    adapter.setClickListener(this);
                });
            }
        });

        // Setting user's highest card
        androidx.cardview.widget.CardView highest_Card = findViewById(R.id.cardview_activity_user_highest_card);
        highest_Card.setOnClickListener(view -> UserDatabase.getUser(user, userDoc -> {
            if (((ArrayList<String>) userDoc.get("qr_code_list")).size() != 0) {
                Intent intent = new Intent(UserActivity.this, QRActivity.class);
                intent.putExtra("scannedQR", highestQR);
                intent.putExtra("user", currUser);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "You need to scan a QR code first!", Toast.LENGTH_SHORT).show();
            }
        }));

        // Setting user's lowest card
        androidx.cardview.widget.CardView lowest_Card = findViewById(R.id.cardview_activity_user_lowest_card);
        lowest_Card.setOnClickListener(view -> UserDatabase.getUser(user, userDoc -> {
            if (((ArrayList<String>) userDoc.get("qr_code_list")).size() != 0) {
                Intent intent = new Intent(UserActivity.this, QRActivity.class);
                intent.putExtra("scannedQR", lowestQR);
                intent.putExtra("user", currUser);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "You need to scan a QR code first!", Toast.LENGTH_SHORT).show();
            }
        }));

        // Setting back button functionality
        ImageButton backBtn = findViewById(R.id.btn_user_back);
        backBtn.setOnClickListener(view -> onBackPressed());
    }

    /**
     * Is implemented in the wallet adapter class
     * @param view
     * @param position
     */
    @Override
    public void onClick(View view, int position) {
    }
}