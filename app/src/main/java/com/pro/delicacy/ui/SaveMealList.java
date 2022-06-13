package com.pro.delicacy.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pro.delicacy.Credentials;
import com.pro.delicacy.R;
import com.pro.delicacy.adapters.FirebaseMealViewHolder;
import com.pro.delicacy.models.Meal;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SaveMealList extends AppCompatActivity {

    private DatabaseReference mMealReference;
    private FirebaseRecyclerAdapter<Meal, FirebaseMealViewHolder> mFirebaseAdapter;

    @BindView(R.id.recyclerView) RecyclerView mRecylerView;
    @BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);
        ButterKnife.bind(this);

        mMealReference = FirebaseDatabase
                .getInstance()
                .getReference(Credentials.FIREBASE_CHILD_MEAL);
        setUpFireBaseAdapter();
        hideProgressBar();
        showMeals();
    }

    private void setUpFireBaseAdapter(){
        FirebaseRecyclerOptions<Meal> options = new FirebaseRecyclerOptions.Builder<Meal>()
                .setQuery(mMealReference, Meal.class)
                .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Meal, FirebaseMealViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseMealViewHolder holder, int position, @NonNull Meal model) {
                holder.bindMeal(model);
            }

            @NonNull
            @Override
            public FirebaseMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meals_categories_item, parent, false);
                return new FirebaseMealViewHolder(view);
            }
        };
        mRecylerView.setLayoutManager(new LinearLayoutManager(this));
        mRecylerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFirebaseAdapter != null){
            mFirebaseAdapter.stopListening();
        }
    }

    private void showMeals() {
        mRecylerView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}
