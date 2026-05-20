package com.chan.restoradar.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chan.restoradar.shared.components.RRFilterChip
import com.chan.restoradar.shared.components.RestaurantCard
import com.chan.restoradar.shared.components.SectionLabel
import com.chan.restoradar.shared.data.FavoriteRequest
import com.chan.restoradar.shared.data.Restaurant
import com.chan.restoradar.shared.network.RestoRadarApi
import com.chan.restoradar.shared.network.RetrofitClient
import com.chan.restoradar.shared.network.SessionStore
import com.chan.restoradar.shared.theme.*
import kotlinx.coroutines.launch

private val CUISINE_TYPES = listOf("All", "Italian", "Japanese", "Mexican", "American", "Cafe", "Asian", "Steakhouse", "Vegetarian")
private val PRICE_RANGES  = listOf("All", "$", "$$", "$$$", "$$$$")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onRestaurantClick: (String) -> Unit) {
    val context = LocalContext.current
    val api     = remember { RetrofitClient.createService(context, RestoRadarApi::class.java) }
    val scope   = rememberCoroutineScope()

    var restaurants     by remember { mutableStateOf<List<Restaurant>>(emptyList()) }
    var isLoading       by remember { mutableStateOf(true) }
    var errorMessage    by remember { mutableStateOf<String?>(null) }
    var searchQuery     by remember { mutableStateOf("") }
    var selectedCuisine by remember { mutableStateOf("All") }
    var selectedPrice   by remember { mutableStateOf("All") }
    var favoriteIds     by remember { mutableStateOf<Set<String>>(emptySet()) }

    fun load() {
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                val cuisine = if (selectedCuisine == "All") null else selectedCuisine
                val price   = if (selectedPrice == "All") null else selectedPrice
                val resp = if (searchQuery.isBlank()) {
                    api.getRestaurants(cuisineType = cuisine, priceRange = price)
                } else {
                    api.searchRestaurants(searchQuery)
                }
                if (resp.isSuccessful) restaurants = resp.body() ?: emptyList()
                else errorMessage = "Failed to load restaurants. Please try again."
            } catch (e: Exception) {
                errorMessage = "Failed to load restaurants. Please try again."
            }
            isLoading = false
        }
    }

    fun loadFavorites() {
        val userId = SessionStore.getUserId(context) ?: return
        scope.launch {
            try {
                val resp = api.getFavorites(userId)
                if (resp.isSuccessful) {
                    favoriteIds = resp.body()?.map { it.restaurantId }?.toSet() ?: emptySet()
                }
            } catch (_: Exception) {}
        }
    }

    fun toggleFavorite(restaurantId: String) {
        scope.launch {
            try {
                if (restaurantId in favoriteIds) {
                    api.removeFavorite(restaurantId)
                    favoriteIds = favoriteIds - restaurantId
                } else {
                    api.addFavorite(FavoriteRequest(restaurantId))
                    favoriteIds = favoriteIds + restaurantId
                }
            } catch (_: Exception) {}
        }
    }

    LaunchedEffect(Unit) { load(); loadFavorites() }
    LaunchedEffect(selectedCuisine, selectedPrice) { if (searchQuery.isBlank()) load() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Discover Restaurants", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White,
                    titleContentColor = TextPrimary
                )
            )
        },
        containerColor = BackgroundPage
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        if (it.isBlank()) load()
                    },
                    placeholder = { Text("Search restaurants, cuisines...", color = TextMuted) },
                    leadingIcon = { Icon(Icons.Filled.Search, null, tint = TextMuted) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Brand,
                        unfocusedBorderColor = BorderColor,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { load() }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                SectionLabel("Cuisine Type", modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(Modifier.height(8.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(CUISINE_TYPES) { type ->
                        RRFilterChip(label = type, selected = selectedCuisine == type, onClick = { selectedCuisine = type })
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            item {
                SectionLabel("Price Range", modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(Modifier.height(8.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(PRICE_RANGES) { price ->
                        RRFilterChip(label = price, selected = selectedPrice == price, onClick = { selectedPrice = price })
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            when {
                isLoading -> item {
                    Box(Modifier.fillMaxWidth().padding(48.dp), Alignment.Center) {
                        CircularProgressIndicator(color = Brand)
                    }
                }
                errorMessage != null -> item {
                    Column(
                        Modifier.fillMaxWidth().padding(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(errorMessage!!, color = ErrorRed, fontSize = 14.sp)
                        Spacer(Modifier.height(12.dp))
                        TextButton(onClick = ::load) {
                            Text("Try again", color = TextPrimary, fontWeight = FontWeight.Medium)
                        }
                    }
                }
                restaurants.isEmpty() -> item {
                    Box(Modifier.fillMaxWidth().padding(48.dp), Alignment.Center) {
                        Text("No restaurants found.", color = TextMuted, fontSize = 14.sp)
                    }
                }
                else -> items(restaurants) { restaurant ->
                    RestaurantCard(
                        restaurant = restaurant,
                        isFavorite = restaurant.id in favoriteIds,
                        onFavoriteToggle = { toggleFavorite(restaurant.id) },
                        onClick = { onRestaurantClick(restaurant.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}