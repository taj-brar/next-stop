package com.tb.nextstop.ui.shared

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tb.nextstop.NextStopApplication
import com.tb.nextstop.data.LocationRepository
import kotlinx.coroutines.launch

private const val DEFAULT_ZOOM = 18.0
private const val WIDTH = 500
private const val HEIGHT = 500

sealed interface MapThumbnailUIState {
    data class Success(
        val snapshot: Bitmap
    ) : MapThumbnailUIState

    object Error : MapThumbnailUIState
    object Loading : MapThumbnailUIState
}

class MapThumbnailViewModel(
    private val locationRepository: LocationRepository,
) : ViewModel() {
    var mapThumbnailUIState: MapThumbnailUIState by mutableStateOf(MapThumbnailUIState.Loading)
        private set

    init {
        mapThumbnailUIState = MapThumbnailUIState.Loading
    }

    fun getSnapshotFromMap(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            Handler(Looper.getMainLooper()).post {
                locationRepository.getMapSnapshot(
                    latitude = latitude,
                    longitude = longitude,
                    width = WIDTH,
                    height = HEIGHT,
                    zoom = DEFAULT_ZOOM
                ) { bitmap ->
                    mapThumbnailUIState = MapThumbnailUIState.Success(bitmap)
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NextStopApplication)
                val locationRepository = application.container.locationRepository
                MapThumbnailViewModel(
                    locationRepository = locationRepository
                )
            }
        }
    }
}