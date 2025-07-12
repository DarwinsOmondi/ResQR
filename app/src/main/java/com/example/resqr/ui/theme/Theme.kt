package com.example.resqr.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


// Dark Color Scheme for PlotPot
val CustomDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF6B6B),           // brighter red for better visibility
    onPrimary = Color.White,              // readable text on red

    background = Color(0xFF121212),       // deep dark, AMOLED-friendly
    onBackground = Color(0xFFECECEC),     // soft bright white

    surface = Color(0xFF1C1C1C),          // subtle lift over background
    onSurface = Color(0xFFE0E0E0),        // readable text/icons

    secondary = Color(0xFFFFB74D),        // softened orange
    onSecondary = Color.Black,

    tertiary = Color(0xFF64B5F6),         // light blue (kept from yours)
    onTertiary = Color.Black,

    outline = Color(0xFF3A3A3A)           // slightly more pronounced for separation
)


// Light Color Scheme for PlotPot
val CustomLightColorScheme = lightColorScheme(
    primary = Color(0xFFFF5252),          // bold red with warmth
    onPrimary = Color.White,

    background = Color(0xFFFFFFFF),       // pure white for cleanliness
    onBackground = Color(0xFF1A1A1A),     // crisp black text

    surface = Color(0xFFF9F9F9),          // near-white for cards
    onSurface = Color(0xFF212121),

    secondary = Color(0xFFFF9800),        // vibrant orange (Material spec)
    onSecondary = Color.Black,

    tertiary = Color(0xFF1565C0),         // stronger blue than before
    onTertiary = Color.White,

    outline = Color(0xFFCCCCCC)           // refined light border for subtle separation
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
    val context = LocalContext.current

    val colorScheme = remember(darkTheme, dynamicColor) {
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> CustomDarkColorScheme
            else -> CustomLightColorScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
