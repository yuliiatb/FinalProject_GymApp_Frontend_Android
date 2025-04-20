package com.example.gymapp.data.models

import java.sql.Date

data class RegistrationDetails(
    val id_registration: Int,
    val session: SessionDetails,
    val registration_date: Date
)