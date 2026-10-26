package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Dark Luxury AirPods Palette (matching reference screenshot)
val DarkBackground = Color(0xFF060A17)
val DarkSurface = Color(0xFF0C1427)
val DarkSurfaceElevated = Color(0xFF131F3B)
val DarkCardBorder = Color(0xFF1E2E54)
val DarkCardBorderHighlight = Color(0xFF2E4376)

// Accent Colors
val ElectricBlue = Color(0xFF387BFF)
val ElectricBlueLight = Color(0xFF5D95FF)
val NeonCyan = Color(0xFF00E5FF)
val NeonPurple = Color(0xFF9333EA)
val NeonPurpleLight = Color(0xFFA855F7)
val NeonMagenta = Color(0xFFD946EF)
val NeonGreen = Color(0xFF10B981)
val WarningAmber = Color(0xFFF59E0B)
val ErrorRed = Color(0xFFEF4444)

// Text Colors
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

// Gradients
val PrimaryButtonGradient = Brush.horizontalGradient(
    colors = listOf(ElectricBlue, NeonPurpleLight)
)

val CyanBlueGradient = Brush.horizontalGradient(
    colors = listOf(NeonCyan, ElectricBlue)
)

val WaveGradient = Brush.horizontalGradient(
    colors = listOf(NeonPurpleLight, NeonCyan, ElectricBlue)
)

val DarkCardGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF111C36), Color(0xFF090E1E))
)

val SliderTrackGradient = Brush.horizontalGradient(
    colors = listOf(NeonCyan, ElectricBlue, NeonPurpleLight)
)
