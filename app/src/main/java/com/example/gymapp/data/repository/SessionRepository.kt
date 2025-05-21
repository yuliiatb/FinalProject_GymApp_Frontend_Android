package com.example.gymapp.data.repository

import com.example.gymapp.api.RetrofitClient
import com.example.gymapp.data.model.SessionDetails

class SessionRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getSessionsForDay(day: String, startDate: String, endDate: String): List<SessionDetails> {
        return apiService.getSessionsForDay(day, startDate, endDate)
    }

    suspend fun getSessionsByIdActivity(id: Int): List<SessionDetails> {
        return apiService.getSessionsByIdActivity(id)
    }

}