package com.eam.parqueaventuraapp.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // URL actualizada con tu link de MockAPI
    private const val BASE_URL = "https://6a0cf961769682b8ee758225.mockapi.io/api/v1/"

    val apiService: AtraccionApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AtraccionApiService::class.java)
    }
}
