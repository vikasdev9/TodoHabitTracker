package com.example.todoappnew.presentation.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoappnew.domain.model.AppBackground
import com.example.todoappnew.domain.model.AppTheme
import com.example.todoappnew.presentation.ui.components.GlassCard
import com.example.todoappnew.presentation.ui.components.PremiumBackground
import com.example.todoappnew.presentation.ui.screens.settings.SettingsEvent
import com.example.todoappnew.presentation.ui.screens.settings.SettingsViewModel
import com.example.todoappnew.presentation.ui.theme.GlassCardBg
import com.example.todoappnew.presentation.ui.theme.NeonPink
import com.example.todoappnew.presentation.ui.theme.NeonPurple

@Composable
fun ProfileScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsState by settingsViewModel.uiState.collectAsState()
    var displayName by remember { mutableStateOf("vikaschauhan0368") }

    PremiumBackground {
        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { padding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),

                contentPadding = PaddingValues(
                    top = WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding() + 24.dp,

                    bottom = WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding() + 100.dp
                )
            ) {

                item { ProfileHeader() }

                item { UserInfoCard(displayName) }

                item {
                    DisplayNameSection(displayName) {
                        displayName = it
                    }
                }

                item {
                    AppearanceSection(settingsState.theme) {
                        settingsViewModel.onEvent(
                            SettingsEvent.ThemeChanged(it)
                        )
                    }
                }

                item { AccentColorSection() }

                item {
                    BackgroundThemeSection(settingsState.background) {
                        settingsViewModel.onEvent(
                            SettingsEvent.BackgroundChanged(it)
                        )
                    }
                }

                item { NotificationSection() }

                item { LogoutSection() }
            }
        }
    }
}

@Composable
fun ProfileHeader() {
    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
        Text(
            text = "Customize your experience.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun UserInfoCard(name: String) {
    GlassCard(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(NeonPink.copy(alpha = 0.2f))
                    .border(2.dp, NeonPink.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.firstOrNull()?.toString()?.uppercase() ?: "U",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = NeonPink
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Text(
                    text = "$name@gmail.com",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun DisplayNameSection(name: String, onNameChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "Display name",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = name,
            onValueChange = onNameChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(GlassCardBg),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = NeonPink,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White.copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Save action */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Save", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun AppearanceSection(selectedTheme: AppTheme, onThemeChange: (AppTheme) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Palette, null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ThemeCard("Light", Icons.Rounded.LightMode, selectedTheme == AppTheme.LIGHT, Modifier.weight(1f)) { onThemeChange(AppTheme.LIGHT) }
            ThemeCard("Dark", Icons.Rounded.DarkMode, selectedTheme == AppTheme.DARK, Modifier.weight(1f)) { onThemeChange(AppTheme.DARK) }
            ThemeCard("System", Icons.Rounded.SettingsSuggest, selectedTheme == AppTheme.SYSTEM, Modifier.weight(1f)) { onThemeChange(AppTheme.SYSTEM) }
        }
    }
}

@Composable
fun ThemeCard(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(80.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) NeonPink else GlassCardBg)
            .border(1.dp, if (isSelected) NeonPink else Color.Transparent, RoundedCornerShape(20.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun AccentColorSection() {
    val colors = listOf(
        "Blue" to Color(0xFF42A5F5),
        "Violet" to Color(0xFF7E57C2),
        "Emerald" to Color(0xFF66BB6A),
        "Rose" to NeonPink,
        "Amber" to Color(0xFFFFCA28),
        "Cyan" to Color(0xFF26C6DA)
    )
    var selectedColor by remember { mutableStateOf("Rose") }

    Column(modifier = Modifier.padding(24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.WaterDrop, null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Accent color",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AccentCard(colors[0].first, colors[0].second, selectedColor == colors[0].first, Modifier.weight(1f)) { selectedColor = colors[0].first }
                AccentCard(colors[1].first, colors[1].second, selectedColor == colors[1].first, Modifier.weight(1f)) { selectedColor = colors[1].first }
                AccentCard(colors[2].first, colors[2].second, selectedColor == colors[2].first, Modifier.weight(1f)) { selectedColor = colors[2].first }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AccentCard(colors[3].first, colors[3].second, selectedColor == colors[3].first, Modifier.weight(1f)) { selectedColor = colors[3].first }
                AccentCard(colors[4].first, colors[4].second, selectedColor == colors[4].first, Modifier.weight(1f)) { selectedColor = colors[4].first }
                AccentCard(colors[5].first, colors[5].second, selectedColor == colors[5].first, Modifier.weight(1f)) { selectedColor = colors[5].first }
            }
        }
    }
}

@Composable
fun AccentCard(label: String, color: Color, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(64.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) color.copy(alpha = 0.2f) else GlassCardBg)
            .border(2.dp, if (isSelected) color else Color.Transparent, RoundedCornerShape(20.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(color))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Bold)
            if (isSelected) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Rounded.Check, null, tint = color, modifier = Modifier.size(14.dp))
            }
        }
    }
}

@Composable
fun BackgroundThemeSection(selectedBg: AppBackground, onBgChange: (AppBackground) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Image, null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Background theme",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Grid of background themes
        val backgrounds = AppBackground.entries
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            for (i in backgrounds.indices step 2) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    BackgroundCard(backgrounds[i], selectedBg == backgrounds[i], Modifier.weight(1f)) { onBgChange(backgrounds[i]) }
                    if (i + 1 < backgrounds.size) {
                        BackgroundCard(backgrounds[i + 1], selectedBg == backgrounds[i + 1], Modifier.weight(1f)) { onBgChange(backgrounds[i + 1]) }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun BackgroundCard(bg: AppBackground, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(100.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, if (isSelected) NeonPink else Color.Transparent, RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        // Mock image with gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Gray, Color.DarkGray) // Replace with actual preview colors
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = bg.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            if (isSelected) {
                Icon(
                    Icons.Rounded.CheckCircle,
                    null,
                    tint = NeonPink,
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).size(20.dp)
                )
            }
        }
    }
}

@Composable
fun NotificationSection() {
    var isEnabled by remember { mutableStateOf(true) }
    GlassCard(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Notifications, null, tint = Color.White.copy(alpha = 0.7f))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Notifications", style = MaterialTheme.typography.bodyLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Reminders for due tasks", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.5f))
                }
            }
            Switch(
                checked = isEnabled,
                onCheckedChange = { isEnabled = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = NeonPink,
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = GlassCardBg
                )
            )
        }
    }
}

@Composable
fun LogoutSection() {
    Button(
        onClick = { /* Logout */ },
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = GlassCardBg),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Rounded.Logout, null, tint = NeonPink, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sign out", color = NeonPink, fontWeight = FontWeight.Bold)
        }
    }
}
