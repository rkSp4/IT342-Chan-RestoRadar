package com.chan.restoradar.feature.review

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chan.restoradar.shared.components.StarRatingInput
import com.chan.restoradar.shared.data.ReviewRequest
import com.chan.restoradar.shared.network.RestoRadarApi
import com.chan.restoradar.shared.network.RetrofitClient
import com.chan.restoradar.shared.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewScreen(
    restaurantId: String,
    onBack: () -> Unit,
    onReviewSubmitted: () -> Unit
) {
    val context = LocalContext.current
    val api     = remember { RetrofitClient.createService(context, RestoRadarApi::class.java) }
    val scope   = rememberCoroutineScope()

    var rating       by remember { mutableIntStateOf(0) }
    var comment      by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val snackState   = remember { SnackbarHostState() }

    fun submit() {
        if (rating == 0) { errorMessage = "Please select a rating."; return }
        scope.launch {
            isSubmitting = true
            errorMessage = null
            try {
                val resp = api.addReview(restaurantId, ReviewRequest(rating, comment.trim()))
                if (resp.isSuccessful) {
                    snackState.showSnackbar("Review submitted!")
                    onReviewSubmitted()
                } else {
                    errorMessage = "Failed to submit review. Please try again."
                }
            } catch (_: Exception) {
                errorMessage = "Failed to submit review. Please try again."
            }
            isSubmitting = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackState) },
        topBar = {
            TopAppBar(
                title = { Text("Add Review", fontWeight = FontWeight.SemiBold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Your Rating", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextPrimary)
                    Spacer(Modifier.height(12.dp))
                    StarRatingInput(rating = rating, onRatingChange = { rating = it })
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = when (rating) {
                            1 -> "Poor"
                            2 -> "Fair"
                            3 -> "Good"
                            4 -> "Very Good"
                            5 -> "Excellent"
                            else -> "Tap a star to rate"
                        },
                        fontSize = 13.sp,
                        color = if (rating == 0) TextMuted else Brand
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Your Review", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextPrimary)
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        placeholder = { Text("Share your experience...", color = TextMuted) },
                        modifier = Modifier.fillMaxWidth().height(140.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Brand,
                            unfocusedBorderColor = BorderColor
                        )
                    )
                }
            }

            errorMessage?.let {
                Text(it, color = ErrorRed, fontSize = 13.sp)
            }

            Button(
                onClick = ::submit,
                enabled = !isSubmitting,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Brand)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Submit Review", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }
            }
        }
    }
}