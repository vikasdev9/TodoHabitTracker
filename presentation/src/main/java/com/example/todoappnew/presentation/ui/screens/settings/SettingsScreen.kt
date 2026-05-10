package com.example.todoappnew.presentation.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todoappnew.domain.model.AppTheme
import com.example.todoappnew.domain.model.AppBackground
import com.example.todoappnew.presentation.util.BackgroundProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ThemeSettingsSection(
                selectedTheme = state.theme,
                onThemeSelected = { viewModel.onEvent(SettingsEvent.ThemeChanged(it)) }
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            BackgroundSettingsSection(
                selectedBackground = state.background,
                onBackgroundSelected = { viewModel.onEvent(SettingsEvent.BackgroundChanged(it)) }
            )
        }
    }
}

@Composable
fun ThemeSettingsSection(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "App Theme",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        Column(Modifier.selectableGroup()) {
            ThemeOption(
                label = "Light Mode",
                selected = selectedTheme == AppTheme.LIGHT,
                onClick = { onThemeSelected(AppTheme.LIGHT) }
            )
            ThemeOption(
                label = "Dark Mode",
                selected = selectedTheme == AppTheme.DARK,
                onClick = { onThemeSelected(AppTheme.DARK) }
            )
            ThemeOption(
                label = "System Default",
                selected = selectedTheme == AppTheme.SYSTEM,
                onClick = { onThemeSelected(AppTheme.SYSTEM) }
            )
        }
    }
}

@Composable
fun ThemeOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun BackgroundSettingsSection(
    selectedBackground: AppBackground,
    onBackgroundSelected: (AppBackground) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Background theme",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))

        val backgroundItems = listOf(
            AppBackground.DEFAULT to "Default",
            AppBackground.AURORA to "Aurora",
            AppBackground.SUNSET to "Sunset",
            AppBackground.OCEAN to "Ocean",
            AppBackground.MINT to "Mint",
            AppBackground.PEACH to "Peach",
            AppBackground.MIDNIGHT to "Midnight",
            AppBackground.LAVENDER to "Lavender",
            AppBackground.SHANGHAI to "Shanghai",
            AppBackground.TOKYO to "Tokyo",
            AppBackground.SAN_FRANCISCO to "San Francisco",
            AppBackground.SYDNEY to "Sydney",
            AppBackground.SPRING to "Spring",
            AppBackground.SUMMER to "Summer",
            AppBackground.AUTUMN to "Autumn",
            AppBackground.WINTER to "Winter",
            AppBackground.AURORA_SKY to "Aurora Sky"
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(650.dp) // Height increased for new items
        ) {
            items(backgroundItems) { (bg, label) ->
                BackgroundThumbnail(
                    bg = bg,
                    label = label,
                    isSelected = selectedBackground == bg,
                    onClick = { onBackgroundSelected(bg) }
                )
            }
        }
    }
}

@Composable
fun BackgroundThumbnail(
    bg: AppBackground,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundImage = BackgroundProvider.getBackgroundImage(bg)
    val backgroundBrush = BackgroundProvider.getBackgroundBrush(bg)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.6f)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .background(backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        if (backgroundImage != null) {
            Image(
                painter = painterResource(id = backgroundImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Overlay for label and selection
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = Color.White
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
