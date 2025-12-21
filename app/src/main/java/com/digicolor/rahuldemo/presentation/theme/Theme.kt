package com.digicolor.rahuldemo.presentation.theme


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

private val LightColorScheme = lightColorScheme(
    primary = NavyBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3F2FD),
    onPrimaryContainer = DarkBlue,

    secondary = Color(0xFF0288D1),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB3E5FC),
    onSecondaryContainer = Color(0xFF01579B),

    tertiary = Color(0xFF5E35B1),
    onTertiary = Color.White,

    background = BackgroundPrimary,
    onBackground = TextPrimary,

    surface = SurfaceColor,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundSecondary,
    onSurfaceVariant = TextSecondary,

    error = ErrorRed,
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C),

    outline = DividerColor,
    outlineVariant = Color(0xFFF5F5F5),

    scrim = Color.Black.copy(alpha = 0.32f),

    inverseSurface = Color(0xFF2C2C2C),
    inverseOnSurface = Color(0xFFF5F5F5),
    inversePrimary = Color(0xFF64B5F6),

    surfaceTint = NavyBlue,
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF64B5F6),
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF004A77),
    onPrimaryContainer = Color(0xFFD1E4FF),

    secondary = Color(0xFF81D4FA),
    onSecondary = Color(0xFF003D5C),
    secondaryContainer = Color(0xFF005782),
    onSecondaryContainer = Color(0xFFBBE9FF),

    tertiary = Color(0xFFB39DDB),
    onTertiary = Color(0xFF3700B3),

    background = Color(0xFF121212),
    onBackground = Color(0xFFE1E1E1),

    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE1E1E1),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFB0B0B0),

    error = Color(0xFFEF5350),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    outline = Color(0xFF3E3E3E),
    outlineVariant = Color(0xFF2C2C2C),

    scrim = Color.Black.copy(alpha = 0.5f),

    inverseSurface = Color(0xFFE1E1E1),
    inverseOnSurface = Color(0xFF1E1E1E),
    inversePrimary = NavyBlue,

    surfaceTint = Color(0xFF64B5F6),
)



// Extension properties for custom colors that aren't in Material Theme
val MaterialTheme.customColors: CustomColors
    @Composable
    get() = if (isSystemInDarkTheme()) DarkCustomColors else LightCustomColors

data class CustomColors(
    val positiveValue: Color,
    val negativeValue: Color,
    val neutralValue: Color,
    val chipBackground: Color,
    val chipText: Color,
    val overlayBackground: Color,
)

private val LightCustomColors = CustomColors(
    positiveValue = SuccessGreen,
    negativeValue = ErrorRed,
    neutralValue = NeutralValue,
    chipBackground = ChipBackground,
    chipText = ChipText,
    overlayBackground = OverlayBackground,
)

private val DarkCustomColors = CustomColors(
    positiveValue = SuccessGreen, // Keep green as is
    negativeValue = ErrorRed,      // Keep red as is
    neutralValue = Color(0xFF9E9E9E),
    chipBackground = Color(0xFF3E3E3E),
    chipText = Color(0xFFB0B0B0),
    overlayBackground = Color(0xE61E1E1E), // Semi-transparent dark
)

@Composable
fun RahulDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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