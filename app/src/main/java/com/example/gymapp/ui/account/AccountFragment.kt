
package com.example.gymapp.ui.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.gymapp.data.models.User
import com.example.gymapp.databinding.FragmentAccountBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        accountViewModel.loadUser(1)

        accountViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.textHello.setText(user.userFirstName)
            binding.userName.setText(user.userFirstName)
            binding.userLastName.setText(user.userLastName)
            binding.userBirthDate.setText(user.userBirthDate)
            binding.userEmail.setText(user.userEmail)
            binding.userPhone.setText(user.userPhone)
            binding.userAddress.setText(user.userAddress)
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
            Snackbar.make(binding.root, "No se guardaron los cambios", Snackbar.LENGTH_SHORT).show()

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
                        Snackbar.make(binding.root, "Se han guardado los cambios", Snackbar.LENGTH_SHORT).show()

                        isInEditMode = false
                        canEditProfile(false)

                        binding.btnCancelChanges.visibility = View.GONE
                        binding.btnSaveChanges.visibility = View.GONE
                        binding.btnEditProfile.visibility = View.VISIBLE
                    } catch (e: Exception) {
                        Log.e("UpdateProfile", "Error updating user", e)
                        Snackbar.make(binding.root, "Se ha producido un error. Los cambios no se han guardado", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }

        }

        return root
    }

    private fun getUpdatedUserData(): User {
        return User(
            idUser = 1,
            userFirstName = binding.userName.text.toString(),
            userLastName = binding.userLastName.text.toString(),
            userBirthDate = binding.userBirthDate.text.toString(),
            userEmail = binding.userEmail.text.toString(),
            userPhone = binding.userPhone.text.toString(),
            userAddress = binding.userAddress.text.toString(),
            userPassword = "12345user"
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
            Snackbar.make(binding.root, "Rellena todos los campos", Snackbar.LENGTH_SHORT).show()
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}