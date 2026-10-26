package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DefaultPresets
import com.example.data.EqualizerPreset
import com.example.ui.components.AirPodsCard
import com.example.ui.components.FrequencyWaveVisualizer
import com.example.ui.components.NeonGradientSlider
import com.example.ui.components.NeonSwitch
import com.example.ui.components.VerticalEqualizerBand
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurpleLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun EqualizerScreen(
    isEqualizerEnabled: Boolean,
    currentPresetId: String,
    bandLevels: List<Float>,
    bassBoost: Int,
    is3dAudioEnabled: Boolean,
    isSessionGlobal: Boolean,
    onToggleEqualizer: (Boolean) -> Unit,
    onSelectPreset: (EqualizerPreset) -> Unit,
    onBandLevelChange: (Int, Float) -> Unit,
    onBassBoostChange: (Int) -> Unit,
    onToggle3dAudio: (Boolean) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val bandLabels = listOf("31", "62", "125", "250", "500", "1K", "2K", "4K", "8K", "16K")
    val currentPreset = DefaultPresets.find { it.id == currentPresetId } ?: DefaultPresets.first()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 18.dp)
            .padding(bottom = 90.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(44.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "بازگشت",
                    tint = TextPrimary
                )
            }
            Text(
                text = "اکولایزر",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.size(44.dp))
        }

        // Wave Visualizer Card
        FrequencyWaveVisualizer(
            title = currentPreset.faTitle
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Preset Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DefaultPresets.forEach { preset ->
                val isSelected = preset.id == currentPresetId
                PresetChipCard(
                    preset = preset,
                    isSelected = isSelected,
                    onClick = { onSelectPreset(preset) }
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Manual EQ Toggle Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "اکولایزر دستی",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            NeonSwitch(
                checked = isEqualizerEnabled,
                onCheckedChange = onToggleEqualizer
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 10 Frequency Vertical Sliders Card
        AirPodsCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "+6 dB", color = TextMuted, fontSize = 10.sp)
                    Text(text = "0 dB", color = TextMuted, fontSize = 10.sp)
                    Text(text = "-6 dB", color = TextMuted, fontSize = 10.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    bandLabels.forEachIndexed { index, label ->
                        val value = bandLevels.getOrElse(index) { 0f }
                        VerticalEqualizerBand(
                            bandLabel = label,
                            value = value,
                            onValueChange = { onBandLevelChange(index, it) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bass Boost Card
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
                        Icon(
                            imageVector = Icons.Default.VolumeMute,
                            contentDescription = null,
                            tint = NeonPurpleLight,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تقویت باس",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = "${(bassBoost / 10)}%",
                        color = NeonCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    NeonGradientSlider(
                        value = bassBoost.toFloat(),
                        onValueChange = { onBassBoostChange(it.toInt()) },
                        valueRange = 0f..1000f,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = null,
                        tint = ElectricBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3D Audio Card
        AirPodsCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "3D Audio",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "صدای سه‌بعدی و فضایی",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
                NeonSwitch(
                    checked = is3dAudioEnabled,
                    onCheckedChange = onToggle3dAudio
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Technical status indicator (honesty about Audio Session)
        Text(
            text = if (isSessionGlobal)
                "اکولایزر سیستم فعال است و بر خروجی رسانه اعمال می‌شود."
            else
                "اکولایزر بر جریان رسانه برنامه متصل است (محدودیت سشن سیستم عامل).",
            color = TextMuted,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun PresetChipCard(
    preset: EqualizerPreset,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) NeonCyan else DarkCardBorder
    val bg = if (isSelected) DarkSurfaceElevated else DarkSurface

    Box(
        modifier = Modifier
            .size(width = 86.dp, height = 74.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(if (isSelected) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(16.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = when (preset.iconType) {
                    "bass" -> Icons.Default.GraphicEq
                    "pop" -> Icons.Default.MusicNote
                    "rock" -> Icons.Default.Waves
                    else -> Icons.Default.GraphicEq
                },
                contentDescription = preset.faTitle,
                tint = if (isSelected) NeonCyan else TextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = preset.faTitle,
                color = if (isSelected) TextPrimary else TextSecondary,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
