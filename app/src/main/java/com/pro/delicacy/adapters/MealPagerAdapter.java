package com.pro.delicacy.adapters;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pro.delicacy.models.Meal;
import com.pro.delicacy.ui.Categories_details;
import com.pro.delicacy.ui.MealDetails;

import java.util.List;

public class MealPagerAdapter extends FragmentPagerAdapter {

    private List<Meal> mMeals;

    public MealPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Meal> mMeals) {
        super(fm, behavior);
        this.mMeals = mMeals;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return MealDetails.newInstance(mMeals.get(position));
    }

    @Override
    public int getCount() {
        return mMeals.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mMeals.get(position).getStrMeal();
    }
}
