package com.todoer.data.api

import com.todoer.data.model.LoginResponse
import com.todoer.data.model.Movie
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitService {

//    @GET("movielist.json")
//    suspend fun getAllMovies() : Response<List<Movie>>

    @POST("login")
    @FormUrlEncoded
    suspend fun login(@Field("email") email: String,
                      @Field("password") password: String) : Response<LoginResponse>

    companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://reqres.in/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(AddLoggingInterceptor.setLogging())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }
}