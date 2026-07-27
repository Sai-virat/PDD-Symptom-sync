package com.example.symptomsync.ui.screens.settings


import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.symptomsync.ui.theme.*
import com.example.symptomsync.ui.viewmodels.HealthProfileViewModel


@Composable
fun UserProfileScreen(
    viewModel: HealthProfileViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize(), color = WellnessDarkCharcoal) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Text("Profile", style = MaterialTheme.typography.headlineMedium, color = WellnessWarmWhite)
            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WellnessDarkCard)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(64.dp),
                        shape = MaterialTheme.shapes.extraLarge,
                        color = RichOrange
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(viewModel.userName, style = MaterialTheme.typography.titleLarge, color = WellnessWarmWhite)
                        Text("Premium Member", style = MaterialTheme.typography.bodySmall, color = WellnessWarmWhite.copy(alpha = 0.6f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            SettingsMenuItem("Edit Health Profile", Icons.Default.Edit) { onNavigate("EditHealthProfile") }
            SettingsMenuItem("Dietary Goal Adjuster", Icons.Default.Settings) { onNavigate("DietaryGoalAdjuster") }
            SettingsMenuItem("App Preferences", Icons.Default.DisplaySettings) { onNavigate("AppPreferences") }
            SettingsMenuItem("Notifications", Icons.Default.Notifications) { onNavigate("NotificationsCenter") }
            SettingsMenuItem("FAQ Center", Icons.Default.QuestionAnswer) { onNavigate("FAQCenter") }
            SettingsMenuItem("Privacy Policy", Icons.Default.PrivacyTip) { onNavigate("PrivacyAbout") }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color(0xFFF44336)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF44336)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Logout", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            confirmButton = {
                TextButton(onClick = onLogout) { Text("Logout", color = Color(0xFFF44336)) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            },
            title = { Text("Logout Confirmation") },
            text = { Text("Are you sure you want to logout from SymptomSync?") },
            containerColor = WellnessDarkCard,
            titleContentColor = WellnessWarmWhite,
            textContentColor = WellnessWarmWhite.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun SettingsMenuItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = RichOrange)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge, color = WellnessWarmWhite)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = WellnessWarmWhite.copy(alpha = 0.3f))
    }
}

@Composable
fun EditHealthProfileScreen(viewModel: HealthProfileViewModel, onBack: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = WellnessLightCream) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = WellnessDarkNavy) }
                Text("Edit Health Profile", style = MaterialTheme.typography.headlineMedium, color = WellnessDarkNavy)
            }
            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    ProfileEditField("Full Name", viewModel.userName) { viewModel.userName = it }
                    ProfileEditField("Age", viewModel.age.toString()) { viewModel.age = it.toIntOrNull() ?: viewModel.age }
                    ProfileEditField("Weight (kg)", viewModel.weight.toString()) { viewModel.weight = it.toFloatOrNull() ?: viewModel.weight }
                    ProfileEditField("Height (cm)", viewModel.height.toString()) { viewModel.height = it.toFloatOrNull() ?: viewModel.height }
                    ProfileEditField("Medical Conditions", viewModel.medicalConditions) { viewModel.medicalConditions = it }
                }
            }

            Button(
                onClick = {
                    // TODO: Multiplatform toast or snackbar
                    onBack()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HealthGreen)
            ) {
                Text("Save Changes", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun ProfileEditField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium, color = WellnessDarkNavy.copy(alpha = 0.6f))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedBorderColor = RichOrange,
                unfocusedBorderColor = WellnessDarkNavy.copy(alpha = 0.2f)
            )
        )
    }
}

@Composable
fun DietaryGoalAdjusterScreen(viewModel: HealthProfileViewModel, onBack: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = WellnessLightCream) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = WellnessDarkNavy) }
                Text("Goal Adjuster", style = MaterialTheme.typography.headlineMedium, color = WellnessDarkNavy)
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text("Daily Calorie Adjustment: ${viewModel.calorieAdjustment.toInt()} kcal", fontWeight = FontWeight.Bold)
            Slider(
                value = viewModel.calorieAdjustment,
                onValueChange = { viewModel.calorieAdjustment = it },
                valueRange = -500f..500f,
                colors = SliderDefaults.colors(thumbColor = RichOrange, activeTrackColor = RichOrange)
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text("Macro Distribution", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            MacroAdjuster("Protein", viewModel.proteinRatio) { viewModel.proteinRatio = it }
            MacroAdjuster("Carbs", viewModel.carbsRatio) { viewModel.carbsRatio = it }
            MacroAdjuster("Fat", viewModel.fatRatio) { viewModel.fatRatio = it }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HealthGreen)
            ) {
                Text("Save Goals", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun MacroAdjuster(label: String, ratio: Int, onRatioChange: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label, modifier = Modifier.weight(1f))
        IconButton(onClick = { if (ratio > 0) onRatioChange(ratio - 5) }) { Icon(Icons.Default.Remove, null) }
        Text("$ratio%", fontWeight = FontWeight.Bold, modifier = Modifier.width(48.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        IconButton(onClick = { if (ratio < 100) onRatioChange(ratio + 5) }) { Icon(Icons.Default.Add, null) }
    }
}

@Composable
fun AppPreferencesScreen(viewModel: HealthProfileViewModel, onBack: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = WellnessWarmWhite) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = WellnessDarkNavy) }
                Text("App Preferences", style = MaterialTheme.typography.headlineMedium, color = WellnessDarkNavy)
            }
            Spacer(modifier = Modifier.height(32.dp))

            PreferenceToggle("Dark Mode", viewModel.isDarkMode) { viewModel.isDarkMode = it }
            PreferenceOption("Unit System", viewModel.unitSystem, listOf("Metric", "Imperial")) { viewModel.unitSystem = it }
            PreferenceOption("Notifications", viewModel.notificationFrequency, listOf("Daily", "Weekly", "None")) { viewModel.notificationFrequency = it }
        }
    }
}

