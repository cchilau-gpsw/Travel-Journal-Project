package com.example.traveljournalproject;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ManageTrip extends AppCompatActivity implements DateChangedListener {

    public static final int GALLERY_REQUEST_CODE = 1;
    public static final int CAMERA_REQUEST_CODE = 2;
    private EditText mEditTextTripName;
    private EditText mEditTextDestination;
    private RadioGroup mRadioGroupTripType;
    private RadioButton mRadioButtonCityBreak;
    private RadioButton mRadioButtonSeaSide;
    private RadioButton mRadioButtonMountains;
    private RatingBar mRatingBarEvaluation;
    private SeekBar mSeekBarPrice;
    private Button mButtonStartDate;
    private Button mButtonEndDate;
    private Button mButtonSaveTrip;
    private String mDatabaseDocumentID;
    private String mImageLocation;
    private Date mStartDate;
    private Date mEndDate;


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
                String startDate = bundle.getString(TravelDestinationsFragment.START_DATE);
                if (startDate != null && !startDate.isEmpty()) {
                    mButtonStartDate.setText(startDate);
                }
                String endDate = bundle.getString(TravelDestinationsFragment.END_DATE);
                if (endDate != null && !endDate.isEmpty()) {
                    mButtonEndDate.setText(endDate);
                }

                mDatabaseDocumentID = bundle.getString(TravelDestinationsFragment.DATABASE_DOCUMENT_ID);
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
        mButtonStartDate = findViewById(R.id.button_start_date);
        mButtonEndDate = findViewById(R.id.button_end_date);
        mRadioGroupTripType = findViewById(R.id.radio_group_trip_type);
        mButtonSaveTrip = findViewById(R.id.button_save);
    }

    public void selectPhotoFromGallery(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (ContextCompat.checkSelfPermission(ManageTrip.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ManageTrip.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE && data != null) {
                getCapturedImageAndSaveToStorage(data);
            }
            if (requestCode == GALLERY_REQUEST_CODE && data != null && data.getData() != null) {
                getGalleryImageAndSaveToStorage(data);

            }
        }
    }

    private void getGalleryImageAndSaveToStorage(Intent data) {
        Uri selectedImageUri = data.getData();
        final String currentUserID = FirebaseAuth.getInstance().getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(TravelDestinationsFragment.DESTINATIONS_COLLECTION + "_" + currentUserID);
        final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                + "." + getFileExtension(selectedImageUri));

        fileReference.putFile(selectedImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("**************", "onSuccess: uri= " + uri.toString());
                                mImageLocation = uri.toString();
                                SharedPreferences prefs = getSharedPreferences("location", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("location", uri.toString());
                                editor.apply();
                                if (mDatabaseDocumentID != null && !mDatabaseDocumentID.isEmpty()) {
                                    FirebaseFirestore.getInstance().collection(TravelDestinationsFragment.DESTINATIONS_COLLECTION + "_" + currentUserID).document(mDatabaseDocumentID)
                                            .update("imageLocation", uri.toString());
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ManageTrip.this, "Failed to upload file", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getCapturedImageAndSaveToStorage(Intent data) {
        Bundle bundle = data.getExtras();
        final Bitmap bmp = (Bitmap) bundle.get("data");
        final String currentUserID = FirebaseAuth.getInstance().getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(TravelDestinationsFragment.DESTINATIONS_COLLECTION + "_" + currentUserID);
        final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] transferData = baos.toByteArray();

        fileReference.putBytes(transferData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("**************", "onSuccess: uri= " + uri.toString());
                                mImageLocation = uri.toString();
                                SharedPreferences prefs = getSharedPreferences("location", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("location", uri.toString());
                                editor.apply();
                                if (mDatabaseDocumentID != null && !mDatabaseDocumentID.isEmpty()) {
                                    FirebaseFirestore.getInstance().collection(TravelDestinationsFragment.DESTINATIONS_COLLECTION + "_" + currentUserID).document(mDatabaseDocumentID)
                                            .update("imageLocation", uri.toString());
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ManageTrip.this, "Failed to upload file", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void selectStartDateOnClick(View view) {
        DialogFragment newFragment = new CustomStartDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    public void selectEndDateOnClick(View view) {
        DialogFragment newFragment = new CustomEndDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ManageTrip.this, MyTrips.class);
        startActivity(intent);
    }

    public void saveDestinationOnClick(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserID = FirebaseAuth.getInstance().getUid();
        Map<String, Object> destination = new HashMap<>();
        destination.put("season", mEditTextTripName.getText().toString());
        destination.put("location", mEditTextDestination.getText().toString());
        destination.put("price", mSeekBarPrice.getProgress());
        destination.put("rating", mRatingBarEvaluation.getRating());
        destination.put("startDate", mStartDate);
        destination.put("endDate", mEndDate);
        SharedPreferences prefs = getSharedPreferences("location", Context.MODE_PRIVATE);
        mImageLocation = prefs.getString("location", "");
        if (mImageLocation != null && !mImageLocation.isEmpty()) {
            destination.put("imageLocation", mImageLocation);
        }
        destination.put("tripType", getTripType());
        if (mDatabaseDocumentID == null || mDatabaseDocumentID.isEmpty()) {
            db.collection(TravelDestinationsFragment.DESTINATIONS_COLLECTION + "_" + currentUserID)
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
        } else {
            DocumentReference docRef = db.collection(TravelDestinationsFragment.DESTINATIONS_COLLECTION + "_" + currentUserID).document(mDatabaseDocumentID);
            docRef.update("season", mEditTextTripName.getText().toString());
            docRef.update("location", mEditTextDestination.getText().toString());
            docRef.update("tripType", getTripType());
            docRef.update("price", mSeekBarPrice.getProgress());
            docRef.update("rating", mRatingBarEvaluation.getRating());
            if (mStartDate != null) {
                docRef.update("startDate", mStartDate);
            }
            if (mEndDate != null) {
                docRef.update("endDate", mEndDate);
            }
        }
        startActivity(new Intent(ManageTrip.this, MyTrips.class));
    }

    private String getTripType() {
        if (mRadioButtonSeaSide.isChecked()) return mRadioButtonSeaSide.getText().toString();
        if (mRadioButtonMountains.isChecked()) return mRadioButtonMountains.getText().toString();
        if (mRadioButtonCityBreak.isChecked()) return mRadioButtonCityBreak.getText().toString();
        return "None selected";
    }

    @Override
    public void onStartDateChanged(int year, int month, int day) {
        mButtonStartDate.setText(day + "/" + (month + 1) + "/" + year);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            mStartDate = format.parse(day + "/" + (month + 1) + "/" + year);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEndDateChanged(int year, int month, int day) {
        mButtonEndDate.setText(day + "/" + (month + 1) + "/" + year);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            mEndDate = format.parse(day + "/" + (month + 1) + "/" + year);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
