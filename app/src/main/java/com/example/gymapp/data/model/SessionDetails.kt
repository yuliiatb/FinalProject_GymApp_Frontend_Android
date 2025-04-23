package com.example.gymapp.data.model

import java.time.LocalDate
import java.time.LocalTime

data class SessionDetails(
    val idSession: Int,
    val sessionDay: String,
    val sessionTime: String,
    val sessionStatus: String,
    val maxParticipantsNumber: Int,
    val activityName: String,
    val roomName: String,
    val instructorName: String,
    val availableSpots: Int = 0 // Default value if not provided by the backend
)