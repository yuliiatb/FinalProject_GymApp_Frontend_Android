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
import com.example.gymapp.data.model.UserRegisteredSession
import com.example.gymapp.data.model.UserSessionRegistration
import com.example.gymapp.data.repository.UserSessionRegistrationRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Locale

class UserSessionsAdapter(
        private var sessions: List<UserRegisteredSession>,
        private val context: Context,
        private val showCancelButton: Boolean,
        private val showCancelWord: Boolean,
        private val showTextTick: Boolean):
    RecyclerView.Adapter<UserSessionsAdapter.UserSessionViewHolder>() {

    inner class UserSessionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val activityName: TextView = view.findViewById(R.id.textMyActivityName)
        val roomName: TextView = view.findViewById(R.id.textMyRoom)
        val instructor: TextView = view.findViewById(R.id.textMyInstructor)
        val classTime: TextView = view.findViewById(R.id.textMyTime)
        val status: TextView = view.findViewById(R.id.textMyStatus)
        val classDate: TextView = view.findViewById(R.id.textMyDate)
        val cancelButton: Button = view.findViewById(R.id.btnMyCancel)
        val textCancel: TextView = view.findViewById(R.id.textCancel)
        val textTick: TextView = view.findViewById(R.id.textTick)
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
        holder.classTime.text = session.sessionTime.trim(':')
        holder.status.text = session.sessionStatus

        val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val adjustedFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedSessionDate = try {
            val date = originalFormat.parse(session.sessionDate)
            adjustedFormat.format(date!!)
        } catch (e:Exception) {
            session.sessionDate
        }
        holder.classDate.setText(formattedSessionDate)

        // Ajustar la visibiladad del botón y palabra "Cancelar" dependiendo si se muestan sesiones futuras o pasadas
        holder.textCancel.visibility = if (showCancelWord) View.VISIBLE else View.GONE
        holder.textTick.visibility = if(showTextTick) View.VISIBLE else View.GONE
        holder.cancelButton.visibility = if (showCancelButton) View.VISIBLE else View.GONE

        holder.cancelButton.setOnClickListener {
            Log.d("UserSessionAdapter", "DEBUG: Detalles de la sesión: $session")
            showCancellationDialogWindow(context, holder.itemView, session.idRegistration)
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

    fun updateSessions(newSessions: List<UserRegisteredSession>) {
        sessions = newSessions
        notifyDataSetChanged()
    }

    fun showCancellationDialogWindow(context: Context, view: View, idRegistration: Int) {
        val dialog = AlertDialog.Builder(context)
            .setMessage("¿Quieres cancelar esta clase?")
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

        // Confirmar la cancelación de la reserva
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = UserSessionRegistrationRepository().cancelRegistration(idRegistration)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Cancelado correctamente. ¡Apúntate a otra clase!", Toast.LENGTH_LONG).show()
                        Log.d("UserSessionAdapter", "DEBUG: Sesión cancelada: $idRegistration")
                        dialog.dismiss()
                    }
                } catch (e: HttpException) { // bloque catch para captar las excepciones enviadas por backend y mostrar los mensajes correspondientes al usuario
                    val backendErrorMessage = e.response()?.errorBody()?.string()
                        ?: "Error desconocido. No se ha cancelado la reserva"
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, backendErrorMessage, Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                } catch (e: Exception) {
                    Log.e("RegistrationError", "Error al cancelar la reserva", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Se ha producido un error. No se ha cancelado la reserva", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                }
            }
        }

        // Descartar la caqncelación de la reserva
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setOnClickListener {
            Toast.makeText(context, "No se ha cancelado la reserva", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
    }

}