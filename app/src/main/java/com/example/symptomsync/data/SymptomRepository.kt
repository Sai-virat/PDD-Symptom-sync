package com.example.symptomsync.data

import androidx.compose.runtime.mutableStateListOf
import com.example.symptomsync.ui.viewmodels.MealDetailSpec
import com.example.symptomsync.ui.viewmodels.PossibleCause
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class SymptomInfo(
    val name: String = "",
    val possibleCauses: List<PossibleCause> = emptyList(),
    val dietPlan: Map<String, MealDetailSpec> = emptyMap(),
    val foodsToAvoid: List<String> = emptyList()
)

object SymptomRepository {
    private val db = FirebaseFirestore.getInstance()
    private val _symptoms = mutableStateListOf<SymptomInfo>()
    val symptoms: List<SymptomInfo> get() = _symptoms

    suspend fun fetchSymptoms() {
        try {
            val snapshot = db.collection("symptoms").get().await()
            val fetched = snapshot.toObjects(SymptomInfo::class.java)
            _symptoms.clear()
            _symptoms.addAll(fetched)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getSymptomNames(): List<String> = _symptoms.map { it.name }
    
    fun getSymptomInfo(name: String): SymptomInfo? = _symptoms.find { it.name == name }
    
    fun searchSymptoms(query: String): List<String> = 
        _symptoms.map { it.name }.filter { it.contains(query, ignoreCase = true) }
}
