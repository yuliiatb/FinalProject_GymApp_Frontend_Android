package com.example.gymapp.data.repository
import com.example.gymapp.api.RetrofitClient
import com.example.gymapp.data.model.LoginDetails
import com.example.gymapp.data.model.LoginResponse
import com.example.gymapp.data.model.User

class UserRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getUserProfile(userId: Int): User {
        return apiService.getUserProfile(userId)
    }

    suspend fun updateUserProfile(userId: Int, user: User) : User {
        return apiService.updateUserProfile(userId, user)
    }

    suspend fun login(email:String, password: String): LoginResponse {
        return apiService.login(LoginDetails(email, password))
    }
}