package com.example.gymapp.data.model;

import java.time.LocalDate

data class SessionInstance (
    val idSessionInstance: Int? = null,
    val sessionInstanceDate: LocalDate,
    val idSession: Session
)



