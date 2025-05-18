package com.example.gymapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gymapp.MainActivity
import com.example.gymapp.data.repository.UserRepository
import com.example.gymapp.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var userRepository = UserRepository()
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            email = savedInstanceState?.getString("email") ?: binding.emailEditText.text.toString().trim()
            password = savedInstanceState?.getString("password") ?: binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Introduce tu correo y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Log.d("DEBUG:", email + password)
            lifecycleScope.launch {
                try {
                    val response = userRepository.login(email, password)
                    Log.d("DEBUG:", email + password)
                    if (response.success) {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    else {
                        Toast.makeText(this@LoginActivity, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e: Exception){
                    Toast.makeText(this@LoginActivity, "Error al iniciar la sesión", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("email", binding.emailEditText.text.toString().trim())
        outState.putString("password", binding.passwordEditText.text.toString().trim())
    }
}