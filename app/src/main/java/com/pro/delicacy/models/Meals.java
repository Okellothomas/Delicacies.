package com.pro.delicacy.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Meals {

    @SerializedName("idMeal")
    @Expose
    private String idMeal;
    @SerializedName("strMeal")
    @Expose
    private String strMeal;
    @SerializedName("strDescription")
    @Expose
    private String strDescription;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Meals() {
    }

    /**
     * 
     * @param strDescription
     * @param idMeal
     * @param strMeal
     */
    public Meals(String idMeal, String strMeal, String strDescription) {
        super();
        this.idMeal = idMeal;
        this.strMeal = strMeal;
        this.strDescription = strDescription;
    }

    public String getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

}
