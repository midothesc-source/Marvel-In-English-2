package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalArenaColors
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class ConfettiParticle(
    val angleRad: Float,
    val speedFactor: Float,
    val sizeDp: Float,
    val color: Color,
    val initialRotation: Float,
    val isDiamond: Boolean
)

@Composable
fun ParticleConfettiOverlay(
    burstTrigger: Int,
    modifier: Modifier = Modifier
) {
    if (burstTrigger <= 0) return

    val colors = LocalArenaColors.current
    val progress = remember { Animatable(0f) }
    val particles = remember(burstTrigger, colors.id) {
        val palette = listOf(
            colors.primary,
            colors.secondary,
            colors.tertiary,
            colors.streakFlame,
            Color.White
        )
        List(44) { index ->
            val rnd = Random(burstTrigger * 997 + index)
            ConfettiParticle(
                angleRad = rnd.nextFloat() * (2f * Math.PI.toFloat()),
                speedFactor = 0.45f + rnd.nextFloat() * 0.85f,
                sizeDp = 5f + rnd.nextFloat() * 7f,
                color = palette[index % palette.size],
                initialRotation = rnd.nextFloat() * 360f,
                isDiamond = index % 2 == 0
            )
        }
    }

    LaunchedEffect(burstTrigger) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
        )
    }

    val t = progress.value
    if (t >= 0.99f) return

    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width * 0.5f
        val centerY = size.height * 0.38f
        val maxRadius = minOf(size.width, size.height) * 0.48f
        val alpha = (1f - t).coerceIn(0f, 1f)

        particles.forEach { p ->
            val distance = maxRadius * p.speedFactor * t
            val gravity = (t * t) * 180f
            val x = centerX + cos(p.angleRad) * distance
            val y = centerY + sin(p.angleRad) * distance + gravity
            val pxSize = p.sizeDp.dp.toPx()

            rotate(
                degrees = p.initialRotation + t * 240f,
                pivot = Offset(x, y)
            ) {
                if (p.isDiamond) {
                    drawRect(
                        color = p.color.copy(alpha = alpha),
                        topLeft = Offset(x - pxSize / 2f, y - pxSize / 2f),
                        size = Size(pxSize, pxSize * 0.65f)
                    )
                } else {
                    drawCircle(
                        color = p.color.copy(alpha = alpha),
                        radius = pxSize * 0.45f,
                        center = Offset(x, y)
                    )
                }
            }
        }
    }
}

@Composable
fun StreakFireBadge(
    streak: Int,
    multiplier: Int,
    modifier: Modifier = Modifier
) {
    val colors = LocalArenaColors.current
    val isHot = streak >= 2
    val infiniteTransition = rememberInfiniteTransition(label = "streak_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isHot) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 650),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val badgeColor = when {
        multiplier >= 4 -> colors.streakFlame
        multiplier >= 3 -> colors.secondary
        multiplier >= 2 -> colors.primary
        else -> colors.cardBorder
    }
    val containerColor = when {
        multiplier >= 4 -> colors.streakContainer
        multiplier >= 2 -> colors.primaryContainer
        else -> colors.surfaceElevated
    }
    val textColor = when {
        multiplier >= 4 -> colors.streakFlame
        multiplier >= 2 -> colors.onPrimaryContainer
        else -> colors.textSecondary
    }

    Row(
        modifier = modifier
            .scale(pulseScale)
            .clip(CircleShape)
            .background(containerColor)
            .border(1.dp, badgeColor, CircleShape)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.LocalFireDepartment,
            contentDescription = "Streak fire",
            tint = if (isHot) badgeColor else colors.textSecondary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = if (isHot) "$streak • ${multiplier}x" else "$streak",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = if (isHot) textColor else colors.textSecondary
        )
    }
}

@Composable
fun GlowingProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val colors = LocalArenaColors.current
    val clamped = progress.coerceIn(0f, 1f)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(colors.surfaceElevated)
            .border(1.dp, colors.cardBorder, RoundedCornerShape(999.dp))
    ) {
        if (clamped > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(clamped)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(999.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(colors.primary, colors.tertiary, colors.secondary)
                        )
                    )
            )
        }
    }
}
