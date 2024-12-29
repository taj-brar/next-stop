package com.tb.nextstop.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tb.nextstop.network.WPTApi
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NearbyScreenViewModel: ViewModel() {
    var stopsUIState: String by mutableStateOf("")
        private set

    init {
        getNearbyStops()
    }

    fun getNearbyStops() {
        viewModelScope.launch {
            try {
                val retro = WPTApi.retrofitService
                val listResult = retro.getNearbyStops()
            } catch(e: HttpException) {
                Log.d("VM", e.toString())
            }
        }
    }
}