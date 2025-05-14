package com.example.gymapp.data.repository

import com.example.gymapp.api.RetrofitClient
import com.example.gymapp.data.model.UserRegisteredSession
import com.example.gymapp.data.model.UserSessionRegistration

class UserSessionRegistrationRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun registerForSession(userSessionRegistration: UserSessionRegistration): UserSessionRegistration {
        return apiService.registerForSession(userSessionRegistration)
    }

    suspend fun getUserSessionsByUser(idUser: Int): List<UserRegisteredSession> {
        return apiService.getFutureUserSessionsByUser(idUser)
    }

    suspend fun getPastUserSessionsByUser(idUser: Int): List<UserRegisteredSession> {
        return apiService.getPastUserSessionsByUser(idUser)
    }

    suspend fun cancelRegistration(idRegistration: Int): Boolean {
        return apiService.cancelRegistration(idRegistration)
    }
}