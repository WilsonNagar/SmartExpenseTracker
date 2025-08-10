package com.wilson.smartexpensetracker.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ThemeToggleSwitch(
    isDarkTheme: Boolean,
    onToggle: () -> Unit
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (isDarkTheme) 24.dp else 0.dp,
        label = "ThumbAnimation"
    )

    Box(
        modifier = Modifier
            .width(52.dp)
            .height(28.dp)
            .clip(RoundedCornerShape(50))
            .background(
                if (isDarkTheme) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.secondary
            )
            .clickable { onToggle() }
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        // Sun / Moon icons
        Icon(
            imageVector = Icons.Default.LightMode,
            contentDescription = null,
            tint = if (!isDarkTheme) Color.Gray else Color.Yellow,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(16.dp)
        )

        Icon(
            imageVector = Icons.Default.DarkMode,
            contentDescription = null,
            tint = if (isDarkTheme) Color.Gray else Color.White,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(16.dp)
        )

        // Moving thumb
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(20.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}
