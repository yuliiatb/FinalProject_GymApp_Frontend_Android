package com.example.gymapp.adapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.R
import com.example.gymapp.data.model.SessionDetails
import com.example.gymapp.data.model.UserSessionRegistration
import com.example.gymapp.data.repository.SessionInstanceRepository
import com.example.gymapp.data.repository.UserSessionRegistrationRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SessionAdapter (private var sessions: List<SessionDetails>,
                      private val context: Context,
                      private val sessionInstanceRepository: SessionInstanceRepository) : //HE AÑADIDO AHORA TODO
    RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    inner class SessionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val activityName: TextView = view.findViewById(R.id.textActivityName)
        val roomName: TextView = view.findViewById(R.id.textRoom)
        val instructor: TextView = view.findViewById(R.id.textInstructor)
        val classTime: TextView = view.findViewById(R.id.textTime)
        val freeSpots: TextView = view.findViewById(R.id.textFreeSpots)
        val classDate: TextView = view.findViewById(R.id.textDate)
        val registerButton: Button = view.findViewById(R.id.btnRegister)
    }

    // Muestra la tarjeta con la sesión
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_activity, parent, false)
        return SessionViewHolder(view)
    }

    override fun getItemCount(): Int = sessions.size

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val session = sessions[position]

        holder.activityName.text = session.activityName
        holder.roomName.text = session.roomName
        holder.instructor.text = session.instructorName
        holder.classTime.text = session.sessionTime
        holder.freeSpots.text = session.availableSpots.toString() + " plazas"
        holder.classDate.text = when {
            session.sessionDate == null -> "---"
            session.sessionDate.isEmpty() -> "---"
            else -> session.sessionDate
        }

        holder.registerButton.setOnClickListener {
            Log.d("SessionAdapter", "Full session details: $session")
            showRegistrationDialogWindow(context, holder.itemView, session.idSessionInstance) //HE AÑADIDO SESSION AQUI
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


    fun showRegistrationDialogWindow(context: Context, view: View, idSessionInstance: Int) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("¡Te esperamos para entrenar juntos!")
            .setMessage("¿Quieres apuntarte a esta clase?")
            .setPositiveButton("Sí", null)
            .setNegativeButton("No", null)
            .create()

        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(context, R.color.white))
            )
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                ?.setTextColor(context.getColor(R.color.green))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                ?.setTextColor(context.getColor(R.color.coral))
        }

        dialog.show()

        // Confirmar la reserva
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val registration = UserSessionRegistration(
                        idUser = 1,
                        idSessionInstance = idSessionInstance
                    )

                    val response = UserSessionRegistrationRepository().registerForSession(registration)

                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "¡Reserva confirmada!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK") {
                                // espera que el usuario pulse "OK" para confirmar que se ha apuntado a la clase
                            }.show()
                        dialog.dismiss()
                    }
                } catch (e: Exception) {
                    Log.e("RegistrationError", "Exception during registration", e)
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Se ha producido un error. No se ha realizado la reserva", Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK") {
                                // espera que el usuario pulse "OK" para confirmar que no se ha apuntado a la clase
                            }.show()
                        dialog.dismiss()
                    }
                }
            }
            //dialog.dismiss()
        }

        // Descartar la reserva
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setOnClickListener {
            Snackbar.make(view, "No se ha realizado la reserva", Snackbar.LENGTH_INDEFINITE)
                .setAction("OK") {
                    // espera que el usuario pulse "OK" para confirmar que no se ha apuntado a la clase
                }.show()

            dialog.dismiss()
        }
    }




}