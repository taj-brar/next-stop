package com.tb.nextstop.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tb.nextstop.R


enum class NextStopApp {
    Nearby,
    Map,
    Saved,
}

@Composable
fun NextStopApp(
    navController: NavHostController = rememberNavController()
) {
    var selectedScreen by remember { mutableStateOf(NextStopApp.Nearby.name) }

    Scaffold(
        topBar = {
            NextStopAppBar()
        },
        bottomBar = {
            NavigationBar(
                onNearbyClicked = {
                    val nearbyName = NextStopApp.Nearby.name
                    navController.navigate(nearbyName)
                    selectedScreen = nearbyName
                },
                onMapClicked = {
                    val mapName = NextStopApp.Map.name
                    navController.navigate(mapName)
                    selectedScreen = mapName
                },
                onSavedClicked = {
                    val savedName = NextStopApp.Saved.name
                    navController.navigate(savedName)
                    selectedScreen = savedName
                },
                selectedScreen = selectedScreen
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NextStopApp.Nearby.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NextStopApp.Nearby.name) {
                NearbyScreen(
                    onStopClicked = {
                        navController.navigate("${StopScheduleDestination.ROUTE}/$it")
                    }
                )
            }
            composable(NextStopApp.Map.name) {
                MapScreen()
            }
            composable(NextStopApp.Saved.name) {
                SavedScreen()
            }
            composable(
                route = StopScheduleDestination.ROUTE_WITH_ARGS,
                arguments = listOf(navArgument(StopScheduleDestination.STOP_ID_ARG) {
                    type = NavType.IntType
                })
            ) {
                StopScheduleScreen(
                    onScheduledStopClicked = {
                        navController.navigate("${LiveTripDestination.ROUTE}/$it")
                    }
                )
            }
            composable(
                route = LiveTripDestination.ROUTE_WITH_ARGS,
                arguments = listOf(navArgument(LiveTripDestination.TRIP_ID_ARG) {
                    type = NavType.IntType
                })
            ) {
                LiveTripScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NextStopAppBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
    )
}