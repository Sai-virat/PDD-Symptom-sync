package com.example.symptomsync.data

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

@Serializable
data class AIDietResponse(
    val symptomReasoning: String,
    val breakfast: AIMeal,
    val lunch: AIDietMeal,
    val snacks: AIDietMeal,
    val dinner: AIDietMeal,
    val foodsToAvoid: List<String>,
    val recipes: List<AIRecipe>
)

@Serializable
data class AIMeal(val name: String, val calories: String, val protein: String, val description: String)

@Serializable
data class AIDietMeal(val name: String, val calories: String, val protein: String, val description: String)

@Serializable
data class AIRecipe(val name: String, val ingredients: List<String>, val steps: List<String>)

object GeminiRepository {
    private const val API_KEY = "YOUR_API_KEY_HERE"
    
    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = API_KEY
    )

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun generateHealthPlan(symptoms: List<String>): Pair<AIDietResponse?, String?> {
        val prompt = """
            You are a professional medical nutritionist. Based on the following symptoms: ${symptoms.joinToString(", ")}, 
            provide a comprehensive health plan in JSON format. 
            Include:
            1. 'symptomReasoning': A clear explanation of why these symptoms might be occurring.
            2. 'breakfast', 'lunch', 'snacks', 'dinner': Each with 'name', 'calories', 'protein', and 'description'.
            3. 'foodsToAvoid': A list of items to stay away from.
            4. 'recipes': A few simple step-by-step recipes for the recommended meals.
            
            Strictly follow this JSON structure. Do not include any text outside the JSON.
        """.trimIndent()

        return try {
            val response = model.generateContent(prompt)
            val text = response.text
            if (text != null) {
                // Clean up the response text in case it has markdown code blocks
                val cleanText = text.replace("```json", "").replace("```", "").trim()
                Pair(json.decodeFromString<AIDietResponse>(cleanText), null)
            } else {
                Pair(null, "AI returned an empty response.")
            }
        } catch (e: Exception) {
            Pair(null, e.message ?: "Unknown AI error occurred.")
        }
    }
}
