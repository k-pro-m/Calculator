package com.duty.dutycalculator.api;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {


    @FormUrlEncoded
    @POST("login")
    Call<LoginResponseModel> getLogin(
            @Field("user") String user,
            @Field("password") String password);


    @FormUrlEncoded
    @POST("signup")
    Call<LoginResponseModel> getSignup(
            @Field("userName") String userName,
            @Field("userFullName") String userFullName,
            @Field("emailAddress") String emailAddress,
            @Field("phoneNumber") String phoneNumber,
            @Field("password") String password);

    @GET("getCategory")
    Call<CategoryResponseModel> getCategory(
            @Query("userId") String userId);
}