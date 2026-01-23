package com.tony.appbooster.presentation.screen.common.basescreen

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SystemAppearance(
    statusBarColor: Color,
    useLightStatusIcons: Boolean? = null
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = statusBarColor.toArgb()

            // isAppearanceLightStatusBars = true -> icone scure (per sfondo chiaro)
            // isAppearanceLightStatusBars = false -> icone chiare (per sfondo scuro)
            // La logica è: se lo sfondo è chiaro (luminanza > 0.5), isAppearanceLightStatusBars deve essere true.
            val isLightAppearance =
                useLightStatusIcons?.not() ?: (statusBarColor.luminance() > 0.5f)

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                isLightAppearance
        }
    }
}
