package com.pro.delicacy.models;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class NameResponse {

    @SerializedName("meals")
    @Expose
    private List<Meal> meals = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public NameResponse() {
    }

    /**
     * 
     * @param meals
     */
    public NameResponse(List<Meal> meals) {
        super();
        this.meals = meals;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

}
