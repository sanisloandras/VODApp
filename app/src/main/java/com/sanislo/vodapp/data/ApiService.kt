package com.sanislo.vodapp.data

import com.sanislo.vodapp.data.entity.VodsResponse
import retrofit2.http.GET

interface ApiService {
    companion object {
        const val BASE_URL = "https://sela-test.herokuapp.com"
    }

    @GET("/assets/hkzxv.json")
    suspend fun assets(): VodsResponse
}