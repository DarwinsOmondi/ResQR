package com.example.resqr.ui.theme

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


// Dark Color Scheme for PlotPot
val CustomDarkColorScheme = darkColorScheme(
    primary = Color(0xFFEF5350),          // red tone for buttons & highlights
    onPrimary = Color.White,              // for text/icons on primary
    background = Color.DarkGray,       // true dark background
    onBackground = Color(0xFFE0E0E0),     // slightly dimmed white for text
    surface = Color(0xFF1E1E1E),          // card/input backgrounds
    onSurface = Color(0xFFE0E0E0),        // input text & icons
    secondary = Color(0xFFFFA726),        // orange for icons/labels
    onSecondary = Color.Black,
    tertiary = Color(0xFF64B5F6),         // blue-ish for variety
    onTertiary = Color.Black,
    outline = Color(0xFF2C2C2C)           // for chip borders, etc.
)

// Light Color Scheme for PlotPot
val CustomLightColorScheme = lightColorScheme(
    primary = Color(0xFFEF5350),          // same red tone for buttons & highlights
    onPrimary = Color.White,              // for text/icons on primary
    background = Color(0xFFF5F5F5),       // soft light gray background
    onBackground = Color(0xFF212121),     // dark text on light bg
    surface = Color.White,                // cards/inputs
    onSurface = Color(0xFF212121),        // text/icons inside inputs
    secondary = Color(0xFFFFA726),        // orange accents (icons/labels)
    onSecondary = Color.Black,
    tertiary = Color(0xFF1976D2),         // blue for variety and buttons
    onTertiary = Color.White,
    outline = Color(0xFFBDBDBD)           // subtle border color
)


val CustomResponderTheme = darkColorScheme(
    primary = ResponderPrimary,
    secondary = ResponderSecondary,
    tertiary = ResponderSuccess,
    background = DarkGray,
    surface = DarkGray,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onSurface = AlertActive
)

@Composable
fun ResponderTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CustomResponderTheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun VictimTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) CustomDarkColorScheme else CustomLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // You can customize this if needed
        content = content
    )
}

@Composable
fun ResQRTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> CustomDarkColorScheme
        else -> CustomLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
