package com.tb.nextstop.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tb.nextstop.R
import com.tb.nextstop.data.Stop
import com.tb.nextstop.ui.theme.NextStopTheme


@Composable
fun NearbyScreen(
    nearbyScreenViewModel: NearbyScreenViewModel = viewModel(factory = NearbyScreenViewModel.Factory)
) {
    when (val stopsUIState = nearbyScreenViewModel.stopsUIState) {
        is StopsUIState.Success -> StopsList(
            stopsUIState.stopsResponse.stops,
            Modifier.fillMaxSize()
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
    stops: List<Stop>,
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
            StopCard(stops[index])
        }
    }
}

@Preview
@Composable
fun NearbyScreenPreview() {
    NextStopTheme {
        NearbyScreen()
    }
}

@Composable
fun StopCard(
    stop: Stop,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(dimensionResource(R.dimen.padding_small))
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text("FEATURES")
                    Text("BUSES")
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
            Stop(
                stopId = 0,
                name = ""
            )
        )
    }
}