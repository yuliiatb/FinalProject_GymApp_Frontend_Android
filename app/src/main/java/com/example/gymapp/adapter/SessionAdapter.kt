package com.example.gymapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.R
import com.example.gymapp.data.model.SessionDetails
import java.time.format.DateTimeFormatter

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

        // Establecer color según el nombre de la actividad
        val context = holder.itemView.context
        val cardView = holder.itemView.findViewById<CardView>(R.id.cardActivity)

        val cardColor = when (session.activityName) {
            "Running" -> ContextCompat.getColor(context, R.color.activity_running)
            "Yoga" -> ContextCompat.getColor(context, R.color.activity_yoga)
            "Danza" -> ContextCompat.getColor(context, R.color.activity_dance)
            "Natación" -> ContextCompat.getColor(context, R.color.activity_swimming)
            "Cycling" -> ContextCompat.getColor(context, R.color.activity_cycling)
            "Boxeo" -> ContextCompat.getColor(context, R.color.activity_boxing)
            "Pilates" -> ContextCompat.getColor(context, R.color.activity_pilates)
            "Funcional" -> ContextCompat.getColor(context, R.color.activity_functional)
            else -> ContextCompat.getColor(context, R.color.white)
        }
        cardView.setCardBackgroundColor(cardColor)

    }

    fun updateSessions(newSessions: List<SessionDetails>) {
        sessions = newSessions
        notifyDataSetChanged()
    }
}