package com.tb.nextstop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tb.nextstop.ui.NextStopApp
import com.tb.nextstop.ui.theme.NextStopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NextStopTheme {
                NextStopApp()
            }
        }
    }
}
