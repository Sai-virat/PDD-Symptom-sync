package com.example.symptomsync.ui.screens.dashboard

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.symptomsync.ui.theme.RichOrange
import com.example.symptomsync.ui.theme.SmoothPurple
import com.example.symptomsync.ui.viewmodels.DashboardViewModel
import com.example.symptomsync.ui.viewmodels.Meal

@Composable
fun MainDashboard(viewModel: DashboardViewModel, onMealClick: (Meal) -> Unit, onLogSymptom: () -> Unit) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onLogSymptom) { Icon(Icons.Default.Add, contentDescription = "Log Symptom") }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Hi, ${viewModel.userName}!", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))

            // Calorie Ring Placeholder
            Card(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        progress = { viewModel.calorieIntake.toFloat() / viewModel.calorieTarget },
                        modifier = Modifier.size(150.dp),
                        strokeWidth = 12.dp
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${viewModel.calorieIntake}", style = MaterialTheme.typography.headlineMedium)
                        Text("of ${viewModel.calorieTarget} kcal")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            // AI Analysis Status
            if (viewModel.isProcessingAI) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = RichOrange)
                Text("AI is analyzing your symptoms...", style = MaterialTheme.typography.labelSmall, color = RichOrange)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (viewModel.aiReasoning != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SmoothPurple.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SmoothPurple)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("AI Insight", fontWeight = FontWeight.Bold, color = SmoothPurple)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(viewModel.aiReasoning!!, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Surface(color = MaterialTheme.colorScheme.tertiaryContainer, shape = MaterialTheme.shapes.medium) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Active Symptoms:", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (viewModel.allSymptoms.isEmpty()) {
                        Text("No symptoms selected", style = MaterialTheme.typography.bodySmall)
                    } else {
                        androidx.compose.foundation.layout.FlowRow {
                            viewModel.allSymptoms.forEach { symptom ->
                                val isSelected = viewModel.selectedSymptomForDiet == symptom
                                val chipColor = getSymptomColor(symptom)
                                
                                SuggestionChip(
                                    onClick = { viewModel.updateDisplayedDiet(symptom) },
                                    label = { Text(symptom, fontSize = 10.sp, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal) },
                                    modifier = Modifier.padding(4.dp),
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = if (isSelected) chipColor else chipColor.copy(alpha = 0.1f),
                                        labelColor = if (isSelected) Color.White else chipColor
                                    ),
                                    border = SuggestionChipDefaults.suggestionChipBorder(
                                        enabled = true,
                                        borderColor = chipColor,
                                        borderWidth = if (isSelected) 2.dp else 1.dp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Today's Meals", style = MaterialTheme.typography.titleLarge)
            LazyColumn {
                itemsIndexed(viewModel.dailyMeals) { _, meal ->
                    MealCard(meal, onClick = { onMealClick(meal) })
                }
            }
        }
    }
}

fun getSymptomColor(symptom: String): Color {
    return when (symptom) {
        "Fever" -> Color(0xFFFF5722) // Deep Orange
        "Migraine" -> Color(0xFF673AB7) // Deep Purple
        "Bloating" -> Color(0xFF4CAF50) // Green
        "Joint Pain" -> Color(0xFF795548) // Brown
        "Anxiety" -> Color(0xFFE91E63) // Pink
        "Insomnia" -> Color(0xFF3F51B5) // Indigo
        "Fatigue" -> Color(0xFFFFC107) // Amber
        "Acidity" -> Color(0xFF00BCD4) // Cyan
        "Cough" -> Color(0xFF009688) // Teal
        "Nausea" -> Color(0xFFCDDC39) // Lime
        "Back Pain" -> Color(0xFF607D8B) // Blue Grey
        "Muscle Cramps" -> Color(0xFFFF9800) // Orange
        "Dizziness" -> Color(0xFF9C27B0) // Purple
        "Constipation" -> Color(0xFF8BC34A) // Light Green
        "Heartburn" -> Color(0xFFF44336) // Red
        "Skin Rash" -> Color(0xFFFF4081) // Accent Pink
        else -> SmoothPurple
    }
}

@Composable
fun MealCard(meal: Meal, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable(onClick = onClick)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondaryContainer))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(meal.name, fontWeight = FontWeight.Bold)
                Text("${meal.calories} kcal • ${meal.time}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun DailyDietPlanScreen(viewModel: DashboardViewModel, onMealClick: (Meal) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Daily Diet Plan", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn {
            itemsIndexed(viewModel.dailyMeals) { _, meal ->
                MealCard(meal, onClick = { onMealClick(meal) })
            }
        }
    }
}

