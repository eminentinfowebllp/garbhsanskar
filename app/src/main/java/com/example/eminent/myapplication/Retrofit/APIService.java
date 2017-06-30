package com.example.eminent.myapplication.Retrofit;

import com.example.eminent.myapplication.Model.ApiResponse;
import com.example.eminent.myapplication.Model.Common;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Android on 6/21/2017.
 */

public interface APIService {

    @POST(Common.LOGIN_API)
    @FormUrlEncoded
    Call<ApiResponse> userLogIn(@Field("key") String key);
}
