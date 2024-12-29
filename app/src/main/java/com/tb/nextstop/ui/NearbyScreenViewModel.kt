package com.tb.nextstop.ui

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
import com.tb.nextstop.data.Stop
import com.tb.nextstop.data.StopFeature
import com.tb.nextstop.data.WPTRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface StopsUIState {
    data class Success(
        val stops: List<Stop>,
        val stopsAndFeatures: List<Pair<Stop, List<StopFeature>>>
    ): StopsUIState
    object Error: StopsUIState
    object Loading: StopsUIState
}

class NearbyScreenViewModel(private val wptRepository: WPTRepository): ViewModel() {
    var stopsUIState: StopsUIState by mutableStateOf(StopsUIState.Loading)
        private set

    init {
        getNearbyStops()
    }

    fun getNearbyStops() {
        viewModelScope.launch {
            stopsUIState = StopsUIState.Loading
            stopsUIState = try {
                val stopsResponse = wptRepository.getNearbyStops()
                val stops = stopsResponse.stops
                val stopsAndFeatures = getNearbyStopsAndFeatures(stops)
                Log.d("VM", stopsAndFeatures.toString())
                StopsUIState.Success(stops, stopsAndFeatures)
            } catch(e: HttpException) {
                Log.d("VM", e.toString())
                StopsUIState.Error
            } catch(e: IOException) {
                Log.d("VM", e.toString())
                StopsUIState.Error
            }
        }
    }

    private suspend fun getNearbyStopsAndFeatures(
        stops: List<Stop>
    ): List<Pair<Stop, List<StopFeature>>> {
        return stops.map { stop ->
            stop to wptRepository.getStopFeatures(stop.stopId).stopFeatures
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NextStopApplication)
                val wptRepository = application.container.wptRepository
                NearbyScreenViewModel(wptRepository = wptRepository)
            }
        }
    }
}