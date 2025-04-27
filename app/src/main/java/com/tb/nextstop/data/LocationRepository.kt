package com.tb.nextstop.data

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tb.nextstop.R
import com.tb.nextstop.utils.locationPermissions
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.Style
import org.maplibre.android.snapshotter.MapSnapshotter

interface LocationRepository {
    val location: LiveData<Location?>
    suspend fun updateLocation()
    fun getMapSnapshot(
        latitude: Double,
        longitude: Double,
        width: Int,
        height: Int,
        zoom: Double,
        afterLoading: (Bitmap) -> Unit,
    )
}

class LocalLocationRepository(
    private val context: Context
) : LocationRepository {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val _location = MutableLiveData<Location?>()
    override val location: LiveData<Location?> get() = _location

    override suspend fun updateLocation() {
        if (locationPermissions.any { perm ->
                ContextCompat.checkSelfPermission(
                    context,
                    perm
                ) == PackageManager.PERMISSION_GRANTED
            }
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    _location.postValue(location)
                }
                .addOnFailureListener {
                    _location.postValue(null)
                }
        }
    }

    override fun getMapSnapshot(
        latitude: Double,
        longitude: Double,
        width: Int,
        height: Int,
        zoom: Double,
        afterLoading: (Bitmap) -> Unit,
    ) {
        val styleJson = context.resources.openRawResource(R.raw.demotiles).reader().readText()
        val options = MapSnapshotter.Options(width, height)
            .withCameraPosition(
                CameraPosition.Builder()
                    .target(LatLng(latitude, longitude))
                    .zoom(zoom)
                    .build()
            )
            .withStyleBuilder(Style.Builder().fromJson(styleJson))
            .withLogo(false)

        val snapshotter = MapSnapshotter(context, options)
        snapshotter.start({ result ->
            val snapshot = result.bitmap
            afterLoading(snapshot)
        })
    }
}