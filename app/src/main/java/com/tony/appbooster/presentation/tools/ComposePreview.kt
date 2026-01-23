package com.tony.appbooster.presentation.tools


import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tony.appbooster.presentation.ui.theme.AppBoosterTheme


@Preview(name = "Light Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
annotation class AppBasePreview


@Composable
fun LightPreview(
    content: @Composable () -> Unit
){
    AppBoosterTheme(){
        content()
    }
}

@Composable
fun DarkPreview(
    content: @Composable () -> Unit
){
    AppBoosterTheme (darkTheme = true){
        content()
    }
}