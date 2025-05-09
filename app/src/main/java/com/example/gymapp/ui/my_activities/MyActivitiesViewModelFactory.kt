package com.example.gymapp.ui.my_activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gymapp.data.repository.UserSessionRegistrationRepository

class MyActivitiesViewModelFactory (
    private val userSessionRepository: UserSessionRegistrationRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyActivitiesViewModel::class.java)) {
            return MyActivitiesViewModel(userSessionRepository) as T
        }
        throw IllegalArgumentException("El programa no reconoce esta clase")
    }
}