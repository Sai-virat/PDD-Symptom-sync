package com.example.symptomsync.ui.screens.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.symptomsync.ui.viewmodels.AnalyticsViewModel
import com.example.symptomsync.ui.viewmodels.ChatMessage

@Composable
fun AnalyticsHubScreen(viewModel: AnalyticsViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Analytics Hub", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TabRow(selectedTabIndex = viewModel.selectedTab) {
            Tab(selected = viewModel.selectedTab == 0, onClick = { viewModel.selectedTab = 0 }) {
                Text("Weekly", modifier = Modifier.padding(16.dp))
            }
            Tab(selected = viewModel.selectedTab == 1, onClick = { viewModel.selectedTab = 1 }) {
                Text("Monthly", modifier = Modifier.padding(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        // Graph Placeholders
        Card(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text("Adherence Trend Graph", style = MaterialTheme.typography.titleMedium)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            InfoCard("Avg Symptoms", "2.5/day")
            InfoCard("Goal Progress", "85%")
        }
    }
}

@Composable
fun InfoCard(title: String, value: String) {
    Card(modifier = Modifier.width(160.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.bodySmall)
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AICoachChatScreen(viewModel: AnalyticsViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.chatMessages) { message ->
                    ChatBubble(message)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = viewModel.currentChatInput,
                onValueChange = { viewModel.currentChatInput = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask your health coach...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedPlaceholderColor = Color.DarkGray,
                    unfocusedPlaceholderColor = Color.DarkGray,
                    cursorColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { viewModel.sendMessage() }) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val alignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    val color = if (message.isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
    
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
        Surface(
            color = color,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(message.text, modifier = Modifier.padding(12.dp))
        }
    }
}

@Composable
fun SymptomCorrelationScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Symptom Correlation", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Foods that might be causing flare-ups:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        listOf("Dairy" to "Bloating (High)", "Spicy Food" to "Heartburn (Medium)", "Gluten" to "Fatigue (Low)").forEach { (food, symptom) ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(food, fontWeight = FontWeight.Bold)
                    Text(symptom, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun CustomRecipeCreatorScreen(viewModel: AnalyticsViewModel, onSave: () -> Unit) {
    var ingredientInput by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Create Custom Recipe", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = viewModel.recipeName,
            onValueChange = { viewModel.recipeName = it },
            label = { Text("Recipe Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.DarkGray,
                cursorColor = Color.Black
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ingredients", style = MaterialTheme.typography.titleMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = ingredientInput,
                onValueChange = { ingredientInput = it },
                label = { Text("Add ingredient") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.DarkGray,
                    cursorColor = Color.Black
                )
            )
            IconButton(onClick = { 
                viewModel.addIngredient(ingredientInput)
                ingredientInput = ""
            }) {
                Icon(Icons.Default.Send, null)
            }
        }
        
        LazyColumn(modifier = Modifier.weight(1f).padding(vertical = 16.dp)) {
            items(viewModel.customIngredients) { ingredient ->
                Text("• $ingredient", modifier = Modifier.padding(vertical = 4.dp))
            }
        }
        
        Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) { Text("Save Recipe") }
    }
}
