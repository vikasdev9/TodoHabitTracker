package com.example.todoappnew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.todoappnew.presentation.navigation.TodoNavHost
import com.example.todoappnew.presentation.ui.theme.TodoAppTheme
import com.example.todoappnew.domain.model.TaskStatus
import com.example.todoappnew.domain.model.AppTheme
import com.example.todoappnew.domain.model.AppBackground
import com.example.todoappnew.presentation.ui.screens.home.HomeViewModel
import com.example.todoappnew.presentation.ui.screens.home.HomeEvent
import com.example.todoappnew.presentation.ui.screens.settings.SettingsViewModel
import com.example.todoappnew.presentation.util.BackgroundProvider
import com.example.todoappnew.util.DynamicIconManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var iconManager: DynamicIconManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsState by settingsViewModel.uiState.collectAsState()
            
            LaunchedEffect(Unit) {
                settingsViewModel.themeChangedEvent.collectLatest { theme ->
                    iconManager.updateIcon(theme)
                }
            }

            val darkTheme = when (settingsState.theme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }

            TodoAppTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                
                val homeViewModel: HomeViewModel = hiltViewModel()
                val homeState by homeViewModel.uiState.collectAsState()

                splashScreen.setKeepOnScreenCondition {
                    homeState.isLoading
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Global Background implementation using Brush (Gradient)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(BackgroundProvider.getBackgroundBrush(settingsState.background))
                        )

                        // Subtle overlay to ensure UI elements are readable
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                        )

                        ModalNavigationDrawer(
                            drawerState = drawerState,
                            drawerContent = {
                                ModalDrawerSheet(
                                    drawerContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                                ) {
                                    Text("Todo App", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                                    HorizontalDivider()
                                    NavigationDrawerItem(
                                        icon = { Icon(Icons.Default.Home, null) },
                                        label = { Text("All Tasks") },
                                        selected = false,
                                        onClick = {
                                            scope.launch { drawerState.close() }
                                            homeViewModel.onEvent(HomeEvent.FilterChanged(null))
                                            navController.navigate("home")
                                        }
                                    )
                                    NavigationDrawerItem(
                                        icon = { Icon(Icons.Default.Done, null) },
                                        label = { Text("Completed") },
                                        selected = false,
                                        onClick = {
                                            scope.launch { drawerState.close() }
                                            homeViewModel.onEvent(HomeEvent.FilterChanged(TaskStatus.COMPLETED))
                                        }
                                    )
                                    NavigationDrawerItem(
                                        icon = { Icon(Icons.Default.PlayArrow, null) },
                                        label = { Text("Running") },
                                        selected = false,
                                        onClick = {
                                            scope.launch { drawerState.close() }
                                            homeViewModel.onEvent(HomeEvent.FilterChanged(TaskStatus.RUNNING))
                                        }
                                    )
                                    NavigationDrawerItem(
                                        icon = { Icon(Icons.Default.PendingActions, null) },
                                        label = { Text("Pending") },
                                        selected = false,
                                        onClick = {
                                            scope.launch { drawerState.close() }
                                            homeViewModel.onEvent(HomeEvent.FilterChanged(TaskStatus.PENDING))
                                        }
                                    )
                                    HorizontalDivider()
                                    NavigationDrawerItem(
                                        icon = { Icon(Icons.Default.Settings, null) },
                                        label = { Text("Settings") },
                                        selected = false,
                                        onClick = {
                                            scope.launch { drawerState.close() }
                                            navController.navigate("settings")
                                        }
                                    )
                                }
                            },
                            scrimColor = Color.Black.copy(alpha = 0.32f)
                        ) {
                            TodoNavHost(
                                navController = navController,
                                onOpenDrawer = { scope.launch { drawerState.open() } }
                            )
                        }
                    }
                }
            }
        }
    }
}
