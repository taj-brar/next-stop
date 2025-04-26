package com.tb.nextstop.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.tb.nextstop.R
import com.tb.nextstop.data.Stop
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker

const val WPG_LAT = 49.8954
const val WPG_LON = -97.1385

private const val DEFAULT_ZOOM = 18.0
private const val MIN_ZOOM = 13.0
private const val N_LAT_LIMIT = 50.0
private const val S_LAT_LIMIT = 49.7
private const val W_LON_LIMIT = -97.4
private const val E_LON_LIMIT = -96.9

@Composable
fun OpenStreetMapComposable(
    stops: List<Stop>
) {
    DisposableEffect(Unit) {
        onDispose { }
    }

    AndroidView(
        factory = { context ->
            val mapView = org.osmdroid.views.MapView(context).apply {
                org.osmdroid.config.Configuration.getInstance().load(
                    context,
                    android.preference.PreferenceManager.getDefaultSharedPreferences(context)
                )

                setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                controller.setZoom(DEFAULT_ZOOM)
                controller.setCenter(GeoPoint(WPG_LAT, WPG_LON))

                setScrollableAreaLimitLatitude(N_LAT_LIMIT, S_LAT_LIMIT, 0)
                setScrollableAreaLimitLongitude(W_LON_LIMIT, E_LON_LIMIT, 0)
                minZoomLevel = MIN_ZOOM
            }

            val locationOverlay = org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay(
                org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider(context),
                mapView
            ).apply {
                enableMyLocation()
                enableFollowLocation()
            }
            mapView.overlays.add(locationOverlay)

            mapView
        },
        update = { mapView ->
            val busStopIcon = ContextCompat.getDrawable(
                mapView.context,
                R.drawable.bus
            )?.mutate()
            busStopIcon?.setTint(ContextCompat.getColor(mapView.context, R.color.teal_700))

            val markers: List<Marker?> = stops.mapNotNull {
                val lat = it.centre.geographic.latitude.toDoubleOrNull()
                val lon = it.centre.geographic.longitude.toDoubleOrNull()

                if (lat != null && lon != null) {
                    val marker = Marker(mapView)
                    marker.position = GeoPoint(lat, lon)
                    marker.title = it.name
                    marker.icon = busStopIcon

                    marker
                } else {
                    null
                }
            }
            mapView.overlays.addAll(markers)
        }
    )
}
