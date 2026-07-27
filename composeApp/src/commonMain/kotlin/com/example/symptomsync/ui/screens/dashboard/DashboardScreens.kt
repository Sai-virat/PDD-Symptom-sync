package com.example.symptomsync.ui.screens.dashboard

import androidx.compose.animation.*
import androidx.compose.foundation.background
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

            // Calorie Ring
            Card(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        progress = { viewModel.calorieIntake.toFloat() / viewModel.calorieTarget.coerceAtLeast(1) },
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
                        @OptIn(ExperimentalLayoutApi::class)
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
        "Fever" -> Color(0xFFFF5722)
        "Migraine" -> Color(0xFF673AB7)
        "Bloating" -> Color(0xFF4CAF50)
        "Joint Pain" -> Color(0xFF795548)
        "Anxiety" -> Color(0xFFE91E63)
        "Insomnia" -> Color(0xFF3F51B5)
        "Fatigue" -> Color(0xFFFFC107)
        "Acidity" -> Color(0xFF00BCD4)
        "Cough" -> Color(0xFF009688)
        "Nausea" -> Color(0xFFCDDC39)
        "Back Pain" -> Color(0xFF607D8B)
        "Muscle Cramps" -> Color(0xFFFF9800)
        "Dizziness" -> Color(0xFF9C27B0)
        "Constipation" -> Color(0xFF8BC34A)
        "Heartburn" -> Color(0xFFF44336)
        "Skin Rash" -> Color(0xFFFF4081)
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
    // Water tracker implementation
}

@Composable
fun GroceryListScreen(viewModel: DashboardViewModel) {
    // Grocery list implementation
}

@Composable
fun RecipeCookingScreen(viewModel: DashboardViewModel, onFinish: () -> Unit) {
    // Recipe mode implementation
}

@Composable
fun SymptomLoggerScreen(viewModel: DashboardViewModel, onSubmit: () -> Unit) {
    // Symptom logger implementation
}
