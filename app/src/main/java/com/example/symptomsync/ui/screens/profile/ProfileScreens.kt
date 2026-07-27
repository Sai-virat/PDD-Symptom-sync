package com.example.symptomsync.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.symptomsync.ui.theme.*
import com.example.symptomsync.ui.viewmodels.HealthProfileViewModel
import kotlinx.coroutines.delay

@Composable
fun BasicInfoScreen(viewModel: HealthProfileViewModel, onNext: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessWarmWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp)
        ) {
            Text("Personal Details", style = MaterialTheme.typography.headlineLarge, color = WellnessDarkNavy)
            Spacer(modifier = Modifier.height(32.dp))

            Text("Age: ${viewModel.age}", color = WellnessDarkNavy)
            Slider(value = viewModel.age.toFloat(), onValueChange = { viewModel.age = it.toInt() }, valueRange = 10f..100f)

            Spacer(modifier = Modifier.height(16.dp))
            Text("Gender", color = WellnessDarkNavy)
            Row {
                listOf("Male", "Female", "Other").forEach {
                    FilterChip(
                        selected = viewModel.gender == it,
                        onClick = { viewModel.gender = it },
                        label = { Text(it) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Height: ${viewModel.height.toInt()} cm", color = WellnessDarkNavy)
            Slider(value = viewModel.height, onValueChange = { viewModel.height = it }, valueRange = 100f..250f)

            Spacer(modifier = Modifier.height(16.dp))
            Text("Weight: ${viewModel.weight.toInt()} kg", color = WellnessDarkNavy)
            Slider(value = viewModel.weight, onValueChange = { viewModel.weight = it }, valueRange = 30f..200f)

            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onNext, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = RichOrange)) { Text("Next") }
        }
    }
}

@Composable
fun DietaryPreferencesScreen(viewModel: HealthProfileViewModel, onNext: () -> Unit) {
    val diets = listOf("Vegan", "Vegetarian", "Non-Veg", "Low Carb")
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessWarmWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp)
        ) {
            Text("Dietary Preferences", style = MaterialTheme.typography.headlineLarge, color = WellnessDarkNavy)
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Choose your diet types", color = WellnessDarkNavy)
            FlowRow(modifier = Modifier.padding(vertical = 8.dp)) {
                diets.forEach { diet ->
                    FilterChip(
                        selected = viewModel.selectedDiets.contains(diet),
                        onClick = { viewModel.toggleDiet(diet) },
                        label = { Text(diet) },
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onNext, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = RichOrange)) { Text("Next") }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    androidx.compose.foundation.layout.FlowRow(modifier = modifier) { content() }
}

@Composable
fun FitnessGoalsScreen(viewModel: HealthProfileViewModel, onNext: () -> Unit) {
    val goals = listOf("Weight Loss", "Muscle Gain", "Maintain Weight", "Improve Energy")
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessWarmWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp)
        ) {
            Text("Your Fitness Goal", style = MaterialTheme.typography.headlineLarge, color = WellnessDarkNavy)
            Spacer(modifier = Modifier.height(16.dp))

            goals.forEach { goal ->
                OutlinedCard(
                    onClick = { viewModel.selectedGoal = goal },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = if (viewModel.selectedGoal == goal) WellnessPastelPink else Color.White
                    )
                ) {
                    Text(goal, modifier = Modifier.padding(16.dp), color = WellnessDarkNavy)
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onNext, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = RichOrange)) { Text("Next") }
        }
    }
}

