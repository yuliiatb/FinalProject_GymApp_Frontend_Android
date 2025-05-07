package com.example.gymapp.data.repository

import com.example.gymapp.api.RetrofitClient
import com.example.gymapp.data.model.UserSessionRegistration

class UserSessionRegistrationRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun registerForSession(userSessionRegistration: UserSessionRegistration): UserSessionRegistration {
        return apiService.registerForSession(userSessionRegistration)
    }

    suspend fun cancelRegistration(idRegistration: Int): Boolean {
        return apiService.cancelRegistration(idRegistration)
    }
}