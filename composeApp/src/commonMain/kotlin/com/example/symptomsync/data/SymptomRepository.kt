package com.example.symptomsync.data

import com.example.symptomsync.ui.viewmodels.MealDetailSpec
import com.example.symptomsync.ui.viewmodels.PossibleCause

data class SymptomInfo(
    val name: String,
    val possibleCauses: List<PossibleCause>,
    val dietPlan: Map<String, MealDetailSpec>,
    val foodsToAvoid: List<String>
)

object SymptomRepository {
    private val symptoms by lazy { getSymptomData() }

    fun getSymptomNames(): List<String> = symptoms.map { it.name }
    
    fun getSymptomInfo(name: String): SymptomInfo? = symptoms.find { it.name == name }
    
    fun searchSymptoms(query: String): List<String> = 
        symptoms.map { it.name }.filter { it.contains(query, ignoreCase = true) }
}
