package com.example.symptomsync.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HealthProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Basic Info
    var userName by mutableStateOf("User")
    var email by mutableStateOf("")
    var age by mutableStateOf(25)
    var gender by mutableStateOf("Male")
    var height by mutableStateOf(170f)
    var weight by mutableStateOf(70f)
    var medicalConditions by mutableStateOf("None")

    // Dietary Preferences
    val selectedDiets = mutableStateListOf<String>()
    val allergies = mutableStateListOf<String>()

    // Fitness Goals
    var selectedGoal by mutableStateOf("Weight Loss")
    var calorieAdjustment by mutableStateOf(0f) // -500 to +500
    var proteinRatio by mutableStateOf(30)
    var carbsRatio by mutableStateOf(40)
    var fatRatio by mutableStateOf(30)

    // Symptoms
    val selectedSymptoms = mutableStateListOf<String>()
    var symptomSearchQuery by mutableStateOf("")
    val symptomSeverities = mutableStateMapOf<String, Float>()

    // Activity & Lifestyle
    var activityLevel by mutableStateOf("Moderate")
    var waterTarget by mutableStateOf(2500)

    // App Preferences
    var isDarkMode by mutableStateOf(true)
    var unitSystem by mutableStateOf("Metric")
    var notificationFrequency by mutableStateOf("Daily")

    // Notifications
    var mealReminderEnabled by mutableStateOf(true)
    var mealReminderTime by mutableStateOf("08:00")
    var waterReminderEnabled by mutableStateOf(true)
    var waterReminderTime by mutableStateOf("10:00")

    // AI Processing Status
    var processingStatus by mutableStateOf("Analyzing your health data...")

    fun saveProfileToFirestore(onComplete: () -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val profile = hashMapOf(
                "age" to age,
                "gender" to gender,
                "height" to height,
                "weight" to weight,
                "activityLevel" to activityLevel,
                "selectedGoal" to selectedGoal,
                "selectedSymptoms" to selectedSymptoms.toList(),
                "symptomSeverities" to symptomSeverities.toMap(),
                "waterTarget" to waterTarget,
                "setupCompleted" to true
            )
            db.collection("users").document(userId)
                .update(profile as Map<String, Any>)
                .addOnCompleteListener { onComplete() }
        } else {
            onComplete()
        }
    }

    init {
        fetchUserData()
    }

    fun fetchUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            email = auth.currentUser?.email ?: ""
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        userName = document.getString("fullName") ?: "User"
                    }
                }
        }
    }

    fun toggleDiet(diet: String) {
        if (selectedDiets.contains(diet)) selectedDiets.remove(diet) else selectedDiets.add(diet)
    }

    fun toggleSymptom(symptom: String) {
        if (selectedSymptoms.contains(symptom)) {
            selectedSymptoms.remove(symptom)
            symptomSeverities.remove(symptom)
        } else {
            selectedSymptoms.add(symptom)
            symptomSeverities[symptom] = 5f
        }
    }

    fun saveGoals() {
        // logic to persist goals
    }
}

private fun <K, V> mutableStateMapOf() = androidx.compose.runtime.mutableStateMapOf<K, V>()
