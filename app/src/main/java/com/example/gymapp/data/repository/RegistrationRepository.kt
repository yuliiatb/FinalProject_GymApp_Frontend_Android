package com.example.gymapp.data.repository
import com.example.gymapp.api.RetrofitClient
import com.example.gymapp.data.model.SessionDetails
import java.time.LocalDate

class RegistrationRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getUserFutureSessions(userId: Int, currentDate: LocalDate): List<SessionDetails> {
        return apiService.getUserFutureSessions(userId, currentDate)
    }
}