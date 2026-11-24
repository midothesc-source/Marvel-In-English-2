package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.DesertRank
import com.example.model.SessionSummary
import com.example.ui.components.EgyptianSecretSauceCard
import com.example.ui.components.GlowingProgressBar
import com.example.ui.components.WhatsAppHelper
import com.example.ui.theme.LocalArenaColors

@Composable
fun PerformanceSummaryScreen(
    summary: SessionSummary,
    currentRank: DesertRank,
    onPlayAgain: () -> Unit,
    onBackToHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackToHub()
    }

    val colors = LocalArenaColors.current
    val context = LocalContext.current
    val missedRecords = summary.records.filter { !it.isCorrect }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Victory / Score Hero Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(colors.surface)
                        .border(1.5.dp, colors.primary.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (summary.isVaultPractice) "VAULT DRILL COMPLETE" else "MISSION REPORT",
                                style = MaterialTheme.typography.labelMedium,
                                color = colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${summary.level.shortTitle} • ${summary.accuracyPercent}% Accuracy",
                                style = MaterialTheme.typography.headlineMedium,
                                color = colors.textPrimary
                            )
                        }

                        if (summary.isNewHighScore) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(colors.secondaryContainer)
                                    .border(1.dp, colors.secondary, RoundedCornerShape(999.dp))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "NEW BEST!",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.onSecondaryContainer,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SummaryMetric(
                            label = "Score",
                            value = "+${summary.totalScore} XP",
                            accent = colors.primary,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryMetric(
                            label = "Correct",
                            value = "${summary.correctCount}/${summary.totalQuestions}",
                            accent = colors.tertiary,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryMetric(
                            label = "Max Fire",
                            value = "${summary.maxCombo}x",
                            accent = colors.streakFlame,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Newly Unlocked Rank Spotlight
            if (summary.newlyUnlockedRank != null) {
                val unlocked = summary.newlyUnlockedRank
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(colors.secondaryContainer)
                            .border(1.5.dp, colors.secondary, RoundedCornerShape(20.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(colors.secondary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.EmojiEvents,
                                contentDescription = "Rank unlocked",
                                tint = colors.secondary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "RANK UNLOCKED: ${unlocked.title.uppercase()}",
                                style = MaterialTheme.typography.labelMedium,
                                color = colors.onSecondaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = unlocked.arabicTitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = colors.textPrimary
                            )
                            Text(
                                text = unlocked.mottoEn,
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.textSecondary
                            )
                        }
                    }
                }
            }

            // Skill Breakdown & Weak Spots
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(colors.surface)
                        .border(1.dp, colors.cardBorder, RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Skill Radar & Weak Spots",
                        style = MaterialTheme.typography.titleLarge,
                        color = colors.textPrimary
                    )

                    summary.categoryBreakdowns.forEach { cat ->
                        val isWeak = cat.accuracyPercent < 70
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${cat.category.label} (${cat.correctCount}/${cat.totalAttempts})",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colors.textPrimary
                                )
                                Text(
                                    text = if (isWeak) "${cat.accuracyPercent}% • Focus Needed" else "${cat.accuracyPercent}% • Solid",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isWeak) colors.error else colors.tertiary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            GlowingProgressBar(progress = cat.accuracyPercent / 100f)
                        }
                    }
                }
            }

            // WhatsApp Share & Tutoring Actions
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(colors.tertiaryContainer.copy(alpha = 0.65f))
                        .border(1.dp, colors.tertiary.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Shield,
                            contentDescription = null,
                            tint = colors.tertiary
                        )
                        Text(
                            text = "Share Victory or Ask Your Tutor",
                            style = MaterialTheme.typography.titleMedium,
                            color = colors.textPrimary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val msg = WhatsAppHelper.buildScoreShareMessage(summary, currentRank)
                                WhatsAppHelper.shareToWhatsApp(context, msg)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("whatsapp_share_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.tertiary,
                                contentColor = colors.onTertiary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            Text("Share Score", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                val msg = WhatsAppHelper.buildTutorInquiryMessage(summary, currentRank)
                                WhatsAppHelper.openWhatsAppB2B(context, msg)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("whatsapp_tutor_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.primary)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Chat,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            Text("WhatsApp B2B", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Missed Questions Instant Review
            if (missedRecords.isNotEmpty()) {
                item {
                    Text(
                        text = "Missed Challenges (${missedRecords.size}) — Saved to Vault",
                        style = MaterialTheme.typography.titleLarge,
                        color = colors.secondary
                    )
                }

                items(missedRecords, key = { it.question.id }) { record ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(colors.surfaceElevated)
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(20.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = record.question.prompt,
                            style = MaterialTheme.typography.titleMedium,
                            color = colors.textPrimary
                        )
                        Text(
                            text = "Correct Answer: ${record.question.options[record.question.correctIndex]}",
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.tertiary
                        )
                        EgyptianSecretSauceCard(
                            question = record.question,
                            isCorrect = null
                        )
                    }
                }
            }

            // Navigation CTAs
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBackToHub,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("back_to_hub_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textPrimary)
                    ) {
                        Text("Arena Hub", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onPlayAgain,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("play_again_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primary,
                            contentColor = colors.onPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Replay,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text("Play Again", fontWeight = FontWeight.Bold)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun SummaryMetric(
    label: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    val colors = LocalArenaColors.current
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(colors.primaryContainer.copy(alpha = 0.45f))
            .border(1.dp, accent.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = accent,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = colors.textSecondary
        )
    }
}
