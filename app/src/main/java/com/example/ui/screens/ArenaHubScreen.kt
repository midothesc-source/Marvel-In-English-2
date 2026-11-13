package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.QuestionBank
import com.example.model.CefrLevel
import com.example.model.SkillCategory
import com.example.ui.ArenaUiState
import com.example.ui.components.GlowingProgressBar
import com.example.ui.theme.ArenaThemes
import com.example.ui.theme.LocalArenaColors

@Composable
fun ArenaHubScreen(
    uiState: ArenaUiState,
    onSelectLevel: (CefrLevel) -> Unit,
    onSelectCategory: (SkillCategory?) -> Unit,
    onSelectTheme: (String) -> Unit,
    onOpenThemeStudio: () -> Unit,
    onOpenVipDialog: () -> Unit,
    onLaunchMission: () -> Unit,
    onPracticeVault: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalArenaColors.current
    val progress = uiState.userProgress
    val selectedLevel = uiState.selectedLevel
    val questionCountForSelection = QuestionBank.questionsForMission(
        level = selectedLevel,
        categoryFilter = uiState.selectedCategoryFilter
    ).let { if (it.isEmpty()) QuestionBank.questionsForMission(selectedLevel, null) else it }.size.coerceAtMost(10)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Desert Force Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .border(1.5.dp, colors.cardBorder, RoundedCornerShape(24.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_desert_arena_hero),
                        contentDescription = "The Desert Force Ultimate Arena",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF070B16).copy(alpha = 0.28f),
                                        Color(0xFF070B16).copy(alpha = 0.92f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFF070B16).copy(alpha = 0.78f))
                                    .border(1.dp, colors.secondary.copy(alpha = 0.8f), CircleShape)
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Shield,
                                    contentDescription = "Rank",
                                    tint = colors.secondary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = uiState.currentRank.title,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color(0xFFFDE68A),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                // VIP Status Pill
                                Row(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color(0xFF070B16).copy(alpha = 0.82f))
                                        .border(
                                            1.dp,
                                            if (progress.isVipUnlocked) colors.tertiary else colors.secondary,
                                            CircleShape
                                        )
                                        .clickable(onClick = onOpenVipDialog)
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                        .testTag("vip_pill_button"),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.WorkspacePremium,
                                        contentDescription = "VIP Pass",
                                        tint = if (progress.isVipUnlocked) colors.tertiary else colors.secondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = if (progress.isVipUnlocked) "VIP ACTIVE" else "VIP CODE",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color(0xFF070B16).copy(alpha = 0.78f))
                                        .border(1.dp, colors.primary.copy(alpha = 0.75f), CircleShape)
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Bolt,
                                        contentDescription = "Total XP",
                                        tint = colors.primary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text(
                                        text = "${progress.totalXp} XP",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Column {
                            Text(
                                text = "MARVEL IN ENGLISH",
                                style = MaterialTheme.typography.labelMedium,
                                color = colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "The Ultimate Arena",
                                style = MaterialTheme.typography.displaySmall,
                                color = Color.White
                            )
                            Text(
                                text = "CEFR Levels 1–3 • Idioms, Proverbs & Cinematic Slang",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFCBD5E1)
                            )
                        }
                    }
                }
            }

            // Quick Colorful Theme Strip + Theme Studio Launcher
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(colors.surface)
                        .border(1.dp, colors.cardBorder, RoundedCornerShape(18.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Palette,
                                contentDescription = "Themes",
                                tint = colors.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Vibe: ${colors.name}",
                                style = MaterialTheme.typography.titleMedium,
                                color = colors.textPrimary
                            )
                        }

                        Row(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(colors.primaryContainer)
                                .clickable(onClick = onOpenThemeStudio)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("open_theme_studio_button"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "All 10 Themes",
                                style = MaterialTheme.typography.labelMedium,
                                color = colors.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ArenaThemes.all.forEach { palette ->
                            val isActive = palette.id == colors.id
                            Row(
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .clip(CircleShape)
                                    .background(if (isActive) palette.primaryContainer else colors.surfaceElevated)
                                    .border(
                                        width = if (isActive) 2.dp else 1.dp,
                                        color = if (isActive) palette.primary else colors.cardBorder,
                                        shape = CircleShape
                                    )
                                    .clickable { onSelectTheme(palette.id) }
                                    .padding(horizontal = 10.dp, vertical = 7.dp)
                                    .testTag("quick_theme_${palette.id}"),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(palette.primary, palette.secondary)
                                            )
                                        )
                                )
                                Text(
                                    text = palette.name,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isActive) palette.onPrimaryContainer else colors.textSecondary,
                                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Quick Telemetry Strip
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatTile(
                        label = "Lvl 1 Best",
                        value = "${progress.highScoreLevel1} XP",
                        icon = Icons.Filled.EmojiEvents,
                        accent = colors.tertiary,
                        modifier = Modifier.weight(1f)
                    )
                    StatTile(
                        label = "Lvl 2 Best",
                        value = "${progress.highScoreLevel2} XP",
                        icon = Icons.Filled.EmojiEvents,
                        accent = colors.primary,
                        modifier = Modifier.weight(1f)
                    )
                    StatTile(
                        label = "Best Fire",
                        value = "${progress.bestStreak}x",
                        icon = Icons.Filled.LocalFireDepartment,
                        accent = colors.streakFlame,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Rank Progress Pill
            item {
                val nextRank = uiState.nextRank
                val prevMin = uiState.currentRank.minXp
                val rankProgress = if (nextRank == null) {
                    1f
                } else {
                    val span = (nextRank.minXp - prevMin).coerceAtLeast(1)
                    ((progress.totalXp - prevMin).toFloat() / span).coerceIn(0f, 1f)
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(colors.surface)
                        .border(1.dp, colors.cardBorder, RoundedCornerShape(18.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "The Desert Force Path",
                            style = MaterialTheme.typography.titleMedium,
                            color = colors.textPrimary
                        )
                        Text(
                            text = if (nextRank != null) {
                                "${nextRank.minXp - progress.totalXp} XP to ${nextRank.title}"
                            } else {
                                "Max Rank Unlocked!"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.secondary
                        )
                    }
                    GlowingProgressBar(progress = rankProgress)
                }
            }

            // Progressive CEFR Level Selector (Levels 1, 2, 3)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Choose CEFR Learning Track",
                        style = MaterialTheme.typography.titleLarge,
                        color = colors.textPrimary
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        CefrLevel.entries.forEach { level ->
                            val isLocked = level.isVipRequired && !progress.isVipUnlocked
                            CefrLevelCard(
                                level = level,
                                isSelected = selectedLevel == level,
                                isLocked = isLocked,
                                highScore = progress.highScoreFor(level),
                                onClick = { onSelectLevel(level) },
                                modifier = Modifier.testTag("level_card_${level.name.lowercase()}")
                            )
                        }
                    }
                }
            }

            // Skill Category Filter Chips
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Mission Focus",
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.textPrimary
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = uiState.selectedCategoryFilter == null,
                            onClick = { onSelectCategory(null) },
                            label = { Text("All Arena Skills") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = colors.primaryContainer,
                                selectedLabelColor = colors.onPrimaryContainer,
                                containerColor = colors.surfaceElevated,
                                labelColor = colors.textSecondary
                            ),
                            modifier = Modifier.testTag("filter_all")
                        )
                        SkillCategory.entries.forEach { category ->
                            val lockedCat = category.isVipRequired && !progress.isVipUnlocked
                            FilterChip(
                                selected = uiState.selectedCategoryFilter == category,
                                onClick = { onSelectCategory(category) },
                                leadingIcon = if (lockedCat) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Lock,
                                            contentDescription = "VIP Category",
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                } else null,
                                label = { Text(category.label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = colors.primaryContainer,
                                    selectedLabelColor = colors.onPrimaryContainer,
                                    containerColor = colors.surfaceElevated,
                                    labelColor = colors.textSecondary
                                ),
                                modifier = Modifier.testTag("filter_${category.name.lowercase()}")
                            )
                        }
                    }
                }
            }

            // Launch Mission CTA
            item {
                Button(
                    onClick = onLaunchMission,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .testTag("launch_mission_button"),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Launch ${selectedLevel.shortTitle} Mission ($questionCountForSelection Qs)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Vault Quick Rescue Card
            if (uiState.vaultQuestions.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(colors.secondaryContainer.copy(alpha = 0.75f))
                            .border(1.dp, colors.secondary.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
                            .clickable(onClick = onPracticeVault)
                            .padding(16.dp)
                            .testTag("quick_vault_drill_card"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Review Vault: ${uiState.vaultQuestions.size} Missed Challenges",
                                style = MaterialTheme.typography.titleMedium,
                                color = colors.onSecondaryContainer
                            )
                            Text(
                                text = "Turn weak spots into XP right now",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.textSecondary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(colors.secondary)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Drill",
                                style = MaterialTheme.typography.labelLarge,
                                color = colors.onSecondary
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }
        }
    }
}

@Composable
private fun StatTile(
    label: String,
    value: String,
    icon: ImageVector,
    accent: Color,
    modifier: Modifier = Modifier
) {
    val colors = LocalArenaColors.current
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(colors.surface)
            .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = accent,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = colors.textSecondary
        )
    }
}

