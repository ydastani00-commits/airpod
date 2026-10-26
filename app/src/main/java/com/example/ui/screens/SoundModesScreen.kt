package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.NoiseAware
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.filled.SurroundSound
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SoundModeType
import com.example.ui.components.AirPodsCard
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
fun SoundModesScreen(
    currentMode: SoundModeType,
    onSelectMode: (SoundModeType) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableStateOf(0) } // 0 = فعال, 1 = همه حالت‌ها

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
                text = "حالت صدا",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.size(44.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Segmented Tab (فعال | همه حالت‌ها)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .clip(RoundedCornerShape(23.dp))
                .background(DarkSurfaceElevated)
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .then(
                        if (selectedTab == 0) Modifier.background(
                            Brush.horizontalGradient(listOf(ElectricBlue, NeonPurpleLight))
                        ) else Modifier
                    )
                    .clickable { selectedTab = 0 },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "فعال",
                    color = if (selectedTab == 0) Color.White else TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .then(
                        if (selectedTab == 1) Modifier.background(
                            Brush.horizontalGradient(listOf(ElectricBlue, NeonPurpleLight))
                        ) else Modifier
                    )
                    .clickable { selectedTab = 1 },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "همه حالت‌ها",
                    color = if (selectedTab == 1) Color.White else TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Sound Mode Cards
        SoundModeOptionCard(
            title = "حالت عادی",
            description = "صدای متعادل و طبیعی",
            icon = Icons.Default.SurroundSound,
            isSelected = currentMode == SoundModeType.NORMAL,
            onClick = { onSelectMode(SoundModeType.NORMAL) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SoundModeOptionCard(
            title = "حالت شفافیت",
            description = "شنیدن صدای محیط اطراف",
            icon = Icons.Default.Hearing,
            isSelected = currentMode == SoundModeType.TRANSPARENCY,
            onClick = { onSelectMode(SoundModeType.TRANSPARENCY) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SoundModeOptionCard(
            title = "حالت حذف نویز",
            description = "کاهش نویز محیط",
            icon = Icons.Default.NoiseAware,
            isSelected = currentMode == SoundModeType.ANC,
            onClick = { onSelectMode(SoundModeType.ANC) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SoundModeOptionCard(
            title = "حالت ضمنی",
            description = "با توجه به محیط خود، حالت مناسب را انتخاب کنید.",
            icon = Icons.Default.SettingsSuggest,
            isSelected = currentMode == SoundModeType.ADAPTIVE,
            onClick = { onSelectMode(SoundModeType.ADAPTIVE) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Technical explanation note
        AirPodsCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(
                    text = "توضیحات سازگاری حالت صدا",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "سوئیچ مستقیم سخت‌افزاری ANC و حالت شفافیت در ایرپاد نیازمند پروتکل اختصاصی اپل (Apple BLE) است. در صورت عدم دریافت مستقیم فرمان توسط ایرپاد، کنترل نویز را می‌توانید با فشردن ساقه ایرپاد یا تنظیمات اکولایزر این برنامه مدیریت کنید.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
fun SoundModeOptionCard(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) NeonCyan else DarkCardBorder
    val bg = if (isSelected) DarkSurfaceElevated else DarkSurface

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(if (isSelected) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(18.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color(0x3300E5FF) else Color(0x1A387BFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = if (isSelected) NeonCyan else TextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = description,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(NeonCyan),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "انتخاب شده",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
