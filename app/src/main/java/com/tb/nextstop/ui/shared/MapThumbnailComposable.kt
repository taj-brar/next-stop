package com.tb.nextstop.ui.shared

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tb.nextstop.R
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.Style
import org.maplibre.android.snapshotter.MapSnapshotter

private const val DEFAULT_ZOOM = 18.0
private const val WIDTH = 500
private const val HEIGHT = 500

@Composable
fun MapThumbnailComposable(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var snapshot by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(latitude, longitude) {
        Handler(Looper.getMainLooper()).post {
            val styleJson =
                context.resources.openRawResource(R.raw.demotiles).reader().readText()
            val options = MapSnapshotter.Options(WIDTH, HEIGHT)
                .withCameraPosition(
                    CameraPosition.Builder()
                        .target(LatLng(latitude, longitude))
                        .zoom(DEFAULT_ZOOM)
                        .build()
                )
                .withStyleBuilder(Style.Builder().fromJson(styleJson))
                .withLogo(false)

            val snapshotter = MapSnapshotter(context, options)
            snapshotter.start({ result -> snapshot = result.bitmap })

        }
    }

    Box(
        modifier = modifier.padding(0.dp)
    ) {
        snapshot?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Location Thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } ?: Image(
            painter = painterResource(R.drawable.bus),
            contentDescription = "Bus",
            modifier = Modifier.fillMaxSize()
        )
    }
}
