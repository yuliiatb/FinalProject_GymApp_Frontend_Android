package com.example.gymapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.R
import com.example.gymapp.data.model.SessionDetails

class SessionAdapter (private var sessions: List<SessionDetails>) :
    RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    inner class SessionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val activityName: TextView = view.findViewById(R.id.textActivityName)
        val roomName: TextView = view.findViewById(R.id.textRoom)
        val instructor: TextView = view.findViewById(R.id.textInstructor)
        val time: TextView = view.findViewById(R.id.textTime)
        val freeSpots: TextView = view.findViewById(R.id.textFreeSpots)
        val registerButton: Button = view.findViewById(R.id.btnRegister)
    }

    // Muestra la tarjeta con la sesión
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_activity, parent, false)
        return SessionViewHolder(view)
    }

    // Muestra el número de las sesiones
    override fun getItemCount(): Int = sessions.size

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val session = sessions[position]
        holder.activityName.text = session.activityName
        holder.roomName.text = session.roomName
        holder.instructor.text = session.instructorName
        holder.time.text = session.sessionTime.toString()
        holder.freeSpots.text = session.availableSpots.toString()

        holder.registerButton.setOnClickListener {
            // handle registration logic
        }
    }

    fun updateSessions(newSessions: List<SessionDetails>) {
        sessions = newSessions
        notifyDataSetChanged()
    }
}