package com.chicken.chlorophyll.utils;

import android.util.Log;

import com.chicken.chlorophyll.models.Recipe;

import java.util.List;

public class Testing {

    public static void printRecipes(List<Recipe> recipes,String tag){
        for (Recipe recipe : recipes) {
            Log.d(tag, "onChanged: Recipe Tilte " + recipe.getTitle());
        }
    }
}
