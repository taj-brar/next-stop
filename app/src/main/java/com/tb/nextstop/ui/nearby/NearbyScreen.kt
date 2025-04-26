package com.tb.nextstop.ui.nearby

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tb.nextstop.R
import com.tb.nextstop.ui.shared.ErrorScreen
import com.tb.nextstop.ui.shared.LoadingScreen
import com.tb.nextstop.ui.shared.StopModalOption
import com.tb.nextstop.ui.shared.StopsList


@Composable
fun NearbyScreen(
    onStopClicked: (Int) -> Unit,
    nearbyScreenViewModel: NearbyScreenViewModel = viewModel(factory = NearbyScreenViewModel.Factory)
) {
    when (val stopsUIState = nearbyScreenViewModel.stopsUIState) {
        is StopsUIState.Success -> StopsList(
            onStopClicked = onStopClicked,
            stops = stopsUIState.stops,
            routesMap = stopsUIState.routesMap,
            featuresMap = stopsUIState.featuresMap,
            modifier = Modifier.fillMaxSize(),
            stopModalOptions = listOf(
                StopModalOption(
                    iconId = R.drawable.saved_black,
                    text = "Add stop to saved",
                    onClick = { nearbyScreenViewModel.saveNearbyStop(it) }
                )
            )
        )

        is StopsUIState.Error -> ErrorScreen()
        is StopsUIState.Loading -> LoadingScreen()
    }
}