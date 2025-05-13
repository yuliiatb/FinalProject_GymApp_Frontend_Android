package com.example.gymapp.ui.my_activities

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.model.SessionDetails
import com.example.gymapp.data.model.UserRegisteredSession
import com.example.gymapp.data.repository.SessionRepository
import com.example.gymapp.data.repository.UserSessionRegistrationRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class MyActivitiesViewModel(private val userSessionRepository: UserSessionRegistrationRepository): ViewModel() {
    val sessions = MutableLiveData<List<UserRegisteredSession>>()

    fun loadUserSessions(idUser: Int) {
        viewModelScope.launch {
            try {
                val result: List<UserRegisteredSession> = userSessionRepository.getUserSessionsByUser(idUser)

                Log.d("DEBUG", "Sessiones cargadas: $result")
                sessions.postValue(result)
            }
            catch (e: Exception) {
                Log.e("Sesiones del usuario", "Error al mostrar las sesiones a las que el usuario se ha registrado", e)
                sessions.postValue(emptyList())
            }
        }
    }
}