package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import com.example.data.QuestionBank
import com.example.model.QuizQuestion
import com.example.model.SkillCategory
import com.example.ui.components.EgyptianSecretSauceCard
import com.example.ui.theme.LocalArenaColors

@Composable
fun CodexLibraryScreen(
    isVipUnlocked: Boolean,
    onOpenVipDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalArenaColors.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<SkillCategory?>(null) }

    val allItems = QuestionBank.allQuestions
    val freeCount = QuestionBank.freeQuestions.size
    val totalCount = allItems.size

    val filtered = remember(searchQuery, selectedCategory, allItems) {
        val q = searchQuery.trim().lowercase()
        allItems.filter { item ->
            val matchesCat = selectedCategory == null || item.category == selectedCategory
            val matchesQuery = q.isEmpty() ||
                item.focusTag.lowercase().contains(q) ||
                item.prompt.lowercase().contains(q) ||
                item.explanationAr.lowercase().contains(q) ||
                item.options[item.correctIndex].lowercase().contains(q)
            matchesCat && matchesQuery
        }
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MenuBook,
                            contentDescription = "Codex",
                            tint = colors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "FULL PHRASEBOOK & CODEX",
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Idioms, Proverbs & Phrasal Verbs",
                        style = MaterialTheme.typography.headlineMedium,
                        color = colors.textPrimary
                    )
                    Text(
                        text = if (isVipUnlocked) {
                            "Full 100% VIP Edition active — all $totalCount expressions unlocked!"
                        } else {
                            "50% Free Edition ($freeCount/$totalCount unlocked). Activate the remaining 50% with code or WhatsApp B2B."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textSecondary
                    )
                }
            }

            if (!isVipUnlocked) {
                item {
                    Button(
                        onClick = onOpenVipDialog,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("codex_unlock_vip_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.secondary,
                            contentColor = colors.onSecondary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.WorkspacePremium,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Unlock Remaining 50% ($totalCount Full Bank)",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = colors.primary
                        )
                    },
                    placeholder = { Text("Search idioms, proverbs, phrasal verbs...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.cardBorder,
                        focusedTextColor = colors.textPrimary,
                        unfocusedTextColor = colors.textPrimary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("codex_search_input")
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("All (${allItems.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colors.primaryContainer,
                            selectedLabelColor = colors.onPrimaryContainer,
                            containerColor = colors.surfaceElevated,
                            labelColor = colors.textSecondary
                        )
                    )
                    SkillCategory.entries.forEach { cat ->
                        val catCount = allItems.count { it.category == cat }
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text("${cat.label} ($catCount)") },
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

            items(filtered, key = { it.id }) { item ->
                val locked = item.isVipOnly && !isVipUnlocked
                CodexEntryCard(
                    question = item,
                    locked = locked,
                    onUnlockClick = onOpenVipDialog
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun CodexEntryCard(
    question: QuizQuestion,
    locked: Boolean,
    onUnlockClick: () -> Unit
) {
    val colors = LocalArenaColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (locked) colors.surfaceElevated.copy(alpha = 0.65f) else colors.surface)
            .border(
                1.dp,
                if (locked) colors.secondary.copy(alpha = 0.45f) else colors.cardBorder,
                RoundedCornerShape(18.dp)
            )
            .then(if (locked) Modifier.clickable(onClick = onUnlockClick) else Modifier)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${question.category.label} • ${question.level.shortTitle}",
                style = MaterialTheme.typography.labelMedium,
                color = colors.primary,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(if (locked) colors.secondaryContainer else colors.tertiaryContainer)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (locked) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "VIP Locked",
                            tint = colors.secondary,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Text(
                        text = if (locked) "VIP LOCKED" else "FREE",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (locked) colors.onSecondaryContainer else colors.onTertiaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Text(
            text = question.focusTag,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )

        if (locked) {
            Text(
                text = "Locked in Free 50% Edition. Tap to activate 100% access via code or WhatsApp B2B (+201126542477).",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary
            )
        } else {
            Text(
                text = question.prompt.replace("_____", "[${question.options[question.correctIndex]}]"),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary
            )
            EgyptianSecretSauceCard(
                question = question,
                isCorrect = null
            )
        }
    }
}
