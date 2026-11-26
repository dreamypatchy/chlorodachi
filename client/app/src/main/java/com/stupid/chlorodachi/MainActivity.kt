package com.stupid.chlorodachi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.stupid.chlorodachi.ui.DashboardScreen
import com.stupid.chlorodachi.ui.theme.ChlorodachiTheme
import com.stupid.chlorodachi.ui.LoginScreen

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
    DashboardScreen(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
}