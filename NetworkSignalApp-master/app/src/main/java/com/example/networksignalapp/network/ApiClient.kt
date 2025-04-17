package com.example.networksignalapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.networksignalapp.network.ApiService

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:5000/"

//    val retrofit: Retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
val apiService: ApiService = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(ApiService::class.java)  // ✅ This avoids the Class<T> mismatch error
}
