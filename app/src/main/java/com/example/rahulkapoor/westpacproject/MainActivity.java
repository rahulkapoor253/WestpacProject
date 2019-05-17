package com.example.rahulkapoor.westpacproject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnGallery;
    private Intent galleryIntent;
    private static int IMG_RESULT = 1;
    private String ImageDecode;
    private ImageView imageViewLoad;
    private Bitmap bmp;
    BitmapFactory.Options options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGallery = (Button) findViewById(R.id.btn_opengallery);
        imageViewLoad = (ImageView) findViewById(R.id.imageView1);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //open up gallery and select images;
                galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


                startActivityForResult(Intent.createChooser(galleryIntent, "Select picture"), IMG_RESULT);

            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("gallery", "on activity result entered");

        try {

            Log.d("gallery", data.getData().toString());

            if (requestCode == IMG_RESULT && resultCode == RESULT_OK
                    && data != null) {

                final Uri URI = data.getData();
                String[] FILE = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(URI,
                        FILE, null, null, null);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                cursor.close();

                Log.d("image path", ImageDecode);

                findViewById(R.id.imageView1).post(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) findViewById(R.id.imageView1)).setImageURI(URI);
                    }
                });

            }
        } catch (Exception e) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG)
                    .show();
        }

    }

}
