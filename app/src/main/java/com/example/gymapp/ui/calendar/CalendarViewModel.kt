package com.example.gymapp.ui.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.model.SessionDetails
import com.example.gymapp.data.repository.SessionRepository
import kotlinx.coroutines.launch

class CalendarViewModel (private val repository: SessionRepository): ViewModel() {
    private val sessions = MutableLiveData<List<SessionDetails>>()
    val sessionsToShow: LiveData<List<SessionDetails>> get() = sessions

    fun loadSessions(day: String) {
        viewModelScope.launch {
            try {
                val result = repository.getSessionsForDay(day)
                sessions.value = result
            }
            catch (e: Exception) {
                Log.e("Session for a day", "Error al mostrar las sesiones para un día")
            }
        }
    }

}