package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SurroundSound
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.BatteryInfo
import com.example.data.ConnectionStatus
import com.example.ui.components.AirPodsCard
import com.example.ui.components.NeonGradientSlider
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurpleLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@Composable
fun HomeScreen(
    connectionStatus: ConnectionStatus,
    deviceName: String,
    batteryInfo: BatteryInfo,
    mediaVolumePercent: Int,
    isPlaying: Boolean,
    onVolumeChange: (Int) -> Unit,
    onPlayPause: () -> Unit,
    onNavigateToEqualizer: () -> Unit,
    onNavigateToSoundModes: () -> Unit,
    onNavigateToFindAirPods: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToBattery: () -> Unit,
    onNavigateToSoundControl: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 18.dp)
            .padding(bottom = 90.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateToSoundControl,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "کنترل صدا",
                    tint = TextPrimary
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "AirPods Control",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(
                                when (connectionStatus) {
                                    ConnectionStatus.CONNECTED -> NeonGreen
                                    ConnectionStatus.CONNECTING -> WarningAmber
                                    else -> Color.Red
                                }
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = connectionStatus.faTitle,
                        fontSize = 12.sp,
                        color = when (connectionStatus) {
                            ConnectionStatus.CONNECTED -> NeonCyan
                            ConnectionStatus.CONNECTING -> WarningAmber
                            else -> TextMuted
                        }
                    )
                }
            }

            IconButton(
                onClick = onNavigateToSettings,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "تنظیمات",
                    tint = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Center Hero: AirPods Case with earbuds
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(26.dp))
                .border(1.dp, DarkCardBorder, RoundedCornerShape(26.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0F1B38), Color(0xFF070B18))
                    )
                )
                .clickable { onNavigateToBattery() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_airpods_case),
                contentDescription = "AirPods Case and Buds",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Device Name Title
        Text(
            text = deviceName,
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Battery Badges (L, R, Case)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToBattery() },
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BatterySummaryPill(
                label = "L",
                level = batteryInfo.leftDisplay,
                isCharging = batteryInfo.isCharging
            )
            BatterySummaryPill(
                label = "R",
                level = batteryInfo.rightDisplay,
                isCharging = batteryInfo.isCharging
            )
            BatterySummaryPill(
                label = "Case",
                level = batteryInfo.caseDisplay,
                isCharging = batteryInfo.isCharging
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Music Player Card
        AirPodsCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(DarkSurfaceElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "در حال پخش",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "بدون عنوان",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Artiste",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    IconButton(
                        onClick = onPlayPause,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceElevated)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "توقف" else "پخش",
                            tint = TextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Volume slider in player card
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeDown,
                        contentDescription = "صدای کم",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    NeonGradientSlider(
                        value = mediaVolumePercent.toFloat(),
                        onValueChange = { onVolumeChange(it.toInt()) },
                        valueRange = 0f..100f,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "صدای زیاد",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4 Quick Action Cards Grid (2x2)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HomeGridActionCard(
                title = "اکولایزر",
                icon = Icons.Default.GraphicEq,
                iconTint = NeonCyan,
                onClick = onNavigateToEqualizer,
                modifier = Modifier.weight(1f)
            )
            HomeGridActionCard(
                title = "حالت صدا",
                icon = Icons.Default.SurroundSound,
                iconTint = NeonPurpleLight,
                onClick = onNavigateToSoundModes,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HomeGridActionCard(
                title = "جستجوی ایرپاد",
                icon = Icons.Default.Radar,
                iconTint = ElectricBlue,
                onClick = onNavigateToFindAirPods,
                modifier = Modifier.weight(1f)
            )
            HomeGridActionCard(
                title = "تنظیمات بیشتر",
                icon = Icons.Default.Settings,
                iconTint = TextPrimary,
                onClick = onNavigateToSettings,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BatterySummaryPill(
    label: String,
    level: String,
    isCharging: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, Color(0x3310B981), RoundedCornerShape(14.dp))
            .background(Color(0x1810B981))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(NeonGreen)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$label: $level",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = NeonGreen
        )
    }
}

@Composable
fun HomeGridActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(84.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, DarkCardBorder, RoundedCornerShape(18.dp))
            .background(DarkSurface)
            .clickable { onClick() }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        }
    }
}
