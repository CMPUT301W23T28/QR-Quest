package com.example.qr_quest;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

/**
 * QRActivity displays the scanned QR code image and a back button.
 */
public class QRActivity extends AppCompatActivity {

    private EditText commentEditText;
    private Button addCommentButton;

    /**
     * This method is called when the activity is created.
     * It sets up the view and displays the image of the scanned QR code.
     * @param savedInstanceState
     *       The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        QR scannedQR = (QR) getIntent().getSerializableExtra("scannedQR");
        User user = (User) getIntent().getSerializableExtra("user");

        // Setting back button
        ImageButton backButton = findViewById(R.id.back);
        Intent intent = getIntent();
        boolean comingFromGeoLocationFragment = intent.getBooleanExtra("Coming from GeoLocationFragment", false);
        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * This method is called when the user clicks a button. It creates a new Intent and starts the HomeActivity,
             * adding data to the Intent to indicate that the user is coming from the QRActivity. Once the Intent is started,
             * the QRActivity is finished and removed from the activity stack.
             * @param view
             *       The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QRActivity.this, HomeActivity.class);
                // Add some data to the intent to indicate that the user is coming from QRActivity
                intent.putExtra("comingFromQRActivity", true);
                startActivity(intent);
                finish();
            }
        });

        // Setting the Qr's avatar
        TextView avatarTextView = findViewById(R.id.avatar);
        avatarTextView.setText(scannedQR.getQRIcon());

        // Setting the QR's name with points
        TextView qrnameTextView = findViewById(R.id.scanned_title);
        qrnameTextView.setText(scannedQR.getQRName() + " - " + scannedQR.getScore() + " pts");

        // Setting the QR's photo
        ImageView showImage = findViewById(R.id.image_shown);
        if(!scannedQR.getImgString().equals("")) {
            setImageFromBase64(scannedQR.getImgString(), showImage);
        }

        // Setting QR's location icon and button
        LinearLayout showLocation = findViewById(R.id.location_shown);
        showLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRActivity.this, HomeActivity.class);
                // Add some data to the intent to indicate that the user is coming from QRActivity
                intent.putExtra("goingToMapsFragment", true);
                intent.putExtra("searchedQR", scannedQR);
                startActivity(intent);
                finish();
            }
        });
        TextView showRegion = findViewById(R.id.region);
        if(!scannedQR.getCity().equals("")) {
            showRegion.setText(scannedQR.getCity());
        }

        // Setting Comment functionality
        Button commentBtn = findViewById(R.id.comment);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(QRActivity.this);
                View view1 = inflater.inflate(R.layout.view_comments, null);

                // Call fillComment to retrieve all the comments for the scanned QR code
                Comment.fillComment(scannedQR, comments -> {
                    RecyclerView recyclerView = view1.findViewById(R.id.recyclerView);
                    CommentAdapter adapter = new CommentAdapter(comments);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(adapter);

                    final AlertDialog alertDialog = new AlertDialog.Builder(QRActivity.this)
                            .setView(view1)
                            .create();
                    alertDialog.show();

                    commentEditText = findViewById(R.id.commentEditText);
                    addCommentButton = findViewById(R.id.commentBtn);

                    addCommentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String commentText = commentEditText.getText().toString();
                            if (!commentText.isEmpty()) {

                                checkUserName(user, check -> QRDatabase.addComment(commentText, check, scannedQR, success -> {
                                    if (success) {
                                        Toast.makeText(QRActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                                        Comment newComment = new Comment(check, commentText);
                                        comments.add(newComment);
                                    }
                                }));



                                // Add the new comment to the list of comments
                                commentList.add(newComment);

                                // Update the RecyclerView to display the updated list of comments
                                CommentAdapter.notifyDataSetChanged();

                                // Clear the text in the EditText view
                                commentEditText.setText("");
                            }
                        }
                    });

                });
            }
        });

        // Setting delete functionality
        Button deleteBtn = findViewById(R.id.delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(QRActivity.this);
                View view1 = inflater.inflate(R.layout.confirm_delete_dialog, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(QRActivity.this)
                        .setView(view1)
                        .create();
                alertDialog.show();

                TextView deleteTitle = view1.findViewById(R.id.confirm_delete_title);
                deleteTitle.setText("Are you sure you want to delete " + scannedQR.getQRName() +
                        " from your wallet?");

                Button deleteConfirm = view1.findViewById(R.id.delete_yes_button);
                deleteConfirm.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        QRDatabase.deleteQR(getApplicationContext(), scannedQR, success -> {
                            if(success) {
                                Toast.makeText(QRActivity.this, scannedQR.getQRName() +
                                        " has been deleted from your wallet!", Toast.LENGTH_SHORT).show();
                                // go back to the Profile page on deletion
                                Intent intent = new Intent(QRActivity.this, HomeActivity.class);
                                intent.putExtra("comingFromQRActivity", true);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(QRActivity.this, "Failed to delete " +
                                        scannedQR.getQRName(), Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });
                    }
                });

                Button deleteCancel = view1.findViewById(R.id.delete_no_button); // add click listener to the "cancel" button
                deleteCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        // Setting Users who have scanned QR list
        checkUserName(user, check -> QRDatabase.getAllScannedUsers(check, scannedQR, this::setUpScannedUserRecyclerView));
    }

    /**
     * This method decodes a Base64 encoded string and sets the resulting image to an ImageView.
     * @param base64String
     *       The Base64 encoded string of the image.
     * @param imageView
     *       The ImageView to display the decoded image.
     */
    public void setImageFromBase64(String base64String, ImageView imageView) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
    }

    private void setUpScannedUserRecyclerView(List<String> scannedUserList) {
        RecyclerView mRecyclerView = findViewById(R.id.scanned_user_recycler_view);
        ScannedUserAdapter mAdapter = new ScannedUserAdapter(scannedUserList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void checkUserName(User user, OnSuccessListener<String> listener) {
        if(user == null) {
            UserDatabase.getCurrentUser(UserDatabase.getDevice(getApplicationContext()), userDoc -> {
                listener.onSuccess(userDoc.getString("user_name"));
            });
        } else {
            listener.onSuccess(user.getUsername());
        }
    }
}
