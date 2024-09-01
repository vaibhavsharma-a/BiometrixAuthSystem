package com.sangh.bioauth;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("store_fcm_token") // Endpoint to send FCM token
    Call<Void> sendToken(@Body TokenRequest token); // Method to send FCM token in the request body
}
