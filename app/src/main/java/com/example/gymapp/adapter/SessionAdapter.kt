package com.example.gymapp.adapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.R
import com.example.gymapp.data.model.SessionDetails
import com.example.gymapp.data.model.UserSessionRegistration
import com.example.gymapp.data.repository.SessionInstanceRepository
import com.example.gymapp.data.repository.UserSessionRegistrationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale


class SessionAdapter (private var sessions: List<SessionDetails>,
                      private val context: Context,
                      private val sessionInstanceRepository: SessionInstanceRepository) :
    RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    inner class SessionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val activityName: TextView = view.findViewById(R.id.textActivityName)
        val roomName: TextView = view.findViewById(R.id.textRoom)
        val instructor: TextView = view.findViewById(R.id.textInstructor)
        val classTime: TextView = view.findViewById(R.id.textTime)
        var availableSpots: TextView = view.findViewById(R.id.textAvailableSpots)
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
        holder.availableSpots.text = when {
            session.availableSpots <= 0 -> "No hay plazas"
            session.availableSpots == 1 -> "${session.availableSpots} plaza"
            else -> "${session.availableSpots} plazas"
        }

        // Establecer el color del texto dependiendo del número de plazas libres
        when {
            session.availableSpots <= 0 -> {
                holder.availableSpots.setTextColor(ContextCompat.getColor(context, R.color.dark_blue))
                holder.registerButton.isEnabled = false
            }
            session.availableSpots <= 5 -> {
                holder.availableSpots.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
            else -> {
                holder.availableSpots.setTextColor(ContextCompat.getColor(context, R.color.black)) // Or any default color
            }
        }

        val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val adjustedFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedSessionDate = try {
            val date = originalFormat.parse(session.sessionDate)
            adjustedFormat.format(date!!)
        } catch (e:Exception) {
            session.sessionDate
        }
        holder.classDate.setText(formattedSessionDate)

        holder.registerButton.setOnClickListener {
            Log.d("SessionAdapter", "DEBUG: Detalles de la sesión: $session")
            showRegistrationDialogWindow(context, holder.itemView, session.idSessionInstance)
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
            .setMessage("¿Quieres apuntarte a esta clase?")
            .setPositiveButton("Sí", null)
            .setNegativeButton("No", null)
            .create()

        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(context, R.color.white))
            )
            val font = ResourcesCompat.getFont(context, R.font.semplicita_medium)
            val messageView = dialog.findViewById<TextView>(android.R.id.message)
            messageView?.apply {
                typeface = font
                textSize = 20f
            }

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.apply {
                setTypeface(font)
                setTextSize(16f)
                setTextColor(context.getColor(R.color.blue))
            }
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.apply {
                setTypeface(font)
                setTextSize(16f)
                setTextColor(context.getColor(R.color.coral))
            }
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
                        Toast.makeText(context, "¡Reserva confirmada!", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                } catch (e: HttpException) { // bloque catch para captar las excepciones enviadas por backend y mostrar los mensajes correspondientes al usuario
                    val backendErrorMessage = e.response()?.errorBody()?.string()
                        ?: "Error desconocido. No se ha realizado la reserva"
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, backendErrorMessage, Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                } catch (e: Exception) {
                    Log.e("RegistrationError", "Error al realizar la reserva", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Se ha producido un error. No se ha realizado la reserva", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                }
            }
        }

        // Descartar la reserva
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setOnClickListener {
            Toast.makeText(context, "No se ha realizado la reserva", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
    }
}