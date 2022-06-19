package com.pro.delicacy.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealDetails extends Fragment implements View.OnClickListener{

    private static final int REQUEST_IMAGE_PICTURE = 111;

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
        Picasso.get().load(mMeal.getStrMealThumb()).into(mImageLabel);
        mNameLabel.setText(mMeal.getStrMeal());
        mDescriptionLabel.setText(mMeal.getStrCategory());

        saveMealButton.setOnClickListener(this);
        return view;
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
                onLaunchCamera();
            default:
                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_PICTURE && resultCode == getActivity().RESULT_OK){
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            mImageLabel.setImageBitmap(bitmap);
            encodeBitmapAndSaveToFirebase(bitmap);
        }
    }

    private void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
    }

    private void onLaunchCamera() {
        Intent picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (picture.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(picture, REQUEST_IMAGE_PICTURE);
        }
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
//            mealRef.push().setValue(mMeal);
            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
        }
    }
}