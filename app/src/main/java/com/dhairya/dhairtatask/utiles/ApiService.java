package com.dhairya.dhairtatask.utiles;

import com.dhairya.dhairtatask.Model.LoginRequest;
import com.dhairya.dhairtatask.Model.LoginResponse;
import com.dhairya.dhairtatask.Model.ProductResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("products")
    Call<ProductResponse> getProducts(@Query("limit") int limit, @Query("skip") int skip);
}
