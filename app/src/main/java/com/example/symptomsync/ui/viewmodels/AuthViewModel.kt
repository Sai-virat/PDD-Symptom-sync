package com.example.symptomsync.ui.viewmodels

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var fullName by mutableStateOf("")
    var isTermsAccepted by mutableStateOf(false)
    var showForgotPasswordDialog by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    var emailError by mutableStateOf<String?>(null)
    var loginError by mutableStateOf<String?>(null)

    // Registration Errors
    var fullNameError by mutableStateOf<String?>(null)
    var regEmailError by mutableStateOf<String?>(null)
    var regPasswordError by mutableStateOf<String?>(null)
    var termsError by mutableStateOf<String?>(null)

    fun onLoginClick(onSuccess: () -> Unit) {
        emailError = null
        loginError = null

        if (email.isBlank()) {
            emailError = "Email cannot be empty"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format. Please include '@'"
            return
        }

        isLoading = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthViewModel", "Login successful")
                    viewModelScope.launch {
                        delay(500)
                        isLoading = false
                        onSuccess()
                    }
                } else {
                    isLoading = false
                    val error = task.exception?.localizedMessage ?: "Invalid email or password"
                    Log.e("AuthViewModel", "Login failed: $error")
                    loginError = error
                }
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
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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
            isLoading = true
            Log.d("AuthViewModel", "Starting registration for: $email")
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("AuthViewModel", "Registration successful, saving to Firestore")
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            val user = hashMapOf(
                                "fullName" to fullName,
                                "email" to email
                            )
                            db.collection("users").document(userId)
                                .set(user)
                                .addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        Log.d("AuthViewModel", "Firestore save successful")
                                        viewModelScope.launch {
                                            delay(500)
                                            isLoading = false
                                            onSuccess()
                                        }
                                    } else {
                                        isLoading = false
                                        val dbError = dbTask.exception?.localizedMessage ?: "Error saving user data"
                                        Log.e("AuthViewModel", "Firestore save failed: $dbError")
                                        regEmailError = dbError
                                    }
                                }
                        } else {
                            isLoading = false
                            onSuccess()
                        }
                    } else {
                        isLoading = false
                        val regError = task.exception?.localizedMessage ?: "Registration failed"
                        Log.e("AuthViewModel", "Registration failed: $regError")
                        regEmailError = regError
                    }
                }
        }
    }
}
