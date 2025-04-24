package com.tb.nextstop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tb.nextstop.ui.NextStopApp
import com.tb.nextstop.ui.theme.NextStopTheme
import org.maplibre.android.MapLibre
import org.maplibre.android.utils.ThreadUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ThreadUtils.init(this)
        MapLibre.getInstance(this)

        enableEdgeToEdge()
        setContent {
            NextStopTheme {
                NextStopApp()
            }
        }
    }
}
