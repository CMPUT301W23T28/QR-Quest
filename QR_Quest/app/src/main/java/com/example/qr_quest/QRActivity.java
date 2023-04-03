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
    private CommentAdapter commentAdapter;
    private User user;

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
        user = (User) getIntent().getSerializableExtra("user");

        // Setting back button
        ImageButton backButton = findViewById(R.id.btn_qr_back);
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
                // Add some data to the intent to indicate that the user is coming from QRActivity
                // or another User's QRActivity
                Intent intent = getIntent();
                Intent i = new Intent(QRActivity.this, HomeActivity.class);
                if (intent.getBooleanExtra("comingFromMapsFragment",false)) {
                    // Add some data to the intent to indicate that the user is coming from QRActivity
                    i.putExtra("goingToMapsFragment", true);
                    i.putExtra("searchedQR", scannedQR);
                    startActivity(i);
                    finish();
                } else if (intent.getBooleanExtra("comingFromProfile",false) || (intent.getBooleanExtra("comingFromGeoLocation",false))) {
                    i.putExtra("goingToProfile", true);
                    startActivity(i);
                    finish();
                } else if (intent.getBooleanExtra("comingFromUserActivity", false)){
                    finish();
                }
            }
        });

        // Setting the QR's avatar
        TextView avatarTextView = findViewById(R.id.txtview_qr_avatar);
        avatarTextView.setText(scannedQR.getQRIcon());

        // Setting QR's name and score
        TextView qrnameTextView = findViewById(R.id.txtview_qr_scanned_title);
        checkUserName(checkName -> QRDatabase.checkIfUserHasQR(checkName, scannedQR, check -> {
            if(check) {
                qrnameTextView.setText(scannedQR.getQRName() + " - " + scannedQR.getScore() + " pts");
            } else {
                qrnameTextView.setText(scannedQR.getQRName());
            }
        }));

        // Setting the QR's photo
        ImageView showImage = findViewById(R.id.imgview_qr_image_shown);
        if(!scannedQR.getImgString().equals("")) {
            setImageFromBase64(scannedQR.getImgString(), showImage);
        }

        // Setting QR's location icon and button
        LinearLayout showLocation = findViewById(R.id.qr_location_shown);
        TextView showRegion = findViewById(R.id.txtview_qr_region);
        if(scannedQR.getLongitude() != -999 && scannedQR.getLatitude() != -999) {
            showRegion.setText(scannedQR.getCity());
            showLocation.setOnClickListener(v -> {
                Intent intent = new Intent(QRActivity.this, HomeActivity.class);
                // Add some data to the intent to indicate that the user is coming from QRActivity
                intent.putExtra("goingToMapsFragment", true);
                intent.putExtra("searchedQR", scannedQR);
                startActivity(intent);
                finish();
            });
        }

        // Setting Comment functionality
        Button commentBtn = findViewById(R.id.btn_qr_comment);
        commentBtn.setOnClickListener(view -> {
            LayoutInflater inflater = LayoutInflater.from(QRActivity.this);
            View view1 = inflater.inflate(R.layout.view_comments, null);

            // Setting the QR's caption
            TextView captionCommenter = view1.findViewById(R.id.txtview_comment_caption_username);
            TextView captionTextView = view1.findViewById(R.id.txtview_comment_caption_text);
            TextView separatorLine = view1.findViewById(R.id.separator);
            captionCommenter.setVisibility(view1.GONE);
            captionTextView.setVisibility(view1.GONE);
            separatorLine.setVisibility(view1.INVISIBLE);
            checkUserName(check -> UserDatabase.getCaption(check, scannedQR, caption -> {
                if(!caption.equals("")) {
                    captionCommenter.setVisibility(view1.VISIBLE);
                    captionTextView.setVisibility(view1.VISIBLE);
                    separatorLine.setVisibility(view1.VISIBLE);
                    captionCommenter.setText(check);
                    captionTextView.setText(caption);
                }
            }));

            // Call fillComment to retrieve all the comments for the scanned QR code
            Comment.fillComment(scannedQR, comments -> {
                RecyclerView recyclerView = view1.findViewById(R.id.recycler_view_leaderboard);
                commentAdapter = new CommentAdapter(comments);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(commentAdapter);

                final AlertDialog alertDialog = new AlertDialog.Builder(QRActivity.this)
                        .setView(view1)
                        .create();
                alertDialog.show();

                commentEditText = view1.findViewById(R.id.edittext_comment);
                addCommentButton = view1.findViewById(R.id.btn_comment);
                addCommentButton.setOnClickListener(v -> checkUserName(checkName -> QRDatabase.checkIfUserHasQR(checkName, scannedQR, check -> {
                    if(check) {
                        String commentText = commentEditText.getText().toString();
                        if (!commentText.isEmpty()) {
                            QRDatabase.addComment(commentText, checkName, scannedQR, success -> {
                                if (success) {
                                    Toast.makeText(QRActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                                    Comment newComment = new Comment(checkName, commentText);
                                    comments.add(newComment);
                                    commentAdapter.notifyDataSetChanged();
                                    commentEditText.setText("");
                                }
                            });
                        } else {
                            Toast.makeText(QRActivity.this, "Your comment was empty!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(QRActivity.this, "You have not scanned this QR yet!", Toast.LENGTH_SHORT).show();
                    }
                })));
            });
        });

        // Setting delete functionality
        Button deleteBtn = findViewById(R.id.btn_qr_delete);
        if(user != null) {
            deleteBtn.setVisibility(View.GONE);
        }
        deleteBtn.setOnClickListener(view -> {
            LayoutInflater inflater = LayoutInflater.from(QRActivity.this);
            View view1 = inflater.inflate(R.layout.confirm_delete_dialog, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(QRActivity.this)
                    .setView(view1)
                    .create();
            alertDialog.show();

            TextView deleteTitle = view1.findViewById(R.id.textview_confirmdel_title);
            deleteTitle.setText("Are you sure you want to delete " + scannedQR.getQRName() +
                    " from your wallet?");

            Button deleteConfirm = view1.findViewById(R.id.btn_confirmdel_yes);
            deleteConfirm.setOnClickListener(view2 -> QRDatabase.deleteQR(getApplicationContext(), scannedQR, success -> {
                if(success) {
                    Toast.makeText(QRActivity.this, scannedQR.getQRName() +
                            " has been deleted from your wallet!", Toast.LENGTH_SHORT).show();

                    // go back to the Profile page on deletion
                    Intent intent = new Intent(QRActivity.this, HomeActivity.class);
                    intent.putExtra("goingToProfile", true);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(QRActivity.this, "Failed to delete " +
                            scannedQR.getQRName(), Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }));

            Button deleteCancel = view1.findViewById(R.id.btn_confirmdel_no);
            deleteCancel.setOnClickListener(v -> alertDialog.dismiss());
        });

        // Setting Users who have scanned QR list
        checkUserName(check -> QRDatabase.getAllScannedUsers(check, scannedQR, this::setUpScannedUserRecyclerView));
    }

    /**
     * This method decodes a Base64 encoded string and sets the resulting image to an ImageView.
     *
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

    /**
     Sets up the RecyclerView for displaying the list of scanned users.

     @param scannedUserList
            the list of scanned user IDs
     */
    private void setUpScannedUserRecyclerView(List<String> scannedUserList) {
        RecyclerView mRecyclerView = findViewById(R.id.scanned_user_recycler_view);
        ScannedUserAdapter mAdapter = new ScannedUserAdapter(scannedUserList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Checks the username of a given user and invokes the onSuccess listener with the username.
     *
     * @param listener
     *      the listener to invoke with the username once it is retrieved
     */
    private void checkUserName(OnSuccessListener<String> listener) {
        if(user == null) {
            UserDatabase.getCurrentUser(UserDatabase.getDevice(getApplicationContext()), userDoc ->
                    listener.onSuccess(userDoc.getString("user_name")));
        } else {
            listener.onSuccess(user.getUsername());
        }
    }
}
