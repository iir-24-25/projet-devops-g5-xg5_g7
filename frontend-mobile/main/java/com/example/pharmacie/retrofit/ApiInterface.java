package com.example.pharmacie.retrofit;

import com.example.pharmacie.models.AuthenticationRequest;
import com.example.pharmacie.models.AuthenticationResponse;
import com.example.pharmacie.models.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("api/auth/authenticate")
    Call<AuthenticationResponse> login(@Body AuthenticationRequest request);

    @POST("api/auth/register")
    Call<AuthenticationResponse> register(@Body RegisterRequest request);
}