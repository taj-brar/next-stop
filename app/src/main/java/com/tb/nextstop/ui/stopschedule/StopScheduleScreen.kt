package com.tb.nextstop.ui.stopschedule

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tb.nextstop.R
import com.tb.nextstop.data.Bus
import com.tb.nextstop.data.RouteScheduledStop
import com.tb.nextstop.data.RouteSchedules
import com.tb.nextstop.data.Stop
import com.tb.nextstop.data.StopSchedule
import com.tb.nextstop.data.dummyFeatures
import com.tb.nextstop.data.dummyRouteScheduledStop
import com.tb.nextstop.data.dummyStopSchedule
import com.tb.nextstop.ui.shared.BusRouteIcon
import com.tb.nextstop.ui.shared.ErrorScreen
import com.tb.nextstop.ui.shared.LoadingScreen
import com.tb.nextstop.ui.shared.StopFeaturesGrid
import com.tb.nextstop.ui.shared.StopRoutesGrid
import com.tb.nextstop.ui.theme.NextStopTheme
import com.tb.nextstop.utils.Timing
import com.tb.nextstop.utils.getHrsMinsFromWPTFormat
import com.tb.nextstop.utils.getTimingFromWPTFormat

@Composable
fun StopScheduleScreen(
    onScheduledStopClicked: (Int) -> Unit,
    stopScheduleViewModel: StopScheduleViewModel = viewModel(factory = StopScheduleViewModel.Factory),
    modifier: Modifier = Modifier
) {
    when (val stopScheduleUIState = stopScheduleViewModel.stopScheduleUIState) {
        is StopScheduleUIState.Success -> StopScheduleSuccessScreen(
            onScheduledStopClicked = onScheduledStopClicked,
            stopSchedule = stopScheduleUIState.stopSchedule,
            modifier = modifier.fillMaxSize()
        )

        is StopScheduleUIState.Error -> ErrorScreen()
        is StopScheduleUIState.Loading -> LoadingScreen()
    }
}

