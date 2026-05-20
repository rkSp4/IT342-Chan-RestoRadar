package com.chan.restoradar.feature.restaurant

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.chan.restoradar.shared.components.RestaurantCard
import com.chan.restoradar.shared.data.Restaurant
import com.chan.restoradar.shared.network.RestoRadarApi
import com.chan.restoradar.shared.network.RetrofitClient
import com.chan.restoradar.shared.theme.*
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(onRestaurantClick: (String) -> Unit) {
    val context = LocalContext.current
    val api     = remember { RetrofitClient.createService(context, RestoRadarApi::class.java) }
    val scope   = rememberCoroutineScope()

    var userLocation   by remember { mutableStateOf<GeoPoint?>(null) }
    var nearbyList     by remember { mutableStateOf<List<Restaurant>>(emptyList()) }
    var isLoading      by remember { mutableStateOf(false) }
    var locationDenied by remember { mutableStateOf(false) }
    val mapViewRef     = remember { mutableStateOf<MapView?>(null) }

    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
    }

    fun loadNearby(lat: Double, lng: Double) {
        scope.launch {
            isLoading = true
            try {
                val resp = api.getNearbyRestaurants(lat, lng, 5.0)
                if (resp.isSuccessful) nearbyList = resp.body() ?: emptyList()
            } catch (_: Exception) {}
            isLoading = false
        }
    }

    fun centerMap(geoPoint: GeoPoint) {
        mapViewRef.value?.controller?.apply {
            setZoom(15.0)
            setCenter(geoPoint)
        }
    }

    fun placeMarkers(restaurants: List<Restaurant>, map: MapView, userPoint: GeoPoint?) {
        map.overlays.clear()
        userPoint?.let {
            val m = Marker(map)
            m.position = it
            m.title = "You are here"
            m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            map.overlays.add(m)
        }
        restaurants.forEach { r ->
            if (r.latitude != null && r.longitude != null) {
                val m = Marker(map)
                m.position = GeoPoint(r.latitude, r.longitude)
                m.title = r.name
                m.snippet = r.cuisineType ?: ""
                m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                map.overlays.add(m)
            }
        }
        map.invalidate()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            fusedClient.lastLocation.addOnSuccessListener { loc: Location? ->
                loc?.let {
                    val gp = GeoPoint(it.latitude, it.longitude)
                    userLocation = gp
                    centerMap(gp)
                    mapViewRef.value?.let { mv -> placeMarkers(nearbyList, mv, gp) }
                    loadNearby(it.latitude, it.longitude)
                }
            }
        } else {
            locationDenied = true
        }
    }

    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            fusedClient.lastLocation.addOnSuccessListener { loc: Location? ->
                loc?.let {
                    val gp = GeoPoint(it.latitude, it.longitude)
                    userLocation = gp
                    loadNearby(it.latitude, it.longitude)
                }
            }
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(nearbyList) {
        mapViewRef.value?.let { placeMarkers(nearbyList, it, userLocation) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Map View", fontWeight = FontWeight.SemiBold, fontSize = 18.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White,
                    titleContentColor = TextPrimary
                ),
                actions = {
                    IconButton(onClick = {
                        userLocation?.let { centerMap(it) }
                            ?: permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }) {
                        Icon(Icons.Filled.MyLocation, "My location", tint = Brand)
                    }
                }
            )
        },
        containerColor = BackgroundPage
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Box(Modifier.fillMaxWidth().weight(0.55f)) {
                AndroidView(
                    factory = { ctx ->
                        MapView(ctx).apply {
                            setTileSource(TileSourceFactory.MAPNIK)
                            setMultiTouchControls(true)
                            controller.setZoom(14.0)
                            controller.setCenter(GeoPoint(14.5995, 120.9842))
                            mapViewRef.value = this
                            userLocation?.let { gp ->
                                controller.setCenter(gp)
                                placeMarkers(nearbyList, this, gp)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
                if (isLoading) {
                    CircularProgressIndicator(color = Brand, modifier = Modifier.align(Alignment.Center))
                }
                if (locationDenied) {
                    Surface(
                        modifier = Modifier.align(Alignment.TopCenter).padding(12.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = ErrorRed
                    ) {
                        Text(
                            "Location permission denied",
                            color = White,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Column(
                Modifier.fillMaxWidth().weight(0.45f).background(BackgroundPage)
            ) {
                Row(
                    Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Nearby Restaurants", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextPrimary)
                    Spacer(Modifier.width(8.dp))
                    Text("(${nearbyList.size})", fontSize = 13.sp, color = TextMuted)
                }
                LazyColumn(contentPadding = PaddingValues(bottom = 8.dp)) {
                    if (nearbyList.isEmpty() && !isLoading) {
                        item {
                            Box(Modifier.fillMaxWidth().padding(24.dp), Alignment.Center) {
                                Text("No restaurants found nearby.", color = TextMuted, fontSize = 14.sp)
                            }
                        }
                    }
                    items(nearbyList) { r ->
                        RestaurantCard(
                            restaurant = r,
                            onClick = { onRestaurantClick(r.id) },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}