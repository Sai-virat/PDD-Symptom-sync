package com.example.symptomsync.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HealthProfileViewModel : ViewModel() {
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
    var calorieAdjustment by mutableStateOf(0f)
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

    // AI Processing Status
    var processingStatus by mutableStateOf("Analyzing your health data...")

    // Reminders
    var mealReminderEnabled by mutableStateOf(true)
    var mealReminderTime by mutableStateOf("08:00")
    var waterReminderEnabled by mutableStateOf(true)
    var waterReminderTime by mutableStateOf("10:00")

    fun saveProfileToFirestore(onComplete: () -> Unit) {
        // Multiplatform saving logic
        onComplete()
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
}

private fun <K, V> mutableStateMapOf() = androidx.compose.runtime.mutableStateMapOf<K, V>()
