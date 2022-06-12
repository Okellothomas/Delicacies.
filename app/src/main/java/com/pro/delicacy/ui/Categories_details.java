package com.pro.delicacy.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pro.delicacy.R;
import com.pro.delicacy.models.Category;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Categories_details#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Categories_details extends Fragment  implements View.OnClickListener{

   @BindView(R.id.delicaciesImageView) ImageView mImageLabel;
   @BindView(R.id.delicaciesNameTextView) TextView mNameLabel;
   @BindView(R.id.delicaciesDescriptionTextView) TextView mDescriptionLabel;
   @BindView(R.id.saveCategory) TextView mSaveCategory;

   private Category mCategory;

    public Categories_details() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     */
    // TODO: Rename and change types and number of parameters
    public static Categories_details newInstance(Category category) {
        Categories_details fragment = new Categories_details();
        Bundle args = new Bundle();
        args.putParcelable("category", Parcels.wrap(category));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        mCategory = Parcels.unwrap(getArguments().getParcelable("category"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_categories_details, container, false);
        ButterKnife.bind(this, view);
        Picasso.get().load(mCategory.getStrCategoryThumb()).into(mImageLabel);
        mNameLabel.setText(mCategory.getStrCategory());
        mDescriptionLabel.setText(mCategory.getStrCategoryDescription());

        mSaveCategory.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mSaveCategory){
            DatabaseReference categoryRef = FirebaseDatabase
                    .getInstance()
                    .getReference();
            categoryRef.push().setValue(mCategory);
            Toast.makeText(getContext(),"Saved", Toast.LENGTH_SHORT).show();
        }
    }
}