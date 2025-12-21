package com.digicolor.rahuldemo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.digicolor.rahuldemo.R
import com.digicolor.rahuldemo.presentation.theme.RahulDemoTheme
import com.digicolor.rahuldemo.presentation.ui.screens.portfolio.PortfolioRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableETE()
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
            val snackbarHostState = remember { SnackbarHostState() }
            val noInternetMessage = stringResource(R.string.error_no_internet)

            // Centralized observer for internet connectivity
            LaunchedEffect(isOnline) {
                if (!isOnline) {
                    snackbarHostState.showSnackbar(
                        message = noInternetMessage,
                        duration = SnackbarDuration.Indefinite
                    )
                } else {
                    snackbarHostState.currentSnackbarData?.dismiss()
                }
            }
            RahulDemoTheme {
                AppNavigation(snackbarHostState)
            }
        }
    }

    private fun enableETE() {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
    }
}

@Composable
fun AppNavigation(snackbarHostState: SnackbarHostState) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "portfolio",
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable("portfolio") {
                PortfolioRoute()
            }
        }
    }
}