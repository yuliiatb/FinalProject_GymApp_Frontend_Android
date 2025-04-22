package com.example.gymapp.data.models

import java.time.LocalDate
import java.time.LocalTime

data class SessionDetails(
    val id_session: Int,
    val session_date: LocalDate,
    val session_time: LocalTime,
    val session_status: String,
    val activity_name: String,
    val room_name: String,
    val instructor_name: String, // para mostrar el nombre completo, no el ID del instructor
    val available_spots: Int,
    val total_spots: Int
)