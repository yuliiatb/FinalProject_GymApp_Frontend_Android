package com.example.gymapp.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.R
import com.example.gymapp.adapter.SessionAdapter
import com.example.gymapp.data.repository.SessionRepository
import com.example.gymapp.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

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

        val calendar = Calendar.getInstance()

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

        fun updateWeekTextView(view: View) {
            val dateFormat = SimpleDateFormat("dd.MM.yyy", Locale.getDefault())

            val startWeek = calendar.clone() as Calendar
            startWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

            val endWeek = startWeek.clone() as Calendar
            endWeek.add(Calendar.DAY_OF_MONTH, 4)

            val weekRange = "${dateFormat.format(startWeek.time)} - ${dateFormat.format(endWeek.time)}"

            val weekTextView = view.findViewById<TextView>(R.id.weekToShow)
            weekTextView.text = weekRange


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
            calendar.add(Calendar.WEEK_OF_YEAR, -1)
            updateWeekTextView(view)
            viewModel.loadSessions(currentDay)
        }

        btnNextWeek.setOnClickListener {
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
            updateWeekTextView(view)
            viewModel.loadSessions(currentDay)
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