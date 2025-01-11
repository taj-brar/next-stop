package com.tb.nextstop.ui.nearby

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tb.nextstop.R
import com.tb.nextstop.data.Route
import com.tb.nextstop.data.Stop
import com.tb.nextstop.data.StopFeature
import com.tb.nextstop.data.dummyStopFeatures
import com.tb.nextstop.data.dummyRoutes
import com.tb.nextstop.data.dummyStop
import com.tb.nextstop.ui.shared.ErrorScreen
import com.tb.nextstop.ui.shared.LoadingScreen
import com.tb.nextstop.ui.shared.StopFeaturesGrid
import com.tb.nextstop.ui.shared.StopRoutesGrid
import com.tb.nextstop.ui.theme.NextStopTheme


@Composable
fun NearbyScreen(
    onStopClicked: (Int) -> Unit,
    nearbyScreenViewModel: NearbyScreenViewModel = viewModel(factory = NearbyScreenViewModel.DetailedStopsFactory)
) {
    when (val stopsUIState = nearbyScreenViewModel.stopsUIState) {
        is StopsUIState.Success -> StopsList(
            onStopClicked = onStopClicked,
            stops = stopsUIState.stops,
            routesMap = stopsUIState.routesMap,
            featuresMap = stopsUIState.featuresMap,
            modifier = Modifier.fillMaxSize()
        )

        is StopsUIState.Error -> ErrorScreen()
        is StopsUIState.Loading -> LoadingScreen()
    }
}

@Composable
fun StopsList(
    onStopClicked: (Int) -> Unit,
    stops: List<Stop>,
    routesMap: MutableMap<Int, List<Route>>,
    featuresMap: MutableMap<Int, List<StopFeature>>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .padding(
                start = dimensionResource(R.dimen.padding_small),
                top = dimensionResource(R.dimen.padding_xxlarge),
                end = dimensionResource(R.dimen.padding_small),
                bottom = dimensionResource(R.dimen.padding_small),
            )
    ) {
        items(count = stops.size) { index ->
            val stop = stops[index]

            StopCard(
                onStopClicked = onStopClicked,
                stop = stop,
                routes = routesMap[stop.stopId] ?: listOf(),
                features = featuresMap[stop.stopId] ?: listOf(),
            )
        }
    }
}

@Composable
fun StopCard(
    onStopClicked: (Int) -> Unit,
    stop: Stop,
    routes: List<Route>,
    features: List<StopFeature>,
    modifier: Modifier = Modifier
) {
    val cardColor = MaterialTheme.colorScheme.background
    OutlinedCard (
        modifier = modifier.padding(dimensionResource(R.dimen.padding_small)),
        onClick = { onStopClicked(stop.stopId) },
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 1.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    vertical = dimensionResource(R.dimen.padding_medium),
                    horizontal = dimensionResource(R.dimen.padding_small),
                )
                .background(color = cardColor)
        ) {
            Image(
                painter = painterResource(R.drawable.map_black),
                contentDescription = "stop thumbnail",
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
                    .size(dimensionResource(R.dimen.stop_thumbnail_size))
                    .background(color = colorResource(R.color.white))
            )
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_medium))
                ) {
                    Text(
                        text = stop.name,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    StopFeaturesGrid(
                        features,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    StopRoutesGrid(
                        routes,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun StopPreview() {
    NextStopTheme {
        StopCard(
            {},
            dummyStop,
            dummyRoutes,
            dummyStopFeatures
        )
    }
}