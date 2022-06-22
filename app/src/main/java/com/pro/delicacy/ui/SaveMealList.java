package com.pro.delicacy.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pro.delicacy.Credentials;
import com.pro.delicacy.R;
import com.pro.delicacy.adapters.FirebaseMealListAdapter;
import com.pro.delicacy.adapters.FirebaseMealViewHolder;
import com.pro.delicacy.models.Meal;
import com.pro.delicacy.util.OnStartDragListener;
import com.pro.delicacy.util.SimpleItemTouchHelperCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Constants;

public class SaveMealList extends AppCompatActivity implements OnStartDragListener {

    private com.google.firebase.database.Query mMealReference;
    private FirebaseMealListAdapter mFirebaseAdapter;
    private ItemTouchHelper mItemTouchHelper;


    @BindView(R.id.recyclerView) RecyclerView mRecylerView;
    @BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);
        ButterKnife.bind(this);


        setUpFireBaseAdapter();
        hideProgressBar();
        showMeals();
    }

    private void setUpFireBaseAdapter(){

        FirebaseUser user = FirebaseAuth
                .getInstance().getCurrentUser();
        String uid = user.getUid();


        Query query = FirebaseDatabase.getInstance().getReference(Credentials.FIREBASE_CHILD_MEAL)
                .child(uid)
                .orderByChild(Credentials.FIREBASE_QUERY_INDEX);



        FirebaseRecyclerOptions<Meal> options = new FirebaseRecyclerOptions.Builder<Meal>()
                .setQuery(query, Meal.class)
                .build();

        mFirebaseAdapter = new FirebaseMealListAdapter(options, query, this, this);
        mRecylerView.setLayoutManager(new LinearLayoutManager(this));
        mRecylerView.setAdapter(mFirebaseAdapter);
        mRecylerView.setHasFixedSize(true);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecylerView);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.stopListening();
    }

    private void showMeals() {
        mRecylerView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
