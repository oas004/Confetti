package dev.johnoreilly.confetti.ui

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.squareup.kotlinpoet.*
import dev.johnoreilly.confetti.ui.component.BackgroundTheme
import dev.johnoreilly.confetti.ui.component.LocalBackgroundTheme
import java.io.File


/**
 * Light default theme color scheme
 */
@VisibleForTesting
val LightDefaultColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    primaryContainer = Purple90,
    onPrimaryContainer = Purple10,
    secondary = Orange40,
    onSecondary = Color.White,
    secondaryContainer = Purple95,
    onSecondaryContainer = Orange10,
    tertiary = Blue40,
    onTertiary = Color.White,
    tertiaryContainer = Blue90,
    onTertiaryContainer = Blue10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkPurpleGray99,
    onBackground = DarkPurpleGray10,
    surface = DarkPurpleGray99,
    onSurface = DarkPurpleGray10,
    surfaceVariant = PurpleGray90,
    onSurfaceVariant = PurpleGray30,
    outline = PurpleGray50
)

/**
 * Dark default theme color scheme
 */
@VisibleForTesting
val DarkDefaultColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Purple20,
    primaryContainer = Purple30,
    onPrimaryContainer = Purple90,
    secondary = Orange80,
    onSecondary = Orange20,
    secondaryContainer = Orange30,
    onSecondaryContainer = Orange90,
    tertiary = Blue80,
    onTertiary = Blue20,
    tertiaryContainer = Blue30,
    onTertiaryContainer = Blue90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkPurpleGray10,
    onBackground = DarkPurpleGray90,
    surface = DarkPurpleGray10,
    onSurface = DarkPurpleGray90,
    surfaceVariant = PurpleGray30,
    onSurfaceVariant = PurpleGray80,
    outline = PurpleGray60
)

/**
 * Light Android theme color scheme
 */
@VisibleForTesting
val LightAndroidColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    primaryContainer = Green90,
    onPrimaryContainer = Green10,
    secondary = DarkGreen40,
    onSecondary = Color.White,
    secondaryContainer = DarkGreen90,
    onSecondaryContainer = DarkGreen10,
    tertiary = Teal40,
    onTertiary = Color.White,
    tertiaryContainer = Teal90,
    onTertiaryContainer = Teal10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkGreenGray99,
    onBackground = DarkGreenGray10,
    surface = DarkGreenGray99,
    onSurface = DarkGreenGray10,
    surfaceVariant = GreenGray90,
    onSurfaceVariant = GreenGray30,
    outline = GreenGray50
)

/**
 * Dark Android theme color scheme
 */
@VisibleForTesting
val DarkAndroidColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = Green20,
    primaryContainer = Green30,
    onPrimaryContainer = Green90,
    secondary = DarkGreen80,
    onSecondary = DarkGreen20,
    secondaryContainer = DarkGreen30,
    onSecondaryContainer = DarkGreen90,
    tertiary = Teal80,
    onTertiary = Teal20,
    tertiaryContainer = Teal30,
    onTertiaryContainer = Teal90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkGreenGray10,
    onBackground = DarkGreenGray90,
    surface = DarkGreenGray10,
    onSurface = DarkGreenGray90,
    surfaceVariant = GreenGray30,
    onSurfaceVariant = GreenGray80,
    outline = GreenGray60
)

/**
 * Light default gradient colors
 */
val LightDefaultGradientColors = GradientColors(
    primary = Purple95,
    secondary = Orange95,
    tertiary = Blue95,
    neutral = DarkPurpleGray95
)


/**
 * Light Android background theme
 */
val LightAndroidBackgroundTheme = BackgroundTheme(color = DarkGreenGray95)

/**
 * Dark Android background theme
 */
val DarkAndroidBackgroundTheme = BackgroundTheme(color = Color.Black)

/**
 * Confetti theme.
 *
 * The order of precedence for the color scheme is: Dynamic color > Android theme > Default theme.
 * Dark theme is independent as all the aforementioned color schemes have light and dark versions.
 * The default theme color scheme is used by default.
 *
 * @param darkTheme Whether the theme should use a dark color scheme (follows system by default).
 * @param dynamicColor Whether the theme should use a dynamic color scheme (Android 12+ only).
 * @param androidTheme Whether the theme should use the Android theme color scheme.
 */
