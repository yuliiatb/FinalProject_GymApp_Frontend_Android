package com.example.gymapp.data.repository

import com.example.gymapp.api.RetrofitClient
import com.example.gymapp.data.model.SessionDetails
import com.example.gymapp.data.model.UserSessionRegistration
import java.time.LocalDate

class UserSessionRegistrationRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun registerForSession(userSessionRegistration: UserSessionRegistration): UserSessionRegistration {
        return apiService.registerForSession(userSessionRegistration)
    }

    suspend fun getUserSessionsByUser(idUser: Int): List<SessionDetails> {
        return apiService.getUserSessionsByUser(idUser)
    }

    suspend fun cancelRegistration(idRegistration: Int): Boolean {
        return apiService.cancelRegistration(idRegistration)
    }
}