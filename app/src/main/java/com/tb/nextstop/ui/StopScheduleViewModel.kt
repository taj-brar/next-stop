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
import com.tb.nextstop.data.StopSchedule
import com.tb.nextstop.data.WPTRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface StopScheduleUIState {
    data class Success(
        val stopSchedule: StopSchedule,
    ) : StopScheduleUIState

    data object Error : StopScheduleUIState
    data object Loading : StopScheduleUIState
}

class StopScheduleViewModel(
    private val wptRepository: WPTRepository,
) : ViewModel()
{
    var stopScheduleUIState: StopScheduleUIState by mutableStateOf(StopScheduleUIState.Loading)
        private set

    init {
        getStopSchedule()
    }

    fun getStopSchedule() {
        val stopId = 10627
        viewModelScope.launch {
            stopScheduleUIState = StopScheduleUIState.Loading
            var stopSchedule = StopSchedule()

            try {
                stopSchedule = wptRepository.getStopSchedule(stopId)
            } catch (e: HttpException) {
                Log.d("VM", "Error getting stop schedule for $stopId\n$e")
                StopScheduleUIState.Error
            } catch (e: IOException) {
                Log.d("VM", "Error getting stop schedule for $stopId\n$e")
                StopScheduleUIState.Error
            }

            stopScheduleUIState = StopScheduleUIState.Success(stopSchedule)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NextStopApplication)
                val wptRepository = application.container.wptRepository
                StopScheduleViewModel(wptRepository = wptRepository)
            }
        }
    }
}
