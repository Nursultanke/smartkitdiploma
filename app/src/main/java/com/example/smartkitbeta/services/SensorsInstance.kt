package com.example.smartkitbeta.services

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SensorsInstance {
    companion object{
        val BASE_URL = "http://192.168.1.101:8000/"
        fun getRetrofitInstance() : Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }
    }
}