package com.chicken.chlorophyll.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.chicken.chlorophyll.models.Recipe;
import com.chicken.chlorophyll.repositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private Boolean mIsViewingRecipes;
    private Boolean mIsPerformingQuery;


    public RecipeListViewModel() {
        mIsViewingRecipes=false;
        mIsPerformingQuery=false;
        mRecipeRepository=RecipeRepository.getInstance();

    }
    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeRepository.getRecipes();
    }
    public LiveData<Boolean> IsQueryExhausted() {
        return mRecipeRepository.IsQueryExhausted();
    }

    public void searchRecipeApi(String query,int pageNumber){
        mIsPerformingQuery=true;
        mIsViewingRecipes=true;
        mRecipeRepository.searchRecipeApi(query,pageNumber);
    }

    public Boolean getIsViewingRecipes() {
        return mIsViewingRecipes;
    }

    public void setIsViewingRecipes(Boolean mIsViewingRecipes) {
        this.mIsViewingRecipes = mIsViewingRecipes;
    }

    public Boolean getIsPerformingQuery() {
        return mIsPerformingQuery;
    }

    public void setIsPerformingQuery(Boolean mIsPerformingQuery) {
        this.mIsPerformingQuery = mIsPerformingQuery;
    }


    public void searchNextPage(){
        if(!mIsPerformingQuery && mIsViewingRecipes
                && !IsQueryExhausted().getValue()){
            mRecipeRepository.searchNextPage();
        }
    }

    public boolean onBackPressed(){
        if(mIsPerformingQuery){
            mRecipeRepository.cancelRequest();
            mIsPerformingQuery=false;
        }
        if(mIsViewingRecipes){
            mIsViewingRecipes=false;
            return false;
        }
        return true;
    }

    public LiveData<Boolean> isRecipeRequestTimedOut(){
        return mRecipeRepository.isRecipeRequestTimedOut();
    }
}
