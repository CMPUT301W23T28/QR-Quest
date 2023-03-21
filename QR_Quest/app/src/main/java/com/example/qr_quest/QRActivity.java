package com.example.qr_quest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
        Intent intent = getIntent();
        boolean comingFromGeoLocationFragment = intent.getBooleanExtra("Coming from GeoLocationFragment", false);
        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * This method is called when the user clicks the back button. It creates a new Intent and starts the HomeActivity,
             * adding data to the Intent to indicate that the user is coming from the QRActivity. Once the Intent is started,
             * the QRActivity is finished and removed from the activity stack.
             * @param view
             *       The view that was clicked.
             */
            @Override
            public void onClick(View view) {

                if (comingFromGeoLocationFragment){
                    Intent intent = new Intent(QRActivity.this, HomeActivity.class);
                    // Add some data to the intent to indicate that the user is coming from QRActivity
                    intent.putExtra("comingFromGeoLocationFrag", true);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(QRActivity.this, HomeActivity.class);
                    // Add some data to the intent to indicate that the user is coming from QRActivity
                    intent.putExtra("comingFromMapsFragment", true);
                    startActivity(intent);
                    finish();
                }



            }
        });

        ImageView showLocation = findViewById(R.id.selected_image);
        if (comingFromGeoLocationFragment){
            showLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(QRActivity.this, HomeActivity.class);
                    // Add some data to the intent to indicate that the user is coming from QRActivity
                    intent.putExtra("comingFromMapsFragment", true);
                    startActivity(intent);
                    finish();
                }
            });
        }
        ImageView showImage = findViewById(R.id.image_shown);
        QR scannedQR = (QR) getIntent().getSerializableExtra("scannedQR");

        if (scannedQR != null) {
            setImageFromBase64(scannedQR.getImgString(), showImage);

            TextView avatarTextView = findViewById(R.id.avatar);
            avatarTextView.setText(scannedQR.getQRIcon());

            TextView qrnameTextView = findViewById(R.id.scanned_title);
            qrnameTextView.setText(scannedQR.getQRName() + " - " + scannedQR.getScore() + " pts");

            // on confirm delete
//            QRDatabase.deleteQR(UserDatabase.getDevice(getApplicationContext()), scannedQR.getQRName(), delete ->{
//                if(delete) {
//                    Toast.makeText(getApplicationContext(),
//                            scannedQR.getQRName() + " has been deleted from your wallet!", Toast.LENGTH_SHORT).show();
//                    //TODO may have to refresh page for wallet, update on users db and both lists
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "Could not delete " + scannedQR.getQRName() + " from your wallet!", Toast.LENGTH_SHORT).show();
//                }
//            });
        }
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
