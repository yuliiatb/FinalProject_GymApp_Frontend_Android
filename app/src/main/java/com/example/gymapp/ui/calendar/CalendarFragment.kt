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
import com.example.gymapp.data.repository.SessionInstanceRepository
import com.example.gymapp.data.repository.SessionRepository
import com.example.gymapp.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
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

        val sessionRepository = SessionRepository()
        val sessionInstanceRepository = SessionInstanceRepository()

        val factory = CalendarViewModelFactory(sessionRepository)
        viewModel = ViewModelProvider(this, factory)[CalendarViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = SessionAdapter(emptyList(), requireContext(), sessionInstanceRepository)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        observeViewModel()

        fun getDayName(): String {
            return when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
                Calendar.MONDAY -> "Lunes"
                Calendar.TUESDAY -> "Martes"
                Calendar.WEDNESDAY -> "Miércoles"
                Calendar.THURSDAY -> "Jueves"
                Calendar.FRIDAY -> "Viernes"
                Calendar.SATURDAY -> "Sábado"
                Calendar.SUNDAY -> "Domingo"
                else -> ""
            }
        }

        // Función para mostrar las sesiones correctamente para una semana
        fun getWeekRange(calendar: Calendar): Pair<String, String> {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val startWeek = calendar.clone() as Calendar
            startWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

            val endWeek = startWeek.clone() as Calendar
            endWeek.add(Calendar.DAY_OF_MONTH, 4)

            val startDate = dateFormat.format(startWeek.time)
            val endDate = dateFormat.format(endWeek.time)

            return Pair(startDate, endDate)
        }

        // Mostrar las sesiones según el día de la semana
        val calendar = Calendar.getInstance()
        var currentDay = getDayName()
        val (startDate, endDate) = getWeekRange(calendar)

        viewModel.loadSessions(currentDay, startDate, endDate) // carga las sesiones que corresponden con el día solicitado y el rango de fechas de la semana

        val btnMonday = view.findViewById<Button>(R.id.btnMonday)
        val btnTuesday = view.findViewById<Button>(R.id.btnTuesday)
        val btnWednesday = view.findViewById<Button>(R.id.btnWednesday)
        val btnThursday = view.findViewById<Button>(R.id.btnThursday)
        val btnFriday = view.findViewById<Button>(R.id.btnFriday)
        val btnPreviousWeek = view.findViewById<Button>(R.id.btnPreviousWeek)
        val btnNextWeek = view.findViewById<Button>(R.id.btnNextWeek)
        val textViewWeekToShow = view.findViewById<TextView>(R.id.weekToShow)

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
            textViewWeekToShow.text = "Semana " + weekRange
        }

        // Mostrar las fechas de la semana actual
        updateWeekTextView(view)

        btnMonday.setOnClickListener {
            currentDay = "Lunes"
            val (startDate, endDate) = getWeekRange(calendar)
            viewModel.loadSessions(currentDay, startDate, endDate)
            changeButtonColor(btnMonday)
        }

        btnTuesday.setOnClickListener {
            currentDay = "Martes"
            val (startDate, endDate) = getWeekRange(calendar)
            viewModel.loadSessions(currentDay, startDate, endDate)
            changeButtonColor(btnTuesday)
        }

        btnWednesday.setOnClickListener {
            currentDay = "Miércoles"
            val (startDate, endDate) = getWeekRange(calendar)
            viewModel.loadSessions(currentDay, startDate, endDate)
            changeButtonColor(btnWednesday)
        }

        btnThursday.setOnClickListener {
            currentDay = "Jueves"
            val (startDate, endDate) = getWeekRange(calendar)
            viewModel.loadSessions(currentDay, startDate, endDate)
            changeButtonColor(btnThursday)
        }

        btnFriday.setOnClickListener {
            currentDay = "Viernes"
            val (startDate, endDate) = getWeekRange(calendar)
            viewModel.loadSessions(currentDay, startDate, endDate)
            changeButtonColor(btnFriday)
        }

        btnPreviousWeek.setOnClickListener {
            calendar.add(Calendar.WEEK_OF_YEAR, -1)
            updateWeekTextView(view)
            val (startDate, endDate) = getWeekRange(calendar)
            viewModel.loadSessions(currentDay, startDate, endDate)
        }

        btnNextWeek.setOnClickListener {
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
            updateWeekTextView(view)
            val (startDate, endDate) = getWeekRange(calendar)
            viewModel.loadSessions(currentDay, startDate, endDate)
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