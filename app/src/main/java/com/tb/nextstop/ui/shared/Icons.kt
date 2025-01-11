package com.tb.nextstop.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.tb.nextstop.R
import com.tb.nextstop.data.Route
import com.tb.nextstop.data.StopFeature
import com.tb.nextstop.data.dummyStopFeatures
import com.tb.nextstop.data.dummyRoutes
import com.tb.nextstop.ui.theme.NextStopTheme
import com.tb.nextstop.utils.BENCH
import com.tb.nextstop.utils.E_SIGN
import com.tb.nextstop.utils.HEATED_SHELTER
import com.tb.nextstop.utils.UNHEATED_SHELTER
import com.tb.nextstop.utils.tryGetValueFromJsonElement
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlin.math.min

@Preview
@Composable
fun BusRouteIcon(
    route: JsonElement = JsonPrimitive("BLUE"),
    modifier: Modifier = Modifier
) {
    val defaultFontSize = 13.sp
    val iconSize = dimensionResource(R.dimen.route_icon_size)

    val routeLabel = tryGetValueFromJsonElement(route)?.toString() ?: ""
    val numChars = routeLabel.length

    val fontSize = if (numChars * defaultFontSize.value > iconSize.value) {
        maxOf(5.sp.value, iconSize.value / numChars).sp
    } else {
        defaultFontSize
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = Color.Black)
            .size(iconSize)
    ) {
        Text(
            text = routeLabel,
            maxLines = 1,
            color = Color.White,
            style = TextStyle(fontSize = fontSize)
        )
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
fun StopRoutesGridPreview() {
    NextStopTheme {
        StopRoutesGrid(dummyRoutes)
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
                Icon(
                    painter = painterResource(featureIconId),
                    contentDescription = "Heated shelter",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(dimensionResource(R.dimen.stop_feature_icon_size))
                )
            }
        }
    }
}

@Preview
@Composable
fun StopFeaturesGridPreview() {
    NextStopTheme {
        StopFeaturesGrid(dummyStopFeatures)
    }
}