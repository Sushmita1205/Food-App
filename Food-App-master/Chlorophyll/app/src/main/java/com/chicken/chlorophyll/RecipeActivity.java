package com.chicken.chlorophyll;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chicken.chlorophyll.models.Recipe;
import com.chicken.chlorophyll.viewmodels.RecipeViewModel;

public class RecipeActivity extends BaseActivity {

    private static final String TAG ="RECIPEACTIVITY";

    private AppCompatImageView mRecipeImage;
    private TextView mRecipeTitle,mRecipeRank;
    private LinearLayout mRecipeIngredientContainer;
    private ScrollView scrollView;

    private RecipeViewModel mRecipeViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mRecipeViewModel=ViewModelProviders.of(this).get(RecipeViewModel.class);
        mRecipeImage=findViewById(R.id.recipe_image);
        mRecipeTitle=findViewById(R.id.recipe_title);
        mRecipeRank=findViewById(R.id.recipe_social_score);
        mRecipeIngredientContainer=findViewById(R.id.ingredients_container);
        scrollView=findViewById(R.id.parent);

        mProgressBar.setVisibility(View.VISIBLE);
        getIncomingIntent();
        subscribeObserber();
    }

    public void getIncomingIntent(){

        if(getIntent().hasExtra("recipe")){
            Recipe recipe=getIntent().getParcelableExtra("recipe");
            mRecipeViewModel.searchRecipeById(recipe.getRecipe_id());
            Log.d(TAG, "getIncomingIntent: "+recipe.getTitle());

        }
    }

    public void subscribeObserber(){
        mRecipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if(recipe!=null){
                    if(recipe.getRecipe_id().equals(mRecipeViewModel.getRecipeId())) {
                        setWidgitProperties(recipe);
                        mRecipeViewModel.setDidRetrieveRecipe(true);
                    }
                }

            }
        });
        mRecipeViewModel.isRecipeRequestTimedOut().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean && !mRecipeViewModel.getDidRetrieveRecipe()){
                    Log.d(TAG, "<<< TIMED OUT >>>: ");
                    displayErrorScreen("Error retrieving data. Check network connection .");
                }
            }
        });
    }

    private void displayErrorScreen(String errorMessage){
        mRecipeTitle.setText("Error Retrieving Recipe");
        mRecipeRank.setText("");
        TextView textView=new TextView(this);
        if(!errorMessage.equals("")){
            textView.setText(errorMessage);
        }
        else{
            textView.setText("Error");
        }
        textView.setTextSize(16);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        mRecipeIngredientContainer.addView(textView);
        RequestOptions requestOptions=new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(R.drawable.ic_launcher_background)
                .into(mRecipeImage);
        showParent();
        showProgressBar(false);


    }
    private void setWidgitProperties(Recipe recipe){

        RequestOptions requestOptions=new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(recipe.getImage_url())
                .into(mRecipeImage);

        mRecipeTitle.setText(recipe.getTitle());
        mRecipeRank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));
        mProgressBar.setVisibility(View.GONE);

        mRecipeIngredientContainer.removeAllViews();
        for(String ingredient:recipe.getIngredients()){
            TextView textView=new TextView(this);
            textView.setText(ingredient);
            textView.setTextSize(16);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            mRecipeIngredientContainer.addView(textView);
        }
        showParent();
    }


    private void showParent(){
        scrollView.setVisibility(View.VISIBLE);
    }
}