@Composable
fun ConfettiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    androidTheme: Boolean = false,
    disableDynamicTheming: Boolean = false,
    content: @Composable() () -> Unit
) {

    val context = LocalContext.current
    val colorScheme = if (androidTheme) {
        if (darkTheme) DarkAndroidColorScheme else LightAndroidColorScheme
    } else if (!disableDynamicTheming && supportsDynamicTheming()) {
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
    }

    generateDynamicColorSchemeFile(darkTheme, context)


    val defaultGradientColors = GradientColors()
    val gradientColors = if (androidTheme || (!disableDynamicTheming && supportsDynamicTheming())) {
        defaultGradientColors
    } else {
        if (darkTheme) defaultGradientColors else LightDefaultGradientColors
    }

    val defaultBackgroundTheme = BackgroundTheme(
        color = colorScheme.surface,
        tonalElevation = 2.dp
    )
    val backgroundTheme = if (androidTheme) {
        if (darkTheme) DarkAndroidBackgroundTheme else LightAndroidBackgroundTheme
    } else {
        defaultBackgroundTheme
    }

    CompositionLocalProvider(
        LocalGradientColors provides gradientColors,
        LocalBackgroundTheme provides backgroundTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = ConfettiTypography,
            content = content
        )
    }
}

private fun FileSpec.Builder.addCclorProperty(propertyName: String, color: Color): FileSpec.Builder {
    val colorInitializer = "Color(${color.red}f, ${color.green}f, ${color.blue}f, ${color.alpha}f)"
    val propertySpec = PropertySpec.builder(propertyName, Color::class, KModifier.INTERNAL)
        .initializer(colorInitializer)
        .build()
    return addProperty(propertySpec)
}



fun generateDynamicColorSchemeFile(darkTheme: Boolean, context: Context) {
    val colorScheme = if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)

    val file = FileSpec.builder("", "Colors")
        .addCclorProperty("md_theme_light_primary", colorScheme.primary)
        .addCclorProperty("md_theme_light_onPrimary", colorScheme.onPrimary)
        .addCclorProperty("md_theme_light_primaryContainer", colorScheme.primaryContainer)
        .addCclorProperty("md_theme_light_onPrimaryContainer", colorScheme.onPrimaryContainer)
        .addCclorProperty("md_theme_light_secondary", colorScheme.secondary)
        .addCclorProperty("md_theme_light_onSecondary", colorScheme.onSecondary)
        .addCclorProperty("md_theme_light_secondaryContainer", colorScheme.secondaryContainer)
        .addCclorProperty("md_theme_light_onSecondaryContainer", colorScheme.onSecondaryContainer)
        .addCclorProperty("md_theme_light_tertiary", colorScheme.tertiary)
        .addCclorProperty("md_theme_light_onTertiary", colorScheme.onTertiary)
        .addCclorProperty("md_theme_light_tertiaryContainer", colorScheme.tertiaryContainer)
        .addCclorProperty("md_theme_light_onTertiaryContainer", colorScheme.onTertiaryContainer)
        .addCclorProperty("md_theme_light_error", colorScheme.error)
        .addCclorProperty("md_theme_light_errorContainer", colorScheme.errorContainer)
        .addCclorProperty("md_theme_light_onError", colorScheme.onError)
        .addCclorProperty("md_theme_light_onErrorContainer", colorScheme.onErrorContainer)
        .addCclorProperty("md_theme_light_background", colorScheme.background)
        .addCclorProperty("md_theme_light_onBackground", colorScheme.onBackground)
        .addCclorProperty("md_theme_light_surface", colorScheme.surface)
        .addCclorProperty("md_theme_light_onSurface", colorScheme.onSurface)
        .addCclorProperty("md_theme_light_surfaceVariant", colorScheme.surfaceVariant)
        .addCclorProperty("md_theme_light_onSurfaceVariant", colorScheme.onSurfaceVariant)
        .addCclorProperty("md_theme_light_outline", colorScheme.outline)
        .addCclorProperty("md_theme_light_inverseOnSurface", colorScheme.inverseOnSurface)
        .addCclorProperty("md_theme_light_inverseSurface", colorScheme.inverseSurface)
        .addCclorProperty("md_theme_light_inversePrimary", colorScheme.inversePrimary)
        .addCclorProperty("md_theme_light_surfaceTint", colorScheme.surfaceTint)
        .build()

    context.getExternalFilesDir("")?.let {
        file.writeTo(it)
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
private fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
