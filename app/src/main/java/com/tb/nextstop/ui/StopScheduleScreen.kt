package com.tb.nextstop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tb.nextstop.R
import com.tb.nextstop.ui.theme.NextStopTheme

@Composable
fun StopScheduleScreen(
    viewModel: StopScheduleViewModel = viewModel(factory = StopScheduleViewModel.Factory),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        StopScheduleHeader(
            modifier = modifier.fillMaxWidth()
        )
        LazyColumn(
            modifier = modifier.fillMaxWidth()
        ) {
            items(5) {
                ScheduledStopCard()
            }
        }
    }
}

@Preview
@Composable
fun StopScheduleScreenPreview() {
    NextStopTheme {
        StopScheduleScreen()
    }
}

@Composable
fun StopScheduleHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StopScheduleHeaderDetails()
        Spacer(Modifier.width(dimensionResource(R.dimen.padding_xxlarge)))
        StopScheduleHeaderBusRoutes()
    }
}

@Composable
fun StopScheduleHeaderDetails(
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = "STOP NAME EXAMPLE HERE",
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = "STOP NUMBER",
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Preview
@Composable
fun StopScheduleHeaderBusRoutes(
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(dimensionResource(R.dimen.route_icon_size)),
        horizontalArrangement = Arrangement.End
    ) {
        items(15) {
            BusRouteIcon()
        }
    }
}

@Composable
fun ScheduledStopCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row {
            Text("TEST")
        }
    }
}