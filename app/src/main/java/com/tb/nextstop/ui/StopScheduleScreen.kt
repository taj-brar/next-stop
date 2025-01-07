package com.tb.nextstop.ui

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tb.nextstop.R
import com.tb.nextstop.data.Bus
import com.tb.nextstop.data.RouteScheduledStop
import com.tb.nextstop.data.RouteSchedules
import com.tb.nextstop.data.Stop
import com.tb.nextstop.data.StopSchedule
import com.tb.nextstop.data.dummyRouteScheduledStop
import com.tb.nextstop.data.dummyStopSchedule
import com.tb.nextstop.ui.theme.NextStopTheme

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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StopScheduleHeaderDetails(
            stop = stopSchedule.stop,
            modifier = Modifier
                .weight(2f)
                .padding(dimensionResource(R.dimen.padding_medium))
        )
        StopScheduleHeaderBusRoutes(
            stopSchedule = stopSchedule,
            modifier = Modifier
                .weight(1f)
                .padding(dimensionResource(R.dimen.padding_medium))
                .align(Alignment.Bottom)
        )
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
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = stop.name,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = "#" + stop.stopNumber,
            color = MaterialTheme.colorScheme.onPrimaryContainer
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
    LazyVerticalGrid(
        columns = GridCells.Adaptive(dimensionResource(R.dimen.route_icon_size)),
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
    ) {
        items(routes) { route ->
            BusRouteIcon(
                route = route.badgeLabel,
            )
        }
    }
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
    return routeScheduledStops
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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .padding(dimensionResource(R.dimen.padding_small)),
        onClick = {
            val tripId = scheduledStop.key.split("-").firstOrNull()
            onScheduledStopClicked(tripId?.toInt() ?: -1)
        }
    ) {
        Row(
            modifier = modifier
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Column(
                modifier = modifier.weight(2f)
            ) {
                Text(
                    text = scheduledStop.variant.name,
                    modifier = modifier
                )
                BusFeaturesRow(
                    bus = bus,
                    modifier = modifier
                )
            }
            Column(
                modifier = modifier.weight(1f)
            ) {
                Text(
                    text = scheduledStop.times.arrival?.estimated ?: "",
                    modifier = modifier
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
        modifier = modifier.fillMaxWidth()
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
            .background(color = MaterialTheme.colorScheme.tertiaryContainer)
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