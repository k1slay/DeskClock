package com.k2.deskclock.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

@Composable
fun bgGradient() =
    listOf(
        MaterialTheme.colors.background.copy(alpha = 1.0F),
        MaterialTheme.colors.background.copy(alpha = 0.9F),
        MaterialTheme.colors.background.copy(alpha = 0.8F),
        MaterialTheme.colors.background.copy(alpha = 0.7F),
        MaterialTheme.colors.background.copy(alpha = 0.6F),
        MaterialTheme.colors.background.copy(alpha = 0.5F),
        MaterialTheme.colors.background.copy(alpha = 0.4F),
        MaterialTheme.colors.background.copy(alpha = 0.3F),
        MaterialTheme.colors.background.copy(alpha = 0.2F),
        MaterialTheme.colors.background.copy(alpha = 0.1F),
        MaterialTheme.colors.background.copy(alpha = 0.0F),
    )
