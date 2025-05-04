package com.example.gymapp.data.repository

import com.example.gymapp.api.RetrofitClient
import com.example.gymapp.data.model.SessionInstance
import java.time.LocalDate

class SessionInstanceRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getSessionDate(date: LocalDate) : SessionInstance {
        return apiService.getSessionDate(date)
    }
}