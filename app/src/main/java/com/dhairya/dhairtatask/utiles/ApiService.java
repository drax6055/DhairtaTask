package com.dhairya.dhairtatask.utiles;

import com.dhairya.dhairtatask.Model.LoginRequest;
import com.dhairya.dhairtatask.Model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
