package com.pro.delicacy.ui;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pro.delicacy.Credentials;
import com.pro.delicacy.R;
import com.pro.delicacy.models.Category;
import com.pro.delicacy.models.Meal;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealDetails extends Fragment implements View.OnClickListener{

    private static final int REQUEST_IMAGE_PICTURE = 111;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 11;
    private String currentPhotoPath;

    @BindView(R.id.mealImageView) ImageView mImageLabel;
    @BindView(R.id.mealNameTextView) TextView mNameLabel;
    @BindView(R.id.mealCategoryTextView) TextView mDescriptionLabel;
    @BindView(R.id.saveMealButton) TextView saveMealButton;


    private Meal mMeal;

    public MealDetails() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MealDetails newInstance(Meal meal) {
        MealDetails fragment = new MealDetails();
        Bundle args = new Bundle();
        args.putParcelable("meal", Parcels.wrap(meal));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert  getArguments() != null;
        mMeal = Parcels.unwrap(getArguments().getParcelable("meal"));
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_details, container, false);
        ButterKnife.bind(this, view);

        if (!mMeal.getStrMealThumb().contains("http")){
            Bitmap image = decodeFromFirebaseBase64(mMeal.getStrMealThumb());
            mImageLabel.setImageBitmap(image);
        } else {
            Picasso.get()
                    .load(mMeal.getStrMealThumb())
                    .into(mImageLabel);
        }

        Picasso.get().load(mMeal.getStrMealThumb()).into(mImageLabel);
        mNameLabel.setText(mMeal.getStrMeal());
        mDescriptionLabel.setText(mMeal.getStrCategory());

        saveMealButton.setOnClickListener(this);
        return view;
    }

    private static Bitmap decodeFromFirebaseBase64(String strMealThumb) {
        byte[] decodeByte = android.util.Base64.decode(strMealThumb, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_photo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_photo:
                onLaunchCameraclick();
            default:
                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_PICTURE && resultCode == getActivity().RESULT_OK){
            Toast.makeText(getContext(), "Image saved", Toast.LENGTH_LONG).show();


            int targetW = mImageLabel.getWidth()/3;
            int targetH = mImageLabel.getHeight()/2;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(currentPhotoPath,bmOptions);

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);


            bmOptions.inPurgeable = true;
            bmOptions.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath,bmOptions);

            mImageLabel.setImageBitmap(bitmap);
            encodeBitmapAndSaveToFirebase(bitmap);
        }
    }

    private void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayInputStream =  new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayInputStream);
        String encodedImage = Base64.encodeToString(byteArrayInputStream.toByteArray(), Base64.DEFAULT);
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference(Credentials.FIREBASE_CHILD_MEAL)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(mMeal.getPushId())
                .child("imageUrl");
        reference.setValue(encodedImage);
    }


    private void onLaunchCameraclick() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            onLaunchCamera();
        } else{
            // let's request permission.getContext,getContext().
            String[] permissionRequest = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                onLaunchCamera();
            }else {
                Toast.makeText(getContext(), "Can't open the camera without permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void onLaunchCamera() {
        Uri photoUri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName()+".fileprovider", createImageFile());
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        // tell the camera to request write permissions.
        takePicture.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(takePicture, REQUEST_IMAGE_PICTURE);
    }

    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "meal_JPG" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDirectory, imageFileName + ".jpg");

        // save a file: path for use with ACTION_VIEW intents
        String currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    @Override
    public void onClick(View v) {
        if (v == saveMealButton){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            DatabaseReference mealRef = FirebaseDatabase
                    .getInstance()
                    .getReference(Credentials.FIREBASE_CHILD_MEAL)
                    .child(uid);

            DatabaseReference pushRef = mealRef.push();
            String pushId = pushRef.getKey();
            mMeal.setPushId(pushId);
            pushRef.setValue(mMeal);

            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
        }
    }

}