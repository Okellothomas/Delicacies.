//package com.pro.delicacy.ui;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.core.view.MenuItemCompat;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.preference.PreferenceManager;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.SearchView;
//
//import com.pro.delicacy.Credentials;
//import com.pro.delicacy.R;
//import com.pro.delicacy.adapters.MealAdapter;
//import com.pro.delicacy.models.Meal;
//import com.pro.delicacy.models.NameResponse;
//import com.pro.delicacy.network.DelicacyAPi;
//import com.pro.delicacy.network.DelicacyClient;
//
//import java.io.IOException;
//import java.security.Provider;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class MealtListFragment extends Fragment {
//
//    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
//
//    private MealAdapter mealAdapter;
//    public ArrayList<Meal> mMeals = new ArrayList<>();
//    private SharedPreferences mSharedPreferences;
//    private SharedPreferences.Editor mEditor;
//    private String mLastMeal;
//
//
//    public MealtListFragment() {
//        // Required empty public constructor
//    }
//
//    public static MealtListFragment newInstance(String param1, String param2) {
//        MealtListFragment fragment = new MealtListFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        mEditor = mSharedPreferences.edit();
//
//        // Instructs fragment to include menu options
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view =  inflater.inflate(R.layout.fragment_mealt_list, container, false);
//        ButterKnife.bind(this,view);
//        mLastMeal = mSharedPreferences.getString(Credentials.PREFERENCE_MEAL_NAME, null);
//        if (mLastMeal != null){
//            getMeals(mLastMeal);
//        }
//        return view;
//    }
//
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_search, menu);
//
//        MenuItem menuItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView
//    }
//
//    private void getMeals(String meal){
//        final MealService mealService = new MealService();
//
//        mealService.findMeals(meal, new Callback<>(){
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) {
//                mMeals = mealService.processResults(response);
//
//                getActivity().runOnUiThread(new Runnable() {
//                    // Line above states 'getActivity()' instead of previous 'RestaurantListActivity.this'
//                    // because fragments do not have own context, and must inherit from corresponding activity.
//                    @Override
//                    public void run() {
//                        mealAdapter = new MealAdapter(getActivity(), mMeals);
//                        // Line above states 'getActivity()' instead of previous own context.
//                        // 'getApplicationContext()' because fragments do not
//                        // must instead inherit it from corresponding activity.
//
//                        mRecyclerView.setAdapter(mealAdapter);
//                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//                        // Line above states 'new LinearLayoutManager(getActivity());' instead of previous.
//                        // new LinearLayoutManager (RestaurantsListActivity.this); when method resided.
//                        // in RestaurantList Activity because Fragments do not have context.
//
//                    }
//                });
//            }
//        }
//    })
//
//    }
//        DelicacyAPi client = DelicacyClient.getClient();
//
//        Call<NameResponse> call = client.mealNames(meal);
//
//        call.enqueue(new Callback<NameResponse>() {
//            @Override
//            public void onResponse(Call<NameResponse> call, Response<NameResponse> response) {
//                if (response.isSuccessful()) {
//                    List<Meal> meals = response.body().getMeals();
//                    if (meals != null) {
//                        mealAdapter = new MealAdapter(mRecylerView.getContext(), meals);
//                        mRecylerView.setAdapter(mealAdapter);
//                        mRecylerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                        mRecylerView.setHasFixedSize(true);
//                        showMeals();
//                        hideProgressBar();
//                    } else {
//                        hideProgressBar();
//                        showUnsuccessfulMessage();
//                    }
//                }
//            }
//
//}