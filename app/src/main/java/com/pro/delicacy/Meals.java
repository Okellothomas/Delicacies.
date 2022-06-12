package com.pro.delicacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pro.delicacy.adapters.CategoriesAdapter;
import com.pro.delicacy.adapters.MealAdapter;
import com.pro.delicacy.models.CategoriesResponse;
import com.pro.delicacy.models.Category;
import com.pro.delicacy.models.Meal;
import com.pro.delicacy.models.NameResponse;
import com.pro.delicacy.network.DelicacyAPi;
import com.pro.delicacy.network.DelicacyClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Meals extends AppCompatActivity {

    private static final String TAG = Meals.class.getSimpleName();

    @BindView(R.id.recyclerView) RecyclerView mRecylerView;
    @BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    private MealAdapter mealAdapter;

    public List<Meal> meals;

//    private SharedPreferences mySharedPreferences;
//    private String myLastMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String meal = intent.getStringExtra("meal");

//        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        myLastMeal = mySharedPreferences.getString(Credentials.PREFERENCE_MEAL_NAME, null);
//        Log.d("Shared Pref Location", myLastMeal);

        DelicacyAPi client = DelicacyClient.getClient();

        Call<NameResponse> call = client.mealNames(meal);

        call.enqueue(new Callback<NameResponse>() {
            @Override
            public void onResponse(Call<NameResponse> call, Response<NameResponse> response) {
                if (response.isSuccessful()){
                    List<Meal> meals = response.body().getMeals();
                    mealAdapter = new MealAdapter(mRecylerView.getContext(), meals);
                    mRecylerView.setAdapter(mealAdapter);
                    mRecylerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mRecylerView.setHasFixedSize(true);
                    showMeals();
                    hideProgressBar();
                } else {
                    hideProgressBar();
                    showUnsuccessfulMessage();
                }
            }

            @Override
            public void onFailure(Call<NameResponse> call, Throwable t) {
                hideProgressBar();
                showFailureMessage();
            }
        });

        // I only implemented one api endpoint but will implement this soon i thought the ip only required
        // implementation of one api end point. if this one is needed as apart of the Ip let me know.
        // i only used this activity for testing not for api consumption

    }

    private void showFailureMessage() {
        mErrorTextView.setText("Something went wrong. Please check your Internet connection and try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showUnsuccessfulMessage() {
        mErrorTextView.setText("Something went wrong. Please try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showMeals() {
        mRecylerView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}