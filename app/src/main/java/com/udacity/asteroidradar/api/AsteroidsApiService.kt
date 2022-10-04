package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.domain.Asteroid
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AsteroidsApiService {

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String
    ): String

    @GET("planetary/apod")
    suspend fun getImageOfDay(@Query("api_key") apiKey: String): Response<PictureOfDay>
}

