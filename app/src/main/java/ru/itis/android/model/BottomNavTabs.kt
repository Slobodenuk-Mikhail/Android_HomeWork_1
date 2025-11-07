package ru.itis.android.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavTabs(
    val route: Any,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String? = null
)