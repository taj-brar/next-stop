package com.tb.nextstop.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tb.nextstop.ui.theme.NextStopTheme


@Composable
fun MapScreen() {
    OpenStreetMapComposable()
}

@Preview
@Composable
fun MapScreenPreview() {
    NextStopTheme {
        MapScreen()
    }
}