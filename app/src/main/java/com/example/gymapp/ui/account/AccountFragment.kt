package com.example.gymapp.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gymapp.databinding.FragmentAccountBinding

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
            binding.userPassword.setText(user.userPassword) //TODO aqui pone la contraseña larga hasheada
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}