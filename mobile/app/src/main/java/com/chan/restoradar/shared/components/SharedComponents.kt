package com.chan.restoradar.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.chan.restoradar.shared.data.Restaurant
import com.chan.restoradar.shared.theme.*

// ── Restaurant Card ───────────────────────────────────────────────────────────

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onFavoriteToggle: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(BorderColor)
            ) {
                if (!restaurant.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = restaurant.imageUrl,
                        contentDescription = restaurant.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                if (onFavoriteToggle != null) {
                    IconButton(
                        onClick = onFavoriteToggle,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(32.dp)
                            .background(White.copy(alpha = 0.85f), CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Brand else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = restaurant.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (!restaurant.priceRange.isNullOrBlank()) {
                        Text(
                            text = restaurant.priceRange,
                            fontSize = 13.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!restaurant.cuisineType.isNullOrBlank()) {
                        Text(restaurant.cuisineType, fontSize = 12.sp, color = TextMuted)
                        Text(" · ", fontSize = 12.sp, color = TextMuted)
                    }
                    if (restaurant.averageRating != null) {
                        Icon(Icons.Filled.Star, null, tint = StarYellow, modifier = Modifier.size(13.dp))
                        Text(" %.1f".format(restaurant.averageRating), fontSize = 12.sp, color = TextMuted)
                        if (restaurant.reviewCount != null) {
                            Text(" (${restaurant.reviewCount})", fontSize = 12.sp, color = TextMuted)
                        }
                    }
                }
                if (!restaurant.address.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = restaurant.address,
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// ── Star Rating Input (tappable) ──────────────────────────────────────────────

@Composable
fun StarRatingInput(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        (1..5).forEach { star ->
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Star $star",
                tint = if (star <= rating) StarYellow else BorderColor,
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onRatingChange(star) }
                    .padding(2.dp)
            )
        }
    }
}

// ── Star Rating Display (read-only) ──────────────────────────────────────────

@Composable
fun StarRatingDisplay(rating: Double, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        (1..5).forEach { star ->
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (star <= rating) StarYellow else BorderColor,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "%.1f".format(rating), fontSize = 13.sp, color = TextMuted)
    }
}

// ── Filter Chip ───────────────────────────────────────────────────────────────

@Composable
fun RRFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg     = if (selected) TextPrimary else White
    val text   = if (selected) White       else TextPrimary
    val border = if (selected) TextPrimary else BorderColor

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = text,
            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

// ── Section Label ─────────────────────────────────────────────────────────────

@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = TextPrimary,
        modifier = modifier
    )
}