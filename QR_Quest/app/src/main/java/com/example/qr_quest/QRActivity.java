package com.example.qr_quest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

public class QRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);
        ImageButton backButton = findViewById(R.id.back);
        ImageView showImage = findViewById(R.id.image_shown);

        Intent intent = getIntent();
        QR scannedQR = (QR) getIntent().getSerializableExtra("scannedQR");

        if (scannedQR.getImgString() != null){
            setImageFromBase64(scannedQR.getImgString(), showImage);
        }



        backButton.setOnClickListener(new View.OnClickListener() {
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

    public void setImageFromBase64(String base64String, ImageView imageView) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
    }
}