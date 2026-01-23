package com.tony.appbooster.presentation.screen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tony.appbooster.presentation.ui.theme.paddingMedium
import com.tony.appbooster.presentation.ui.theme.paddingXXLarge

/**
 * A stylish and reusable loading indicator screen based on Material Design 3.
 *
 * @param modifier Modifier to be applied to the Column layout.
 * @param loadingText The text to display below the progress indicator.
 * @param progress Optional progress value for a linear progress indicator (0-100).
 * @param backgroundColor Background color of the loading screen.
 *
 */
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    loadingText: String = "Loading...",
    progress: Int? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    Column(
        modifier = modifier.fillMaxSize().background(color = backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (progress == null) {
            CircularProgressIndicator(
                modifier = Modifier.size(paddingXXLarge),
                strokeWidth = 4.dp
            )
        } else {
            LinearProgressIndicator(
                progress = { progress / 100f },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(paddingMedium))
        Text(
            text = loadingText,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingUIPreview() {
    MaterialTheme {
        LoadingScreen()
    }
}