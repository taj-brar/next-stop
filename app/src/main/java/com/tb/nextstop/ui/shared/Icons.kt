package com.tb.nextstop.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.tb.nextstop.R
import com.tb.nextstop.utils.tryGetValueFromJsonElement
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

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