package com.chan.restoradar.feature.restaurant

import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.chan.restoradar.shared.components.StarRatingDisplay
import com.chan.restoradar.shared.data.FavoriteRequest
import com.chan.restoradar.shared.data.Restaurant
import com.chan.restoradar.shared.data.Review
import com.chan.restoradar.shared.network.RestoRadarApi
import com.chan.restoradar.shared.network.RetrofitClient
import com.chan.restoradar.shared.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    restaurantId: String,
    onBack: () -> Unit,
    onAddReview: (String) -> Unit
) {
    val context = LocalContext.current
    val api     = remember { RetrofitClient.createService(context, RestoRadarApi::class.java) }
    val scope   = rememberCoroutineScope()

    var restaurant   by remember { mutableStateOf<Restaurant?>(null) }
    var reviews      by remember { mutableStateOf<List<Review>>(emptyList()) }
    var isLoading    by remember { mutableStateOf(true) }
    var isFavorite   by remember { mutableStateOf(false) }
    val snackState   = remember { SnackbarHostState() }

    fun load() {
        scope.launch {
            try {
                val r  = api.getRestaurant(restaurantId)
                val rv = api.getReviews(restaurantId)
                if (r.isSuccessful)  restaurant = r.body()
                if (rv.isSuccessful) reviews    = rv.body() ?: emptyList()
            } catch (_: Exception) {}
            isLoading = false
        }
    }

    fun toggleFavorite() {
        scope.launch {
            try {
                if (isFavorite) {
                    api.removeFavorite(restaurantId)
                    isFavorite = false
                    snackState.showSnackbar("Removed from favorites")
                } else {
                    api.addFavorite(FavoriteRequest(restaurantId))
                    isFavorite = true
                    snackState.showSnackbar("Added to favorites")
                }
            } catch (_: Exception) {}
        }
    }

    LaunchedEffect(restaurantId) { load() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackState) },
        topBar = {
            TopAppBar(
                title = { Text(restaurant?.name ?: "", fontWeight = FontWeight.SemiBold, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = ::toggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Brand else TextMuted
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddReview(restaurantId) },
                containerColor = Brand,
                contentColor = White
            ) {
                Icon(Icons.Filled.Edit, "Add Review")
            }
        },
        containerColor = BackgroundPage
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                CircularProgressIndicator(color = Brand)
            }
            return@Scaffold
        }

        val r = restaurant ?: return@Scaffold

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                Box(
                    Modifier.fillMaxWidth().height(200.dp).background(BorderColor)
                ) {
                    if (!r.imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = r.imageUrl,
                            contentDescription = r.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(r.name, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                        Spacer(Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (!r.cuisineType.isNullOrBlank()) {
                                AssistChip(onClick = {}, label = { Text(r.cuisineType, fontSize = 12.sp) },
                                    colors = AssistChipDefaults.assistChipColors(containerColor = BrandLight, labelColor = Brand))
                            }
                            if (!r.priceRange.isNullOrBlank()) {
                                AssistChip(onClick = {}, label = { Text(r.priceRange, fontSize = 12.sp) },
                                    colors = AssistChipDefaults.assistChipColors(containerColor = BackgroundPage, labelColor = TextMuted))
                            }
                        }
                        if (r.averageRating != null) {
                            Spacer(Modifier.height(8.dp))
                            StarRatingDisplay(r.averageRating)
                        }
                        if (!r.address.isNullOrBlank()) {
                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.LocationOn, null, tint = TextMuted, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(r.address, fontSize = 13.sp, color = TextMuted)
                            }
                        }
                        if (!r.phoneNumber.isNullOrBlank()) {
                            Spacer(Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Phone, null, tint = TextMuted, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(r.phoneNumber, fontSize = 13.sp, color = TextMuted)
                            }
                        }
                        if (!r.openingHours.isNullOrBlank()) {
                            Spacer(Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Schedule, null, tint = TextMuted, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(r.openingHours, fontSize = 13.sp, color = TextMuted)
                            }
                        }
                        if (!r.description.isNullOrBlank()) {
                            Spacer(Modifier.height(10.dp))
                            HorizontalDivider(color = BorderColor)
                            Spacer(Modifier.height(10.dp))
                            Text(r.description, fontSize = 14.sp, color = TextMuted, lineHeight = 20.sp)
                        }
                    }
                }
            }

            item {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Reviews", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextPrimary)
                    Text("${reviews.size} review${if (reviews.size != 1) "s" else ""}", fontSize = 13.sp, color = TextMuted)
                }
            }

            if (reviews.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), Alignment.Center) {
                        Text("No reviews yet. Be the first!", color = TextMuted, fontSize = 14.sp)
                    }
                }
            } else {
                items(reviews) { review ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 5.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(review.userName ?: "Anonymous", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = TextPrimary)
                                StarRatingDisplay(review.rating.toDouble())
                            }
                            if (!review.comment.isNullOrBlank()) {
                                Spacer(Modifier.height(6.dp))
                                Text(review.comment, fontSize = 13.sp, color = TextMuted, lineHeight = 19.sp)
                            }
                            if (!review.createdAt.isNullOrBlank()) {
                                Spacer(Modifier.height(4.dp))
                                Text(review.createdAt, fontSize = 11.sp, color = BorderColor)
                            }
                        }
                    }
                }
            }
        }
    }
}