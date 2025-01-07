package com.tb.nextstop.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tb.nextstop.R
import com.tb.nextstop.data.BusFeatures
import com.tb.nextstop.data.LiveRoute
import com.tb.nextstop.data.LiveTripScheduledStop
import com.tb.nextstop.data.dummyBusFeatures
import com.tb.nextstop.data.dummyBusId
import com.tb.nextstop.data.dummyLiveRoute
import com.tb.nextstop.data.dummyLiveScheduledStops
import com.tb.nextstop.ui.theme.NextStopTheme
import kotlinx.serialization.json.JsonPrimitive

@Composable
fun LiveTripScreen(
    liveTripViewModel: LiveTripViewModel = viewModel(factory = LiveTripViewModel.Factory),
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        while (true) {
            liveTripViewModel.getLiveScheduledStops()
            kotlinx.coroutines.delay(LiveTripViewModel.LIVE_REFRESH_DELAY)
        }
    }

    when (val liveTripUIState = liveTripViewModel.liveTripUIState) {
        is LiveTripUIState.Success -> LiveTripSuccessScreen(
            busId = liveTripUIState.busId,
            route = liveTripUIState.route,
            busFeatures = liveTripUIState.busFeatures,
            liveScheduledStops = liveTripUIState.liveScheduledStops,
            modifier = modifier.fillMaxSize()
        )

        is LiveTripUIState.Error -> ErrorScreen()
        is LiveTripUIState.Loading -> LoadingScreen()
    }
}

@Composable
fun LiveTripSuccessScreen(
    busId: String,
    route: LiveRoute,
    busFeatures: BusFeatures,
    liveScheduledStops: List<LiveTripScheduledStop>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LiveTripHeader(
            busId = busId,
            route = route,
            busFeatures = busFeatures,
            modifier = Modifier
        )
        LiveTripBody(
            liveScheduledStops = liveScheduledStops,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun LiveTripHeader(
    busId: String,
    route: LiveRoute,
    busFeatures: BusFeatures,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(
            modifier = Modifier.weight(4f)
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BusRouteIcon(JsonPrimitive(route.badgeLabel))
                Text(text = route.label)
            }
            Text(text = "#$busId")
        }
        Row(
            modifier = modifier.weight(1f)
        ) {
            if (busFeatures.bikeRack == true) {
                BusFeatureIcon(BusFeature.BIKE_RACK)
            }
            if (busFeatures.wifi == true) {
                BusFeatureIcon(BusFeature.WIFI)
            }
        }
    }
}

@Composable
fun LiveTripBody(
    liveScheduledStops: List<LiveTripScheduledStop>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        items(liveScheduledStops) { liveScheduledStop ->
            LiveScheduledStopRow(
                liveScheduledStop = liveScheduledStop,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun LiveScheduledStopRow(
    liveScheduledStop: LiveTripScheduledStop,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LiveStopIcon(
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = liveScheduledStop.name,
            modifier = Modifier.weight(7f)
        )
        Text(
            text = liveScheduledStop.estimatedTime,
            modifier = Modifier.weight(2f)
        )
    }
}

@Composable
fun LiveStopIcon(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.paststop),
        contentDescription = "Live progress",
        modifier = modifier
    )
}

@Preview
@Composable
fun LiveTripSuccessScreenPreview() {
    NextStopTheme {
        LiveTripSuccessScreen(dummyBusId, dummyLiveRoute, dummyBusFeatures, dummyLiveScheduledStops)
    }
}