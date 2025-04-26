package com.tb.nextstop.ui.saved

import android.util.Log
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
import com.tb.nextstop.data.Route
import com.tb.nextstop.data.Stop
import com.tb.nextstop.data.StopFeature
import com.tb.nextstop.data.WPTRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface SavedStopsUIState {
    data class Success(
        val stops: MutableList<Stop>,
        val routesMap: MutableMap<Int, List<Route>>,
        val featuresMap: MutableMap<Int, List<StopFeature>>,
    ) : SavedStopsUIState

    object Error : SavedStopsUIState
    object Loading : SavedStopsUIState
}

class SavedScreenViewModel(
    private val wptRepository: WPTRepository,
) : ViewModel() {
    var savedStopsUIState: SavedStopsUIState by mutableStateOf(SavedStopsUIState.Loading)
        private set

    init {
        getSavedStops()
    }

    private fun getSavedStops() {
        viewModelScope.launch {
            savedStopsUIState = SavedStopsUIState.Loading
            try {
                val stops = wptRepository.getSavedStops()
                val stopsAndRoutes = getSavedStopsAndRoutes(stops)
                val stopsAndFeatures = getSavedStopsAndFeatures(stops)
                savedStopsUIState = SavedStopsUIState.Success(
                    stops.toMutableList(),
                    stopsAndRoutes,
                    stopsAndFeatures
                )
            } catch (e: HttpException) {
                Log.d("VM", "Error getting stops\n$e")
                savedStopsUIState = SavedStopsUIState.Error
            } catch (e: IOException) {
                Log.d("VM", "Error getting stops\n$e")
                savedStopsUIState = SavedStopsUIState.Error
            }
        }
    }

    private suspend fun getSavedStopsAndRoutes(
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

    private suspend fun getSavedStopsAndFeatures(
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

    fun removeSavedStop(stopId: Int) {
        viewModelScope.launch {
            if (savedStopsUIState is SavedStopsUIState.Success) {
                wptRepository.removeSavedStop(stopId)
                getSavedStops()
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NextStopApplication)
                val wptRepository = application.container.wptRepository
                SavedScreenViewModel(
                    wptRepository = wptRepository,
                )
            }
        }
    }
}