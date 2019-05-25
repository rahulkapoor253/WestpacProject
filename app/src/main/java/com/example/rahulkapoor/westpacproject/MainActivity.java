package com.example.rahulkapoor.westpacproject;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnGallery, btnSendMail;
    private Intent galleryIntent;
    private static int IMG_RESULT = 1;
    private String ImageEncode;
    private EditText etEmail;
    private Bitmap bmp;
    BitmapFactory.Options options;
    private ArrayList<String> imagesList = new ArrayList<>();
    private GalleryAdapter galleryAdapter;
    private GridView gvGallery;
    private ArrayList<Uri> mArrayUri = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGallery = (Button) findViewById(R.id.btn_opengallery);
        btnSendMail = (Button) findViewById(R.id.btn_sendmail);
        gvGallery = (GridView) findViewById(R.id.gv);
        etEmail = (EditText) findViewById(R.id.etEmail);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //open up gallery and select images;
                galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                // galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


                startActivityForResult(Intent.createChooser(galleryIntent, "Select picture"), IMG_RESULT);

            }
        });

        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //check size of uri list before sending mail
                if (mArrayUri.size() > 0) {
                    boolean checkConnection = isOnline();
                    if (checkConnection) {
                        //net is connected
                        String userEmail = etEmail.getText().toString() + "@westpac.com.au";

                        //send email via smtp;


                    } else {
                        //net is off
                        Toast.makeText(MainActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Select an Image First", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("gallery", "on activity result entered");

        try {

            // Log.d("gallery", data.getData() + "");


            if (requestCode == IMG_RESULT && resultCode == RESULT_OK
                    && null != data) {

                String[] FILE = {MediaStore.Images.Media.DATA};
                imagesList = new ArrayList<String>();

                if (data.getData() != null) {

                    Log.d("gallery", data.getData() + "");

                    final Uri URI = data.getData();
                    // String[] FILE = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(URI,
                            FILE, null, null, null);

                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(FILE[0]);
                    ImageEncode = cursor.getString(columnIndex);
                    cursor.close();


                    mArrayUri = new ArrayList<Uri>();
                    mArrayUri.add(URI);
                    galleryAdapter = new GalleryAdapter(getApplicationContext(), mArrayUri);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                } else {

                    if (data.getClipData() != null) {
//when multiple images are selected;
                        Log.d("gallery", data.getClipData().getItemCount() + "");

                        ClipData mClipData = data.getClipData();
                        mArrayUri = new ArrayList<Uri>();

                        //limit user of selecting 9 images;
                        if (mClipData.getItemCount() > 9) {
                            Toast.makeText(this, "Cannot proceed with more than 9 images", Toast.LENGTH_SHORT).show();
                        } else {

                            for (int i = 0; i < mClipData.getItemCount(); i++) {

                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                Log.d("uri data", item.getUri() + "");
                                mArrayUri.add(uri);
                                // Get the cursor
                                Cursor cursor = getContentResolver().query(uri, FILE, null, null, null);
                                // Move to first row
                                cursor.moveToFirst();

                                galleryAdapter = new GalleryAdapter(getApplicationContext(), mArrayUri);
                                gvGallery.setAdapter(galleryAdapter);
                                gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                        .getLayoutParams();
                                mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);
                            }

                        }

                    }


                }
            } else {
                Toast.makeText(this, "You havent picked any image", Toast.LENGTH_LONG)
                        .show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG)
                    .show();
        }

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}
