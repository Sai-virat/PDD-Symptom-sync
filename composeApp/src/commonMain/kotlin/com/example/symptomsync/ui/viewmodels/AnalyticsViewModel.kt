package com.example.symptomsync.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

data class ChatMessage(val text: String, val isUser: Boolean)

class AnalyticsViewModel : ViewModel() {
    // Screen 21: Analytics Hub
    var selectedTab by mutableIntStateOf(0) // 0: Weekly, 1: Monthly
    
    // Screen 25: AI Health Coach
    val chatMessages = mutableStateListOf(
        ChatMessage("Hello! I'm your AI health coach. How can I help you today?", false)
    )
    var currentChatInput by mutableStateOf("")

    // Screen 28: Custom Recipe
    var recipeName by mutableStateOf("")
    val customIngredients = mutableStateListOf<String>()

    fun sendMessage() {
        if (currentChatInput.isNotBlank()) {
            chatMessages.add(ChatMessage(currentChatInput, true))
            currentChatInput = ""
            // Mock AI response
            chatMessages.add(ChatMessage("That sounds interesting! Based on your profile, I recommend focusing on fiber-rich foods.", false))
        }
    }

    fun addIngredient(name: String) {
        if (name.isNotBlank()) customIngredients.add(name)
    }
}
