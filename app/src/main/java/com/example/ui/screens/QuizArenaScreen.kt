package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.QuizSessionState
import com.example.ui.components.EgyptianSecretSauceCard
import com.example.ui.components.GlowingProgressBar
import com.example.ui.components.ParticleConfettiOverlay
import com.example.ui.components.StreakFireBadge
import com.example.ui.theme.LocalArenaColors

@Composable
fun QuizArenaScreen(
    session: QuizSessionState,
    onSelectOption: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    onExitQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onExitQuiz()
    }

    val colors = LocalArenaColors.current
    val question = session.currentQuestion ?: return
    val isLastQuestion = session.currentIndex >= session.questions.lastIndex
    val lastRecord = session.records.lastOrNull()?.takeIf { it.question.id == question.id }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top Arena HUD
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = onExitQuiz,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .clip(CircleShape)
                                .background(colors.surfaceElevated)
                                .testTag("exit_quiz_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Exit mission",
                                tint = colors.textPrimary
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (session.isVaultPractice) "VAULT RESCUE" else session.level.badge.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Challenge ${session.currentIndex + 1} / ${session.questions.size} • ${session.sessionScore} XP",
                                style = MaterialTheme.typography.titleMedium,
                                color = colors.textPrimary
                            )
                        }

                        StreakFireBadge(
                            streak = session.liveStreak,
                            multiplier = session.comboMultiplier
                        )
                    }

                    GlowingProgressBar(progress = session.progressFraction)
                }
            }

            // Question Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(colors.surface)
                        .border(1.5.dp, colors.cardBorder, RoundedCornerShape(22.dp))
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(colors.primaryContainer)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = question.category.label,
                                style = MaterialTheme.typography.labelMedium,
                                color = colors.onPrimaryContainer
                            )
                        }
                        Text(
                            text = "+${question.baseXp * session.comboMultiplier} XP potential",
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.secondary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = question.prompt,
                        style = MaterialTheme.typography.headlineSmall.copy(lineHeight = 29.sp),
                        color = colors.textPrimary,
                        modifier = Modifier.testTag("question_prompt")
                    )
                }
            }

            // Answer Choices
            item {
                val labels = listOf("A", "B", "C", "D")
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    question.options.forEachIndexed { index, optionText ->
                        val isSelected = session.selectedOptionIndex == index
                        val isCorrectOption = index == question.correctIndex
                        val revealed = session.isRevealed

                        val bgColor = when {
                            !revealed -> colors.surfaceElevated
                            isCorrectOption -> colors.tertiaryContainer
                            isSelected && !isCorrectOption -> colors.errorContainer
                            else -> colors.surface.copy(alpha = 0.7f)
                        }

                        val borderColor = when {
                            !revealed -> colors.cardBorder
                            isCorrectOption -> colors.tertiary
                            isSelected && !isCorrectOption -> colors.error
                            else -> colors.cardBorder.copy(alpha = 0.5f)
                        }

                        val badgeBg = when {
                            !revealed -> colors.surface
                            isCorrectOption -> colors.tertiary
                            isSelected && !isCorrectOption -> colors.error
                            else -> colors.surface
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .minimumInteractiveComponentSize()
                                .clip(RoundedCornerShape(16.dp))
                                .background(bgColor)
                                .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
                                .clickable(enabled = !revealed) { onSelectOption(index) }
                                .padding(horizontal = 14.dp, vertical = 14.dp)
                                .testTag("option_${index}"),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(badgeBg),
                                contentAlignment = Alignment.Center
                            ) {
                                if (revealed && isCorrectOption) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Correct",
                                        tint = colors.onTertiary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else if (revealed && isSelected) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Incorrect",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Text(
                                        text = labels.getOrElse(index) { "${index + 1}" },
                                        style = MaterialTheme.typography.labelLarge,
                                        color = colors.textPrimary
                                    )
                                }
                            }

                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (revealed && isCorrectOption) FontWeight.Bold else FontWeight.Medium,
                                color = colors.textPrimary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Egyptian Colloquial "Secret Sauce" Explanation
            item {
                AnimatedVisibility(
                    visible = session.isRevealed,
                    enter = fadeIn() + expandVertically(
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                    )
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        EgyptianSecretSauceCard(
                            question = question,
                            isCorrect = lastRecord?.isCorrect,
                            earnedXp = lastRecord?.earnedXp ?: 0
                        )

                        Button(
                            onClick = onNextQuestion,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("next_question_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.primary,
                                contentColor = colors.onPrimary
                            )
                        ) {
                            Text(
                                text = if (isLastQuestion) "Finish & View Arena Report" else "Next Challenge",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }

        ParticleConfettiOverlay(burstTrigger = session.confettiBurstCounter)
    }
}
