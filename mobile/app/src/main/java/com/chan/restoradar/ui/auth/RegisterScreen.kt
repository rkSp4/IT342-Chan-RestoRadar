package com.chan.restoradar.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val BgStart       = Color(0xFFFDF0EA)
private val BgEnd         = Color(0xFFFAE8E0)
private val CardBg        = Color(0xFFFFFFFF)
private val OrangeAccent  = Color(0xFFE8470A)
private val DarkButton    = Color(0xFF1A1A1A)
private val FieldBg       = Color(0xFFF4F4F4)
private val LabelColor    = Color(0xFF1A1A1A)
private val SubtitleColor = Color(0xFF9CA3AF)
private val ErrorColor    = Color(0xFFDC2626)

@Composable
fun RegisterScreen(
    onRegisterSuccess: (token: String, fullName: String) -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var fullName               by remember { mutableStateOf("") }
    var email                  by remember { mutableStateOf("") }
    var password               by remember { mutableStateOf("") }
    var confirmPassword        by remember { mutableStateOf("") }
    var passwordVisible        by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            val data = (uiState as AuthUiState.Success).data
            onRegisterSuccess(data.token, data.user.fullName)
            viewModel.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BgStart, BgEnd))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── App Icon ──────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.verticalGradient(listOf(Color(0xFFFF6B35), Color(0xFFE8470A)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = "RestoRadar",
                    tint = Color.White,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── White Card ────────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Create Account",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = LabelColor
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Join RestoRadar and start discovering amazing food",
                        fontSize = 14.sp,
                        color = SubtitleColor,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    FieldLabel("Full Name")
                    CleanTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        placeholder = "John Doe"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FieldLabel("Email")
                    CleanTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "you@example.com",
                        keyboardType = KeyboardType.Email
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FieldLabel("Password")
                    CleanTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "8+ chars, 1 uppercase, 1 digit",
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onTogglePassword = { passwordVisible = !passwordVisible }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FieldLabel("Confirm Password")
                    CleanTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = "Re-enter your password",
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        passwordVisible = confirmPasswordVisible,
                        onTogglePassword = { confirmPasswordVisible = !confirmPasswordVisible }
                    )

                    if (uiState is AuthUiState.Error) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = (uiState as AuthUiState.Error).message,
                            color = ErrorColor,
                            fontSize = 13.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel.register(
                                fullName.trim(),
                                email.trim(),
                                password,
                                confirmPassword
                            )
                        },
                        enabled = uiState !is AuthUiState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkButton,
                            disabledContainerColor = DarkButton.copy(alpha = 0.5f)
                        )
                    ) {
                        if (uiState is AuthUiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "Create Account",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Already have an account?", color = SubtitleColor, fontSize = 14.sp)
                        TextButton(
                            onClick = onNavigateToLogin,
                            contentPadding = PaddingValues(start = 4.dp)
                        ) {
                            Text(
                                "Sign in",
                                color = OrangeAccent,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Shared Components (used by both screens) ─────────────────────────────────

@Composable
fun FieldLabel(text: String) {
    Text(
        text = "$text *",
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = LabelColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
    )
}

@Composable
fun CleanTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color(0xFFBDBDBD), fontSize = 14.sp) },
        singleLine = true,
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon = if (isPassword && onTogglePassword != null) {
            {
                TextButton(onClick = onTogglePassword) {
                    Text(
                        text = if (passwordVisible) "Hide" else "Show",
                        color = OrangeAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else null,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = LabelColor,
            unfocusedTextColor = LabelColor,
            focusedContainerColor = FieldBg,
            unfocusedContainerColor = FieldBg,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = OrangeAccent
        )
    )
}