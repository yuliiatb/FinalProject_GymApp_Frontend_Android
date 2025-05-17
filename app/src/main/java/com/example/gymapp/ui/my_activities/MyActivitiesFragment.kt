package com.example.gymapp.ui.my_activities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.R
import com.example.gymapp.adapter.SessionAdapter
import com.example.gymapp.adapter.UserSessionsAdapter
import com.example.gymapp.data.model.UserSessionRegistration
import com.example.gymapp.data.repository.SessionInstanceRepository
import com.example.gymapp.data.repository.SessionRepository
import com.example.gymapp.data.repository.UserSessionRegistrationRepository
import com.example.gymapp.databinding.FragmentMyActivitiesBinding
import com.example.gymapp.ui.calendar.CalendarViewModel
import com.example.gymapp.ui.calendar.CalendarViewModelFactory
import com.example.gymapp.ui.dialog_past_activities.PastActivitiesDialogFragment

class MyActivitiesFragment : Fragment() {

    private var _binding: FragmentMyActivitiesBinding? = null

    private lateinit var viewModel: MyActivitiesViewModel
    private lateinit var adapter: UserSessionsAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_my_activities, container, false)

        val userSessionRepository = UserSessionRegistrationRepository()

        val factory = MyActivitiesViewModelFactory(userSessionRepository)
        viewModel = ViewModelProvider(this, factory)[MyActivitiesViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = UserSessionsAdapter(emptyList(), requireContext(), true, true, false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.sessions.observe(viewLifecycleOwner) { registeredSessions ->
            adapter.updateSessions(registeredSessions)

            val textNoSessions = view.findViewById<TextView>(R.id.textNoActivities)

            if (registeredSessions.isEmpty()) {
                textNoSessions.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                textNoSessions.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        viewModel.loadUserSessions(1)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnRefresh = view.findViewById<Button>(R.id.btnRefresh)

        btnRefresh.setOnClickListener {
            viewModel.loadUserSessions(1)
        }

        val btnMyPastActivities = view.findViewById<Button>(R.id.btnMyPastActivities)
        btnMyPastActivities.setOnClickListener {
            val dialog = PastActivitiesDialogFragment()
            dialog.show(parentFragmentManager, "PastActivitiesDialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}