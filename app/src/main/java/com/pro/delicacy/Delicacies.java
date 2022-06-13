package com.pro.delicacy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.pro.delicacy.ui.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pro.delicacy.adapters.CategoriesAdapter;
import com.pro.delicacy.models.CategoriesResponse;
import com.pro.delicacy.models.Category;
import com.pro.delicacy.network.DelicacyAPi;
import com.pro.delicacy.network.DelicacyClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Delicacies extends AppCompatActivity {

    @BindView(R.id.recyclerView) RecyclerView mRecylerView;
    @BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.mealButton) TextView mealButton;
    @BindView(R.id.SavedButton) TextView savedButton;

    private CategoriesAdapter mAdapter;

    private DatabaseReference mSearchedMealReference;
    private ValueEventListener mSearchedMealReferenceListner;
//    public List<Category> categories;

    // implementing shared preferences.
//    private SharedPreferences mySharedPreference;
//    private SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSearchedMealReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Credentials.FIREBASE_CHILD_SEARCHED_MEAL);

        // adding an event listener.
         mSearchedMealReferenceListner = mSearchedMealReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mealSnapshot: snapshot.getChildren()){
                    String meal = mealSnapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // for handling errors.
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delicacies);
        ButterKnife.bind(this);

//        mySharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
//        myEditor = mySharedPreference.edit();

        // the button and edittext fields.
        mealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (v == mealButton){
//                    String meal = mEditMealName.getText().toString();
//                    addToMySharedPreferences(meal);
//                    Intent intent = new Intent(Delicacies.this, Meals.class);
//                    startActivity(intent);
//                }
                // get user input

                Intent intent = new Intent(Delicacies.this, Meals.class);
                startActivity(intent);

//                if (v == mealButton){
//                    String meal = mEditMealName.getText().toString();
//                    saveMealToFirebase(meal);
//                    // declare the intent variable.
//                    Intent intent = new Intent(Delicacies.this, Meals.class);
//                    startActivity(intent);
//
//                }
//
//
//                String meal = mEditMealName.getText().toString();
//                // declare the intent variable.
//                Intent intent = new Intent(Delicacies.this, Meals.class);
//                intent.putExtra("meal", meal);
//                startActivity(intent);
//            }
//
//            public void saveMealToFirebase(String meal){
//                mSearchedMealReference.push().setValue(meal);
//            }
////            private void addToMySharedPreferences(String meal) {
//                myEditor.putString(Credentials.PREFERENCE_MEAL_NAME, meal).apply();
//            }
            }
        });

        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Delicacies.this, SaveMealList.class);
                startActivity(intent);
            }
        });


        DelicacyAPi client = DelicacyClient.getClient();

        Call<CategoriesResponse> call = client.categoryMeals();

        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.isSuccessful()){
                    List<Category> categories = response.body().getCategories();
                    mAdapter = new CategoriesAdapter(mRecylerView.getContext(),categories);
                    mRecylerView.setAdapter(mAdapter);
                    mRecylerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mRecylerView.setHasFixedSize(true);
                    showCategories();
                    hideProgressBar();
                } else {
                    showUnsuccessfulMessage();
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                hideProgressBar();
                showFailureMessage();
            }
        });
    }

    private void showFailureMessage() {
        mErrorTextView.setText("Something went wrong. Please check your Internet connection and try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showUnsuccessfulMessage() {
        mErrorTextView.setText("Something went wrong. Please try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showCategories() {
        mRecylerView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchedMealReference.removeEventListener(mSearchedMealReferenceListner);
    }
}