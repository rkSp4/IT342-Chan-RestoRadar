package com.chan.restoradar.feature.favorite

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chan.restoradar.shared.components.RestaurantCard
import com.chan.restoradar.shared.data.Favorite
import com.chan.restoradar.shared.network.RestoRadarApi
import com.chan.restoradar.shared.network.RetrofitClient
import com.chan.restoradar.shared.network.SessionStore
import com.chan.restoradar.shared.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(onRestaurantClick: (String) -> Unit) {
    val context = LocalContext.current
    val api     = remember { RetrofitClient.createService(context, RestoRadarApi::class.java) }
    val scope   = rememberCoroutineScope()

    var favorites    by remember { mutableStateOf<List<Favorite>>(emptyList()) }
    var isLoading    by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun load() {
        val userId = SessionStore.getUserId(context) ?: return
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                val resp = api.getFavorites(userId)
                if (resp.isSuccessful) favorites = resp.body() ?: emptyList()
                else errorMessage = "Failed to load favorites."
            } catch (_: Exception) {
                errorMessage = "Failed to load favorites."
            }
            isLoading = false
        }
    }

    fun removeFavorite(restaurantId: String) {
        scope.launch {
            try {
                api.removeFavorite(restaurantId)
                favorites = favorites.filter { it.restaurantId != restaurantId }
            } catch (_: Exception) {}
        }
    }

    LaunchedEffect(Unit) { load() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorites", fontWeight = FontWeight.SemiBold, fontSize = 18.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White,
                    titleContentColor = TextPrimary
                )
            )
        },
        containerColor = BackgroundPage
    ) { padding ->
        when {
            isLoading -> Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                CircularProgressIndicator(color = Brand)
            }
            errorMessage != null -> Column(
                Modifier.fillMaxSize().padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(errorMessage!!, color = ErrorRed, fontSize = 14.sp)
                Spacer(Modifier.height(12.dp))
                TextButton(onClick = ::load) {
                    Text("Try again", color = TextPrimary)
                }
            }
            favorites.isEmpty() -> Column(
                Modifier.fillMaxSize().padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Filled.FavoriteBorder, null, tint = BorderColor, modifier = Modifier.size(56.dp))
                Spacer(Modifier.height(12.dp))
                Text("No favorites yet.", fontWeight = FontWeight.Medium, fontSize = 15.sp, color = TextPrimary)
                Spacer(Modifier.height(4.dp))
                Text("Explore restaurants and save your favorites!", fontSize = 13.sp, color = TextMuted)
            }
            else -> LazyColumn(
                Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(favorites, key = { it.id }) { fav ->
                    fav.restaurant?.let { restaurant ->
                        RestaurantCard(
                            restaurant = restaurant,
                            isFavorite = true,
                            onFavoriteToggle = { removeFavorite(fav.restaurantId) },
                            onClick = { onRestaurantClick(fav.restaurantId) },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }
    }
}