@Composable
fun PreferenceToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedThumbColor = RichOrange, checkedTrackColor = RichOrange.copy(alpha = 0.5f)))
    }
}

@Composable
fun PreferenceOption(label: String, selected: String, options: List<String>, onOptionSelected: (String) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            options.forEach { option ->
                FilterChip(
                    selected = selected == option,
                    onClick = { onOptionSelected(option) },
                    label = { Text(option) },
                    modifier = Modifier.padding(end = 8.dp),
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = SmoothPurple, selectedLabelColor = Color.White)
                )
            }
        }
    }
}

@Composable
fun NotificationsCenterScreen(viewModel: HealthProfileViewModel, onBack: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = WellnessLightCream) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = WellnessDarkNavy) }
                Text("Notifications", style = MaterialTheme.typography.headlineMedium, color = WellnessDarkNavy)
            }
            Spacer(modifier = Modifier.height(32.dp))

            ReminderItem(
                "Meal Reminder",
                viewModel.mealReminderEnabled,
                viewModel.mealReminderTime,
                { viewModel.mealReminderEnabled = it },
                {
                    // TODO: Multiplatform time picker
                }
            )

            ReminderItem(
                "Hydration Alert",
                viewModel.waterReminderEnabled,
                viewModel.waterReminderTime,
                { viewModel.waterReminderEnabled = it },
                {
                    // TODO: Multiplatform time picker
                }
            )
        }
    }
}

@Composable
fun ReminderItem(label: String, enabled: Boolean, time: String, onToggle: (Boolean) -> Unit, onTimeClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label, fontWeight = FontWeight.Bold)
                Text(time, modifier = Modifier.clickable(onClick = onTimeClick), color = RichOrange, style = MaterialTheme.typography.titleMedium)
            }
            Switch(checked = enabled, onCheckedChange = onToggle)
        }
    }
}

@Composable
fun FAQCenterScreen(onBack: () -> Unit) {
    val faqs = listOf(
        "How does AI plan my diet?" to "We analyze your symptoms, goals, and dietary preferences using advanced algorithms to optimize your nutrient ratios and avoid flare-ups.",
        "Can I track multiple symptoms?" to "Yes, you can select and rate multiple symptoms in the logger. Our AI will look for correlations between all of them and your food intake.",
        "Is my data private?" to "Absolutely, we encrypt all your health data and never share it with third parties. Your privacy is our top priority.",
        "How often should I log symptoms?" to "For the best results, log your symptoms whenever you feel a change. Consistency helps the AI find more accurate patterns."
    )
    
    Surface(modifier = Modifier.fillMaxSize(), color = WellnessWarmWhite) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = WellnessDarkNavy) }
                Text("FAQ Center", style = MaterialTheme.typography.headlineMedium, color = WellnessDarkNavy)
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(faqs) { faq ->
                    var expanded by remember { mutableStateOf(false) }
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(faq.first, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), color = WellnessDarkNavy)
                                Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null)
                            }
                            AnimatedVisibility(visible = expanded) {
                                Text(faq.second, modifier = Modifier.padding(top = 8.dp), color = WellnessDarkNavy.copy(alpha = 0.7f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrivacyAboutScreen(onBack: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = WellnessWarmWhite) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = WellnessDarkNavy) }
                Text("Privacy & About", style = MaterialTheme.typography.headlineMedium, color = WellnessDarkNavy)
            }
            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                item {
                    AboutSection("App Version", "SymptomSync v1.0.0 (Production Ready)")
                    AboutSection("Privacy Policy", "Your data is stored locally and encrypted. We use end-to-end encryption for any cloud-based AI processing features to ensure your health information remains strictly confidential.")
                    AboutSection("Terms of Service", "By using SymptomSync, you agree to our terms regarding data usage for personalized health recommendations. Please consult a doctor before making significant dietary changes.")
                    AboutSection("About the Project", "SymptomSync is an AI-powered platform designed to bridge the gap between nutrition and symptomatic health, providing data-driven meal planning for a better lifestyle.")
                }
            }
        }
    }
}

@Composable
fun AboutSection(title: String, content: String) {
    Column {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RichOrange)
        Spacer(modifier = Modifier.height(8.dp))
        Text(content, style = MaterialTheme.typography.bodyLarge, color = WellnessDarkNavy.copy(alpha = 0.8f))
    }
}
