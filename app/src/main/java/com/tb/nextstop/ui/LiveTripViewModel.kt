package com.tb.nextstop.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tb.nextstop.NextStopApplication
import com.tb.nextstop.data.BusFeatures
import com.tb.nextstop.data.LiveRoute
import com.tb.nextstop.data.LiveTripScheduledStop
import com.tb.nextstop.data.WPTRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface LiveTripUIState {
    data class Success(
        val busId: String,
        val route: LiveRoute,
        val busFeatures: BusFeatures,
        val liveScheduledStops: List<LiveTripScheduledStop>,
    ) : LiveTripUIState

    data object Error : LiveTripUIState
    data object Loading : LiveTripUIState
}

class LiveTripViewModel(
    savedStateHandle: SavedStateHandle,
    private val wptRepository: WPTRepository,
) : ViewModel() {
    private val tripId: Int = checkNotNull(savedStateHandle[LiveTripDestination.TRIP_ID_ARG])
    var liveTripUIState: LiveTripUIState by mutableStateOf(LiveTripUIState.Loading)
        private set

    init {
        liveTripUIState = LiveTripUIState.Loading
        viewModelScope.launch {
            getLiveScheduledStops()
        }
    }

    fun getLiveScheduledStops() {
        Log.d("TEST", "GETTING LIVE DATA")
        viewModelScope.launch {
            try {
                val liveTripResponse = wptRepository.getLiveTrip(tripId)
                liveTripUIState = LiveTripUIState.Success(
                    busId = liveTripResponse.busId,
                    route = liveTripResponse.route,
                    busFeatures = liveTripResponse.busFeatures,
                    liveScheduledStops = liveTripResponse.scheduledStops
                )
            } catch (e: HttpException) {
                Log.d("VM", "Error getting live schedule for $tripId\n$e")
                liveTripUIState = LiveTripUIState.Error
            } catch (e: IOException) {
                Log.d("VM", "Error getting live schedule for $tripId\n$e")
                liveTripUIState = LiveTripUIState.Error
            }
        }
    }

    companion object {
        const val LIVE_REFRESH_DELAY = 5000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NextStopApplication)
                val wptRepository = application.container.wptRepository
                LiveTripViewModel(
                    this.createSavedStateHandle(),
                    wptRepository = wptRepository
                )
            }
        }
    }
}

object LiveTripDestination {
    const val NAME = "Live Trip"
    const val ROUTE = "LIVE_TRIP"
    const val TRIP_ID_ARG = "tripId"
    const val ROUTE_WITH_ARGS = "$ROUTE/{$TRIP_ID_ARG}"
}
