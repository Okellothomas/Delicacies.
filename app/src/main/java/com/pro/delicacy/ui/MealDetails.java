package com.pro.delicacy.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
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