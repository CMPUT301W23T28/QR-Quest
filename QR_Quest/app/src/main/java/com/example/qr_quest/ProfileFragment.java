package com.example.qr_quest;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Class that represents the fragment for the profile page.
 */
public class ProfileFragment extends Fragment implements ItemClickListener{

    private TextView editButton;
    private WalletAdapter adapter;
    private String prevUserName;

    private androidx.cardview.widget.CardView highest_Card, lowest_Card;

    public ProfileFragment() {}

    /**
     * Initializes the profile view. Checks the user database if the user exists, then, if they
     * exist, the user's profile page is displayed. It displays information about the player:
     * username, name, and contact information. Also displays the user's wallet. Handles click event
     * for allowing the user to edit their profile.
     * @param inflater
     *       The LayoutInflater object that can be used to inflate
     *       any views in the fragment,
     * @param container
     *       If non-null, this is the parent view that the fragment's
     *       UI should be attached to.  The fragment should not add the view itself,
     *       but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState
     *       If non-null, this fragment is being re-constructed
     *       from a previous saved state as given here.
     * @return
     *       Returns a view that is associated with ProfileFragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        UserDatabase.getCurrentUser(UserDatabase.getDevice(getContext()), userDoc ->  {
            if (userDoc != null && userDoc.exists()) {
                // Set the TextViews to the values retrieved from the Firestore database
                TextView usernameTextView = view.findViewById(R.id.user_name);
                String username = userDoc.getString("user_name");
                usernameTextView.setText(username);

                TextView nameTextView = view.findViewById(R.id.full_name);
                String f_name = userDoc.getString("first_name");
                String l_name = userDoc.getString("last_name");
                nameTextView.setText(f_name + " " + l_name);

                TextView emailTextView = view.findViewById(R.id.email);
                String email = userDoc.getString("email");
                emailTextView.setText(email);

                int qr_num = ((ArrayList<String>) userDoc.get("qr_code_list")).size();

                TextView statsTextView = view.findViewById(R.id.userStats);
                UserDatabase.getRank(UserDatabase.getDevice(getContext()), rank ->
                        statsTextView.setText(userDoc.getLong("score") + "pts       " +
                                qr_num + " QR's Collected       Rank: " + rank));

                QRDatabase.getHighestQR(username, qrDoc -> {
                    if (qrDoc != null) {
                        TextView highestIcon = view.findViewById(R.id.highest_icon);
                        TextView highestName = view.findViewById(R.id.highest_name);
                        TextView highestPoint = view.findViewById(R.id.highest_points);

                        highestIcon.setText(qrDoc.getString("avatar"));
                        highestName.setText(qrDoc.getId());
                        highestPoint.setText("Highest QR: " + qrDoc.getLong("score") + " pts");
                    }
                });

                QRDatabase.getLowestQR(username, qrDoc -> {
                    if (qrDoc != null) {
                        TextView lowestIcon = view.findViewById(R.id.lowest_icon);
                        TextView lowestName = view.findViewById(R.id.lowest_name);
                        TextView lowestPoint = view.findViewById(R.id.lowest_points);

                        lowestIcon.setText(qrDoc.getString("avatar"));
                        lowestName.setText(qrDoc.getId());
                        lowestPoint.setText("Lowest QR: " + qrDoc.getLong("score") + " pts");
                    }
                });

            } else {
                Log.e(TAG, "User document not found");
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        Wallet.fillWallet(wallets -> {
            adapter = new WalletAdapter(wallets);
            recyclerView.setAdapter(adapter);
            adapter.setClickListener(this);
        });

        highest_Card = view.findViewById(R.id.highest_card);
        highest_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDatabase.getCurrentUser(UserDatabase.getDevice(getContext()), userDoc -> {
                    if (((ArrayList<String>) userDoc.get("qr_code_list")).size() != 0) {
                        startActivity(new Intent(getContext(), QRActivity.class));
                    } else {
                        Toast.makeText(getContext(), "You need to scan a QR code first!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        lowest_Card = view.findViewById(R.id.lowest_card);
        lowest_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDatabase.getCurrentUser(UserDatabase.getDevice(getContext()), userDoc -> {
                    if (((ArrayList<String>) userDoc.get("qr_code_list")).size() != 0) {
                        startActivity(new Intent(getContext(), QRActivity.class));
                    } else {
                        Toast.makeText(getContext(), "You need to scan a QR code first!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        editButton = view.findViewById(R.id.edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view1 = inflater.inflate(R.layout.edit_profile, null);

                EditText usernameEditText = view1.findViewById(R.id.username_edit);
                EditText firstNameEditText = view1.findViewById(R.id.firstname_edit);
                EditText lastNameEditText = view1.findViewById(R.id.lastname_edit);
                EditText emailEditText = view1.findViewById(R.id.email_edit);
                EditText phoneEditText = view1.findViewById(R.id.phone_edit);

                // sets the EditText fields to the user's current information
                UserDatabase.getCurrentUser(UserDatabase.getDevice(getContext()), userDoc -> {
                    prevUserName = userDoc.getString("user_name");
                    usernameEditText.setText(prevUserName);
                    firstNameEditText.setText(userDoc.getString("first_name"));
                    lastNameEditText.setText(userDoc.getString("last_name"));
                    emailEditText.setText(userDoc.getString("email"));
                    phoneEditText.setText(userDoc.getString("phone"));
                });

                final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setView(view1)
                        .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // checks changes in the EditText fields and sets any new user information
                                User user = new User();
                                user.setUsername(usernameEditText.getText().toString());
                                user.setFirstName(firstNameEditText.getText().toString());
                                user.setLastName(lastNameEditText.getText().toString());
                                user.setEmail(emailEditText.getText().toString());
                                user.setPhoneNumber(phoneEditText.getText().toString());

                                UserDatabase.updateUserDetails(prevUserName, user,
                                        UserDatabase.getDevice(getContext()), success -> {
                                    if(success) {
                                        Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                    } else{
                                        Toast.makeText(getContext(), "That Username has been taken!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
        return view;
    }

    /**
     * Starts a new QRActivity, which will display a selected QR code profile, when a QR code is clicked.
     * @param view
     * @param position
     */
    @Override
    public void onClick(View view, int position) {
        Intent i = new Intent(getContext(), QRActivity.class);
        startActivity(i);
    }
}
