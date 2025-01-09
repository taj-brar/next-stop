package com.tb.nextstop.ui.saved

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tb.nextstop.ui.theme.NextStopTheme


@Composable
fun SavedScreen() {
    Text(
        text = "Saved",
    )
}

@Preview
@Composable
fun SavedScreenPreview() {
    NextStopTheme {
        SavedScreen()
    }
}