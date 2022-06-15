package com.pro.delicacy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.pro.delicacy.R;
import com.pro.delicacy.models.Meal;
import com.pro.delicacy.util.ItemTouchHelperAdapter;
import com.pro.delicacy.util.OnStartDragListener;

public class FirebaseMealListAdapter extends FirebaseRecyclerAdapter<Meal, FirebaseMealViewHolder> implements ItemTouchHelperAdapter {

    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListiner;
    private Context mContext;


    public FirebaseMealListAdapter(@NonNull FirebaseRecyclerOptions<Meal> options, DatabaseReference mRef, OnStartDragListener mOnStartDragListiner, Context mContext) {
        super(options);
        this.mRef = mRef;
        this.mOnStartDragListiner = mOnStartDragListiner;
        this.mContext = mContext;
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
