package com.tb.nextstop.ui.map

import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tb.nextstop.NextStopApplication
import com.tb.nextstop.data.LocationRepository
import com.tb.nextstop.data.Stop
import com.tb.nextstop.data.WPTRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface MapScreenUIState {
    data class Success(
        val stops: List<Stop>,
    ) : MapScreenUIState

    data object Error : MapScreenUIState
    data object Loading : MapScreenUIState
}

class MapScreenViewModel(
    private val wptRepository: WPTRepository,
    private val locationRepository: LocationRepository,
) : ViewModel() {
    var mapScreenUIState: MapScreenUIState by mutableStateOf(MapScreenUIState.Loading)
        private set

    private val location: LiveData<Location?> = locationRepository.location

    init {
        viewModelScope.launch {
            locationRepository.updateLocation()
            location.observeForever { location ->
                location?.let {
                    getNearbyStops(it.latitude, it.longitude)
                }
            }
        }
    }

    private fun getNearbyStops(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val stops = wptRepository.getNearbyStops(lat, lon)
                mapScreenUIState = MapScreenUIState.Success(stops)
            } catch (e: HttpException) {
                Log.d("VM", "Error getting nearby stops for map\n$e")
                mapScreenUIState = MapScreenUIState.Error
            } catch (e: IOException) {
                Log.d("VM", "Error getting nearby stops for map\n$e")
                mapScreenUIState = MapScreenUIState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NextStopApplication)
                val wptRepository = application.container.wptRepository
                val locationRepository = application.container.locationRepository
                MapScreenViewModel(
                    wptRepository = wptRepository,
                    locationRepository = locationRepository,
                )
            }
        }
    }
}