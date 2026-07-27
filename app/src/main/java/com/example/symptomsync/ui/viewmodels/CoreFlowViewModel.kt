package com.example.symptomsync.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.symptomsync.data.*
import kotlinx.coroutines.launch

data class SymptomResult(val name: String = "", val severity: String = "")
data class PossibleCause(val title: String = "", val description: String = "", val parentSymptom: String = "")
data class MealPlanItem(val title: String = "", val time: String = "", val targetingSymptom: String = "")
data class ProgressData(val day: String = "", val value: Float = 0f)
data class MealDetailSpec(
    val name: String = "",
    val description: String = "",
    val calories: String = "",
    val protein: String = "",
    val fiber: String = "",
    val time: String = "",
    val addressedSymptom: String = ""
)

class CoreFlowViewModel : ViewModel() {
    init {
        viewModelScope.launch {
            SymptomRepository.fetchSymptoms()
        }
    }

    // Screen 2: Symptom Selection
    val availableSymptoms get() = SymptomRepository.getSymptomNames()
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
        aiReasoning = null
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

        // Use Firestore data instead of AI
        if (detectedSymptomInfos.isNotEmpty()) {
            generateSystemPlan()
        }
    }

    private fun generateSystemPlan() {
        aiReasoning = "We have analyzed your symptoms and generated a personalized health plan from our database."
        
        mealPlan.clear()
        
        // Add meal items for EVERY selected symptom
        detectedSymptomInfos.forEach { info ->
            mealPlan.add(MealPlanItem("Breakfast", "8:00 AM", info.name))
            mealPlan.add(MealPlanItem("Lunch", "1:00 PM", info.name))
            mealPlan.add(MealPlanItem("Snacks", "4:00 PM", info.name))
            mealPlan.add(MealPlanItem("Dinner", "7:00 PM", info.name))
        }
        
        foodsToAvoid.clear()
        val avoidList = detectedSymptomInfos.flatMap { it.foodsToAvoid }.distinct()
        foodsToAvoid.addAll(avoidList)
        
        possibleCauses.clear()
        detectedSymptomInfos.forEach { info ->
            info.possibleCauses.forEach { cause ->
                possibleCauses.add(cause.copy(parentSymptom = info.name))
            }
        }
    }

    fun updateSymptomSeverity(symptomName: String, newSeverity: String) {
        val index = analysisResults.indexOfFirst { it.name == symptomName }
        if (index != -1) {
            analysisResults[index] = analysisResults[index].copy(severity = newSeverity)
        }
    }

    fun getMealDetail(mealType: String, symptomName: String): MealDetailSpec {
        val info = if (symptomName.isNotEmpty()) {
            SymptomRepository.getSymptomInfo(symptomName)
        } else {
            // Fallback to priority logic if no symptom name provided
            val highSeverity = analysisResults.filter { it.severity == "High" }.map { it.name }
            val medSeverity = analysisResults.filter { it.severity == "Medium" }.map { it.name }
            val prioritySymptoms = (highSeverity + medSeverity + analysisResults.map { it.name }).distinct()
            
            var bestMatch: SymptomInfo? = null
            for (name in prioritySymptoms) {
                bestMatch = SymptomRepository.getSymptomInfo(name)
                if (bestMatch != null) break
            }
            bestMatch
        }

        return info?.dietPlan?.get(mealType)?.copy(addressedSymptom = info.name) ?: MealDetailSpec(
            name = "Healthy $mealType",
            description = "A balanced meal with protein and fiber.",
            calories = "400 Calories",
            protein = "20g",
            fiber = "5g",
            time = "Scheduled",
            addressedSymptom = "General Health"
        )
    }
}
