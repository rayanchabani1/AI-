package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.VioletSecondary

@Composable
fun AppIconView(
    iconUrl: String?,
    iconResName: String?,
    category: String,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        IndigoPrimary,
                        VioletSecondary
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!iconUrl.isNullOrBlank()) {
            AsyncImage(
                model = iconUrl,
                contentDescription = "App Icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size)
                    .clip(RoundedCornerShape(14.dp))
            )
        } else {
            val iconVector = getIconVector(iconResName, category)
            Icon(
                imageVector = iconVector,
                contentDescription = "App Category Icon",
                tint = Color.White,
                modifier = Modifier.size(size * 0.55f)
            )
        }
    }
}

fun getIconVector(iconResName: String?, category: String): ImageVector {
    return when (iconResName) {
        "ic_calc" -> Icons.Default.Calculate
        "ic_notes" -> Icons.Default.EditNote
        "ic_game" -> Icons.Default.Gamepad
        "ic_cleaner" -> Icons.Default.CleaningServices
        "ic_music" -> Icons.Default.MusicNote
        "ic_weather" -> Icons.Default.Cloud
        "ic_code" -> Icons.Default.Code
        "ic_tool" -> Icons.Default.Build
        else -> when (category) {
            "ألعاب", "Games" -> Icons.Default.Gamepad
            "أدوات", "Tools" -> Icons.Default.Build
            "إنتاجية", "Productivity" -> Icons.Default.EditNote
            "وسائط", "Media" -> Icons.Default.MusicNote
            "خدمات", "Services" -> Icons.Default.Widgets
            else -> Icons.Default.Android
        }
    }
}
