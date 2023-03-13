package com.example.qr_quest;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Class that represents the fragment for the profile page. 
 */
public class ProfileFragment extends Fragment implements ItemClickListener{

    private TextView editButton;
    private WalletAdapter adapter;

    Wallet[] myQrData = new Wallet[] {
            new Wallet("---   --- \n    ||   \n  `---`   ", "iAmG", "168pts"),
            new Wallet("---   --- \n    ||   \n  `---`   ", "chubs", "450pts"),
            new Wallet("---   --- \n    ||   \n  `---`   ", "wall-e", "80pts"),
    };

    public ProfileFragment() {}

    /**
     * Initializes the profile view. Checks the user database if the user exists, then, if they
     * exist, the user's profile page is displayed. It displays information about the player:
     * username, name, and contact information. Also displays the user's wallet. Handles click event
     * for allowing the user to edit their profile.
     * @param
     *       inflater The LayoutInflater object that can be used to inflate
     *       any views in the fragment,
     * @param
     *       container If non-null, this is the parent view that the fragment's
     *       UI should be attached to.  The fragment should not add the view itself,
     *       but this can be used to generate the LayoutParams of the view.
     * @param
     *       savedInstanceState If non-null, this fragment is being re-constructed
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

//                    TextView nameTextView = view.findViewById(R.id.full_name);
//                    String f_name = userDoc.getString("first_name");
//                    String l_name = userDoc.getString("last_name");
//                    nameTextView.setText(first_name + " " + last_name);

                TextView emailTextView = view.findViewById(R.id.email);
                String email = userDoc.getString("email");
                emailTextView.setText(email);

//                    TextView phoneTextView = view.findViewById(R.id.phone);
//                    String phone = userDoc.getString("phone");
//                    phoneTextView.setText(phone);

            } else {
                Log.e(TAG, "User document not found");
            }
        });

        editButton = view.findViewById(R.id.edit);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView1);
        adapter = new WalletAdapter(myQrData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view1 = inflater.inflate(R.layout.edit_profile, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setView(view1)
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
