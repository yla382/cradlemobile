package com.cradletrial.cradlevhtapp;

import com.cradletrial.cradlevhtapp.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserRetrofitApi {

    @GET("/users/login")
    Call<User> login(@Query("email") String email, @Query("password") String password);


    @POST("/users/signup")
    Call<User> signup(@Body User user);
}
