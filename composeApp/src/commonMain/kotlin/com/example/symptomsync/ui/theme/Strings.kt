package com.example.symptomsync.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object AppStrings {
    var currentLanguage by mutableStateOf("English")

    val welcome: String
        get() = if (currentLanguage == "English") "Welcome Back" else "స్వాగతం"
    
    val login: String
        get() = if (currentLanguage == "English") "Login" else "లాగిన్"
    
    val addSymptom: String
        get() = if (currentLanguage == "English") "+ Add Symptom" else "+ లక్షణాన్ని జోడించండి"
        
    val healthPlan: String
        get() = if (currentLanguage == "English") "Your Health Plan" else "మీ ఆరోగ్య ప్రణాళిక"

    fun toggleLanguage() {
        currentLanguage = if (currentLanguage == "English") "Telugu" else "English"
    }
}
