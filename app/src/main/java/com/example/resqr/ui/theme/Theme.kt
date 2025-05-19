package com.example.resqr.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Define PlotPot's color palette
val Purple = Color(0xFF6B3FA0) // Primary purple
val Teal = Color(0xFF00C4B4)   // Secondary teal
val Yellow = Color(0xFFFFC107)  // Tertiary yellow
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF121212)
val GrayBackground = Color(0xFFFAFAFA)
val DarkGrayText = Color(0xFF333333)

// Gradient for Sign In / Sign Up
val GradientStart = Color(0xFFFF512F) // Red-Orange
val GradientEnd = Color(0xFFDD2476)   // Deep Magenta

// Victim Theme (Requester)
val VictimPrimary = Color(0xFFD32F2F)    // Strong Red
val VictimSecondary = Color(0xFFFFCDD2)   // Light Red
val VictimWarning = Color(0xFFFFC107)     // Amber

// Responder Theme
val ResponderPrimary = Color(0xFF1976D2)  // Strong Blue
val ResponderSecondary = Color(0xFFBBDEFB) // Light Blue
val ResponderSuccess = Color(0xFF4CAF50)   // Green
val AlertActive = Color(0xFFF44336)        // Alert Red

// Lock Screen Overlay
val OverlayBlackTranslucent = 0x80000000 // Black 50% opacity

val SoftWhite = Color(0xFFF5F5F5) // Light mode background/surface
val DarkGray = Color(0xFF121212)  // Dark mode background/surface
val TextDark = Color(0xFF1C1B1F)  // Text color for light mode
val TextLight = Color(0xFFE0E0E0) // Text color for dark mode

// Dark Color Scheme for PlotPot
private val DarkColorScheme = darkColorScheme(
    primary = VictimPrimary,          // Purple for primary elements (e.g., buttons)
    secondary = VictimSecondary,          // Teal for secondary elements
    tertiary = VictimWarning,         // Yellow for highlights
    background = White,     // Dark gray background
    surface = DarkGray,        // Dark gray surface (e.g., cards)
    onPrimary = Color.White,   // White text/icons on primary color
    onSecondary = Color.White, // White text/icons on secondary color
    onTertiary = Color.Black,  // Black text/icons on yellow for contrast
    onBackground = TextLight,  // Light gray text on dark background
    onSurface = TextLight      // Light gray text on dark surface
)

// Light Color Scheme for PlotPot
private val LightColorScheme = lightColorScheme(
    primary = Purple,          // Purple for primary elements
    secondary = Teal,          // Teal for secondary elements
    tertiary = Yellow,         // Yellow for highlights
    background = White,    // Soft white background
    surface = SoftWhite,       // Soft white surface
    onPrimary = Color.White,   // White text/icons on primary color
    onSecondary = Color.White, // White text/icons on secondary color
    onTertiary = Color.Black,  // Black text/icons on yellow for contrast
    onBackground = TextDark,   // Dark text on light background
    onSurface = TextDark       // Dark text on light surface
)

@Composable
fun ResQRTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}