@Composable
private fun CefrLevelCard(
    level: CefrLevel,
    isSelected: Boolean,
    isLocked: Boolean,
    highScore: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalArenaColors.current
    val accent = when (level) {
        CefrLevel.LEVEL_1 -> colors.tertiary
        CefrLevel.LEVEL_2 -> colors.primary
        CefrLevel.LEVEL_3 -> colors.secondary
    }
    val borderColor = if (isSelected) accent else colors.cardBorder
    val bgColor = if (isSelected) colors.surfaceElevated else colors.surface

    Row(
        modifier = modifier
            .fillMaxWidth()
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .border(if (isSelected) 2.dp else 1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(accent.copy(alpha = 0.16f))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${level.shortTitle} • ${level.cefrRange}",
                        style = MaterialTheme.typography.labelSmall,
                        color = accent,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (level.isVipRequired) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(colors.secondaryContainer)
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = if (isLocked) Icons.Filled.Lock else Icons.Filled.WorkspacePremium,
                            contentDescription = "VIP",
                            tint = colors.secondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = if (isLocked) "VIP LOCKED" else "VIP UNLOCKED",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.onSecondaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = level.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
            Text(
                text = level.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = if (isLocked) "Unlock" else "$highScore XP",
                style = MaterialTheme.typography.labelLarge,
                color = accent,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (isLocked) "Enter Code" else "Best",
                style = MaterialTheme.typography.labelSmall,
                color = colors.textMuted
            )
        }
    }
}
