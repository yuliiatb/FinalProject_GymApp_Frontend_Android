package com.example.gymapp.data.model

import java.time.LocalDate
import java.time.LocalTime

data class Session(
    val idSession: Int? = null,
    val sessionDate: LocalDate,
    val sessionTime: LocalTime,
    val sessionStatus: String,
    val maxParticipantsNumber: Int,
    val idActivity: Int,
    val idRoom: Int,
    val idInstructor: Int
)