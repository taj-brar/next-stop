package com.tb.nextstop.ui.nearby

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
import com.tb.nextstop.data.Route
import com.tb.nextstop.data.Stop
import com.tb.nextstop.data.StopFeature
import com.tb.nextstop.data.WPTRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface StopsUIState {
    data class Success(
        val stops: List<Stop>,
        val routesMap: MutableMap<Int, List<Route>>,
        val featuresMap: MutableMap<Int, List<StopFeature>>,
    ) : StopsUIState

    object Error : StopsUIState
    object Loading : StopsUIState
}

class NearbyScreenViewModel(
    private val wptRepository: WPTRepository,
    private val locationRepository: LocationRepository,
) : ViewModel() {
    var stopsUIState: StopsUIState by mutableStateOf(StopsUIState.Loading)
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
            stopsUIState = StopsUIState.Loading
            try {
                val stops = wptRepository.getNearbyStops(lat, lon)
                val stopsAndRoutes = getNearbyStopsAndRoutes(stops)
                val stopsAndFeatures = getNearbyStopsAndFeatures(stops)
                stopsUIState = StopsUIState.Success(stops, stopsAndRoutes, stopsAndFeatures)
            } catch (e: HttpException) {
                Log.d("VM", "Error getting stops\n$e")
                stopsUIState = StopsUIState.Error
            } catch (e: IOException) {
                Log.d("VM", "Error getting stops\n$e")
                stopsUIState = StopsUIState.Error
            }
        }
    }

    private suspend fun getNearbyStopsAndRoutes(
        stops: List<Stop>
    ): MutableMap<Int, List<Route>> {
        val routesMap: MutableMap<Int, List<Route>> = mutableMapOf()
        stops.forEach { stop ->
            var stopRoutes = listOf<Route>()
            try {
                stopRoutes = wptRepository.getStopRoutes(stop.stopId)
            } catch (e: HttpException) {
                Log.d("VM", "Error getting routes for stop ${stop.stopId}\n$e")
            }
            routesMap[stop.stopId] = stopRoutes
        }
        return routesMap
    }

    private suspend fun getNearbyStopsAndFeatures(
        stops: List<Stop>
    ): MutableMap<Int, List<StopFeature>> {
        val featuresMap: MutableMap<Int, List<StopFeature>> = mutableMapOf()
        stops.forEach { stop ->
            var stopFeatures: List<StopFeature> = listOf()
            try {
                stopFeatures = wptRepository.getStopFeatures(stop.stopId)
            } catch (e: HttpException) {
                Log.d("VM", "Error getting stop features for stop ${stop.stopId}\n$e")
            }
            featuresMap[stop.stopId] = stopFeatures
        }
        return featuresMap
    }

    override fun onCleared() {
        super.onCleared()
        location.removeObserver { }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NextStopApplication)
                val wptRepository = application.container.wptRepository
                val locationRepository = application.container.locationRepository
                NearbyScreenViewModel(
                    wptRepository = wptRepository,
                    locationRepository = locationRepository,
                )
            }
        }
    }
}