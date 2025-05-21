package com.example.gymapp.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.R
import com.example.gymapp.adapter.SessionAdapter
import com.example.gymapp.data.repository.SessionInstanceRepository
import com.example.gymapp.data.repository.SessionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchByActivityNameActivity: AppCompatActivity() {
    private lateinit var spinner: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var sessionAdapter: SessionAdapter
    private var sessionInstanceRepository: SessionInstanceRepository = SessionInstanceRepository()
    private var sessionRepository: SessionRepository = SessionRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_activity_name)

        supportActionBar?.title = "Buscar por actividad"
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Habilitar el botón para volver atrás

        // Mapear las cadenas del spinner para que correspondan con IDs de las actividades
        val activitiesMap = mapOf(
            "Running" to 1,
            "Cycling" to 2,
            "Danza" to 3,
            "Pilates" to 4,
            "Yoga" to 5,
            "Natación" to 6,
            "Boxeo" to 7,
            "Funcional" to 8
        )

        // Habilitar el spinner
        spinner = findViewById(R.id.spinnerActivities)
        val activityNames = listOf("Elige una actividad") + activitiesMap.keys
        val spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.activity_names, android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = parent?.getItemAtPosition(position).toString()

                if (selected == "Elige una actividad") return // ignorar esta opción

                val activityId = activitiesMap[selected]
                if (activityId != null) {
                    getSessionsByIdActivity(activityId)
                } else {
                    // En el caso el usuario elige una actividad que está en la lista, pero no está en la BD
                    Log.w("Spinner", "Actividad desconocida: $selected")
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // el programa no hace nada
            }
        }

        recyclerView = findViewById(R.id.recyclerSearchSessions)
        recyclerView.layoutManager = LinearLayoutManager(this)
        sessionAdapter = SessionAdapter(emptyList(), this, sessionInstanceRepository)
        recyclerView.adapter = sessionAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Habilitar el botón para volver atrás
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getSessionsByIdActivity(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sessions = sessionRepository.getSessionsByIdActivity(id)
                withContext(Dispatchers.Main) {
                    sessionAdapter.updateSessions(sessions)
                }
            } catch (e: Exception) {
                Log.e("SearchActivity", "Error loading sessions", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SearchByActivityNameActivity, "Error cargando sesiones", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}