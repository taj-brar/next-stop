package com.tb.nextstop.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.tb.nextstop.data.Stop
import com.tb.nextstop.utils.checkAndRequestLocationPermission
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker

const val WPG_LAT = 49.8954
const val WPG_LON = -97.1385
private const val DEFAULT_ZOOM = 18.0

@Composable
fun OpenStreetMapComposable(
    stops: List<Stop>
) {
    val localContext = LocalContext.current

    DisposableEffect(Unit) {
        checkAndRequestLocationPermission(localContext)
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
            val markers: List<Marker?> = stops.mapNotNull {
                val lat = it.centre.geographic.latitude.toDoubleOrNull()
                val lon = it.centre.geographic.longitude.toDoubleOrNull()

                if (lat != null && lon != null) {
                    val marker = Marker(mapView)
                    marker.position = GeoPoint(lat, lon)
                    marker.title = it.name
                    marker
                } else {
                    null
                }
            }
            mapView.overlays.addAll(markers)
        }
    )
}
