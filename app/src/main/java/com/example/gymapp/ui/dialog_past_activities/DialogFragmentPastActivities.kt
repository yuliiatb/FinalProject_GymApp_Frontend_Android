package com.example.gymapp.ui.dialog_past_activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.R
import com.example.gymapp.adapter.UserSessionsAdapter
import com.example.gymapp.data.repository.UserSessionRegistrationRepository
import com.example.gymapp.ui.my_activities.MyActivitiesViewModel
import com.example.gymapp.ui.my_activities.MyActivitiesViewModelFactory

class PastActivitiesDialogFragment : DialogFragment() {

    private lateinit var viewModel: MyActivitiesViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_my_activities, container, false)

        val userSessionRepository = UserSessionRegistrationRepository()

        val factory = MyActivitiesViewModelFactory(userSessionRepository)
        viewModel = ViewModelProvider(this, factory)[MyActivitiesViewModel::class.java]

        return inflater.inflate(R.layout.dialog_fragment_past_activities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnClose = view.findViewById<Button>(R.id.btnCloseDialog)
        btnClose.setOnClickListener {
            dismiss()
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPastSessions)
        val adapter = UserSessionsAdapter(emptyList(), requireContext(), false, false, true)
        recyclerView.adapter = adapter


        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.sessions.observe(viewLifecycleOwner) { registeredSessions ->
            adapter.updateSessions(registeredSessions)
        }

        viewModel.loadUserPastSessions(1)
    }
}