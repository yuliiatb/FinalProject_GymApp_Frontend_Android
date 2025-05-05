package com.example.gymapp.ui.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.model.SessionDetails
import com.example.gymapp.data.repository.SessionRepository
import kotlinx.coroutines.launch

class CalendarViewModel (private val sessionRepository: SessionRepository): ViewModel() {
    val sessions = MutableLiveData<List<SessionDetails>>()

    fun loadSessions(day: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            try {
                val result = sessionRepository.getSessionsForDay(day, startDate, endDate)
                sessions.postValue(result)
            }
            catch (e: Exception) {
                Log.e("Sesiones para el día", "Error al mostrar las sesiones para el día", e)
                sessions.postValue(emptyList())
            }
        }
    }
}