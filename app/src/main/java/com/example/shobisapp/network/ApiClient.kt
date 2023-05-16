package com.example.shobisapp.network

import com.example.shobisapp.model.ResponsesLogin
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiClient {

    @FormUrlEncoded
    @POST("shobisapp/login_service.php")
    fun login(

        @Field("post_email") email : String,
        @Field("post_password") password : String
    ): Call<ResponsesLogin>
}