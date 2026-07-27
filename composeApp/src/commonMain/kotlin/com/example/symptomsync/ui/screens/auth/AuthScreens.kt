package com.example.symptomsync.ui.screens.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.symptomsync.ui.theme.*
import com.example.symptomsync.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val scale = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        scale.animateTo(1.2f, animationSpec = tween(1000))
        delay(1000)
        onTimeout()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "SymptomSync",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.scale(scale.value)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("AI-Powered Diet Planner", fontSize = 16.sp)
        }
    }
}

@Composable
fun LoginScreen(viewModel: AuthViewModel, onLoginSuccess: () -> Unit, onSignUpClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessWarmWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
        Text("Welcome Back", style = MaterialTheme.typography.headlineMedium)
        
        if (viewModel.loginError != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = viewModel.loginError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { 
                viewModel.email = it
                viewModel.emailError = null
                viewModel.loginError = null
            },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Black.copy(alpha = 0.6f)) },
            modifier = Modifier.fillMaxWidth(),
            isError = viewModel.emailError != null,
            supportingText = {
                if (viewModel.emailError != null) {
                    Text(viewModel.emailError!!, color = MaterialTheme.colorScheme.error)
                }
            },
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
            value = viewModel.password,
            onValueChange = { 
                viewModel.password = it
                viewModel.loginError = null
            },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Black.copy(alpha = 0.6f)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = viewModel.loginError != null,
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

        TextButton(
            onClick = { viewModel.showForgotPasswordDialog = true },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Forgot Password?")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.onLoginClick(onLoginSuccess) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onSignUpClick) {
            Text("Don't have an account? Sign Up")
        }
    }
}

    if (viewModel.showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showForgotPasswordDialog = false },
            confirmButton = {
                Button(onClick = { viewModel.showForgotPasswordDialog = false }) { Text("Send Reset Link") }
            },
            title = { Text("Forgot Password") },
            text = { Text("Enter your email to receive a password reset link.") }
        )
    }
}

@Composable
fun RegistrationScreen(viewModel: AuthViewModel, onRegisterSuccess: () -> Unit, onLoginClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WellnessWarmWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
        Text("Create Account", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = viewModel.fullName,
            onValueChange = { 
                viewModel.fullName = it
                viewModel.fullNameError = null
            },
            label = { Text("Full Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.Black.copy(alpha = 0.6f)) },
            modifier = Modifier.fillMaxWidth(),
            isError = viewModel.fullNameError != null,
            supportingText = {
                if (viewModel.fullNameError != null) {
                    Text(viewModel.fullNameError!!, color = MaterialTheme.colorScheme.error)
                }
            },
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
            value = viewModel.email,
            onValueChange = { 
                viewModel.email = it
                viewModel.regEmailError = null
            },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Black.copy(alpha = 0.6f)) },
            modifier = Modifier.fillMaxWidth(),
            isError = viewModel.regEmailError != null,
            supportingText = {
                if (viewModel.regEmailError != null) {
                    Text(viewModel.regEmailError!!, color = MaterialTheme.colorScheme.error)
                }
            },
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
            value = viewModel.password,
            onValueChange = { 
                viewModel.password = it
                viewModel.regPasswordError = null
            },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Black.copy(alpha = 0.6f)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = viewModel.regPasswordError != null,
            supportingText = {
                if (viewModel.regPasswordError != null) {
                    Text(viewModel.regPasswordError!!, color = MaterialTheme.colorScheme.error)
                }
            },
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

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = viewModel.isTermsAccepted, 
                    onCheckedChange = { 
                        viewModel.isTermsAccepted = it
                        viewModel.termsError = null
                    },
                    colors = CheckboxDefaults.colors(checkedColor = SmoothPurple)
                )
                Text(
                    text = "I agree to the Terms and Conditions",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            if (viewModel.termsError != null) {
                Text(
                    text = viewModel.termsError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.onRegisterClick(onRegisterSuccess) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onLoginClick) {
            Text("Already have an account? Login")
        }
    }
}
}
