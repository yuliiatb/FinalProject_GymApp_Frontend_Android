package com.example.gymapp.data.models

import java.sql.Date
import java.sql.Time

data class Session(
    val id_session: Int? = null,
    val session_date: Date,
    val session_time: Time,
    val session_status: String,
    val max_participants_number: Int,
    val id_activity: Int,
    val id_room: Int,
    val id_instructor: Int
)