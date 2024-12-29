package com.tb.nextstop.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tb.nextstop.ui.theme.NextStopTheme


@Composable
fun MapScreen(
    nearbyScreenViewModel: NearbyScreenViewModel = viewModel(factory = NearbyScreenViewModel.Factory)
) {
    val stops = when (val stopsUIState = nearbyScreenViewModel.stopsUIState) {
        is StopsUIState.Success -> stopsUIState.stops
        else -> listOf()
    }
    OpenStreetMapComposable(
        stops = stops
    )
}

@Preview
@Composable
fun MapScreenPreview() {
    NextStopTheme {
        MapScreen()
    }
}