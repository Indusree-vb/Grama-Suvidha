package com.gramasuvidha.portal.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramasuvidha.portal.data.local.entities.UserEntity
import com.gramasuvidha.portal.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableSharedFlow<Pair<Boolean, String?>>()
    val loginResult = _loginResult.asSharedFlow()

    fun login(email: String, password: String, selectedRole: String) {
        viewModelScope.launch {
            val user = repository.getUserByEmail(email)
            if (user != null && user.password == password) {
                // Check if user has the selected role
                if (user.role.equals(selectedRole, ignoreCase = true)) {
                    _loginResult.emit(Pair(true, user.role))
                } else {
                    // Role mismatch
                    _loginResult.emit(Pair(false, "Role mismatch"))
                }
            } else {
                // For dev convenience, also allow the dummy admin login
                if (email == "admin" && password == "admin123" && selectedRole == "Admin") {
                    _loginResult.emit(Pair(true, "Admin"))
                } else if (selectedRole == "User") {
                     // Handle user role logic if needed
                     _loginResult.emit(Pair(false, null))
                } else {
                    _loginResult.emit(Pair(false, null))
                }
            }
        }
    }

    fun register(user: UserEntity) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }
}
