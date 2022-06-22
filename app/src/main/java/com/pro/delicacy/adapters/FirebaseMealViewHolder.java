package com.pro.delicacy.adapters;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.pro.delicacy.util.ItemTouchHelperViewHolder;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirebaseMealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {
    View mView;
    Context mContext;
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;
    public ImageView mMealImageView;

    public FirebaseMealViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
//        itemView.setOnClickListener(this);
    }

    public void bindMeal(Meal meal){

        mMealImageView = (ImageView) mView.findViewById(R.id.mealImageView);
        TextView mMealName = (TextView) mView.findViewById(R.id.mealNameTextView);
        TextView mMealCategory = (TextView) mView.findViewById(R.id.mealCategoryTextView);

        if (!meal.getStrMealThumb().contains("http")){
            try {
                Bitmap imageBit = decodeFromFirebaseBase64(meal.getStrMealThumb());
                mMealImageView.setImageBitmap(imageBit);
            } catch (IOException e){
                e.printStackTrace();
            }
        } else {
            Picasso.get().load(meal.getStrMealThumb()).into(mMealImageView);
            mMealName.setText(meal.getStrMeal());
            mMealCategory.setText(meal.getStrCategory());
        }
            Picasso.get().load(meal.getStrMealThumb()).into(mMealImageView);
            mMealName.setText(meal.getStrMeal());
            mMealCategory.setText(meal.getStrCategory());
        }

    private static Bitmap decodeFromFirebaseBase64(String strMealThumb) throws IOException{
        byte[] decodedByteArray = android.util.Base64.decode(strMealThumb, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }


        @Override
        public void onClick(View v) {

        final ArrayList<Meal> meals = new ArrayList<>();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();

            DatabaseReference ref = FirebaseDatabase
                    .getInstance()
                    .getReference(Credentials.FIREBASE_CHILD_MEAL)
                    .child(uid);

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

    @Override
    public void onItemSelected() {

        itemView.animate()
                .alpha(0.7f)
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(600);
    }

    @Override
    public void onItemClear() {
        itemView.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f);
    }
}
