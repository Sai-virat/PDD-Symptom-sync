package com.example.symptomsync.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.symptomsync.data.*
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Meal(
    val id: String,
    val name: String,
    val calories: Int,
    val time: String,
    val image: String,
    val ingredients: List<String> = listOf("Ingredient 1", "Ingredient 2"),
    val instructions: String = "Step 1: Prep... Step 2: Cook...",
    val benefit: String = "Good for bloating"
)

class DashboardViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var userName by mutableStateOf("User")

    // Screen 13: Dashboard
    var calorieIntake by mutableIntStateOf(0)
    var calorieTarget by mutableIntStateOf(2000)
    var recentSymptom by mutableStateOf("None")
    var allSymptoms = mutableStateListOf<String>()
    var selectedSymptomForDiet by mutableStateOf<String?>(null)

    // AI Integration
    var aiReasoning by mutableStateOf<String?>(null)
    var isProcessingAI by mutableStateOf(false)

    var dailyMeals by mutableStateOf(listOf<Meal>())

    // Screen 15: Meal Detail
    var selectedMeal by mutableStateOf<Meal?>(null)
    var isFavorite by mutableStateOf(false)

    // Screen 17: Symptom Logger
    var symptomNote by mutableStateOf("")
    var loggedSymptom by mutableStateOf("")

    // Screen 18: Water Tracker
    var waterIntake by mutableIntStateOf(1250)
    val waterTarget = 2500

    init {
        fetchUserData()
    }

    fun fetchUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        userName = document.getString("fullName") ?: "User"
                        
                        // Get real symptoms
                        val symptoms = document.get("selectedSymptoms") as? List<String>
                        val severities = document.get("symptomSeverities") as? Map<String, Double>
                        
                        allSymptoms.clear()
                        if (symptoms != null) {
                            allSymptoms.addAll(symptoms)
                        }
                        
                        // Priority symptom: the one with highest severity
                        val prioritySymptom = if (symptoms != null && severities != null) {
                            symptoms.maxByOrNull { (severities[it] ?: 0.0) }
                        } else {
                            symptoms?.firstOrNull()
                        }
                        
                        selectedSymptomForDiet = prioritySymptom
                        recentSymptom = symptoms?.joinToString(", ") ?: "None"
                        
                        // Load real diet plan
                        updateDisplayedDiet(prioritySymptom)

                        // Calculate real calorie target

                        // Calculate real calorie target
                        val weight = document.getDouble("weight") ?: 70.0
                        val height = document.getDouble("height") ?: 170.0
                        val age = document.getLong("age") ?: 25L
                        val gender = document.getString("gender") ?: "Male"
                        val activityLevel = document.getString("activityLevel") ?: "Moderate"

                        val bmr = if (gender == "Male") {
                            10 * weight + 6.25 * height - 5 * age + 5
                        } else {
                            10 * weight + 6.25 * height - 5 * age - 161
                        }

                        val multiplier = when (activityLevel) {
                            "Sedentary" -> 1.2
                            "Light" -> 1.375
                            "Moderate" -> 1.55
                            "Active" -> 1.725
                            "Very Active" -> 1.9
                            else -> 1.55
                        }

                        calorieTarget = (bmr * multiplier).toInt()
                    }
                }
        }
    }

    // Screen 19: Grocery List
    val groceryList = mutableStateListOf(
        "Avocado" to false, "Quinoa" to true, "Salmon" to false, "Asparagus" to false, "Spinach" to true
    )

    // Screen 20: Recipe Mode
    var currentStepIndex by mutableIntStateOf(0)
    val recipeSteps = listOf("Wash vegetables", "Chop avocado", "Toast the bread", "Assemble and serve")

    fun updateDisplayedDiet(symptomName: String?) {
        selectedSymptomForDiet = symptomName
        
        // Trigger AI update
        if (symptomName != null) {
            fetchAIDiet(symptomName)
        }
    }

    private fun fetchAIDiet(symptom: String) {
        isProcessingAI = true
        aiReasoning = "Fetching specialized health plan..."
        viewModelScope.launch {
            try {
                // Fetch from Firestore via SymptomRepository
                if (SymptomRepository.symptoms.isEmpty()) {
                    SymptomRepository.fetchSymptoms()
                }
                
                val info = SymptomRepository.getSymptomInfo(symptom)
                if (info != null) {
                    aiReasoning = "Personalized plan based on your ${info.name} profile."
                    
                    val meals = mutableListOf<Meal>()
                    info.dietPlan.forEach { (type, spec) ->
                        meals.add(Meal(
                            id = type,
                            name = spec.name,
                            calories = spec.calories.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 400,
                            time = spec.time,
                            image = type.lowercase(),
                            benefit = spec.description
                        ))
                    }
                    dailyMeals = meals
                    calorieIntake = (dailyMeals.sumOf { it.calories } * 0.8).toInt()
                } else {
                    aiReasoning = "Plan not found in database for $symptom."
                }
            } catch (e: Exception) {
                aiReasoning = "Connection Error: ${e.message}"
            }
            isProcessingAI = false
        }
    }

    fun addWater(amount: Int) {
        waterIntake += amount
    }

    fun toggleGrocery(index: Int) {
        val item = groceryList[index]
        groceryList[index] = item.first to !item.second
    }
}
