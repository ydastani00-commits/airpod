package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkCardBorderHighlight
import com.example.ui.theme.DarkCardGradient
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NeonPurpleLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AirPodsCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = DarkSurface,
    borderColor: Color = DarkCardBorder,
    borderWidth: Dp = 1.dp,
    shape: RoundedCornerShape = RoundedCornerShape(20.dp),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .border(borderWidth, borderColor, shape)
            .background(DarkCardGradient)
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun FrequencyWaveVisualizer(
    modifier: Modifier = Modifier,
    title: String = "پیش‌فرض"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, DarkCardBorder, RoundedCornerShape(22.dp))
            .background(DarkCardGradient)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val midY = height * 0.55f

            // Background subtle grid lines
            drawLine(
                color = Color(0x15387BFF),
                start = Offset(0f, midY),
                end = Offset(width, midY),
                strokeWidth = 1f
            )

            // Wave 1: Cyan/Blue wave
            val path1 = Path()
            val steps = 80
            for (i in 0..steps) {
                val x = width * (i.toFloat() / steps)
                val normalizedX = (i.toFloat() / steps) * 4 * Math.PI.toFloat()
                val y = midY + sin(normalizedX + phase) * 28f * sin(i.toFloat() / steps * Math.PI.toFloat())
                if (i == 0) path1.moveTo(x, y) else path1.lineTo(x, y)
            }
            drawPath(
                path = path1,
                brush = Brush.horizontalGradient(
                    listOf(NeonMagenta, NeonPurpleLight, NeonCyan, ElectricBlue)
                ),
                style = Stroke(width = 3.5f, cap = StrokeCap.Round)
            )

            // Wave 2: Purple secondary wave
            val path2 = Path()
            for (i in 0..steps) {
                val x = width * (i.toFloat() / steps)
                val normalizedX = (i.toFloat() / steps) * 3 * Math.PI.toFloat()
                val y = midY + cos(normalizedX - phase * 0.8f) * 22f * sin(i.toFloat() / steps * Math.PI.toFloat())
                if (i == 0) path2.moveTo(x, y) else path2.lineTo(x, y)
            }
            drawPath(
                path = path2,
                brush = Brush.horizontalGradient(
                    listOf(NeonCyan, ElectricBlue, NeonMagenta)
                ),
                style = Stroke(width = 2f, cap = StrokeCap.Round)
            )
        }

        Text(
            text = title,
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
        )
    }
}

@Composable
fun CircularBatteryGauge(
    percentage: Int?,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    size: Dp = 140.dp,
    strokeWidth: Dp = 10.dp,
    isCase: Boolean = false,
    icon: @Composable (() -> Unit)? = null
) {
    val progress = (percentage ?: 0).coerceIn(0, 100) / 100f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(size)
        ) {
            Canvas(modifier = Modifier.size(size)) {
                val arcSize = Size(this.size.width - strokeWidth.toPx(), this.size.height - strokeWidth.toPx())
                val topLeft = Offset(strokeWidth.toPx() / 2, strokeWidth.toPx() / 2)

                // Background track
                drawArc(
                    color = Color(0x221E2E54),
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )

                // Progress Arc
                if (percentage != null) {
                    val sweep = 270f * progress
                    val brush = Brush.sweepGradient(
                        colors = listOf(NeonCyan, ElectricBlue, NeonGreen, NeonCyan),
                        center = center
                    )
                    drawArc(
                        brush = brush,
                        startAngle = 135f,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    icon()
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = percentage?.let { "$it%" } ?: "نامشخص",
                    color = TextPrimary,
                    fontSize = if (isCase) 22.sp else 18.sp,
                    fontWeight = FontWeight.Bold
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        if (title.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun RadarFinderCanvas(
    isScanning: Boolean,
    modifier: Modifier = Modifier,
    targetBlip: Offset? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val maxRadius = (size.minDimension / 2) * 0.9f

        // Concentric circles
        val ringCount = 4
        for (i in 1..ringCount) {
            val r = maxRadius * (i.toFloat() / ringCount)
            drawCircle(
                color = Color(0x1F2979FF),
                radius = r,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1.5f)
            )
        }

        // Cross lines
        drawLine(
            color = Color(0x122979FF),
            start = Offset(centerX, centerY - maxRadius),
            end = Offset(centerX, centerY + maxRadius),
            strokeWidth = 1f
        )
        drawLine(
            color = Color(0x122979FF),
            start = Offset(centerX - maxRadius, centerY),
            end = Offset(centerX + maxRadius, centerY),
            strokeWidth = 1f
        )

        // Pulsing wave
        if (isScanning) {
            drawCircle(
                color = NeonCyan.copy(alpha = (1f - pulse) * 0.4f),
                radius = maxRadius * pulse,
                center = Offset(centerX, centerY),
                style = Stroke(width = 2.5f)
            )
        }

        // Target dot
        val blipOffset = targetBlip ?: Offset(centerX + maxRadius * 0.45f, centerY - maxRadius * 0.35f)
        drawCircle(
            color = NeonMagenta,
            radius = 7.dp.toPx(),
            center = blipOffset
        )
        drawCircle(
            color = NeonMagenta.copy(alpha = 0.4f),
            radius = 12.dp.toPx(),
            center = blipOffset
        )
    }
}

@Composable
fun VerticalEqualizerBand(
    bandLabel: String,
    value: Float, // -6f to +6f
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Vertical track with drag gesture
        BoxWithConstraints(
            modifier = Modifier
                .width(28.dp)
                .height(180.dp)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, _ ->
                        change.consume()
                        val y = change.position.y.coerceIn(0f, size.height.toFloat())
                        // y = 0 means max (+6), y = height means min (-6)
                        val fraction = 1f - (y / size.height.toFloat())
                        val newVal = -6f + fraction * 12f
                        onValueChange(newVal.coerceIn(-6f, 6f))
                    }
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            val trackHeight = maxHeight
            val fraction = ((value + 6f) / 12f).coerceIn(0f, 1f)

            // Background track
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(Color(0xFF1E2942))
                    .align(Alignment.Center)
            )

            // Active filled track from bottom to thumb
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(trackHeight * fraction)
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            listOf(NeonCyan, ElectricBlue)
                        )
                    )
                    .align(Alignment.BottomCenter)
            )

            // Glowing thumb knob
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = (trackHeight * fraction - 8.dp).coerceAtLeast(0.dp))
                    .size(16.dp)
                    .shadow(8.dp, CircleShape, spotColor = NeonCyan)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, NeonCyan, CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Frequency Label
        Text(
            text = bandLabel,
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun NeonGradientSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        colors = SliderDefaults.colors(
            thumbColor = Color.White,
            activeTrackColor = ElectricBlue,
            inactiveTrackColor = Color(0xFF1E2E54)
        ),
        modifier = modifier
    )
}

@Composable
fun NeonSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = ElectricBlue,
            uncheckedThumbColor = TextMuted,
            uncheckedTrackColor = Color(0xFF1A2644),
            uncheckedBorderColor = Color(0xFF2E3E66)
        ),
        modifier = modifier
    )
}
