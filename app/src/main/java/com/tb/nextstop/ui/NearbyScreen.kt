package com.tb.nextstop.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tb.nextstop.R
import com.tb.nextstop.data.Route
import com.tb.nextstop.data.Stop
import com.tb.nextstop.data.StopFeature
import com.tb.nextstop.ui.theme.NextStopTheme
import com.tb.nextstop.utils.BENCH
import com.tb.nextstop.utils.E_SIGN
import com.tb.nextstop.utils.HEATED_SHELTER
import com.tb.nextstop.utils.UNHEATED_SHELTER
import com.tb.nextstop.utils.tryGetValueFromJsonElement
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive


@Composable
fun NearbyScreen(
    onStopClicked: () -> Unit,
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
    onStopClicked: () -> Unit,
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
    onStopClicked: () -> Unit,
    stop: Stop,
    routes: List<Route>,
    features: List<StopFeature>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(dimensionResource(R.dimen.padding_small)),
        onClick = onStopClicked
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(
                vertical = dimensionResource(R.dimen.padding_medium),
                horizontal = dimensionResource(R.dimen.padding_small),
            )
        ) {
            Image(
                painter = painterResource(R.drawable.map),
                contentDescription = "stop thumbnail",
                modifier = modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
                    .size(dimensionResource(R.dimen.stop_thumbnail_size))
                    .background(color = colorResource(R.color.white))
            )
            Column(
                modifier = modifier.padding(dimensionResource(R.dimen.padding_small))
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stop.name,
                        overflow = TextOverflow.Ellipsis,
                        minLines = 2,
                        maxLines = 2,
                    )
                }
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    StopFeaturesRow(features)
                    StopRoutesRow(routes)
                }
            }
        }
    }
}

@Composable
fun StopRoutesRow(
    routesList: List<Route>,
    modifier: Modifier = Modifier
) {
    val routes = routesList.map { route ->
        route.badgeLabel
    }
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.route_icons_space_by)),
    ) {
        routes.forEach { route ->
            BusRouteIcon(route)
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
            .background(color = colorResource(R.color.black))
            .size(dimensionResource(R.dimen.route_icon_size))
    ) {
        Text(
            text = routeLabel.toString(),
            maxLines = 1,
            color = colorResource(R.color.white),
            style = TextStyle(fontSize = 15.sp)
        )
    }
}

@Composable
fun StopFeaturesRow(
    featuresList: List<StopFeature>,
    modifier: Modifier = Modifier
) {
    val features = featuresList.map { feature ->
        feature.name
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.stop_feature_icons_space_by)),
    ) {
        if (features.contains(HEATED_SHELTER)) {
            FeatureIcon("H")
        }
        if (features.contains(UNHEATED_SHELTER)) {
            FeatureIcon("U")
        }
        if (features.contains(BENCH)) {
            FeatureIcon("B")
        }
        if (features.contains(E_SIGN)) {
            FeatureIcon("E")
        }
    }
}

@Preview
@Composable
fun FeatureIcon(
    feature: String = "H",
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = colorResource(R.color.yellow))
            .size(dimensionResource(R.dimen.stop_feature_icon_size))
    ) {
        Text(
            text = feature,
        )
    }
}

@Preview
@Composable
fun StopPreview() {
    NextStopTheme {
        StopCard({}, dummyStop, dummyRoutes, dummyFeatures)
    }
}

val dummyStop = Stop(
    stopId = 19035,
    name = "Eastbound at Maple Wood and Ester Village"
)

val dummyRoutes = listOf(
    Route(
        key = JsonPrimitive("75"),
        number = JsonPrimitive("75"),
        badgeLabel = JsonPrimitive("75")
    ),
    Route(
        key = JsonPrimitive("67"),
        number = JsonPrimitive("67"),
        badgeLabel = JsonPrimitive("67")
    ),
    Route(
        key = JsonPrimitive("14"),
        number = JsonPrimitive("14"),
        badgeLabel = JsonPrimitive("14")
    ),
    Route(
        key = JsonPrimitive("16"),
        number = JsonPrimitive("16"),
        badgeLabel = JsonPrimitive("16")
    ),
    Route(
        key = JsonPrimitive("11"),
        number = JsonPrimitive("11"),
        badgeLabel = JsonPrimitive("11")
    ),
    Route(
        key = JsonPrimitive("BLUE"),
        number = JsonPrimitive("BLUE"),
        badgeLabel = JsonPrimitive("B")
    ),
    Route(
        key = JsonPrimitive("47"),
        number = JsonPrimitive("47"),
        badgeLabel = JsonPrimitive("47")
    ),
    Route(
        key = JsonPrimitive("676"),
        number = JsonPrimitive("676"),
        badgeLabel = JsonPrimitive("676")
    ),
)

val dummyFeatures = listOf(
    StopFeature(
        name = "Heated Shelter",
    ),
    StopFeature(
        name = "Unheated Shelter",
    ),
    StopFeature(
        name = "Bench",
    ),
    StopFeature(
        name = "BUSwatch Electronic Sign",
    )
)