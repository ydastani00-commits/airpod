package com.example.ui.screens

import android.content.Intent
import android.provider.Settings
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun SettingsScreen(
    deviceName: String,
    firmwareVersion: String,
    onNavigateToAdvanced: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
                text = "تنظیمات",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.size(44.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Device Name Item
        SettingsMenuItem(
            title = "نام دستگاه",
            subtitle = deviceName,
            icon = Icons.Default.QrCodeScanner,
            iconTint = ElectricBlue,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Connection Management
        SettingsMenuItem(
            title = "مدیریت اتصال",
            subtitle = "اتصال خودکار، چند دستگاه",
            icon = Icons.Default.Link,
            iconTint = NeonCyan,
            onClick = {
                // Open Android Bluetooth settings
                try {
                    context.startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
                } catch (ignored: Exception) {}
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Firmware Version
        SettingsMenuItem(
            title = "به‌روزرسانی فریم‌ور",
            subtitle = "آخرین نسخه: $firmwareVersion",
            icon = Icons.Default.CloudDownload,
            iconTint = ElectricBlue,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Advanced Settings
        SettingsMenuItem(
            title = "تنظیمات پیشرفته",
            subtitle = "دوبار لمس، میکروفون، صدا",
            icon = Icons.Default.Settings,
            iconTint = NeonPurpleLight,
            onClick = onNavigateToAdvanced
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Notifications
        SettingsMenuItem(
            title = "اطلاع‌رسانی‌ها",
            subtitle = "وضعیت باتری، هشدارها",
            icon = Icons.Default.Notifications,
            iconTint = NeonCyan,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(12.dp))

        // About
        SettingsMenuItem(
            title = "درباره برنامه",
            subtitle = "نسخه 1.0.0 (سازگار با Android 5.1.1 و بالاتر)",
            icon = Icons.Default.Info,
            iconTint = TextSecondary,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Button to open system Bluetooth settings
        FilledTonalButton(
            onClick = {
                try {
                    context.startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
                } catch (ignored: Exception) {}
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = DarkSurfaceElevated,
                contentColor = NeonCyan
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Bluetooth,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "رفتن به تنظیمات بلوتوث اندروید", fontSize = 13.sp)
        }
    }
}

@Composable
fun SettingsMenuItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
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
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
