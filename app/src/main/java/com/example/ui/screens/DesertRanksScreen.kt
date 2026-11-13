package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.QuestionBank
import com.example.model.SkillCategory
import com.example.ui.ArenaUiState
import com.example.ui.components.GlowingProgressBar
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.LocalArenaColors

@Composable
fun DesertRanksScreen(
    uiState: ArenaUiState,
    onOpenThemeStudio: () -> Unit,
    onResetProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalArenaColors.current
    val progress = uiState.userProgress
    var showResetDialog by remember { mutableStateOf(false) }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Arena Progress?") },
            text = {
                Text("This will clear your local XP, high scores, unlocked Desert Force ranks, and Review Vault history.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetDialog = false
                        onResetProgress()
                    }
                ) {
                    Text("Reset", color = colors.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Current Rank Showcase
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(colors.surface)
                        .border(1.5.dp, colors.secondary.copy(alpha = 0.65f), RoundedCornerShape(24.dp))
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_desert_force_emblem),
                        contentDescription = "Desert Force Emblem",
                        modifier = Modifier
                            .size(82.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .border(1.dp, colors.secondary, RoundedCornerShape(18.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "THE DESERT FORCE",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.secondary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = uiState.currentRank.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = colors.textPrimary
                        )
                        Text(
                            text = "${progress.totalXp} Total XP • ${progress.sessionsCompleted} Missions",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.primary
                        )
                    }
                }
            }

            // Theme Studio Launcher Card
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(colors.primaryContainer.copy(alpha = 0.65f))
                        .border(1.dp, colors.primary.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
                        .clickable(onClick = onOpenThemeStudio)
                        .padding(14.dp)
                        .testTag("ranks_theme_studio_card"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(colors.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Palette,
                                contentDescription = "Arena Theme Studio",
                                tint = colors.onPrimary
                            )
                        }
                        Column {
                            Text(
                                text = "Arena Theme: ${colors.name}",
                                style = MaterialTheme.typography.titleMedium,
                                color = colors.textPrimary
                            )
                            Text(
                                text = "Choose from 10 colorful themes",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.textSecondary
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        colors.previewSwatches.drop(1).forEach { swatch ->
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(swatch)
                            )
                        }
                    }
                }
            }

            // Lifetime Skill Accuracy
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(colors.surface)
                        .border(1.dp, colors.cardBorder, RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Lifetime Mastery by Skill",
                        style = MaterialTheme.typography.titleLarge,
                        color = colors.textPrimary
                    )
                    SkillCategory.entries.forEach { cat ->
                        val total = progress.categoryTotal[cat] ?: 0
                        val correct = progress.categoryCorrect[cat] ?: 0
                        val pct = if (total == 0) 0 else ((correct.toFloat() / total) * 100).toInt()
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = cat.label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colors.textPrimary
                                )
                                Text(
                                    text = if (total == 0) "No drills yet" else "$pct% ($correct/$total)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (total == 0) colors.textMuted else colors.primary
                                )
                            }
                            GlowingProgressBar(progress = pct / 100f)
                        }
                    }
                }
            }

            // Rank Progression Ladder
            item {
                Text(
                    text = "Rank Progression Ladder",
                    style = MaterialTheme.typography.titleLarge,
                    color = colors.textPrimary
                )
            }

            items(QuestionBank.desertRanks, key = { it.id }) { rank ->
                val isUnlocked = progress.totalXp >= rank.minXp || progress.unlockedRankIds.contains(rank.id)
                val isCurrent = uiState.currentRank.id == rank.id
                val border = when {
                    isCurrent -> colors.secondary
                    isUnlocked -> colors.tertiary.copy(alpha = 0.6f)
                    else -> colors.cardBorder
                }
                val bg = if (isCurrent) colors.secondaryContainer.copy(alpha = 0.55f) else colors.surfaceElevated

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(bg)
                        .border(if (isCurrent) 1.5.dp else 1.dp, border, RoundedCornerShape(18.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isUnlocked) colors.tertiary.copy(alpha = 0.18f) else colors.surface
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isUnlocked) Icons.Filled.Verified else Icons.Filled.Lock,
                                    contentDescription = if (isUnlocked) "Unlocked" else "Locked",
                                    tint = if (isUnlocked) colors.tertiary else colors.textMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = rank.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (isUnlocked) colors.textPrimary else colors.textSecondary
                                )
                                Text(
                                    text = rank.arabicTitle,
                                    fontFamily = CairoFontFamily,
                                    fontSize = 13.sp,
                                    color = if (isUnlocked) colors.secondary else colors.textMuted
                                )
                            }
                        }

                        Text(
                            text = "${rank.minXp} XP",
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isUnlocked) colors.primary else colors.textMuted
                        )
                    }

                    Text(
                        text = rank.mottoEn,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textSecondary
                    )

                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Text(
                            text = rank.mottoAr,
                            fontFamily = CairoFontFamily,
                            fontSize = 13.sp,
                            color = if (isUnlocked) colors.onSecondaryContainer else colors.textMuted,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Reset Local Progress
            item {
                OutlinedButton(
                    onClick = { showResetDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("reset_progress_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.error)
                ) {
                    Icon(
                        imageVector = Icons.Filled.RestartAlt,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Reset Local Progress")
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
