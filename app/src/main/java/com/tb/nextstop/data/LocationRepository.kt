package com.tb.nextstop.data

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val DEFAULT_ZOOM = 18.0
private const val WIDTH = 500
private const val HEIGHT = 500

interface LocationRepository {
    val location: LiveData<Location?>
    suspend fun updateLocation()
    fun getSnapshot(
        id: Int,
        latitude: Double,
        longitude: Double,
        afterGetting: (Bitmap) -> Unit
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

    override fun getSnapshot(
        id: Int,
        latitude: Double,
        longitude: Double,
        afterGetting: (Bitmap) -> Unit
    ) {
        try {
            val file = File(context.filesDir, "$id.png")

            if (file.exists()) {
                val snapshot = BitmapFactory.decodeFile(file.absolutePath)
                afterGetting(snapshot)
            } else {
                createSnapshot(
                    latitude = latitude,
                    longitude = longitude,
                    afterCreating = { bitmap ->
                        saveSnapshot(id, bitmap)
                        afterGetting(bitmap)
                    }
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun createSnapshot(
        latitude: Double,
        longitude: Double,
        afterCreating: (Bitmap) -> Unit,
    ) {
        val styleJson = context.resources.openRawResource(R.raw.demotiles).reader().readText()
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
        snapshotter.start({ result ->
            val snapshot = result.bitmap
            afterCreating(snapshot)
        })
    }

    private fun saveSnapshot(id: Int, snapshot: Bitmap) {
        try {
            val outputStream: FileOutputStream = context.openFileOutput(
                "$id.png",
                Context.MODE_PRIVATE
            )
            snapshot.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}