@Composable
fun SymptomSelectionScreen(viewModel: HealthProfileViewModel, onNext: () -> Unit) {
    val symptoms = listOf(
        "Fever", "Migraine", "Bloating", "Joint Pain", "Anxiety", "Insomnia", 
        "Fatigue", "Acidity", "Cough", "Nausea", "Back Pain", 
        "Muscle Cramps", "Dizziness", "Constipation", "Heartburn", "Skin Rash"
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessPastelPink
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .padding(24.dp)
        ) {
            Text("Select Symptoms", style = MaterialTheme.typography.headlineLarge, color = WellnessDarkNavy)
            OutlinedTextField(
                value = viewModel.symptomSearchQuery,
                onValueChange = { viewModel.symptomSearchQuery = it },
                label = { Text("Search symptoms") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
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

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(symptoms.filter { it.contains(viewModel.symptomSearchQuery, ignoreCase = true) }) { symptom ->
                    FilterChip(
                        selected = viewModel.selectedSymptoms.contains(symptom),
                        onClick = { viewModel.toggleSymptom(symptom) },
                        label = { Text(symptom) },
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNext, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = SmoothPurple)) { Text("Next") }
        }
    }
}

@Composable
fun SeverityScaleScreen(viewModel: HealthProfileViewModel, onNext: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessPastelPink
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp)
        ) {
            Text("Symptom Severity", style = MaterialTheme.typography.headlineLarge, color = WellnessDarkNavy)
            Text("Rate the intensity of selected symptoms (1-10)", style = MaterialTheme.typography.bodyMedium, color = WellnessDarkNavy.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(viewModel.selectedSymptoms) { symptom ->
                    val intensity = viewModel.symptomSeverities[symptom] ?: 5f
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(symptom, fontWeight = FontWeight.Bold, color = WellnessDarkNavy)
                            Text(
                                text = intensity.toInt().toString(),
                                fontWeight = FontWeight.Bold,
                                color = SmoothPurple,
                                fontSize = 18.sp
                            )
                        }
                        Slider(
                            value = intensity,
                            onValueChange = { viewModel.symptomSeverities[symptom] = it },
                            valueRange = 1f..10f,
                            colors = SliderDefaults.colors(
                                thumbColor = SmoothPurple,
                                activeTrackColor = SmoothPurple
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onNext, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = SmoothPurple)) { Text("Next") }
        }
    }
}

@Composable
fun ActivityLevelScreen(viewModel: HealthProfileViewModel, onNext: () -> Unit) {
    val levels = listOf("Sedentary", "Light", "Moderate", "Active", "Very Active")
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessWarmWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp)
        ) {
            Text("Activity Level", style = MaterialTheme.typography.headlineLarge, color = WellnessDarkNavy)
            Spacer(modifier = Modifier.height(16.dp))

            levels.forEach { level ->
                RadioButtonOption(
                    label = level,
                    selected = viewModel.activityLevel == level,
                    onClick = { viewModel.activityLevel = level }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("Daily Water Target: ${viewModel.waterTarget} ml", color = WellnessDarkNavy)
            Slider(value = viewModel.waterTarget.toFloat(), onValueChange = { viewModel.waterTarget = it.toInt() }, valueRange = 1000f..5000f)

            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onNext, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = RichOrange)) { Text("Next") }
        }
    }
}

@Composable
fun RadioButtonOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        RadioButton(selected = selected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = RichOrange))
        Text(label, color = WellnessDarkNavy)
    }
}

@Composable
fun AIProcessingScreen(viewModel: HealthProfileViewModel, onComplete: () -> Unit) {
    val statuses = listOf("Analyzing your health data...", "Checking symptom correlations...", "Optimizing nutrient ratios...", "Generating your diet plan...")
    var currentStatusIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        repeat(4) {
            viewModel.processingStatus = statuses[currentStatusIndex]
            delay(1000)
            if (currentStatusIndex < 3) currentStatusIndex++
        }
        viewModel.saveProfileToFirestore {
            onComplete()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessLightCream
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(modifier = Modifier.size(64.dp), color = SmoothPurple)
                Spacer(modifier = Modifier.height(24.dp))
                Text(viewModel.processingStatus, style = MaterialTheme.typography.bodyLarge, color = WellnessDarkNavy)
            }
        }
    }
}

@Composable
fun ProfileSuccessScreen(onGoToDashboard: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessLightCream
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("All Set!", style = MaterialTheme.typography.headlineLarge, color = HealthGreen)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Your AI-powered diet plan is ready based on your health profile.",
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = WellnessDarkNavy
                )
                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    onClick = onGoToDashboard,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HealthGreen)
                ) {
                    Text("Go to Dashboard", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
