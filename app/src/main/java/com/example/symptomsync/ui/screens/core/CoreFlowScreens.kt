package com.example.symptomsync.ui.screens.core

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.symptomsync.ui.theme.*
import com.example.symptomsync.ui.viewmodels.CoreFlowViewModel

@Composable
fun CoreHomeScreen(
    userName: String,
    onAddSymptom: () -> Unit,
    onWaterClick: () -> Unit,
    onDietPlanClick: () -> Unit,
    onProgressClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessDarkCharcoal
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Text(
                text = "Welcome, $userName!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = WellnessWarmWhite,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Health Insights Badge
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = SmoothPurple.copy(alpha = 0.2f))
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, null, tint = RichOrange)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Health Insights Ready", fontWeight = FontWeight.Bold, color = WellnessWarmWhite)
                }
            }

            Button(
                onClick = onAddSymptom,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RichOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("+ Add Symptom", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            val widgets = listOf(
                WidgetData("Water Tracker", Icons.Default.WaterDrop, onWaterClick),
                WidgetData("Diet Plan", Icons.Default.Restaurant, onDietPlanClick),
                WidgetData("Progress", Icons.AutoMirrored.Filled.TrendingUp, onProgressClick),
                WidgetData("History", Icons.Default.History, onHistoryClick)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(widgets) { widget ->
                    WidgetCard(widget)
                }
            }
        }
    }
}

data class WidgetData(val title: String, val icon: ImageVector, val onClick: () -> Unit)

