package com.example.todoappnew.presentation.ui.screens.profile

import androidx.compose.animation.core.*
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.todoappnew.presentation.ui.theme.TodoAppTheme
import com.example.todoappnew.presentation.util.BackgroundProvider

@Composable
fun ProfileScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsState by settingsViewModel.uiState.collectAsState()

    ProfileScreenContent(
        theme = settingsState.theme,
        background = settingsState.background,
        selectedPreviewBackground = settingsState.selectedPreviewBackground,
        onThemeChanged = {
            settingsViewModel.onEvent(SettingsEvent.ThemeChanged(it))
        },
        onBackgroundChanged = {
            settingsViewModel.onEvent(SettingsEvent.BackgroundChanged(it))
        },
        onShowPreview = {
            settingsViewModel.onEvent(SettingsEvent.ShowPreview(it))
        },
        onDismissPreview = {
            settingsViewModel.onEvent(SettingsEvent.DismissPreview)
        }
    )
}

@Composable
fun ProfileScreenContent(
    theme: AppTheme,
    background: AppBackground,
    selectedPreviewBackground: AppBackground?,
    onThemeChanged: (AppTheme) -> Unit,
    onBackgroundChanged: (AppBackground) -> Unit,
    onShowPreview: (AppBackground) -> Unit,
    onDismissPreview: () -> Unit
) {
    var displayName by remember { mutableStateOf("vikaschauhan0368") }

    Box(modifier = Modifier.fillMaxSize()) {
        PremiumBackground(background = background) {
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
                        AppearanceSection(theme) {
                            onThemeChanged(it)
                        }
                    }

                    item { AccentColorSection() }

                    item {
                        BackgroundThemeSection(background) {
                            onShowPreview(it)
                        }
                    }

                    item { NotificationSection() }

                    item { LogoutSection() }
                }
            }
        }

        // Preview Overlay
        AnimatedVisibility(
            visible = selectedPreviewBackground != null,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it }
        ) {
            selectedPreviewBackground?.let { previewBg ->
                com.example.todoappnew.presentation.ui.screens.settings.BackgroundPreviewScreen(
                    background = previewBg,
                    onCancel = onDismissPreview,
                    onConfirm = { onBackgroundChanged(previewBg) }
                )
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
fun UserInfoCard(
    name: String,
    email: String = "$name@gmail.com"
) {

    val cardGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF4E7F5),
            Color(0xFFEAD8EC)
        )
    )

    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {

        Box(
            modifier = Modifier
                .background(cardGradient)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 20.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Avatar
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFB57BFF),
                                    Color(0xFF9D6BFF)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = name.first().uppercase(),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.width(18.dp))

                Column {

                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1E1E1E)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6B6B6B)
                    )
                }
            }
        }
    }
}

