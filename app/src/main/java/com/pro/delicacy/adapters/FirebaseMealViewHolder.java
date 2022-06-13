package com.pro.delicacy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pro.delicacy.Credentials;
import com.pro.delicacy.Delicacies;
import com.pro.delicacy.MyMealDetails;
import com.pro.delicacy.R;
import com.pro.delicacy.models.Meal;
import com.pro.delicacy.ui.MealDetails;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirebaseMealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    View mView;
    Context mContext;

    public FirebaseMealViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindMeal(Meal meal){

            ImageView mMealImageView = (ImageView) mView.findViewById(R.id.mealImageView);
            TextView mMealName = (TextView) mView.findViewById(R.id.mealNameTextView);
            TextView mMealCategory = (TextView) mView.findViewById(R.id.mealCategoryTextView);

            Picasso.get().load(meal.getStrMealThumb()).into(mMealImageView);
            mMealName.setText(meal.getStrMeal());
            mMealCategory.setText(meal.getStrCategory());
        }

        @Override
        public void onClick(View v) {

        final ArrayList<Meal> meals = new ArrayList<>();

            DatabaseReference ref = FirebaseDatabase
                    .getInstance()
                    .getReference(Credentials.FIREBASE_CHILD_MEAL);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot mealSnapShot : snapshot.getChildren()) {
                        meals.add(mealSnapShot.getValue(Meal.class));
                    }

                    int itemPosition = getLayoutPosition();

                    Intent intent = new Intent(mContext, MyMealDetails.class);
                    intent.putExtra("position", itemPosition + "");
                    intent.putExtra("meal", Parcels.wrap(meals));
                    mContext.startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }
}
