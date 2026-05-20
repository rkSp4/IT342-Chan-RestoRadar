package com.chan.restoradar.feature.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chan.restoradar.shared.data.UpdateUserRequest
import com.chan.restoradar.shared.data.UserProfile
import com.chan.restoradar.shared.network.RestoRadarApi
import com.chan.restoradar.shared.network.RetrofitClient
import com.chan.restoradar.shared.network.SessionStore
import com.chan.restoradar.shared.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onSignOut: () -> Unit) {
    val context = LocalContext.current
    val api     = remember { RetrofitClient.createService(context, RestoRadarApi::class.java) }
    val scope   = rememberCoroutineScope()

    var profile      by remember { mutableStateOf<UserProfile?>(SessionStore.getUser(context)) }
    var isEditing    by remember { mutableStateOf(false) }
    var editName     by remember { mutableStateOf("") }
    var editEmail    by remember { mutableStateOf("") }
    var isSaving     by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val snackState   = remember { SnackbarHostState() }

    fun loadProfile() {
        val userId = SessionStore.getUserId(context) ?: return
        scope.launch {
            try {
                val resp = api.getUser(userId)
                if (resp.isSuccessful) profile = resp.body()
            } catch (_: Exception) {}
        }
    }

    fun saveProfile() {
        val userId = SessionStore.getUserId(context) ?: return
        scope.launch {
            isSaving = true
            errorMessage = null
            try {
                val resp = api.updateUser(userId, UpdateUserRequest(editName.trim(), editEmail.trim()))
                if (resp.isSuccessful) {
                    profile = resp.body()
                    isEditing = false
                    snackState.showSnackbar("Profile updated!")
                } else {
                    errorMessage = "Failed to update profile."
                }
            } catch (_: Exception) {
                errorMessage = "Failed to update profile."
            }
            isSaving = false
        }
    }

    fun signOut() {
        scope.launch {
            try { api.logout() } catch (_: Exception) {}
            SessionStore.clear(context)
            onSignOut()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackState) },
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.SemiBold, fontSize = 18.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White,
                    titleContentColor = TextPrimary
                ),
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = {
                            editName  = profile?.fullName  ?: ""
                            editEmail = profile?.email ?: ""
                            isEditing = true
                        }) {
                            Icon(Icons.Filled.Edit, "Edit", tint = Brand)
                        }
                    }
                }
            )
        },
        containerColor = BackgroundPage
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier.size(80.dp).clip(CircleShape).background(BrandLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Person, null, tint = Brand, modifier = Modifier.size(44.dp))
            }

            Text(profile?.fullName ?: "—", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
            Text(profile?.email ?: "—", fontSize = 14.sp, color = TextMuted)

            profile?.role?.let { role ->
                Surface(shape = RoundedCornerShape(50), color = BrandLight) {
                    Text(
                        text = role,
                        color = Brand,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            if (isEditing) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Edit Profile", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextPrimary)
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Brand,
                                unfocusedBorderColor = BorderColor
                            )
                        )
                        OutlinedTextField(
                            value = editEmail,
                            onValueChange = { editEmail = it },
                            label = { Text("Email") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Brand,
                                unfocusedBorderColor = BorderColor
                            )
                        )
                        errorMessage?.let { Text(it, color = ErrorRed, fontSize = 13.sp) }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedButton(
                                onClick = { isEditing = false },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) { Text("Cancel", color = TextPrimary) }
                            Button(
                                onClick = ::saveProfile,
                                enabled = !isSaving,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Brand)
                            ) {
                                if (isSaving) CircularProgressIndicator(color = White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                                else Text("Save", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = ::signOut,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed)
            ) {
                Text("Sign Out", fontWeight = FontWeight.SemiBold, color = ErrorRed)
            }
        }
    }
}