package com.chan.restoradar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.chan.restoradar.feature.auth.LoginScreen
import com.chan.restoradar.feature.auth.RegisterScreen
import com.chan.restoradar.feature.favorite.FavoritesScreen
import com.chan.restoradar.feature.home.HomeScreen
import com.chan.restoradar.feature.restaurant.MapScreen
import com.chan.restoradar.feature.restaurant.RestaurantDetailScreen
import com.chan.restoradar.feature.review.AddReviewScreen
import com.chan.restoradar.feature.user.ProfileScreen
import com.chan.restoradar.shared.network.SessionStore
import com.chan.restoradar.shared.theme.*

// ── Routes ────────────────────────────────────────────────────────────────────

object Routes {
    const val LOGIN             = "login"
    const val REGISTER          = "register"
    const val HOME              = "home"
    const val MAP               = "map"
    const val FAVORITES         = "favorites"
    const val PROFILE           = "profile"
    const val RESTAURANT_DETAIL = "restaurant/{restaurantId}"
    const val ADD_REVIEW        = "add_review/{restaurantId}"

    fun restaurantDetail(id: String) = "restaurant/$id"
    fun addReview(id: String)        = "add_review/$id"
}

// ── Bottom nav items ──────────────────────────────────────────────────────────

data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)

val bottomNavItems = listOf(
    BottomNavItem(Routes.HOME,      "Explore",   Icons.Filled.Home),
    BottomNavItem(Routes.MAP,       "Map View",  Icons.Filled.Map),
    BottomNavItem(Routes.FAVORITES, "Favorites", Icons.Filled.Favorite),
    BottomNavItem(Routes.PROFILE,   "Profile",   Icons.Filled.Person)
)

// ── MainActivity ──────────────────────────────────────────────────────────────

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestoRadarTheme {
                RestoRadarApp()
            }
        }
    }
}

// ── Root composable ───────────────────────────────────────────────────────────

@Composable
fun RestoRadarApp() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val startDestination = if (SessionStore.isLoggedIn(context)) Routes.HOME else Routes.LOGIN

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) BottomNavBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = startDestination,
            modifier         = Modifier.padding(innerPadding)
        ) {
            // ── Auth ──────────────────────────────────────────────────────────
            composable(Routes.LOGIN) {
                // 1. Get the context so you can use SessionStore
                val context = LocalContext.current

                LoginScreen(
                    // 2. Capture the token and save it before navigating
                    onLoginSuccess = { token, fullName ->
                        SessionStore.saveToken(context, accessToken = token, refreshToken = "")
                        SessionStore.saveUser(context, fullName)
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Routes.REGISTER)
                    }
                )
            }

            composable(Routes.REGISTER) {
                // 1. Get the context so you can use SessionStore
                val context = LocalContext.current

                RegisterScreen(
                    // 2. Capture the token and save it before navigating
                    onRegisterSuccess = { token, fullName ->
                        SessionStore.saveToken(context, accessToken = token, refreshToken = "")
                        SessionStore.saveUser(context, fullName)
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            // ── Main tabs ─────────────────────────────────────────────────────
            composable(Routes.HOME) {
                HomeScreen(onRestaurantClick = { id -> navController.navigate(Routes.restaurantDetail(id)) })
            }
            composable(Routes.MAP) {
                MapScreen(onRestaurantClick = { id -> navController.navigate(Routes.restaurantDetail(id)) })
            }
            composable(Routes.FAVORITES) {
                FavoritesScreen(onRestaurantClick = { id -> navController.navigate(Routes.restaurantDetail(id)) })
            }
            composable(Routes.PROFILE) {
                ProfileScreen(onSignOut = {
                    navController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                })
            }

            // ── Detail screens ────────────────────────────────────────────────
            composable(
                route     = Routes.RESTAURANT_DETAIL,
                arguments = listOf(navArgument("restaurantId") { type = NavType.StringType })
            ) { backStackEntry ->
                val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: return@composable
                RestaurantDetailScreen(
                    restaurantId = restaurantId,
                    onBack       = { navController.popBackStack() },
                    onAddReview  = { id -> navController.navigate(Routes.addReview(id)) }
                )
            }
            composable(
                route     = Routes.ADD_REVIEW,
                arguments = listOf(navArgument("restaurantId") { type = NavType.StringType })
            ) { backStackEntry ->
                val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: return@composable
                AddReviewScreen(
                    restaurantId      = restaurantId,
                    onBack            = { navController.popBackStack() },
                    onReviewSubmitted = { navController.popBackStack() }
                )
            }
        }
    }
}

// ── Bottom Nav Bar ────────────────────────────────────────────────────────────

@Composable
fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDest = navBackStackEntry?.destination

    NavigationBar(containerColor = White, tonalElevation = 4.dp) {
        bottomNavItems.forEach { item ->
            val selected = currentDest?.hierarchy?.any { it.route == item.route } == true
            NavigationBarItem(
                selected = selected,
                onClick  = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                icon  = { Icon(item.icon, item.label) },
                label = { Text(item.label, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = Brand,
                    selectedTextColor   = Brand,
                    indicatorColor      = BrandLight,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                )
            )
        }
    }
}

// ── Theme wrapper (add to your existing Theme.kt if not already there) ────────

@Composable
fun RestoRadarTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary             = Brand,
        onPrimary           = White,
        primaryContainer    = BrandLight,
        onPrimaryContainer  = Brand,
        background          = BackgroundPage,
        onBackground        = TextPrimary,
        surface             = White,
        onSurface           = TextPrimary,
        outline             = BorderColor,
        error               = ErrorRed
    )
    MaterialTheme(colorScheme = colorScheme, content = content)
}