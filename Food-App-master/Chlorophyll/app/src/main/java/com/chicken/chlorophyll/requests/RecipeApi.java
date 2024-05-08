package com.chicken.chlorophyll.requests;

import com.chicken.chlorophyll.requests.responses.RecipeResponse;
import com.chicken.chlorophyll.requests.responses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {


    @GET("api/search")
    Call<RecipeSearchResponse> searchResponse(
            @Query("key") String key,
            @Query("q") String q,
            @Query("page") String page
            );


   @GET("api/get")
    Call<RecipeResponse> getResponse(
       @Query("key") String key,
       @Query("rId") String rId
       );
}
