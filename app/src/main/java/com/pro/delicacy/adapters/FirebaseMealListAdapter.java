package com.pro.delicacy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.pro.delicacy.R;
import com.pro.delicacy.models.Meal;
import com.pro.delicacy.util.ItemTouchHelperAdapter;
import com.pro.delicacy.util.OnStartDragListener;

import java.util.ArrayList;

public class FirebaseMealListAdapter extends FirebaseRecyclerAdapter<Meal, FirebaseMealViewHolder> implements ItemTouchHelperAdapter {

    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListiner;
    private Context mContext;

    private ChildEventListener mChildEventListener;
    private ArrayList<Meal> mMeals = new ArrayList<>();


    public FirebaseMealListAdapter(@NonNull FirebaseRecyclerOptions<Meal> options, DatabaseReference mRef, OnStartDragListener mOnStartDragListiner, Context mContext) {
        super(options);
        this.mRef = mRef;
        this.mOnStartDragListiner = mOnStartDragListiner;
        this.mContext = mContext;


        mChildEventListener = mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onBindViewHolder(@NonNull FirebaseMealViewHolder holder, int position, @NonNull Meal model) {
        holder.bindMeal(model);
        holder.mMealImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN){
                    mOnStartDragListiner.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @NonNull
    @Override
    public FirebaseMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meals_categories_item_drag, parent, false);
        return new FirebaseMealViewHolder(view);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        getRef(position).removeValue();
    }
}
