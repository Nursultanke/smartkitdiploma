package com.example.smartkitbeta.services

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SensorService {

    @GET("/android_update")
    suspend fun getSensors() : Response<SensorsItem>

    @GET("/android_set")
    fun updateSensors(@Query("lamp") lamp:Int) : Call<SensorsItem>

    @GET("/android_set")
    fun updateSocket(@Query("socket") socket:Int) : Call<SensorsItem>
}