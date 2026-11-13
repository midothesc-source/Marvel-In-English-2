package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.model.CefrLevel
import com.example.model.QuizQuestion
import com.example.ui.components.EgyptianSecretSauceCard
import com.example.ui.theme.LocalArenaColors

@Composable
fun ReviewVaultScreen(
    vaultQuestions: List<QuizQuestion>,
    onPracticeVault: (CefrLevel?) -> Unit,
    onMarkMastered: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalArenaColors.current
    var levelFilter by remember { mutableStateOf<CefrLevel?>(null) }
    val filtered = remember(vaultQuestions, levelFilter) {
        vaultQuestions.filter { levelFilter == null || it.level == levelFilter }
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
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "REVIEW VAULT",
                        style = MaterialTheme.typography.labelMedium,
                        color = colors.secondary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Missed Questions Archive",
                        style = MaterialTheme.typography.headlineMedium,
                        color = colors.textPrimary
                    )
                    Text(
                        text = "Every missed question lands here with its Egyptian Arabic explanation and حركة فنان rule.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textSecondary
                    )
                }
            }

            if (vaultQuestions.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = levelFilter == null,
                            onClick = { levelFilter = null },
                            label = { Text("All (${vaultQuestions.size})") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = colors.primaryContainer,
                                selectedLabelColor = colors.onPrimaryContainer,
                                containerColor = colors.surfaceElevated,
                                labelColor = colors.textSecondary
                            )
                        )
                        CefrLevel.entries.forEach { lvl ->
                            val count = vaultQuestions.count { it.level == lvl }
                            FilterChip(
                                selected = levelFilter == lvl,
                                onClick = { levelFilter = lvl },
                                label = { Text("${lvl.shortTitle} ($count)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = colors.primaryContainer,
                                    selectedLabelColor = colors.onPrimaryContainer,
                                    containerColor = colors.surfaceElevated,
                                    labelColor = colors.textSecondary
                                )
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = { onPracticeVault(levelFilter) },
                        enabled = filtered.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("practice_vault_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.secondary,
                            contentColor = colors.onSecondary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Drill ${filtered.size} Vault Challenges",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (filtered.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(colors.surface)
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(24.dp))
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = if (vaultQuestions.isEmpty()) {
                                Icons.Filled.BookmarkAdded
                            } else {
                                Icons.Outlined.Inventory2
                            },
                            contentDescription = "Empty vault",
                            tint = colors.tertiary,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = if (vaultQuestions.isEmpty()) {
                                "Your Review Vault is Clear!"
                            } else {
                                "No missed questions in ${levelFilter?.shortTitle}"
                            },
                            style = MaterialTheme.typography.titleLarge,
                            color = colors.textPrimary,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Complete Arena levels to track tricky idioms, phrasal verbs, and collocations automatically.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.textSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(filtered, key = { it.id }) { question ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(colors.surface)
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(20.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${question.level.shortTitle} • ${question.category.label}",
                                style = MaterialTheme.typography.labelMedium,
                                color = colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = question.focusTag,
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.secondary
                            )
                        }

                        Text(
                            text = question.prompt,
                            style = MaterialTheme.typography.titleMedium,
                            color = colors.textPrimary
                        )

                        Text(
                            text = "Answer: ${question.options[question.correctIndex]}",
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.tertiary
                        )

                        EgyptianSecretSauceCard(
                            question = question,
                            isCorrect = null
                        )

                        OutlinedButton(
                            onClick = { onMarkMastered(question.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("master_vault_${question.id}"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.tertiary)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            Text("Mark as Mastered", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
