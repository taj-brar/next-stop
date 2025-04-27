package com.tb.nextstop.ui.livetrip

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tb.nextstop.R
import com.tb.nextstop.data.BusFeatures
import com.tb.nextstop.data.LiveRoute
import com.tb.nextstop.data.LiveTripScheduledStop
import com.tb.nextstop.data.dummyBusFeatures
import com.tb.nextstop.data.dummyBusId
import com.tb.nextstop.data.dummyLiveRoute
import com.tb.nextstop.data.dummyLiveScheduledStops
import com.tb.nextstop.ui.shared.BusFeaturesRow
import com.tb.nextstop.ui.shared.BusRouteIcon
import com.tb.nextstop.ui.shared.ErrorScreen
import com.tb.nextstop.ui.shared.LoadingScreen
import com.tb.nextstop.ui.theme.NextStopTheme
import com.tb.nextstop.utils.StopTiming
import com.tb.nextstop.utils.getHrsMinsFromWPTLiveFormat
import com.tb.nextstop.utils.getStopTimingFromWPTLiveFormat
import com.tb.nextstop.utils.simplifyStopName
import kotlinx.coroutines.delay
import kotlinx.serialization.json.JsonPrimitive

@Composable
fun LiveTripScreen(
    modifier: Modifier = Modifier,
    liveTripViewModel: LiveTripViewModel = viewModel(factory = LiveTripViewModel.Factory),
) {
    LaunchedEffect(Unit) {
        while (true) {
            liveTripViewModel.getLiveScheduledStops()
            delay(LiveTripViewModel.LIVE_REFRESH_DELAY)
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
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(dimensionResource(R.dimen.padding_large)),
    ) {
        Row(
            modifier = Modifier.weight(4f),
            verticalAlignment = Alignment.Top
        ) {
            BusRouteIcon(route = JsonPrimitive(route.badgeLabel))
            Text(
                text = route.label,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
        ) {
            BusFeaturesRow(
                busFeatures = busFeatures,
                modifier = Modifier
            )
            Text(
                text = "#$busId",
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}

@Preview
@Composable
fun LiveTripHeaderPreview() {
    NextStopTheme {
        LiveTripHeader(
            busId = dummyBusId,
            route = dummyLiveRoute,
            busFeatures = dummyBusFeatures,
        )
    }
}

@Composable
fun LiveTripBody(
    liveScheduledStops: List<LiveTripScheduledStop>,
    modifier: Modifier = Modifier
) {
    var currentStopFound = false
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        LazyColumn(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            itemsIndexed(liveScheduledStops) { index, liveScheduledStop ->
                var currentStopTiming =
                    getStopTimingFromWPTLiveFormat(liveScheduledStop.estimatedTime)
                if (!currentStopFound && currentStopTiming == StopTiming.PAST) {
                    val nextStop = liveScheduledStops.getOrNull(index + 1)
                    if (nextStop != null
                        && getStopTimingFromWPTLiveFormat(nextStop.estimatedTime) == StopTiming.FUTURE
                    ) {
                        currentStopTiming = StopTiming.CURRENT
                        currentStopFound = true
                    }
                }
                LiveScheduledStopRow(
                    liveScheduledStop = liveScheduledStop,
                    stopTiming = currentStopTiming,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun LiveScheduledStopRow(
    liveScheduledStop: LiveTripScheduledStop,
    stopTiming: StopTiming,
    modifier: Modifier = Modifier
) {
    val stopIconId = when (stopTiming) {
        StopTiming.PAST -> R.drawable.paststop
        StopTiming.CURRENT -> R.drawable.currentstop
        StopTiming.FUTURE -> R.drawable.futurestop
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LiveStopIcon(
            iconId = stopIconId,
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = simplifyStopName(liveScheduledStop.name),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .padding(end = dimensionResource(R.dimen.padding_medium))
                .weight(7f)
        )
        Text(
            text = getHrsMinsFromWPTLiveFormat(liveScheduledStop.estimatedTime),
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .weight(2f)
        )
    }
}

@Preview
@Composable
fun LiveScheduledStopRowPreview() {
    NextStopTheme {
        LiveScheduledStopRow(dummyLiveScheduledStops[0], StopTiming.CURRENT)
    }
}

@Composable
fun LiveStopIcon(
    @DrawableRes iconId: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(iconId),
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