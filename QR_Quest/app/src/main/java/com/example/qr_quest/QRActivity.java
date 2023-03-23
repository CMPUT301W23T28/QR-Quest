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
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * QRActivity displays the scanned QR code image and a back button.
 */
public class QRActivity extends AppCompatActivity {


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
        ImageButton backButton = findViewById(R.id.back);
        ImageView showImage = findViewById(R.id.image_shown);
        Button commentBtn = findViewById(R.id.comment);

        comment[] comments = new comment[]{
                new comment("meshit","Great QR code!"),
                new comment("bobo_619","I have to get this one"),


        };


        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(QRActivity.this);
                View view1 = inflater.inflate(R.layout.view_comments, null);

                CommentAdapter adapter;
                RecyclerView recyclerView = view1.findViewById(R.id.recyclerView);
                adapter = new CommentAdapter(comments);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);


                final AlertDialog alertDialog = new AlertDialog.Builder(QRActivity.this)
                        .setView(view1)
                        .create();

                alertDialog.show();
            }
        });

        QR scannedQR = (QR) getIntent().getSerializableExtra("scannedQR");
        if (scannedQR != null) {
            setImageFromBase64(scannedQR.getImgString(), showImage);
        }

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
}
