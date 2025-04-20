package com.example.gymapp.ui.account

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.models.User
import com.example.gymapp.data.repositories.UserRepository
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            try {
                val userData = repository.getUserProfile(userId)
                _user.value = userData
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Failed to load user", e)
            }
        }
    }
}