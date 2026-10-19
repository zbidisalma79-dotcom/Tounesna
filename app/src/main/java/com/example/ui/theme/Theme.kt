package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = TunisianRedPrimary,
    onPrimary = Color.White,
    primaryContainer = TunisianRedContainerDark,
    onPrimaryContainer = Color(0xFFFFDAD8),
    secondary = SidiBouSaidBlueLight,
    onSecondary = Color.Black,
    secondaryContainer = SidiBouSaidBlueDark,
    onSecondaryContainer = Color.White,
    tertiary = JasmineGold,
    onTertiary = Color.Black,
    background = DarkBackground,
    onBackground = JasmineWhite,
    surface = DarkSurface,
    onSurface = JasmineWhite,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = TunisianRedPrimary,
    onPrimary = Color.White,
    primaryContainer = TunisianRedContainerLight,
    onPrimaryContainer = TunisianRedDark,
    secondary = SidiBouSaidBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD7E8F7),
    onSecondaryContainer = SidiBouSaidBlueDark,
    tertiary = JasmineGoldDark,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = Color(0xFF0F172A),
    surface = LightSurface,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = LightSurfaceElevated,
    onSurfaceVariant = Color(0xFF475569),
    outline = LightBorder
)

@Composable
fun TelfazetnaTheme(
    darkTheme: Boolean = true, // Default to rich cinema dark theme for TV live streaming
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