@Composable
fun WidgetCard(data: WidgetData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = data.onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = WellnessDarkCard,
            contentColor = WellnessWarmWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                data.icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = RichOrange
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(data.title, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun CoreSymptomSelectScreen(viewModel: CoreFlowViewModel, onContinue: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessPastelPink
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .padding(16.dp)
        ) {
            Text(
                "Add Symptoms",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = WellnessDarkNavy
            )
            Text(
                "Select or search for your symptoms",
                style = MaterialTheme.typography.bodyMedium,
                color = WellnessDarkNavy.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = { viewModel.searchQuery = it },
                label = { Text("Search symptoms...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black.copy(alpha = 0.6f)) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.DarkGray,
                    focusedPlaceholderColor = Color.DarkGray,
                    unfocusedPlaceholderColor = Color.DarkGray,
                    cursorColor = Color.Black,
                    focusedBorderColor = SmoothPurple,
                    unfocusedBorderColor = WellnessDarkNavy.copy(alpha = 0.2f)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Symptoms List", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = WellnessDarkNavy)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        viewModel.filteredSymptoms().forEach { symptom ->
                            val isSelected = viewModel.selectedSymptoms.contains(symptom)
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.toggleSymptom(symptom) },
                                label = { Text(symptom) },
                                modifier = Modifier.padding(4.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SmoothPurple,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.performAnalysis()
                    onContinue()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SmoothPurple)
            ) {
                Text("Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRow(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    androidx.compose.foundation.layout.FlowRow(modifier = modifier) { content() }
}

@Composable
fun CoreAnalysisScreen(viewModel: CoreFlowViewModel, onViewCauses: () -> Unit, onGetDietPlan: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessPastelPink
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Text(
                "Analysis Results",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = WellnessDarkNavy
            )
            Text(
                "Here's what we detected",
                style = MaterialTheme.typography.bodyMedium,
                color = WellnessDarkNavy.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            if (viewModel.aiReasoning != null) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SmoothPurple.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, null, tint = SmoothPurple)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Health Insights", fontWeight = FontWeight.Bold, color = SmoothPurple)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(viewModel.aiReasoning!!, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(viewModel.analysisResults) { result ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(result.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = WellnessDarkNavy)
                                Text("Intensity: ${result.severity}", style = MaterialTheme.typography.labelSmall, color = WellnessDarkNavy.copy(alpha = 0.5f))
                            }
                            SeveritySelector(
                                selectedSeverity = result.severity,
                                onSeveritySelected = { newSeverity ->
                                    viewModel.updateSymptomSeverity(result.name, newSeverity)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onViewCauses,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SmoothPurple)
                ) {
                    Text("View Possible Causes", fontSize = 16.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onGetDietPlan,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HealthGreen)
                ) {
                    Text("Get Diet Plan", fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun SeveritySelector(selectedSeverity: String, onSeveritySelected: (String) -> Unit) {
    val options = listOf("Low", "Medium", "High")
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        options.forEach { option ->
            val isSelected = selectedSeverity == option
            val color = when (option) {
                "Low" -> Color(0xFF4CAF50)
                "Medium" -> Color(0xFFFFC107)
                "High" -> Color(0xFFF44336)
                else -> Color.Gray
            }

            Surface(
                onClick = { onSeveritySelected(option) },
                color = if (isSelected) color else color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, color),
                modifier = Modifier.height(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 8.dp)) {
                    Text(
                        text = option,
                        color = if (isSelected) Color.White else color,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CoreCausesScreen(viewModel: CoreFlowViewModel, onGetDietPlan: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessPastelPink
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Text(
                "Possible Causes",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = WellnessDarkNavy
            )
            Text(
                "Understanding your symptoms",
                style = MaterialTheme.typography.bodyMedium,
                color = WellnessDarkNavy.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            if (viewModel.aiReasoning != null) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SmoothPurple.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, null, tint = SmoothPurple)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Health Insights", fontWeight = FontWeight.Bold, color = SmoothPurple)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(viewModel.aiReasoning!!, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(viewModel.possibleCauses) { cause ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(cause.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SmoothPurple, modifier = Modifier.weight(1f))
                                if (cause.parentSymptom.isNotEmpty()) {
                                    Surface(
                                        color = SmoothPurple.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            cause.parentSymptom,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SmoothPurple
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(cause.description, style = MaterialTheme.typography.bodyLarge, color = WellnessDarkNavy)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onGetDietPlan,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HealthGreen)
            ) {
                Text("Get Personalized Diet Plan", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun CoreDietPlanScreen(viewModel: CoreFlowViewModel, onMealClick: (String, String) -> Unit) {
    var showAvoidDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessDarkCharcoal
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Text(
                "Your Diet Plan",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = WellnessWarmWhite
            )
            Text(
                "Tailored to alleviate detected symptoms",
                style = MaterialTheme.typography.bodyMedium,
                color = WellnessWarmWhite.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            if (viewModel.aiReasoning != null) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SmoothPurple.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, null, tint = SmoothPurple)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Health Insights", fontWeight = FontWeight.Bold, color = SmoothPurple)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(viewModel.aiReasoning!!, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(viewModel.mealPlan) { meal ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { onMealClick(meal.title, meal.targetingSymptom) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = WellnessDarkCard,
                            contentColor = WellnessWarmWhite
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(meal.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    if (meal.targetingSymptom.isNotEmpty()) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Surface(
                                            color = HealthGreen.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                meal.targetingSymptom,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = HealthGreen
                                            )
                                        }
                                    }
                                }
                                Text(meal.time, style = MaterialTheme.typography.bodyMedium, color = WellnessWarmWhite.copy(alpha = 0.5f))
                            }
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = WellnessWarmWhite.copy(alpha = 0.5f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showAvoidDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = HealthGreen,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                Text(
                    text = "View Foods to Avoid",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    softWrap = true,
                    maxLines = 1
                )
            }
        }
    }

    if (showAvoidDialog) {
        AlertDialog(
            onDismissRequest = { showAvoidDialog = false },
            confirmButton = {
                TextButton(onClick = { showAvoidDialog = false }) { Text("Got it") }
            },
            title = { Text("Foods to Avoid", color = RichOrange, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("The following foods may trigger flare-ups for your selected symptoms:", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    viewModel.foodsToAvoid.forEach { food ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(RichOrange))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(food, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            },
            containerColor = WellnessWarmWhite
        )
    }
}

@Composable
fun CoreWaterTrackerScreen(viewModel: CoreFlowViewModel, onDone: () -> Unit) {
    val progress = (viewModel.waterGlassCount.toFloat() / viewModel.waterGoal).coerceIn(0f, 1f)
    val isGoalAchieved = viewModel.waterGlassCount >= viewModel.waterGoal

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(WellnessBlueUndertone, WellnessPurpleUndertone)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Water Tracker", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = WellnessDarkNavy)
            Spacer(modifier = Modifier.height(32.dp))

            // Droplet with dynamic fill
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(220.dp)) {
                // Background droplet (Empty)
                Icon(
                    Icons.Default.WaterDrop,
                    contentDescription = null,
                    modifier = Modifier.size(200.dp),
                    tint = Color.LightGray.copy(alpha = 0.3f)
                )
                
                // Filling droplet (Masked by height)
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(DropletShape)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(progress)
                            .align(Alignment.BottomCenter)
                            .background(Color(0xFF2196F3).copy(alpha = 0.8f))
                    )
                }

                Text(
                    text = "${viewModel.waterGlassCount}",
                    style = MaterialTheme.typography.displayLarge,
                    color = if (progress > 0.4f) Color.White else WellnessDarkNavy,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "${viewModel.waterGlassCount} / ${viewModel.waterGoal} Glasses",
                style = MaterialTheme.typography.titleLarge,
                color = WellnessDarkNavy,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${(progress * 100).toInt()}% of daily goal",
                style = MaterialTheme.typography.bodyMedium,
                color = WellnessDarkNavy.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = isGoalAchieved,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Surface(
                    color = HealthGreen.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HealthGreen),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Goal Achieved! 🎉", color = HealthGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }

            IconButton(
                onClick = { viewModel.addWaterGlass() },
                modifier = Modifier
                    .size(64.dp)
                    .background(SmoothPurple, CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Glass", tint = Color.White, modifier = Modifier.size(32.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Smart Reminders Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = RichOrange, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Smart Reminders", fontWeight = FontWeight.Bold, color = WellnessDarkNavy)
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = viewModel.waterRemindersEnabled,
                            onCheckedChange = { viewModel.waterRemindersEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = RichOrange, checkedTrackColor = RichOrange.copy(alpha = 0.5f))
                        )
                    }
                    
                    if (viewModel.waterRemindersEnabled) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Reminder Interval", style = MaterialTheme.typography.labelSmall, color = WellnessDarkNavy.copy(alpha = 0.6f))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("1 Hour", "2 Hours", "3 Hours").forEach { interval ->
                                FilterChip(
                                    selected = viewModel.waterReminderInterval == interval,
                                    onClick = { viewModel.waterReminderInterval = interval },
                                    label = { Text(interval, fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = SmoothPurple,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onDone,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = WellnessDarkNavy)
            ) {
                Text("Done", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

// Custom shape to match the WaterDrop icon for clipping
val DropletShape = object : androidx.compose.ui.graphics.Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: androidx.compose.ui.unit.Density
    ): androidx.compose.ui.graphics.Outline {
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(size.width / 2f, 0f)
            cubicTo(
                size.width / 2f, 0f,
                0f, size.height * 0.6f,
                0f, size.height * 0.8f
            )
            cubicTo(
                0f, size.height,
                size.width, size.height,
                size.width, size.height * 0.8f
            )
            cubicTo(
                size.width, size.height * 0.6f,
                size.width / 2f, 0f,
                size.width / 2f, 0f
            )
            close()
        }
        return androidx.compose.ui.graphics.Outline.Generic(path)
    }
}

@Composable
fun CoreProgressScreen(viewModel: CoreFlowViewModel, onBack: () -> Unit) {
    val waterProgress = (viewModel.waterGlassCount.toFloat() / viewModel.waterGoal).coerceIn(0f, 1f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(WellnessPurpleUndertone, WellnessBlueUndertone)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Text("Daily Progress", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = WellnessDarkNavy)
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Weekly Overview", fontWeight = FontWeight.Bold, color = WellnessDarkNavy)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        viewModel.weeklyProgress.forEach { data ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .fillMaxHeight(data.value * 0.8f)
                                        .background(SmoothPurple, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(data.day, fontSize = 10.sp, color = WellnessDarkNavy)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ProgressMetricCard("${viewModel.selectedSymptoms.size} Symptoms", Icons.Default.Warning, Modifier.weight(1f))
                ProgressMetricCard("${(waterProgress * 100).toInt()}% Water", Icons.Default.WaterDrop, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            ProgressMetricCard("92% Diet Adherence", Icons.Default.Restaurant, Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = WellnessDarkNavy)
            ) {
                Text("Back to Home", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun ProgressMetricCard(text: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = SmoothPurple)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, fontWeight = FontWeight.Medium, color = WellnessDarkNavy)
        }
    }
}

@Composable
fun CoreHistoryScreen(onBack: () -> Unit) {
    val historyItems = listOf(
        HistoryItem("July 22", "Headache, Fatigue", "90% Adherence"),
        HistoryItem("July 21", "Acidity", "85% Adherence"),
        HistoryItem("July 20", "None", "95% Adherence"),
        HistoryItem("July 19", "Cough", "70% Adherence")
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessWarmWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = WellnessDarkNavy) }
                Text("History Log", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = WellnessDarkNavy)
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(historyItems) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(item.date, fontWeight = FontWeight.Bold, color = WellnessDarkNavy)
                                Text(item.adherence, color = HealthGreen)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Symptoms: ${item.symptoms}", color = WellnessDarkNavy.copy(alpha = 0.6f))
                        }
                    }
                }
            }
        }
    }
}

data class HistoryItem(val date: String, val symptoms: String, val adherence: String)

@Composable
fun CoreMealDetailScreen(mealType: String, symptomName: String, viewModel: CoreFlowViewModel, onBack: () -> Unit) {
    val detail = viewModel.getMealDetail(mealType, symptomName)
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessLightCream
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = WellnessDarkNavy) }
                Column {
                    Text(mealType, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = WellnessDarkNavy)
                    Text(detail.time, style = MaterialTheme.typography.bodyMedium, color = WellnessDarkNavy.copy(alpha = 0.5f))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Food Banner Image Placeholder
            MealBannerImage(mealType)

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    if (detail.addressedSymptom.isNotEmpty()) {
                        Text(
                            text = "Recommended for: ${detail.addressedSymptom}",
                            style = MaterialTheme.typography.labelLarge,
                            color = HealthGreen,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Text(detail.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = WellnessDarkNavy)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(detail.description, style = MaterialTheme.typography.bodyLarge, color = WellnessDarkNavy.copy(alpha = 0.7f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Nutritional Value", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = WellnessDarkNavy)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NutritionCard("Calories", detail.calories, IndicatorYellow, Modifier.weight(1f))
                NutritionCard("Protein", detail.protein, IndicatorRed, Modifier.weight(1f))
                NutritionCard("Fiber", detail.fiber, IndicatorGreen, Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun MealBannerImage(mealType: String) {
    val backgroundColor = when (mealType) {
        "Breakfast" -> Color(0xFFFFE0B2) // Warm Oat/Orange
        "Lunch" -> Color(0xFFC8E6C9)    // Fresh Green
        "Snacks" -> Color(0xFFF8BBD0)   // Fruity Pink
        "Dinner" -> Color(0xFFD1C4E9)   // Soup Purple/Warm
        else -> Color.LightGray
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (mealType == "Breakfast") {
            // High-fidelity Breakfast Placeholder
            Box(modifier = Modifier.fillMaxSize()) {
                // Stylized "Oats" pattern background
                repeat(5) { i ->
                    Box(
                        modifier = Modifier
                            .offset(x = (i * 60).dp, y = (i * 30).dp)
                            .size(100.dp)
                            .background(Color.White.copy(alpha = 0.1f), CircleShape)
                    )
                }
                
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.BreakfastDining,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = WellnessDarkNavy.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Oats & Fruits Bowl",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = WellnessDarkNavy.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "Fresh Berries • Sliced Banana • Chia Seeds",
                        style = MaterialTheme.typography.labelMedium,
                        color = WellnessDarkNavy.copy(alpha = 0.5f)
                    )
                }
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.Restaurant,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = WellnessDarkNavy.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Premium $mealType Image",
                    style = MaterialTheme.typography.labelLarge,
                    color = WellnessDarkNavy.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun NutritionCard(label: String, value: String, indicatorColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(indicatorColor))
                Spacer(modifier = Modifier.width(4.dp))
                Text(label, style = MaterialTheme.typography.labelSmall, color = WellnessDarkNavy.copy(alpha = 0.5f))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = WellnessDarkNavy)
        }
    }
}
