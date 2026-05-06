package com.example.todoappnew.presentation.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoappnew.domain.model.ViewType
import com.example.todoappnew.presentation.ui.components.SearchBar
import com.example.todoappnew.presentation.ui.components.TaskCompactItem
import com.example.todoappnew.presentation.ui.components.TaskGridItem
import com.example.todoappnew.presentation.ui.components.TaskItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToDetail: (String) -> Unit,
    onOpenDrawer: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showViewMenu by remember { mutableStateOf(false) }

    LaunchedEffect(state.syncMessage) {
        state.syncMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(HomeEvent.DismissSyncMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("My Tasks", style = MaterialTheme.typography.headlineSmall) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    if (state.isSyncing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    } else {
                        IconButton(onClick = { viewModel.onEvent(HomeEvent.SyncTasks) }) {
                            Icon(Icons.Default.CloudSync, contentDescription = "Sync Tasks")
                        }
                    }
                    Box {
                        IconButton(onClick = { showViewMenu = true }) {
                            val viewIcon = when (state.viewType) {
                                ViewType.LIST -> Icons.AutoMirrored.Filled.ViewList
                                ViewType.GRID -> Icons.Default.GridView
                                ViewType.COMPACT -> Icons.Default.ViewStream
                            }
                            Icon(viewIcon, contentDescription = "Change View")
                        }
                        DropdownMenu(
                            expanded = showViewMenu,
                            onDismissRequest = { showViewMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("List View") },
                                onClick = {
                                    viewModel.onEvent(HomeEvent.ChangeViewType(ViewType.LIST))
                                    showViewMenu = false
                                },
                                leadingIcon = {
                                    Icon(Icons.AutoMirrored.Filled.ViewList, null)
                                },
                                trailingIcon = {
                                    if (state.viewType == ViewType.LIST) {
                                        Icon(Icons.Default.Check, null)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Grid View") },
                                onClick = {
                                    viewModel.onEvent(HomeEvent.ChangeViewType(ViewType.GRID))
                                    showViewMenu = false
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.GridView, null)
                                },
                                trailingIcon = {
                                    if (state.viewType == ViewType.GRID) {
                                        Icon(Icons.Default.Check, null)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Compact View") },
                                onClick = {
                                    viewModel.onEvent(HomeEvent.ChangeViewType(ViewType.COMPACT))
                                    showViewMenu = false
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.ViewStream, null)
                                },
                                trailingIcon = {
                                    if (state.viewType == ViewType.COMPACT) {
                                        Icon(Icons.Default.Check, null)
                                    }
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToDetail("") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            SearchBar(
                query = state.searchQuery,
                onQueryChange = { viewModel.onEvent(HomeEvent.SearchQueryChanged(it)) }
            )

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (state.tasks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tasks found matching your criteria.")
                }
            } else {
                when (state.viewType) {
                    ViewType.LIST -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.tasks, key = { it.id }) { task ->
                                TaskItem(
                                    task = task,
                                    onTaskClick = { onNavigateToDetail(it.id) },
                                    onDeleteClick = { viewModel.onEvent(HomeEvent.DeleteTask(task)) },
                                    onToggleCompletion = { viewModel.onEvent(HomeEvent.ToggleTaskCompletion(task)) }
                                )
                            }
                        }
                    }
                    ViewType.GRID -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.tasks, key = { it.id }) { task ->
                                TaskGridItem(
                                    task = task,
                                    onTaskClick = { onNavigateToDetail(it.id) },
                                    onToggleCompletion = { viewModel.onEvent(HomeEvent.ToggleTaskCompletion(task)) }
                                )
                            }
                        }
                    }
                    ViewType.COMPACT -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(state.tasks, key = { it.id }) { task ->
                                TaskCompactItem(
                                    task = task,
                                    onTaskClick = { onNavigateToDetail(it.id) },
                                    onToggleCompletion = { viewModel.onEvent(HomeEvent.ToggleTaskCompletion(task)) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
