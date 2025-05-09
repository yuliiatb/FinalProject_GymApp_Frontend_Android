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
import com.example.gymapp.adapter.SessionAdapter.SessionViewHolder
import com.example.gymapp.data.model.SessionDetails
import com.example.gymapp.data.model.UserSessionRegistration
import com.example.gymapp.data.repository.UserSessionRegistrationRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class UserSessionsAdapter(
        private var sessions: List<SessionDetails>,
        private val context: Context) :
    RecyclerView.Adapter<UserSessionsAdapter.UserSessionViewHolder>() {

    inner class UserSessionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val activityName: TextView = view.findViewById(R.id.textMyActivityName)
        val roomName: TextView = view.findViewById(R.id.textMyRoom)
        val instructor: TextView = view.findViewById(R.id.textMyInstructor)
        val classTime: TextView = view.findViewById(R.id.textMyTime)
        val status: TextView = view.findViewById(R.id.textMyStatus)
        val classDate: TextView = view.findViewById(R.id.textMyDate)
        val cancelButton: Button = view.findViewById(R.id.btnMyCancel)
    }

    // Muestra la tarjeta con la sesión
    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): UserSessionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_user_activity, parent, false)
        return UserSessionViewHolder(view)
    }

    override fun getItemCount(): Int = sessions.size

    override fun onBindViewHolder(holder: UserSessionsAdapter.UserSessionViewHolder, position: Int) {
        val session = sessions[position]

        holder.activityName.text = session.activityName
        holder.roomName.text = session.roomName
        holder.instructor.text = session.instructorName
        holder.classTime.text = session.sessionTime
        holder.status.text = session.sessionStatus
        holder.classDate.text = when {
            session.sessionDate == null -> "---"
            session.sessionDate.isEmpty() -> "---"
            else -> session.sessionDate
        }

        holder.cancelButton.setOnClickListener {
            Log.d("UserSessionAdapter", "DEBUG: Detalles de la sesión: $session")
            showCancellationDialogWindow(context, holder.itemView, session.idSessionInstance)
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

    fun showCancellationDialogWindow(context: Context, view: View, idSessionInstance: Int) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Cancelación de la reserva")
            .setMessage("¿Quieres cancelar esta clase?")
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
                        Snackbar.make(view, "Cancelado correctamente. ¡Apúntate a otra clase!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK") {
                                // espera que el usuario pulse "OK" para confirmar que se ha apuntado a la clase
                            }.show()
                        dialog.dismiss()
                    }
                } catch (e: HttpException) { // bloque catch para captar las excepciones enviadas por backend y mostrar los mensajes correspondientes al usuario
                    val backendErrorMessage = e.response()?.errorBody()?.string()
                        ?: "Error desconocido. No se ha cancelado la reserva"
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, backendErrorMessage, Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK") {
                                // espera que el usuario pulse "OK" para confirmar que no se ha apuntado a la clase
                            }.show()
                        dialog.dismiss()
                    }
                } catch (e: Exception) {
                    Log.e("RegistrationError", "Error al cancelar la reserva", e)
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Se ha producido un error. No se ha cancelado la reserva", Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK") {
                                // espera que el usuario pulse "OK" para confirmar que no se ha apuntado a la clase
                            }.show()
                        dialog.dismiss()
                    }
                }
            }
        }

        // Descartar la reserva
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setOnClickListener {
            Snackbar.make(view, "No se ha cancelado la reserva", Snackbar.LENGTH_INDEFINITE)
                .setAction("OK") {
                    // espera que el usuario pulse "OK" para confirmar que no se ha apuntado a la clase
                }.show()

            dialog.dismiss()
        }
    }

}