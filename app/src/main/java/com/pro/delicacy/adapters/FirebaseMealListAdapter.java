package com.pro.delicacy.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.Query;
import com.pro.delicacy.MyMealDetails;
import com.pro.delicacy.R;
import com.pro.delicacy.models.Meal;
import com.pro.delicacy.util.ItemTouchHelperAdapter;
import com.pro.delicacy.util.OnStartDragListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

public class FirebaseMealListAdapter extends FirebaseRecyclerAdapter<Meal, FirebaseMealViewHolder> implements ItemTouchHelperAdapter {

    private Query mRef;
    private OnStartDragListener mOnStartDragListiner;
    private Context mContext;

    private ChildEventListener mChildEventListener;
    private ArrayList<Meal> mMeals = new ArrayList<>();


    public FirebaseMealListAdapter(@NonNull FirebaseRecyclerOptions<Meal> options, Query mRef, OnStartDragListener mOnStartDragListiner, Context mContext) {
        super(options);
        this.mRef = mRef;
        this.mOnStartDragListiner = mOnStartDragListiner;
        this.mContext = mContext;


        mChildEventListener = mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                mMeals.add(snapshot.getValue(Meal.class));
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyMealDetails.class);
                intent.putExtra("position", holder.getAdapterPosition());
                intent.putExtra("meal", Parcels.wrap(mMeals));
                mContext.startActivity(intent);
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
        Collections.swap(mMeals, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        setIndexInFirebase();
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        mMeals.remove(position);
        getRef(position).removeValue();
    }

    @Override
    public void stopListening() {
        super.stopListening();
        mRef.removeEventListener(mChildEventListener);
    }



    private void setIndexInFirebase(){
        for (Meal meal : mMeals){
            int index = mMeals.indexOf(meal);
            DatabaseReference reference = getRef(index);
            meal.setIndex(Integer.toString(index));
            reference.setValue(meal);
        }
    }


}
