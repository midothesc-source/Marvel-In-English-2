package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.ArenaSubScreen
import com.example.ui.ArenaUiState
import com.example.ui.ArenaViewModel
import com.example.ui.components.ThemePickerSheet
import com.example.ui.components.VipUnlockDialog
import com.example.ui.screens.ArenaHubScreen
import com.example.ui.screens.DesertRanksScreen
import com.example.ui.screens.PerformanceSummaryScreen
import com.example.ui.screens.QuizArenaScreen
import com.example.ui.screens.ReviewVaultScreen
import com.example.ui.theme.LocalArenaColors
import com.example.ui.theme.MarvelArenaTheme

const val ROUTE_ARENA = "arena"
const val ROUTE_VAULT = "vault"
const val ROUTE_RANKS = "ranks"

private data class NavTabItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val TopNavTabs = listOf(
    NavTabItem(
        route = ROUTE_ARENA,
        label = "Arena",
        selectedIcon = Icons.Filled.Bolt,
        unselectedIcon = Icons.Outlined.Bolt
    ),
    NavTabItem(
        route = ROUTE_VAULT,
        label = "Vault",
        selectedIcon = Icons.Filled.Inventory2,
        unselectedIcon = Icons.Outlined.Inventory2
    ),
    NavTabItem(
        route = ROUTE_RANKS,
        label = "Ranks",
        selectedIcon = Icons.Filled.MilitaryTech,
        unselectedIcon = Icons.Outlined.MilitaryTech
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ArenaViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            MarvelArenaTheme(themeId = uiState.userProgress.selectedThemeId) {
                MarvelArenaApp(
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun MarvelArenaApp(
    uiState: ArenaUiState,
    viewModel: ArenaViewModel
) {
    val colors = LocalArenaColors.current
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ROUTE_ARENA
    val inQuizMode = currentRoute == ROUTE_ARENA && uiState.subScreen == ArenaSubScreen.QUIZ
    val vaultCount = uiState.vaultQuestions.size
    var showThemeSheet by remember { mutableStateOf(false) }

    if (showThemeSheet) {
        ThemePickerSheet(
            selectedThemeId = uiState.userProgress.selectedThemeId,
            onSelectTheme = { themeId ->
                viewModel.selectTheme(themeId)
            },
            onDismiss = { showThemeSheet = false }
        )
    }

    if (uiState.showVipDialog) {
        VipUnlockDialog(
            errorText = uiState.vipError,
            onSubmitCode = viewModel::submitVipCode,
            onDismiss = viewModel::dismissVipDialog
        )
    }

    fun navigateTab(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colors.background,
                        colors.surface.copy(alpha = 0.8f),
                        colors.background
                    )
                )
            )
    ) {
        val useRail = maxWidth >= 600.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                if (!useRail && !inQuizMode) {
                    NavigationBar(
                        containerColor = colors.surface,
                        windowInsets = WindowInsets.navigationBars
                    ) {
                        TopNavTabs.forEach { tab ->
                            val selected = currentRoute == tab.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = { navigateTab(tab.route) },
                                icon = {
                                    TabIconWithBadge(
                                        tab = tab,
                                        selected = selected,
                                        vaultCount = vaultCount
                                    )
                                },
                                label = { Text(tab.label) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = colors.primary,
                                    selectedTextColor = colors.primary,
                                    indicatorColor = colors.primaryContainer,
                                    unselectedIconColor = colors.textSecondary,
                                    unselectedTextColor = colors.textSecondary
                                ),
                                modifier = Modifier.testTag("nav_${tab.route}")
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (useRail && !inQuizMode) {
                    NavigationRail(
                        containerColor = colors.surface,
                        modifier = Modifier.statusBarsPadding()
                    ) {
                        TopNavTabs.forEach { tab ->
                            val selected = currentRoute == tab.route
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateTab(tab.route) },
                                icon = {
                                    TabIconWithBadge(
                                        tab = tab,
                                        selected = selected,
                                        vaultCount = vaultCount
                                    )
                                },
                                label = { Text(tab.label) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = colors.primary,
                                    selectedTextColor = colors.primary,
                                    indicatorColor = colors.primaryContainer,
                                    unselectedIconColor = colors.textSecondary,
                                    unselectedTextColor = colors.textSecondary
                                ),
                                modifier = Modifier.testTag("rail_${tab.route}")
                            )
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    NavHost(
                        navController = navController,
                        startDestination = ROUTE_ARENA
                    ) {
                        composable(ROUTE_ARENA) {
                            Crossfade(
                                targetState = uiState.subScreen,
                                label = "arena_subscreen"
                            ) { subScreen ->
                                when (subScreen) {
                                    ArenaSubScreen.HUB -> {
                                        ArenaHubScreen(
                                            uiState = uiState,
                                            onSelectLevel = viewModel::selectLevel,
                                            onSelectCategory = viewModel::selectCategoryFilter,
                                            onSelectTheme = viewModel::selectTheme,
                                            onOpenThemeStudio = { showThemeSheet = true },
                                            onOpenVipDialog = viewModel::openVipDialog,
                                            onLaunchMission = { viewModel.startMission() },
                                            onPracticeVault = { viewModel.startVaultPractice() }
                                        )
                                    }

                                    ArenaSubScreen.QUIZ -> {
                                        val session = uiState.activeSession
                                        if (session != null) {
                                            QuizArenaScreen(
                                                session = session,
                                                onSelectOption = viewModel::submitAnswer,
                                                onNextQuestion = viewModel::nextQuestion,
                                                onExitQuiz = viewModel::exitQuizToHub
                                            )
                                        }
                                    }

                                    ArenaSubScreen.SUMMARY -> {
                                        val summary = uiState.latestSummary
                                        if (summary != null) {
                                            PerformanceSummaryScreen(
                                                summary = summary,
                                                currentRank = uiState.currentRank,
                                                onPlayAgain = {
                                                    if (summary.isVaultPractice && uiState.vaultQuestions.isNotEmpty()) {
                                                        viewModel.startVaultPractice()
                                                    } else {
                                                        viewModel.startMission(summary.level)
                                                    }
                                                },
                                                onBackToHub = viewModel::exitQuizToHub
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        composable(ROUTE_VAULT) {
                            ReviewVaultScreen(
                                vaultQuestions = uiState.vaultQuestions,
                                onPracticeVault = { levelFilter ->
                                    viewModel.startVaultPractice(levelFilter)
                                    navigateTab(ROUTE_ARENA)
                                },
                                onMarkMastered = viewModel::markVaultQuestionMastered
                            )
                        }

                        composable(ROUTE_RANKS) {
                            DesertRanksScreen(
                                uiState = uiState,
                                onOpenThemeStudio = { showThemeSheet = true },
                                onResetProgress = viewModel::resetProgress
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabIconWithBadge(
    tab: NavTabItem,
    selected: Boolean,
    vaultCount: Int
) {
    val colors = LocalArenaColors.current
    val iconVector = if (selected) tab.selectedIcon else tab.unselectedIcon
    if (tab.route == ROUTE_VAULT && vaultCount > 0) {
        BadgedBox(
            badge = {
                Badge(
                    containerColor = colors.secondary,
                    contentColor = colors.onSecondary
                ) {
                    Text(text = vaultCount.toString())
                }
            }
        ) {
            Icon(imageVector = iconVector, contentDescription = tab.label)
        }
    } else {
        Icon(imageVector = iconVector, contentDescription = tab.label)
    }
}
