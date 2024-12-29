package com.tb.nextstop.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tb.nextstop.data.StopsResponse
import com.tb.nextstop.network.WPTApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface StopsUIState {
    data class Success(val stopsResponse: StopsResponse): StopsUIState
    object Error: StopsUIState
    object Loading: StopsUIState
}

class NearbyScreenViewModel: ViewModel() {
    var stopsUIState: StopsUIState by mutableStateOf(StopsUIState.Loading)
        private set

    init {
        getNearbyStops()
    }

    fun getNearbyStops() {
        viewModelScope.launch {
            stopsUIState = StopsUIState.Loading
            stopsUIState = try {
                val stopsResponse = WPTApi.retrofitService.getNearbyStops()
                Log.d("VM", stopsResponse.stops.size.toString())
                StopsUIState.Success(stopsResponse)
            } catch(e: HttpException) {
                Log.d("VM", e.toString())
                StopsUIState.Error
            } catch(e: IOException) {
                Log.d("VM", e.toString())
                StopsUIState.Error
            }
        }
    }
}