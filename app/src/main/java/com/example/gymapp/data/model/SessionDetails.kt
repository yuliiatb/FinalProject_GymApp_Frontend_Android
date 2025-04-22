package com.example.gymapp.data.model

import java.time.LocalDate
import java.time.LocalTime

data class SessionDetails(
    val idSession: Int,
    val sessionDate: LocalDate,
    val sessionTime: LocalTime,
    val sessionStatus: String,
    val activityName: String,
    val roomName: String,
    val instructorName: String, // para mostrar el nombre completo, no el ID del instructor
    val availableSpots: Int,
    val totalSpots: Int
)