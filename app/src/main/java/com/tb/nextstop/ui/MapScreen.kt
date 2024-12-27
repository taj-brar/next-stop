package com.tb.nextstop.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tb.nextstop.ui.theme.NextStopTheme


@Composable
fun MapScreen() {
    Text(
        text = "Map",
    )
}

@Preview
@Composable
fun MapScreenPreview() {
    NextStopTheme {
        MapScreen()
    }
}