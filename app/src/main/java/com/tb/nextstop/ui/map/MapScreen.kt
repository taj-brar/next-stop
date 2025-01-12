package com.tb.nextstop.ui.map

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MapScreen(
    mapScreenViewModel: MapScreenViewModel = viewModel(factory = MapScreenViewModel.Factory)
) {
    val stops = when (val mapScreenUIState = mapScreenViewModel.mapScreenUIState) {
        is MapScreenUIState.Success -> mapScreenUIState.stops
        else -> listOf()
    }
    OpenStreetMapComposable(
        stops = stops
    )
}