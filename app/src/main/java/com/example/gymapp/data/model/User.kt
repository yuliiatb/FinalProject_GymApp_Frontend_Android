package com.example.gymapp.data.model

import java.time.LocalDate

data class User(
    val idUser: Int? = null,
    val userFirstName: String,
    val userLastName: String,
    val userBirthDate: String,
    val userAddress: String,
    val userEmail: String,
    val userPhone: String,
    val userPassword: String
)