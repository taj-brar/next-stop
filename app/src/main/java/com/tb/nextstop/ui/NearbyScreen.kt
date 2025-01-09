package com.tb.nextstop.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tb.nextstop.R
import com.tb.nextstop.data.Route
import com.tb.nextstop.data.Stop
import com.tb.nextstop.data.StopFeature
import com.tb.nextstop.data.dummyFeatures
import com.tb.nextstop.data.dummyRoutes
import com.tb.nextstop.data.dummyStop
import com.tb.nextstop.ui.theme.NextStopTheme
import com.tb.nextstop.utils.BENCH
import com.tb.nextstop.utils.E_SIGN
import com.tb.nextstop.utils.HEATED_SHELTER
import com.tb.nextstop.utils.UNHEATED_SHELTER
import com.tb.nextstop.utils.tryGetValueFromJsonElement
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlin.math.min


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
fun ErrorScreen(
    modifier: Modifier = Modifier
) {
    Text(
        text = "ERROR"
    )
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Text(
        text = "Loading..."
    )
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
    Card(
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

@Composable
fun StopRoutesGrid(
    routesList: List<Route>,
    modifier: Modifier = Modifier
) {
    val numRoutes = routesList.size
    val maxColumns = 5
    val numRows = (numRoutes - 1) / maxColumns + 1
    val numColumns = if (numRoutes > 0) min(maxColumns, numRoutes) else 1
    val routeIconSize = dimensionResource(R.dimen.route_icon_size)
    val gridHeight = routeIconSize * numRows
    val gridWidth = routeIconSize * numColumns
    val routes = routesList.map { route ->
        route.badgeLabel
    }
    LazyHorizontalGrid(
        rows = GridCells.Fixed(numRows),
        modifier = modifier
            .height(gridHeight)
            .width(gridWidth),
        horizontalArrangement = Arrangement.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        items(routes) { route ->
            BusRouteIcon(
                route = route,
            )
        }
    }
}

@Preview
@Composable
fun BusRouteIcon(
    route: JsonElement = JsonPrimitive("75"),
    modifier: Modifier = Modifier
) {
    val routeLabel = tryGetValueFromJsonElement(route)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = Color.Black)
            .size(dimensionResource(R.dimen.route_icon_size))
    ) {
        Text(
            text = routeLabel.toString(),
            maxLines = 1,
            color = Color.White,
            style = TextStyle(fontSize = 13.sp)
        )
    }
}

@Composable
fun StopFeaturesGrid(
    featuresList: List<StopFeature>,
    modifier: Modifier = Modifier
) {
    val numFeatures = featuresList.size
    val maxColumns = 2
    val numRows = (numFeatures - 1) / maxColumns + 1
    val numColumns = if (numFeatures > 0) min(maxColumns, numFeatures) else 1
    val routeIconSize = dimensionResource(R.dimen.stop_feature_icon_size)
    val gridHeight = routeIconSize * numRows
    val gridWidth = routeIconSize * numColumns
    val features = featuresList.map { feature ->
        feature.name
    }
    LazyHorizontalGrid(
        rows = GridCells.Fixed(numRows),
        modifier = modifier
            .height(gridHeight)
            .width(gridWidth),
        horizontalArrangement = Arrangement.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        items(features) { feature ->
            val featureIconId = when (feature) {
                HEATED_SHELTER -> R.drawable.heated_shelter
                UNHEATED_SHELTER -> R.drawable.unheated_shelter
                BENCH -> R.drawable.bench
                E_SIGN -> R.drawable.clock
                else -> Int.MIN_VALUE
            }
            if (featureIconId != Int.MIN_VALUE) {
                Image(
                    painter = painterResource(featureIconId),
                    contentDescription = "Heated shelter",
                    modifier = Modifier.size(dimensionResource(R.dimen.stop_feature_icon_size))
                )
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
            dummyFeatures
        )
    }
}