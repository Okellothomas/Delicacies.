package com.pro.delicacy;

public class Credentials {

        public static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
        public static final int API_KEY = BuildConfig.DELICACIES_API_KEY;

        // add the shared preference variable.

        public static final String PREFERENCE_MEAL_NAME = "meal";

        // define a child variable for firebase node.
        public static final String FIREBASE_CHILD_SEARCHED_MEAL = "searchedMeal";
}
