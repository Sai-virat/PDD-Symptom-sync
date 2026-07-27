package com.example.symptomsync.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var fullName by mutableStateOf("")
    var isTermsAccepted by mutableStateOf(false)
    var showForgotPasswordDialog by mutableStateOf(false)

    var emailError by mutableStateOf<String?>(null)
    var loginError by mutableStateOf<String?>(null)

    // Registration Errors
    var fullNameError by mutableStateOf<String?>(null)
    var regEmailError by mutableStateOf<String?>(null)
    var regPasswordError by mutableStateOf<String?>(null)
    var termsError by mutableStateOf<String?>(null)

    private val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    fun onLoginClick(onSuccess: () -> Unit) {
        emailError = null
        loginError = null

        if (email.isBlank()) {
            emailError = "Email cannot be empty"
            return
        }

        if (!emailRegex.matches(email)) {
            emailError = "Invalid email format. Please include '@'"
            return
        }

        // Hardcoded credential check
        if (email == "reddyomsai350@gmail.com" && password == "1130") {
            onSuccess()
        } else {
            loginError = "Invalid email or password"
        }
    }

    fun onRegisterClick(onSuccess: () -> Unit) {
        fullNameError = null
        regEmailError = null
        regPasswordError = null
        termsError = null

        var isValid = true

        if (fullName.isBlank()) {
            fullNameError = "Name cannot be empty"
            isValid = false
        }

        if (email.isBlank()) {
            regEmailError = "Email cannot be empty"
            isValid = false
        } else if (!emailRegex.matches(email)) {
            regEmailError = "Invalid email format. Please include '@'"
            isValid = false
        }

        if (password.isBlank()) {
            regPasswordError = "Password cannot be empty"
            isValid = false
        } else if (password.length < 6) {
            regPasswordError = "Password must be at least 6 characters"
            isValid = false
        }

        if (!isTermsAccepted) {
            termsError = "Please accept terms and conditions"
            isValid = false
        }

        if (isValid) {
            onSuccess()
        }
    }
}
