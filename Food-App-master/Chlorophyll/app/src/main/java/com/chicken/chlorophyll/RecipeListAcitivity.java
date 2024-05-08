package com.chicken.chlorophyll;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.chicken.chlorophyll.adapters.OnRecipeListener;
import com.chicken.chlorophyll.adapters.RecipeRecyclerAdapter;
import com.chicken.chlorophyll.models.Recipe;

import com.chicken.chlorophyll.utils.Testing;
import com.chicken.chlorophyll.utils.VerticalSpacingItemDecorator;
import com.chicken.chlorophyll.viewmodels.RecipeListViewModel;

import java.util.List;


public class RecipeListAcitivity extends BaseActivity  implements OnRecipeListener {

    private static final String TAG ="RECIPEACTIVITY";

    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;
    private  SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecyclerView=findViewById(R.id.recipe_list);
        searchView=findViewById(R.id.search_item);


        mRecipeListViewModel=ViewModelProviders.of(this).get(RecipeListViewModel.class);

        initRecyclerView();
        subscribeObservers();
        initSearchView();
        if(!mRecipeListViewModel.getIsViewingRecipes()){
            displayCategories();
        }
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

    }


    private void subscribeObservers(){
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if(recipes!=null) {
                    if(mRecipeListViewModel.getIsViewingRecipes()){
                        Testing.printRecipes(recipes,TAG);
                        mRecipeListViewModel.setIsPerformingQuery(false);
                        mAdapter.setRecipes(recipes);

                   }

                }
            }
        });

        mRecipeListViewModel.IsQueryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean) {
                    Log.d(TAG, "onChanged:QUERY IS EXHAUSTED");
                    mAdapter.setQueryExhausted();
                }
            }
        });

        mRecipeListViewModel.isRecipeRequestTimedOut().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean && !mRecipeListViewModel.getIsViewingRecipes()){
                    Log.d(TAG, "<<< TIMED OUT >>>: ");
                    Toast.makeText(getApplicationContext(),"Error retrieving data. Check network connection .",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    private void initSearchView(){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                mAdapter.displayLoading();
                mRecipeListViewModel.searchRecipeApi(s,1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }


    private  void initRecyclerView() {

        mAdapter = new RecipeRecyclerAdapter(this);
        VerticalSpacingItemDecorator verticalSpacingItemDecorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(verticalSpacingItemDecorator);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(!recyclerView.canScrollVertically(1)){
                    //search the next page
                    mRecipeListViewModel.searchNextPage();
                }
            }
        });

    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent=new Intent(this,RecipeActivity.class);
        intent.putExtra("recipe",mAdapter.getSelectedItem(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        mAdapter.displayLoading();
        mRecipeListViewModel.searchRecipeApi(category,1);

    }


    private void displayCategories(){
        mRecipeListViewModel.setIsViewingRecipes(false);
        mAdapter.displayCategory();
    }

    @Override
    public void onBackPressed() {
        if(mRecipeListViewModel.onBackPressed()){
            super.onBackPressed();
        }
        else {
            displayCategories();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_category){
            displayCategories();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_list_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
