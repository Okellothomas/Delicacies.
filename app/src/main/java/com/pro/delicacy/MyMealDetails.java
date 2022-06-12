package com.pro.delicacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.pro.delicacy.adapters.MealPagerAdapter;
import com.pro.delicacy.adapters.PagerAdapter;
import com.pro.delicacy.models.Category;
import com.pro.delicacy.models.Meal;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyMealDetails extends AppCompatActivity {

    @BindView(R.id.viewPager) ViewPager mViewPager;
    private MealPagerAdapter adapterViewPager;
    List<Meal> meals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meal_details);
        ButterKnife.bind(this);

        meals = Parcels.unwrap(getIntent().getParcelableExtra("meal"));
        int startingPosition = getIntent().getIntExtra("position",0);
        adapterViewPager = new MealPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, meals);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
    }
}