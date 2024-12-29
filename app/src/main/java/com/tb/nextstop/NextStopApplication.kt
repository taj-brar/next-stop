package com.tb.nextstop

import android.app.Application
import com.tb.nextstop.data.AppContainer
import com.tb.nextstop.data.DefaultAppContainer

class NextStopApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}