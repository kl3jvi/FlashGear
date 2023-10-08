package com.kl3jvi.yonda.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val LightColors = lightColorScheme(
    primary = flash_gear_theme_light_primary,
    onPrimary = flash_gear_theme_light_onPrimary,
    primaryContainer = flash_gear_theme_light_primaryContainer,
    onPrimaryContainer = flash_gear_theme_light_onPrimaryContainer,
    secondary = flash_gear_theme_light_secondary,
    onSecondary = flash_gear_theme_light_onSecondary,
    secondaryContainer = flash_gear_theme_light_secondaryContainer,
    onSecondaryContainer = flash_gear_theme_light_onSecondaryContainer,
    tertiary = flash_gear_theme_light_tertiary,
    onTertiary = flash_gear_theme_light_onTertiary,
    tertiaryContainer = flash_gear_theme_light_tertiaryContainer,
    onTertiaryContainer = flash_gear_theme_light_onTertiaryContainer,
    error = flash_gear_theme_light_error,
    errorContainer = flash_gear_theme_light_errorContainer,
    onError = flash_gear_theme_light_onError,
    onErrorContainer = flash_gear_theme_light_onErrorContainer,
    background = flash_gear_theme_light_background,
    onBackground = flash_gear_theme_light_onBackground,
    surface = flash_gear_theme_light_surface,
    onSurface = flash_gear_theme_light_onSurface,
    surfaceVariant = flash_gear_theme_light_surfaceVariant,
    onSurfaceVariant = flash_gear_theme_light_onSurfaceVariant,
    outline = flash_gear_theme_light_outline,
    inverseOnSurface = flash_gear_theme_light_inverseOnSurface,
    inverseSurface = flash_gear_theme_light_inverseSurface,
    inversePrimary = flash_gear_theme_light_inversePrimary,
    surfaceTint = flash_gear_theme_light_surfaceTint,
)

val DarkColors = darkColorScheme(
    primary = flash_gear_theme_dark_primary,
    onPrimary = flash_gear_theme_dark_onPrimary,
    primaryContainer = flash_gear_theme_dark_primaryContainer,
    onPrimaryContainer = flash_gear_theme_dark_onPrimaryContainer,
    secondary = flash_gear_theme_dark_secondary,
    onSecondary = flash_gear_theme_dark_onSecondary,
    secondaryContainer = flash_gear_theme_dark_secondaryContainer,
    onSecondaryContainer = flash_gear_theme_dark_onSecondaryContainer,
    tertiary = flash_gear_theme_dark_tertiary,
    onTertiary = flash_gear_theme_dark_onTertiary,
    tertiaryContainer = flash_gear_theme_dark_tertiaryContainer,
    onTertiaryContainer = flash_gear_theme_dark_onTertiaryContainer,
    error = flash_gear_theme_dark_error,
    errorContainer = flash_gear_theme_dark_errorContainer,
    onError = flash_gear_theme_dark_onError,
    onErrorContainer = flash_gear_theme_dark_onErrorContainer,
    background = flash_gear_theme_dark_background,
    onBackground = flash_gear_theme_dark_onBackground,
    surface = flash_gear_theme_dark_surface,
    onSurface = flash_gear_theme_dark_onSurface,
    surfaceVariant = flash_gear_theme_dark_surfaceVariant,
    onSurfaceVariant = flash_gear_theme_dark_onSurfaceVariant,
    outline = flash_gear_theme_dark_outline,
    inverseOnSurface = flash_gear_theme_dark_inverseOnSurface,
    inverseSurface = flash_gear_theme_dark_inverseSurface,
    inversePrimary = flash_gear_theme_dark_inversePrimary,
    surfaceTint = flash_gear_theme_dark_surfaceTint,
)

@Composable
fun FlashGearTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
