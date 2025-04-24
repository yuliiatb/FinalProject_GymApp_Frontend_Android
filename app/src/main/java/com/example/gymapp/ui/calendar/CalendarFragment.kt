package com.example.gymapp.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.R
import com.example.gymapp.adapter.SessionAdapter
import com.example.gymapp.data.repository.SessionRepository
import com.example.gymapp.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null

    private lateinit var viewModel: CalendarViewModel
    private lateinit var adapter: SessionAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        val repository = SessionRepository()
        val factory = CalendarViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CalendarViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = SessionAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        observeViewModel()

        // Mostrar las sesiones según el día de la semana
        var currentDay = "Lunes" //día por defecto
        viewModel.loadSessions(currentDay)

        val btnMonday = view.findViewById<Button>(R.id.btnMonday)
        val btnTuesday = view.findViewById<Button>(R.id.btnTuesday)
        val btnWednesday = view.findViewById<Button>(R.id.btnWednesday)
        val btnThursday = view.findViewById<Button>(R.id.btnThursday)
        val btnFriday = view.findViewById<Button>(R.id.btnFriday)
        val btnPreviousWeek = view.findViewById<Button>(R.id.btnPreviousWeek)
        val btnNextWeek = view.findViewById<Button>(R.id.btnNextWeek)

        // Función para cambiar el color del botón pulsado
        fun changeButtonColor(btnPressed: Button) {
            //Resetear el color
            listOf(btnMonday, btnTuesday, btnWednesday, btnThursday, btnFriday).forEach { button ->
                button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
            }

            // Cambiar el color si el botón está pulsado
            btnPressed.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_pink))
        }

        btnMonday.setOnClickListener {
            currentDay = "Lunes"
            viewModel.loadSessions(currentDay)
            changeButtonColor(btnMonday)
        }

        btnTuesday.setOnClickListener {
            currentDay = "Martes"
            viewModel.loadSessions(currentDay)
            changeButtonColor(btnTuesday)
        }

        btnWednesday.setOnClickListener {
            currentDay = "Miércoles"
            viewModel.loadSessions(currentDay)
            changeButtonColor(btnWednesday)
        }

        btnThursday.setOnClickListener {
            currentDay = "Jueves"
            viewModel.loadSessions(currentDay)
            changeButtonColor(btnThursday)
        }

        btnFriday.setOnClickListener {
            currentDay = "Viernes"
            viewModel.loadSessions(currentDay)
            changeButtonColor(btnFriday)
        }

        btnPreviousWeek.setOnClickListener {
            //TODO implementar
        }

        btnNextWeek.setOnClickListener {
            //TODO implementar
        }



        return view
    }

    private fun observeViewModel() {
        viewModel.sessions.observe(viewLifecycleOwner) { sessionList ->
            adapter.updateSessions(sessionList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}