package com.example.symptomsync.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.symptomsync.data.*
import kotlinx.coroutines.launch

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
        // In Web/Common, we would fetch from a Multiplatform Data Source
        // For now, initializing with defaults or a mock for Web preview
        mockFetchUserData()
    }

    fun mockFetchUserData() {
        userName = "Sai"
        allSymptoms.clear()
        allSymptoms.addAll(listOf("Fever", "Migraine"))
        selectedSymptomForDiet = "Fever"
        updateDisplayedDiet("Fever")
        calorieTarget = 2491
    }

    fun updateDisplayedDiet(symptomName: String?) {
        selectedSymptomForDiet = symptomName
        
        // Trigger AI update
        if (symptomName != null) {
            fetchAIDiet(symptomName)
        }
    }

    private fun fetchAIDiet(symptom: String) {
        isProcessingAI = true
        aiReasoning = "AI is connecting to Gemini..."
        viewModelScope.launch {
            try {
                println("DEBUG: Fetching AI plan for $symptom...")
                val response = GeminiRepository.generateHealthPlan(listOf(symptom))
                if (response != null) {
                    println("DEBUG: AI Response received successfully")
                    aiReasoning = response.symptomReasoning
                    
                    dailyMeals = listOf(
                        mapAIMealToMeal("Breakfast", response.breakfast).copy(name = "AI: " + response.breakfast.name),
                        mapAIMealToAIDietMeal("Lunch", response.lunch).copy(name = "AI: " + response.lunch.name),
                        mapAIMealToAIDietMeal("Snacks", response.snacks).copy(name = "AI: " + response.snacks.name),
                        mapAIMealToAIDietMeal("Dinner", response.dinner).copy(name = "AI: " + response.dinner.name)
                    )
                    calorieIntake = (dailyMeals.sumOf { it.calories } * 0.8).toInt()
                } else {
                    aiReasoning = "AI Response was empty. Check API key status in Google AI Studio."
                    println("DEBUG: AI Response was null")
                }
            } catch (e: Exception) {
                aiReasoning = "AI Error: ${e.message}"
                println("DEBUG: AI Error: ${e.message}")
            }
            isProcessingAI = false
        }
    }

    private fun mapAIMealToMeal(id: String, ai: AIMeal) = Meal(
        id = id,
        name = ai.name,
        calories = ai.calories.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 400,
        time = when(id) { "Breakfast" -> "8:00 AM" else -> "Scheduled" },
        image = id.lowercase(),
        benefit = ai.description
    )

    private fun mapAIMealToAIDietMeal(id: String, ai: AIDietMeal) = Meal(
        id = id,
        name = ai.name,
        calories = ai.calories.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 400,
        time = when(id) { "Lunch" -> "1:00 PM" "Snacks" -> "4:00 PM" "Dinner" -> "7:00 PM" else -> "Scheduled" },
        image = id.lowercase(),
        benefit = ai.description
    )

    fun addWater(amount: Int) {
        waterIntake += amount
    }

    fun toggleGrocery(index: Int) {
        // Implementation for grocery list
    }
    
    // Screen 19: Grocery List
    val groceryList = mutableStateListOf(
        "Avocado" to false, "Quinoa" to true, "Salmon" to false, "Asparagus" to false, "Spinach" to true
    )

    // Screen 20: Recipe Mode
    var currentStepIndex by mutableIntStateOf(0)
    val recipeSteps = listOf("Wash vegetables", "Chop avocado", "Toast the bread", "Assemble and serve")
}