@Composable
fun DisplayNameSection(
    name: String,
    onNameChange: (String) -> Unit
) {

    val cardGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF4E7F5),
            Color(0xFFE9D8EC)
        )
    )

    Card(
        modifier = Modifier
            .padding(
                horizontal = 20.dp,
                vertical = 12.dp
            )
            .fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {

        Column(
            modifier = Modifier
                .background(cardGradient)
                .padding(24.dp)
        ) {

            // Title
            Text(
                text = "Display name",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color(0xFF1E1E1E)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Input
            TextField(
                value = name,
                onValueChange = onNameChange,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = TextFieldDefaults.colors(

                    focusedContainerColor = Color(0xFFF7F7F7),
                    unfocusedContainerColor = Color(0xFFF7F7F7),

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                    cursorColor = Color(0xFF9D6BFF),

                    focusedTextColor = Color(0xFF1E1E1E),
                    unfocusedTextColor = Color(0xFF1E1E1E),

                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                ),
                placeholder = {
                    Text("Enter display name")
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Save Button
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF5C93)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {

                Text(
                    text = "Save",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AppearanceSection(
    selectedTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit
) {

    Card(
        modifier = Modifier
            .padding(
                horizontal = 20.dp,
                vertical = 12.dp
            )
            .fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF1E4F1)
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Rounded.Palette,
                    contentDescription = null,
                    tint = Color(0xFF2D2D2D),
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Appearance",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFF2B2B2B)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                CompactThemeCard(
                    title = "Light",
                    icon = Icons.Rounded.LightMode,
                    selected = selectedTheme == AppTheme.LIGHT,
                    modifier = Modifier.weight(1f)
                ) {
                    onThemeChange(AppTheme.LIGHT)
                }

                CompactThemeCard(
                    title = "Dark",
                    icon = Icons.Rounded.DarkMode,
                    selected = selectedTheme == AppTheme.DARK,
                    modifier = Modifier.weight(1f)
                ) {
                    onThemeChange(AppTheme.DARK)
                }

                CompactThemeCard(
                    title = "System",
                    icon = Icons.Rounded.SettingsSuggest,
                    selected = selectedTheme == AppTheme.SYSTEM,
                    modifier = Modifier.weight(1f)
                ) {
                    onThemeChange(AppTheme.SYSTEM)
                }
            }
        }
    }
}

@Composable
fun CompactThemeCard(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    val bgColor =
        if (selected) {
            Color(0xFFFF5C93)
        } else {
            Color(0xFFF7F7F7)
        }

    val contentColor =
        if (selected) {
            Color.White
        } else {
            Color(0xFF2D2D2D)
        }

    Box(
        modifier = modifier
            .height(92.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(bgColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                color = contentColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun AccentColorSection() {

    val colors = listOf(
        "Blue" to Color(0xFF5B8DEF),
        "Violet" to Color(0xFF9B6DFF),
        "Emerald" to Color(0xFF43C97A),
        "Rose" to Color(0xFFFF5C93),
        "Amber" to Color(0xFFF5B544),
        "Cyan" to Color(0xFF32C7E2)
    )

    var selectedColor by remember {
        mutableStateOf("Rose")
    }

    Card(
        modifier = Modifier
            .padding(
                horizontal = 20.dp,
                vertical = 12.dp
            )
            .fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF1E4F1)
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Rounded.WaterDrop,
                    contentDescription = null,
                    tint = Color(0xFF2D2D2D),
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Accent color",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFF2D2D2D)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    AccentCard(
                        colors[0].first,
                        colors[0].second,
                        selectedColor == colors[0].first,
                        Modifier.weight(1f)
                    ) {
                        selectedColor = colors[0].first
                    }

                    AccentCard(
                        colors[1].first,
                        colors[1].second,
                        selectedColor == colors[1].first,
                        Modifier.weight(1f)
                    ) {
                        selectedColor = colors[1].first
                    }

                    AccentCard(
                        colors[2].first,
                        colors[2].second,
                        selectedColor == colors[2].first,
                        Modifier.weight(1f)
                    ) {
                        selectedColor = colors[2].first
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    AccentCard(
                        colors[3].first,
                        colors[3].second,
                        selectedColor == colors[3].first,
                        Modifier.weight(1f)
                    ) {
                        selectedColor = colors[3].first
                    }

                    AccentCard(
                        colors[4].first,
                        colors[4].second,
                        selectedColor == colors[4].first,
                        Modifier.weight(1f)
                    ) {
                        selectedColor = colors[4].first
                    }

                    AccentCard(
                        colors[5].first,
                        colors[5].second,
                        selectedColor == colors[5].first,
                        Modifier.weight(1f)
                    ) {
                        selectedColor = colors[5].first
                    }
                }
            }
        }
    }
}

@Composable
fun AccentCard(
    label: String,
    color: Color,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {

    val bgColor =
        if (isSelected) {
            color.copy(alpha = 0.95f)
        } else {
            Color(0xFFF8F8F8)
        }

    val textColor =
        if (isSelected) {
            Color.White
        } else {
            Color(0xFF2D2D2D)
        }

    Box(
        modifier = modifier
            .height(74.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(bgColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) {
                            Color.Transparent
                        } else {
                            color
                        }
                    )
                    .border(
                        width = 2.dp,
                        color =
                            if (isSelected) {
                                Color.White.copy(alpha = 0.9f)
                            } else {
                                Color.Transparent
                            },
                        shape = CircleShape
                    )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = label,
                color = textColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )

            if (isSelected) {

                Spacer(modifier = Modifier.width(6.dp))

                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun BackgroundThemeSection(
    selectedBg: AppBackground,
    onBgChange: (AppBackground) -> Unit
) {

    Card(
        modifier = Modifier
            .padding(
                horizontal = 20.dp,
                vertical = 12.dp
            )
            .fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF1E4F1)
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Rounded.Image,
                    contentDescription = null,
                    tint = Color(0xFF2D2D2D),
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Background theme",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFF2D2D2D)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val backgrounds = AppBackground.entries

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                for (i in backgrounds.indices step 2) {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        BackgroundCard(
                            bg = backgrounds[i],
                            isSelected = selectedBg == backgrounds[i],
                            modifier = Modifier.weight(1f)
                        ) {
                            onBgChange(backgrounds[i])
                        }

                        if (i + 1 < backgrounds.size) {

                            BackgroundCard(
                                bg = backgrounds[i + 1],
                                isSelected = selectedBg == backgrounds[i + 1],
                                modifier = Modifier.weight(1f)
                            ) {
                                onBgChange(backgrounds[i + 1])
                            }

                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BackgroundCard(
    bg: AppBackground,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "GlowTransition")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowAlpha"
    )

    Box(
        modifier = modifier
            .height(110.dp)
            .clip(RoundedCornerShape(24.dp))
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = NeonPink.copy(alpha = glowAlpha),
                        shape = RoundedCornerShape(24.dp)
                    )
                } else {
                    Modifier.border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(24.dp)
                    )
                }
            )
            .clickable { onClick() }
    ) {
        // Background Preview (Gradient or Image)
        val backgroundImage = BackgroundProvider.getBackgroundImage(bg)
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundProvider.getBackgroundBrush(bg))
        ) {
            if (backgroundImage != null) {
                Image(
                    painter = painterResource(id = backgroundImage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Glassmorphism overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f)
                        )
                    )
                )
        )

        // Theme Name
        Text(
            text = bg.name
                .replace("_", " ")
                .lowercase()
                .replaceFirstChar { it.uppercase() },
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )

        // Selected check
        if (isSelected) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(NeonPink)
                    .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun NotificationSection() {

    var isEnabled by remember {
        mutableStateOf(true)
    }

    Card(
        modifier = Modifier
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp
            )
            .fillMaxWidth(),

        shape = RoundedCornerShape(28.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8D9EA)
        ),

        border = BorderStroke(
            1.dp,
            Color.White.copy(alpha = 0.4f)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 16.dp
                ),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Rounded.NotificationsNone,
                    contentDescription = null,

                    tint = Color(0xFF2A2A2A),

                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column {

                    Text(
                        text = "Notifications",

                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),

                        color = Color(0xFF1E1E1E)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Reminders for due tasks",

                        style = MaterialTheme.typography.bodySmall,

                        color = Color(0xFF666666)
                    )
                }
            }

            Switch(
                checked = isEnabled,

                onCheckedChange = {
                    isEnabled = it
                },

                colors = SwitchDefaults.colors(

                    checkedThumbColor = Color.White,

                    checkedTrackColor = Color(0xFF9B6DFF),

                    uncheckedThumbColor = Color.White,

                    uncheckedTrackColor = Color.LightGray,

                    checkedBorderColor = Color.Transparent,

                    uncheckedBorderColor = Color.Transparent
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
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp
            )
            .navigationBarsPadding()
            .fillMaxWidth()
            .height(58.dp),

        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE8D9EA)
        ),

        shape = RoundedCornerShape(30.dp),

        border = BorderStroke(
            1.dp,
            Color.White.copy(alpha = 0.25f)
        ),

        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp
        )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Logout,
                contentDescription = null,

                tint = NeonPink,

                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Sign out",

                color = NeonPink,

                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    TodoAppTheme {
        ProfileScreenContent(
            theme = AppTheme.DARK,
            background = AppBackground.AURORA,
            selectedPreviewBackground = null,
            onThemeChanged = {},
            onBackgroundChanged = {},
            onShowPreview = {},
            onDismissPreview = {}
        )
    }
}
