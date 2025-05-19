
package com.example.gymapp.ui.account

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.gymapp.R
import com.example.gymapp.data.model.User
import com.example.gymapp.data.repository.UserSessionRegistrationRepository
import com.example.gymapp.databinding.FragmentAccountBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Locale

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        accountViewModel.loadUser(1)

        accountViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.textHello.setText(user.userFirstName)
            binding.userName.setText(user.userFirstName)
            binding.userLastName.setText(user.userLastName)
            binding.userEmail.setText(user.userEmail)
            binding.userPhone.setText(user.userPhone)
            binding.userAddress.setText(user.userAddress)
            binding.userPass.setText(user.userPassword)

            // Formatear la fecha
            val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val adjustedFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val formattedBirthDate = try {
                val date = originalFormat.parse(user.userBirthDate)
                adjustedFormat.format(date!!)
            } catch (e: Exception) {
                user.userBirthDate // si la fecha no se parsea, se muestra el formato original (1990-07-12)
            }
            binding.userBirthDate.setText(formattedBirthDate)
        }

        var isInEditMode = false
        binding.btnEditProfile.setOnClickListener {
            isInEditMode = !isInEditMode// permitir escribir en los campos para modificar los datos
            canEditProfile(isInEditMode)
        }

        binding.btnCancelChanges.setOnClickListener {
            isInEditMode = false
            canEditProfile(false)

            accountViewModel.loadUser(1) // volver a cargar los datos del usuario si se cancelan los cambios
            Toast.makeText(context, "No se han guardado los cambios", Toast.LENGTH_LONG).show()

            binding.btnCancelChanges.visibility = View.GONE
            binding.btnSaveChanges.visibility = View.GONE
            binding.btnEditProfile.visibility = View.VISIBLE
        }

        binding.btnSaveChanges.setOnClickListener {
            if (checkNewData()) {
                val updatedUser = getUpdatedUserData()

                lifecycleScope.launch {
                    try {
                        val result = accountViewModel.updateUserProfile(1, updatedUser)
                        Toast.makeText(context, "¡Se han guardado los cambios!", Toast.LENGTH_LONG).show()

                        isInEditMode = false
                        canEditProfile(false)

                        binding.btnCancelChanges.visibility = View.GONE
                        binding.btnSaveChanges.visibility = View.GONE
                        binding.btnEditProfile.visibility = View.VISIBLE
                    } catch (e: Exception) {
                        Log.e("UpdateProfile", "Error updating user", e)
                        Toast.makeText(context, "Se ha producido un error. Los cambios no se han guardado", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        return root
    }

    private fun getUpdatedUserData(): User {
        // Formatear la fecha para enviarla en el formato correcto para el servidor
        val inputDate = binding.userBirthDate.text.toString()

        val serverDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        val birthDateToSendToServer = try {
            val parsedDate = currentFormat.parse(inputDate)
            serverDateFormat.format(parsedDate!!)
        } catch (e: Exception) {
            inputDate
        }

        return User(
            idUser = 1,
            userFirstName = binding.userName.text.toString(),
            userLastName = binding.userLastName.text.toString(),
            userBirthDate = birthDateToSendToServer,
            userEmail = binding.userEmail.text.toString(),
            userPhone = binding.userPhone.text.toString(),
            userAddress = binding.userAddress.text.toString(),
            userPassword = binding.userPass.text.toString()
        )
    }

    private fun canEditProfile(enabled: Boolean) {
        with(binding) {
            btnEditProfile.isVisible = false
            btnSaveChanges.isVisible = true
            btnCancelChanges.isVisible = true

            userName.isFocusable = enabled
            userName.isFocusableInTouchMode = enabled
            userName.isClickable = enabled
            userName.isCursorVisible = enabled

            userLastName.isFocusable = enabled
            userLastName.isFocusableInTouchMode = enabled
            userLastName.isClickable = enabled
            userLastName.isCursorVisible = enabled

            userBirthDate.isFocusable = enabled
            userBirthDate.isFocusableInTouchMode = enabled
            userBirthDate.isClickable = enabled
            userBirthDate.isCursorVisible = enabled

            userEmail.isFocusable = enabled
            userEmail.isFocusableInTouchMode = enabled
            userEmail.isClickable = enabled
            userEmail.isCursorVisible = enabled

            userPhone.isFocusable = enabled
            userPhone.isFocusableInTouchMode = enabled
            userPhone.isClickable = enabled
            userPhone.isCursorVisible = enabled

            userAddress.isFocusable = enabled
            userAddress.isFocusableInTouchMode = enabled
            userAddress.isClickable = enabled
            userAddress.isCursorVisible = enabled

            userPass.isFocusable = enabled
            userPass.isFocusableInTouchMode = enabled
            userPass.isClickable = enabled
            userPass.isCursorVisible = enabled
        }
    }

    private fun checkNewData() : Boolean {
        return if (binding.userName.text.isNotEmpty() &&
            binding.userLastName.text.isNotEmpty() &&
            binding.userBirthDate.text.isNotEmpty() &&
            binding.userPhone.text.isNotEmpty() &&
            binding.userEmail.text.isNotEmpty()) {
            true
        }
        else {
            Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_LONG).show()
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}