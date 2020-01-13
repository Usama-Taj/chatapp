package com.example.mychatapp;

import com.example.mychatapp.utils.TranslateResponse;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface APICalls {
    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/translate")
    Call<TranslateResponse> translate(@Field("key") String key, @Field("text") String text, @Field("lang") String lang);
}
