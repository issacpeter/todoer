package com.todoer.data.repository

import com.todoer.data.api.RetrofitService

class MainRepository constructor(private val retrofitService: RetrofitService) {

    suspend fun login(email: String, password: String) = retrofitService.login(email, password)

}