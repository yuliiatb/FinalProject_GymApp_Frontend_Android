package com.example.gymapp.data.models

import java.time.LocalDate
import java.time.LocalTime

data class Session(
    val id_session: Int? = null,
    val session_date: LocalDate,
    val session_time: LocalTime,
    val session_status: String,
    val max_participants_number: Int,
    val id_activity: Int,
    val id_room: Int,
    val id_instructor: Int
)