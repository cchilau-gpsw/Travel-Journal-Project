package com.example.traveljournalproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.Logging;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ManageTrip extends AppCompatActivity {

    public static final int GALLERY_REQUEST_CODE = 1;
    public static final int CAMERA_REQUEST_CODE = 2;
    private EditText mEditTextTripName;
    private EditText mEditTextDestination;
    private RadioButton mRadioButtonCityBreak;
    private RadioButton mRadioButtonSeaSide;
    private RadioButton mRadioButtonMountains;
    private RatingBar mRatingBarEvaluation;
    private SeekBar mSeekBarPrice;
    private String mImageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_trip);

        initView();

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String tripName = bundle.getString(TravelDestinationsFragment.TRIP_NAME);
                mEditTextTripName.setText(tripName);
                String destination = bundle.getString(TravelDestinationsFragment.DESTINATION);
                mEditTextDestination.setText(destination);
                String tripType = bundle.getString(TravelDestinationsFragment.TRIP_TYPE);
                if (tripType != null && !tripType.isEmpty()) {
                    switch (tripType) {
                        case "Sea Side":
                            mRadioButtonSeaSide.toggle();
                            break;
                        case "Mountains":
                            mRadioButtonMountains.toggle();
                            break;
                        case "City Break":
                            mRadioButtonCityBreak.toggle();
                            break;
                    }
                }
                float rating = bundle.getFloat(TravelDestinationsFragment.RATING);
                mRatingBarEvaluation.setRating(rating);
                int price = bundle.getInt(TravelDestinationsFragment.PRICE);
                mSeekBarPrice.setProgress(price);
            }
        }
    }

    private void initView() {
        mEditTextTripName = findViewById(R.id.edit_text_trip_name);
        mEditTextDestination = findViewById(R.id.edit_text_destination);
        mRadioButtonCityBreak = findViewById(R.id.radio_button_city_break);
        mRadioButtonMountains = findViewById(R.id.radio_button_mountains);
        mRadioButtonSeaSide = findViewById(R.id.radio_button_sea_side);
        mRatingBarEvaluation = findViewById(R.id.rating_bar);
        mSeekBarPrice = findViewById(R.id.seek_bar_price);
    }

    public void selectPhotoFromGallery(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    public void takePicture(View view) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (ContextCompat.checkSelfPermission(ManageTrip.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ManageTrip.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("destinations");
            if (requestCode == GALLERY_REQUEST_CODE) {
                Uri selectedImageUri = data.getData();

                storageReference.putFile(selectedImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                               String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                               Log.d("URL::::::::: ", downloadUrl);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ManageTrip.this, "Failed to upload file", Toast.LENGTH_LONG).show();
                            }
                        });


            }
        }
    }

    public void selectStartDateOnClick(View view) {
        DialogFragment newFragment = new CustomDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    public void selectEndDateOnClick(View view) {
        DialogFragment newFragment = new CustomDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ManageTrip.this, MyTrips.class);
        startActivity(intent);
    }

    public void saveDestinationOnClick(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> destination = new HashMap<>();
        destination.put("season", mEditTextTripName.getText().toString());
        destination.put("location", mEditTextDestination.getText().toString());
        destination.put("imageLocation", "https://static.toiimg.com/photo/msid-58515713,width-96,height-65.cms");

        db.collection("destinations")
                .add(destination)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(ManageTrip.this, "DocumentSnapshot added with ID: " + documentReference.getId(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ManageTrip.this, "Error adding document", Toast.LENGTH_LONG).show();
                    }
                });

    }
}