@Composable
fun StopScheduleSuccessScreen(
    onScheduledStopClicked: (Int) -> Unit,
    stopSchedule: StopSchedule,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        StopScheduleHeader(
            stopSchedule = stopSchedule,
            modifier = Modifier
                .fillMaxWidth()
        )
        ScheduledStopList(
            onScheduledStopClicked = onScheduledStopClicked,
            stopSchedule = stopSchedule,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Preview
@Composable
fun StopScheduleSuccessScreenPreview(
    modifier: Modifier = Modifier
) {
    NextStopTheme {
        StopScheduleSuccessScreen(
            onScheduledStopClicked = { },
            stopSchedule = dummyStopSchedule
        )
    }
}

@Composable
fun StopScheduleHeader(
    stopSchedule: StopSchedule,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        StopScheduleHeaderDetails(
            stop = stopSchedule.stop,
            modifier = Modifier
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            StopFeaturesGrid(
                featuresList = dummyFeatures,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .align(Alignment.CenterVertically)
            )
            StopScheduleHeaderBusRoutes(
                stopSchedule = stopSchedule,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview
@Composable
fun StopScheduleHeaderPreview() {
    NextStopTheme {
        StopScheduleHeader(
            stopSchedule = dummyStopSchedule
        )
    }
}

@Composable
fun StopScheduleHeaderDetails(
    stop: Stop,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = stop.name,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .weight(8f)
                .padding(dimensionResource(R.dimen.padding_small))
        )
        Text(
            text = "#" + stop.stopNumber,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = TextStyle(fontStyle = FontStyle.Italic),
            modifier = Modifier
                .weight(2f)
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}

@Composable
fun StopScheduleHeaderBusRoutes(
    stopSchedule: StopSchedule,
    modifier: Modifier = Modifier
) {
    val routes = stopSchedule.routeSchedules.map { routeSchedule ->
        routeSchedule.route
    }
    StopRoutesGrid(
        routesList = routes,
        modifier = modifier
    )
}

@Composable
fun ScheduledStopList(
    onScheduledStopClicked: (Int) -> Unit,
    stopSchedule: StopSchedule,
    modifier: Modifier = Modifier
) {
    val routeScheduledStops = getScheduledStopsFromRouteSchedules(stopSchedule.routeSchedules)
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(routeScheduledStops) { routeScheduledStop ->
            ScheduledStopCard(
                onScheduledStopClicked = onScheduledStopClicked,
                routeScheduledStop = routeScheduledStop
            )
        }
    }
}

fun getScheduledStopsFromRouteSchedules(
    routeSchedules: List<RouteSchedules>
): List<RouteScheduledStop> {
    val routeScheduledStops = mutableListOf<RouteScheduledStop>()
    routeSchedules.forEach { routeSchedule ->
        routeSchedule.scheduledStops.forEach { scheduledStop ->
            routeScheduledStops.add(
                RouteScheduledStop(
                    route = routeSchedule.route,
                    scheduledStop = scheduledStop
                )
            )
        }
    }
    return routeScheduledStops.sortedWith { stop1, stop2 ->
        val stop1Time = stop1.scheduledStop.times.arrival?.estimated ?: ""
        val stop2Time = stop2.scheduledStop.times.arrival?.estimated ?: ""
        stop1Time.compareTo(stop2Time)
    }
}

@Composable
fun ScheduledStopCard(
    onScheduledStopClicked: (Int) -> Unit,
    routeScheduledStop: RouteScheduledStop,
    modifier: Modifier = Modifier
) {
    val bus = routeScheduledStop.scheduledStop.bus
    val scheduledStop = routeScheduledStop.scheduledStop
    val route = routeScheduledStop.route
    val expectedArrival: String
    val clockTint: Color

    if (scheduledStop.times.arrival != null) {
        val estimatedTime = scheduledStop.times.arrival.estimated
        val scheduledTime = scheduledStop.times.arrival.scheduled
        expectedArrival = getHrsMinsFromWPTFormat(estimatedTime)
        clockTint = when (getTimingFromWPTFormat(estimatedTime, scheduledTime)) {
            Timing.EARLY -> Color.Red
            Timing.ON_TIME -> Color.Black
            Timing.LATE -> Color.Red
        }
    } else {
        expectedArrival = ""
        clockTint = Color.Black
    }

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_small)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 1.dp
        ),
        onClick = {
            val tripId = scheduledStop.key.split("-").firstOrNull()
            onScheduledStopClicked(tripId?.toInt() ?: -1)
        }
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_large))
        ) {
            Column(
                modifier = Modifier.weight(2f)
            ) {
                BusRouteIcon(
                    route = route.number,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                )
                Text(
                    text = scheduledStop.variant.name,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.clock_filled),
                        contentDescription = "",
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.delay_icon_size))
                            .padding(dimensionResource(R.dimen.padding_small)),
                        tint = clockTint
                    )
                    Text(
                        text = expectedArrival,
                    )
                }
                BusFeaturesRow(
                    bus = bus,
                )
            }
        }
    }
}

@Composable
fun BusFeaturesRow(
    bus: Bus?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        if (bus?.bikeRack.toBoolean()) {
            BusFeatureIcon(BusFeature.BIKE_RACK)
        }
        if (bus?.wifi.toBoolean()) {
            BusFeatureIcon(BusFeature.WIFI)
        }
    }
}

@Composable
fun BusFeatureIcon(
    busFeature: BusFeature,
    modifier: Modifier = Modifier
) {
    val drawableIcon = when (busFeature) {
        BusFeature.BIKE_RACK -> R.drawable.bike
        BusFeature.WIFI -> R.drawable.wifi
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(dimensionResource(R.dimen.stop_feature_icon_size))
    ) {
        Image(
            painter = painterResource(drawableIcon),
            contentDescription = "Bike rack",
        )
    }
}

enum class BusFeature {
    BIKE_RACK,
    WIFI
}

@Preview
@Composable
fun ScheduledStopCardPreview() {
    NextStopTheme {
        ScheduledStopCard(
            onScheduledStopClicked = { },
            routeScheduledStop = dummyRouteScheduledStop
        )
    }
}