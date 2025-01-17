package com.tb.nextstop.ui.stopschedule

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
import com.tb.nextstop.data.StopFeature
import com.tb.nextstop.data.StopSchedule
import com.tb.nextstop.data.WPTRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface StopScheduleUIState {
    data class Success(
        val stopSchedule: StopSchedule,
        val stopFeatures: List<StopFeature>
    ) : StopScheduleUIState

    data object Error : StopScheduleUIState
    data object Loading : StopScheduleUIState
}

class StopScheduleViewModel(
    savedStateHandle: SavedStateHandle,
    private val wptRepository: WPTRepository,
) : ViewModel() {
    private val stopId: Int = checkNotNull(savedStateHandle[StopScheduleDestination.STOP_ID_ARG])
    var stopScheduleUIState: StopScheduleUIState by mutableStateOf(StopScheduleUIState.Loading)
        private set

    init {
        getStopSchedule()
    }

    private fun getStopSchedule() {
        viewModelScope.launch {
            stopScheduleUIState = StopScheduleUIState.Loading

            try {
                val stopSchedule = wptRepository.getStopSchedule(stopId)
                val stopFeatures = wptRepository.getStopFeatures(stopId)
                stopScheduleUIState = StopScheduleUIState.Success(
                    stopSchedule = stopSchedule,
                    stopFeatures = stopFeatures
                )
            } catch (e: HttpException) {
                Log.d("VM", "Error getting stop schedule for $stopId\n$e")
                stopScheduleUIState = StopScheduleUIState.Error
            } catch (e: IOException) {
                Log.d("VM", "Error getting stop schedule for $stopId\n$e")
                stopScheduleUIState = StopScheduleUIState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NextStopApplication)
                val wptRepository = application.container.wptRepository
                StopScheduleViewModel(
                    this.createSavedStateHandle(),
                    wptRepository = wptRepository
                )
            }
        }
    }
}

object StopScheduleDestination {
    const val NAME = "Stop Schedule"
    const val ROUTE = "stop_schedule"
    const val STOP_ID_ARG = "stopId"
    const val ROUTE_WITH_ARGS = "$ROUTE/{$STOP_ID_ARG}"
}