@Composable
fun MealDetailScreen(meal: Meal, onSwap: () -> Unit, onBack: () -> Unit) {
    var isFavorite by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(meal.name) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                actions = {
                    IconButton(onClick = { isFavorite = !isFavorite }) {
                        Icon(if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null, tint = Color.Red)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp).clip(MaterialTheme.shapes.medium).background(Color.LightGray))
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Benefit: ${meal.benefit}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Ingredients", style = MaterialTheme.typography.titleLarge)
            meal.ingredients.forEach { Text("• $it") }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Instructions", style = MaterialTheme.typography.titleLarge)
            Text(meal.instructions)
            
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onSwap, modifier = Modifier.fillMaxWidth()) { Text("Swap Meal") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SmallTopAppBar(title: @Composable () -> Unit, navigationIcon: @Composable () -> Unit, actions: @Composable RowScope.() -> Unit) {
    androidx.compose.material3.TopAppBar(title = title, navigationIcon = navigationIcon, actions = actions)
}

@Composable
fun WaterTrackerScreen(viewModel: DashboardViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Water Tracker", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(48.dp))
        
        Box(modifier = Modifier.size(200.dp).clip(CircleShape).background(Color.Cyan.copy(alpha = 0.2f)), contentAlignment = Alignment.BottomCenter) {
            val fillHeight = (viewModel.waterIntake.toFloat() / viewModel.waterTarget).coerceIn(0f, 1f)
            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(fillHeight).background(Color.Cyan))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text("${viewModel.waterIntake} / ${viewModel.waterTarget} ml", style = MaterialTheme.typography.headlineSmall)
        
        Spacer(modifier = Modifier.height(32.dp))
        Row {
            Button(onClick = { viewModel.addWater(250) }) { Text("+250ml") }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { viewModel.addWater(500) }) { Text("+500ml") }
        }
    }
}

@Composable
fun GroceryListScreen(viewModel: DashboardViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Grocery List", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn {
            itemsIndexed(viewModel.groceryList) { index, item ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { viewModel.toggleGrocery(index) }.padding(8.dp)) {
                    Checkbox(checked = item.second, onCheckedChange = { viewModel.toggleGrocery(index) })
                    Text(
                        item.first,
                        textDecoration = if (item.second) TextDecoration.LineThrough else null,
                        color = if (item.second) Color.Gray else Color.Unspecified
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeCookingScreen(viewModel: DashboardViewModel, onFinish: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Cooking Mode", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Step ${viewModel.currentStepIndex + 1} of ${viewModel.recipeSteps.size}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(viewModel.recipeSteps[viewModel.currentStepIndex], style = MaterialTheme.typography.headlineSmall)
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = { if (viewModel.currentStepIndex > 0) viewModel.currentStepIndex-- }) { Text("Previous") }
            Button(onClick = {
                if (viewModel.currentStepIndex < viewModel.recipeSteps.size - 1) viewModel.currentStepIndex++ else onFinish()
            }) {
                Text(if (viewModel.currentStepIndex == viewModel.recipeSteps.size - 1) "Finish" else "Next Step")
            }
        }
    }
}

@Composable
fun SymptomLoggerScreen(viewModel: DashboardViewModel, onSubmit: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Log Symptom", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = viewModel.loggedSymptom,
            onValueChange = { viewModel.loggedSymptom = it },
            label = { Text("What are you feeling?") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.DarkGray,
                focusedPlaceholderColor = Color.DarkGray,
                unfocusedPlaceholderColor = Color.DarkGray,
                cursorColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = viewModel.symptomNote,
            onValueChange = { viewModel.symptomNote = it },
            label = { Text("Notes (optional)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.DarkGray,
                focusedPlaceholderColor = Color.DarkGray,
                unfocusedPlaceholderColor = Color.DarkGray,
                cursorColor = Color.Black
            )
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onSubmit, modifier = Modifier.fillMaxWidth()) { Text("Submit") }
    }
}
