package com.example.symptomsync.ui.screens.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.symptomsync.data.AIRecipe
import com.example.symptomsync.ui.theme.*

@Composable
fun RecipeCookingScreen(recipe: AIRecipe, onBack: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessWarmWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = WellnessDarkNavy) }
                Text("Cooking Mode", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            Text(recipe.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = SmoothPurple)
            
            Spacer(modifier = Modifier.height(24.dp))

            Text("Ingredients", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            recipe.ingredients.forEach { ingredient ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Box(modifier = Modifier.size(6.dp).background(RichOrange, CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(ingredient)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Step-by-Step Instructions", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(recipe.steps) { index, step ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${index + 1}",
                                fontWeight = FontWeight.Bold,
                                color = SmoothPurple,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(step)
                        }
                    }
                }
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(top = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HealthGreen)
            ) {
                Icon(Icons.Default.CheckCircle, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("I'm Done Cooking!", fontWeight = FontWeight.Bold)
            }
        }
    }
}
