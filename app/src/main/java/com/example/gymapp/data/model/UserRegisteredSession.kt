package com.example.gymapp.data.model

data class UserRegisteredSession (
    val idRegistration: Int,
    val activityName: String,
    val roomName: String,
    val instructorName: String,
    val sessionTime: String,
    val sessionDate: String,
    val sessionStatus: String,
    val idUser: Int,
    val idSessionInstance: Int
)