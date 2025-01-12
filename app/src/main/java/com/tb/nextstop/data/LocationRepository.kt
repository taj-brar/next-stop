package com.tb.nextstop.data

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tb.nextstop.utils.checkAndRequestLocationPermission

interface LocationRepository {
    val location: LiveData<Location?>
    suspend fun updateLocation()
}

class LocalLocationRepository(
    private val context: Context
) : LocationRepository {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val _location = MutableLiveData<Location?>()
    override val location: LiveData<Location?> get() = _location

    override suspend fun updateLocation() {
        checkAndRequestLocationPermission(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                _location.postValue(location)
            }
            .addOnFailureListener {
                _location.postValue(null)
            }
    }
}