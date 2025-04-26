package com.example.gymapp.data.model


data class Session(
    val idSession: Int? = null,
    val sessionDay: String,
    val sessionTime: String,
    val sessionStatus: String,
    val maxParticipantsNumber: Int,
    val idActivity: Int,
    val idRoom: Int,
    val idInstructor: Int
)