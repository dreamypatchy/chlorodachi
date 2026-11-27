package com.stupid.chlorodachi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.stupid.chlorodachi.ui.DashboardRoute
import com.stupid.chlorodachi.ui.theme.ChlorodachiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChlorodachiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ChlorodachiApp()
                }
            }
        }
    }
}

@Composable
fun ChlorodachiApp() {
    DashboardRoute()
}