package com.tb.nextstop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tb.nextstop.ui.livetrip.LiveTripDestination
import com.tb.nextstop.ui.livetrip.LiveTripScreen
import com.tb.nextstop.ui.map.MapScreen
import com.tb.nextstop.ui.nearby.NearbyScreen
import com.tb.nextstop.ui.saved.SavedScreen
import com.tb.nextstop.ui.stopschedule.StopScheduleDestination
import com.tb.nextstop.ui.stopschedule.StopScheduleScreen


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

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            selectedScreen = when (destination.route) {
                NextStopApp.Nearby.name -> NextStopApp.Nearby.name
                NextStopApp.Map.name -> NextStopApp.Map.name
                NextStopApp.Saved.name -> NextStopApp.Saved.name
                LiveTripDestination.ROUTE_WITH_ARGS -> LiveTripDestination.NAME
                StopScheduleDestination.ROUTE_WITH_ARGS -> StopScheduleDestination.NAME
                else -> ""
            }
        }
    }

    Scaffold(
        topBar = {
            NextStopAppBar(titleText = selectedScreen)
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
            modifier = Modifier
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
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
                SavedScreen(
                    onStopClicked = {
                        navController.navigate("${StopScheduleDestination.ROUTE}/$it")
                    }
                )
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
    titleText: String,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = titleText,
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