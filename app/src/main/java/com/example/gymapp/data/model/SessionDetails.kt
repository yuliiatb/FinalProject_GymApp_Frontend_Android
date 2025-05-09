package com.example.gymapp.data.model

data class SessionDetails(
    val idSessionInstance: Int,
    val idSession: Int,
    val sessionDay: String,
    val sessionTime: String,
    val sessionStatus: String,
    val maxParticipantsNumber: Int,
    val activityName: String,
    val roomName: String,
    val instructorName: String,
    val sessionDate: String? =  null,
    val availableSpots: Int = 0,

)
