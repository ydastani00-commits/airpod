package com.example

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AdvancedSettingsScreen
import com.example.ui.screens.BatteryScreen
import com.example.ui.screens.EqualizerScreen
import com.example.ui.screens.FindAirPodsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SoundControlScreen
import com.example.ui.screens.SoundModesScreen
import com.example.ui.theme.AirPodsTheme
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AirPodsTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    AirPodsApp()
                }
            }
        }
    }
}

@Composable
fun AirPodsApp(
    viewModel: AirPodsViewModel = viewModel()
) {
    // Permission launcher for Bluetooth & Location
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        viewModel.refreshBattery()
    }

    LaunchedEffect(Unit) {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        permissionLauncher.launch(permissions.toTypedArray())
    }

    val currentScreen by viewModel.currentScreen.collectAsState()
    val connectionStatus by viewModel.connectionStatus.collectAsState()
    val connectedDeviceName by viewModel.connectedDeviceName.collectAsState()
    val batteryInfo by viewModel.batteryInfo.collectAsState()
    val mediaVolumePercent by viewModel.mediaVolumePercent.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val isEqualizerEnabled by viewModel.isEqualizerEnabled.collectAsState()
    val currentPresetId by viewModel.currentPresetId.collectAsState()
    val bandLevels by viewModel.bandLevels.collectAsState()
    val bassBoost by viewModel.bassBoost.collectAsState()
    val is3dAudio by viewModel.is3dAudio.collectAsState()
    val soundMode by viewModel.soundMode.collectAsState()
    val scannedDevices by viewModel.scannedDevices.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()
    val isPlayingChirp by viewModel.isPlayingChirp.collectAsState()
    val settings by viewModel.settings.collectAsState()

    // Handle Back Press
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        containerColor = DarkBackground,
        bottomBar = {
            AirPodsBottomNavigation(
                currentScreen = currentScreen,
                onSelectTab = { screen ->
                    viewModel.selectBottomTab(screen)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    AppScreen.HOME -> {
                        HomeScreen(
                            connectionStatus = connectionStatus,
                            deviceName = connectedDeviceName,
                            batteryInfo = batteryInfo,
                            mediaVolumePercent = mediaVolumePercent,
                            isPlaying = isPlaying,
                            onVolumeChange = { viewModel.setVolume(it) },
                            onPlayPause = { viewModel.togglePlayPause() },
                            onNavigateToEqualizer = { viewModel.navigateTo(AppScreen.EQUALIZER) },
                            onNavigateToSoundModes = { viewModel.navigateTo(AppScreen.SOUND_MODES) },
                            onNavigateToFindAirPods = { viewModel.navigateTo(AppScreen.FIND_AIRPODS) },
                            onNavigateToSettings = { viewModel.navigateTo(AppScreen.SETTINGS) },
                            onNavigateToBattery = { viewModel.navigateTo(AppScreen.BATTERY) },
                            onNavigateToSoundControl = { viewModel.navigateTo(AppScreen.SOUND_CONTROL) }
                        )
                    }

                    AppScreen.EQUALIZER -> {
                        EqualizerScreen(
                            isEqualizerEnabled = isEqualizerEnabled,
                            currentPresetId = currentPresetId,
                            bandLevels = bandLevels,
                            bassBoost = bassBoost,
                            is3dAudioEnabled = is3dAudio,
                            isSessionGlobal = viewModel.audioFx.isSessionGlobalSupported,
                            onToggleEqualizer = { viewModel.setEqualizerEnabled(it) },
                            onSelectPreset = { viewModel.selectPreset(it) },
                            onBandLevelChange = { band, level -> viewModel.setBandLevel(band, level) },
                            onBassBoostChange = { viewModel.setBassBoost(it) },
                            onToggle3dAudio = { viewModel.set3dAudio(it) },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    AppScreen.BATTERY -> {
                        BatteryScreen(
                            batteryInfo = batteryInfo,
                            onRefreshBattery = { viewModel.refreshBattery() },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    AppScreen.SOUND_CONTROL -> {
                        SoundControlScreen(
                            volumePercent = mediaVolumePercent,
                            onVolumeChange = { viewModel.setVolume(it) },
                            onVolumeDelta = { viewModel.adjustVolumeDelta(it) },
                            onToggleMute = { viewModel.toggleMute() },
                            onMediaPlayPause = { viewModel.togglePlayPause() },
                            onMediaNext = { viewModel.mediaNext() },
                            onMediaPrevious = { viewModel.mediaPrevious() },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    AppScreen.SOUND_MODES -> {
                        SoundModesScreen(
                            currentMode = soundMode,
                            onSelectMode = { viewModel.setSoundMode(it) },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    AppScreen.FIND_AIRPODS -> {
                        FindAirPodsScreen(
                            isScanning = isScanning,
                            scannedDevices = scannedDevices,
                            isPlayingChirp = isPlayingChirp,
                            lastSeenTime = viewModel.prefs.lastSeenTimestamp,
                            lastRssi = viewModel.prefs.lastRssi,
                            onStartScan = { viewModel.startScan() },
                            onStopScan = { viewModel.stopScan() },
                            onToggleChirp = { viewModel.toggleChirp() },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    AppScreen.SETTINGS -> {
                        SettingsScreen(
                            deviceName = settings.deviceName,
                            firmwareVersion = settings.firmwareVersion,
                            onNavigateToAdvanced = { viewModel.navigateTo(AppScreen.ADVANCED_SETTINGS) },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    AppScreen.ADVANCED_SETTINGS -> {
                        AdvancedSettingsScreen(
                            settings = settings,
                            onUpdateInEar = { viewModel.updateInEar(it) },
                            onUpdateMic = { viewModel.updateMic(it) },
                            onUpdateLowLatency = { viewModel.updateLowLatency(it) },
                            onUpdateFloatingShortcut = { viewModel.updateFloatingShortcut(it) },
                            onUpdateAmbientSound = { viewModel.updateAmbientSound(it) },
                            onResetToDefaults = { viewModel.resetToDefaults() },
                            onBack = { viewModel.navigateBack() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AirPodsBottomNavigation(
    currentScreen: AppScreen,
    onSelectTab: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(26.dp))
            .border(1.dp, DarkCardBorder, RoundedCornerShape(26.dp))
            .background(DarkSurface)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                title = "خانه",
                icon = Icons.Default.Home,
                isSelected = currentScreen == AppScreen.HOME,
                onClick = { onSelectTab(AppScreen.HOME) }
            )
            BottomNavItem(
                title = "اکولایزر",
                icon = Icons.Default.GraphicEq,
                isSelected = currentScreen == AppScreen.EQUALIZER,
                onClick = { onSelectTab(AppScreen.EQUALIZER) }
            )
            BottomNavItem(
                title = "تنظیمات",
                icon = Icons.Default.Settings,
                isSelected = currentScreen == AppScreen.SETTINGS || currentScreen == AppScreen.ADVANCED_SETTINGS,
                onClick = { onSelectTab(AppScreen.SETTINGS) }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) ElectricBlue else TextMuted,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = title,
            color = if (isSelected) ElectricBlue else TextMuted,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
