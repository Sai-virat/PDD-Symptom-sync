package com.example.symptomsync.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.symptomsync.data.*
import kotlinx.coroutines.launch

data class SymptomResult(val name: String, val severity: String)
data class PossibleCause(val title: String, val description: String)
data class MealPlanItem(val title: String, val time: String)
data class ProgressData(val day: String, val value: Float)
data class MealDetailSpec(
    val name: String,
    val description: String,
    val calories: String,
    val protein: String,
    val fiber: String,
    val time: String
)

class CoreFlowViewModel : ViewModel() {
    // Screen 2: Symptom Selection
    val availableSymptoms = SymptomRepository.getSymptomNames()
    val selectedSymptoms = mutableStateListOf<String>()
    var searchQuery by mutableStateOf("")

    fun filteredSymptoms() = SymptomRepository.searchSymptoms(searchQuery)

    // Screen 3: Analysis Results
    val analysisResults = mutableStateListOf<SymptomResult>()
    val detectedSymptomInfos = mutableStateListOf<SymptomInfo>()

    // Screen 4: Possible Causes
    val possibleCauses = mutableStateListOf<PossibleCause>()

    // Screen 5: Diet Plan
    val mealPlan = mutableStateListOf<MealPlanItem>()
    val foodsToAvoid = mutableStateListOf<String>()
    
    // Track the most important symptom to build the diet around
    var primarySymptomName by mutableStateOf<String?>(null)

    // AI Integration
    var aiReasoning by mutableStateOf<String?>(null)
    var isProcessingAI by mutableStateOf(false)
    var aiRecipes = mutableStateListOf<AIRecipe>()

    // Core Water Tracker
    var waterGlassCount by mutableIntStateOf(5)
    var waterGoal by mutableIntStateOf(8)
    var waterRemindersEnabled by mutableStateOf(true)
    var waterReminderInterval by mutableStateOf("2 Hours")

    // Core Progress
    val weeklyProgress = listOf(
        ProgressData("Mon", 0.8f),
        ProgressData("Tue", 0.6f),
        ProgressData("Wed", 0.9f),
        ProgressData("Thu", 0.7f),
        ProgressData("Fri", 0.85f),
        ProgressData("Sat", 0.5f),
        ProgressData("Sun", 0.4f)
    )

    fun addWaterGlass() {
        waterGlassCount++
    }

    fun toggleSymptom(symptom: String) {
        if (selectedSymptoms.contains(symptom)) {
            selectedSymptoms.remove(symptom)
        } else {
            selectedSymptoms.add(symptom)
        }
    }

    fun performAnalysis() {
        analysisResults.clear()
        detectedSymptomInfos.clear()
        possibleCauses.clear()
        mealPlan.clear()
        foodsToAvoid.clear()
        aiRecipes.clear()

        selectedSymptoms.forEach { symptomName ->
            val info = SymptomRepository.getSymptomInfo(symptomName)
            if (info != null) {
                detectedSymptomInfos.add(info)
                analysisResults.add(SymptomResult(symptomName, "Medium"))
            }
        }

        // Trigger AI Analysis
        if (selectedSymptoms.isNotEmpty()) {
            fetchAIPlan()
        }
    }

    private fun fetchAIPlan() {
        isProcessingAI = true
        viewModelScope.launch {
            val response = GeminiRepository.generateHealthPlan(selectedSymptoms)
            if (response != null) {
                aiReasoning = response.symptomReasoning
                
                // Map AI meals to local structure
                mealPlan.clear()
                mealPlan.add(MealPlanItem("Breakfast", "8:00 AM"))
                mealPlan.add(MealPlanItem("Lunch", "1:00 PM"))
                mealPlan.add(MealPlanItem("Snacks", "4:00 PM"))
                mealPlan.add(MealPlanItem("Dinner", "7:00 PM"))
                
                foodsToAvoid.clear()
                foodsToAvoid.addAll(response.foodsToAvoid)
                
                aiRecipes.clear()
                aiRecipes.addAll(response.recipes)
                
                // Also add AI reasoning to possible causes
                possibleCauses.clear()
                possibleCauses.add(PossibleCause("AI Diagnosis", response.symptomReasoning))
            }
            isProcessingAI = false
        }
    }

    fun updateSymptomSeverity(symptomName: String, newSeverity: String) {
        val index = analysisResults.indexOfFirst { it.name == symptomName }
        if (index != -1) {
            analysisResults[index] = analysisResults[index].copy(severity = newSeverity)
        }
        updatePrimarySymptomAndMealPlan()
    }

    private fun updatePrimarySymptomAndMealPlan() {
        // Find the symptom with the highest severity
        val highSeverity = analysisResults.filter { it.severity == "High" }.map { it.name }
        val medSeverity = analysisResults.filter { it.severity == "Medium" }.map { it.name }
        val lowSeverity = analysisResults.filter { it.severity == "Low" }.map { it.name }
        
        primarySymptomName = (highSeverity + medSeverity + lowSeverity).firstOrNull()
        
        mealPlan.clear()
        val info = primarySymptomName?.let { SymptomRepository.getSymptomInfo(it) }
        
        if (info != null) {
            info.dietPlan.forEach { (type, spec) ->
                mealPlan.add(MealPlanItem(type, spec.time))
            }
        } else {
            // Default generic plan
            listOf("Breakfast" to "8:00 AM", "Lunch" to "1:00 PM", "Snacks" to "4:00 PM", "Dinner" to "7:00 PM").forEach {
                mealPlan.add(MealPlanItem(it.first, it.second))
            }
        }
    }

    fun getMealDetail(mealType: String): MealDetailSpec {
        val info = primarySymptomName?.let { SymptomRepository.getSymptomInfo(it) }
        
        return info?.dietPlan?.get(mealType) ?: MealDetailSpec(
            name = "Healthy $mealType",
            description = "A balanced meal with protein and fiber.",
            calories = "400 Calories",
            protein = "20g",
            fiber = "5g",
            time = "Scheduled"
        )
    }
}
