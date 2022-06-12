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

import com.pro.delicacy.CategoriesDetails;
import com.pro.delicacy.MyMealDetails;
import com.pro.delicacy.R;
import com.pro.delicacy.models.Category;
import com.pro.delicacy.models.LatestResponse;
import com.pro.delicacy.models.Meal;
import com.pro.delicacy.ui.MealDetails;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.mealViewHolder>{

    private Context mContext;
    private List<Meal> meals;

    public MealAdapter(Context mContext, List<Meal> meals) {
        this.mContext = mContext;
        this.meals = meals;
    }

    @NonNull
    @Override
    public mealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meals_categories_item, parent, false);
        mealViewHolder mealViewHolder = new mealViewHolder(view);
        return mealViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull mealViewHolder holder, int position) {
        holder.bindMeal(meals.get(position));
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public class mealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.mealImageView) ImageView mMealImageView;
        @BindView(R.id.mealNameTextView) TextView mMealName;
        @BindView(R.id.mealCategoryTextView) TextView mMealCategory;

        private Context mContext;

        public mealViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        public void bindMeal(Meal meal){
            Picasso.get().load(meal.getStrMealThumb()).into(mMealImageView);
            mMealName.setText(meal.getStrMeal());
            mMealCategory.setText(meal.getStrCategory());
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, MyMealDetails.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("meal", Parcels.wrap(meals));
            mContext.startActivity(intent);
        }
    }
}
