package com.tb.nextstop.ui.saved

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tb.nextstop.ui.shared.ErrorScreen
import com.tb.nextstop.ui.shared.LoadingScreen
import com.tb.nextstop.ui.shared.StopsList
import com.tb.nextstop.ui.theme.NextStopTheme


@Composable
fun SavedScreen(
    onStopClicked: (Int) -> Unit = {},
    savedScreenViewModel: SavedScreenViewModel = viewModel(factory = SavedScreenViewModel.Factory)
) {
    when (val savedStopsUIState = savedScreenViewModel.savedStopsUIState) {
        is SavedStopsUIState.Success -> StopsList(
            onStopClicked = onStopClicked,
            stops = savedStopsUIState.stops,
            routesMap = savedStopsUIState.routesMap,
            featuresMap = savedStopsUIState.featuresMap,
            modifier = Modifier.fillMaxSize()
        )

        is SavedStopsUIState.Error -> ErrorScreen()
        is SavedStopsUIState.Loading -> LoadingScreen()
    }
}

@Preview
@Composable
fun SavedScreenPreview() {
    NextStopTheme {
        SavedScreen()
    }
}