package com.tb.nextstop.ui.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tb.nextstop.R

@Composable
fun MapThumbnailComposable(
    stopId: Int,
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier,
    mapThumbnailViewModel: MapThumbnailViewModel = viewModel(
        factory = MapThumbnailViewModel.Factory,
        key = latitude.toString() + longitude.toString()
    )
) {
    LaunchedEffect(latitude, longitude) {
        mapThumbnailViewModel.getSnapshotFromMap(stopId, latitude, longitude)
    }

    Box(
        modifier = modifier.padding(0.dp)
    ) {
        when (val mapThumbnailUIState = mapThumbnailViewModel.mapThumbnailUIState) {
            is MapThumbnailUIState.Success -> {
                Image(
                    bitmap = mapThumbnailUIState.snapshot.asImageBitmap(),
                    contentDescription = "Location Thumbnail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is MapThumbnailUIState.Loading -> Image(
                painter = painterResource(R.drawable.bus),
                contentDescription = "Bus",
                modifier = Modifier.fillMaxSize()
            )

            is MapThumbnailUIState.Error -> Image(
                painter = painterResource(R.drawable.bus),
                contentDescription = "Bus",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
