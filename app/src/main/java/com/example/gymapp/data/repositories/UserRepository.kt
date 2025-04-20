package com.example.gymapp.data.repositories
import com.example.gymapp.api.RetrofitClient
import com.example.gymapp.data.models.User

class UserRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getUserProfile(userId: Int): User {
        return apiService.getUserProfile(userId)
    }
/*
    suspend fun updateUserProfile(userId: Int, user: User) : User {
        return apiService.updateUserProfile(userId, user)
    }

 */
}