package com.example.gymapp.data.repository

import com.example.gymapp.api.RetrofitClient
import com.example.gymapp.data.model.SessionDetails

class SessionRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getSessionsForDay(day: String): List<SessionDetails> {
        return apiService.getSessionsForDay(day)
    }

}