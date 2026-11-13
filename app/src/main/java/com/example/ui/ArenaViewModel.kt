package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ProgressRepository
import com.example.data.QuestionBank
import com.example.model.CategoryProgress
import com.example.model.CefrLevel
import com.example.model.DesertRank
import com.example.model.QuizQuestion
import com.example.model.SessionAnswerRecord
import com.example.model.SessionSummary
import com.example.model.SkillCategory
import com.example.model.UserProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ArenaSubScreen {
    HUB,
    QUIZ,
    SUMMARY
}

data class QuizSessionState(
    val level: CefrLevel,
    val isVaultPractice: Boolean,
    val questions: List<QuizQuestion>,
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isRevealed: Boolean = false,
    val liveStreak: Int = 0,
    val maxSessionStreak: Int = 0,
    val sessionScore: Int = 0,
    val records: List<SessionAnswerRecord> = emptyList(),
    val confettiBurstCounter: Int = 0
) {
    val currentQuestion: QuizQuestion?
        get() = questions.getOrNull(currentIndex)

    val comboMultiplier: Int
        get() = when {
            liveStreak >= 6 -> 4
            liveStreak >= 4 -> 3
            liveStreak >= 2 -> 2
            else -> 1
        }

    val progressFraction: Float
        get() {
            if (questions.isEmpty()) return 0f
            val completed = if (isRevealed) currentIndex + 1 else currentIndex
            return (completed.toFloat() / questions.size.toFloat()).coerceIn(0f, 1f)
        }
}

data class ArenaUiState(
    val selectedLevel: CefrLevel = CefrLevel.LEVEL_1,
    val selectedCategoryFilter: SkillCategory? = null,
    val subScreen: ArenaSubScreen = ArenaSubScreen.HUB,
    val userProgress: UserProgress = UserProgress(),
    val currentRank: DesertRank = QuestionBank.desertRanks.first(),
    val nextRank: DesertRank? = QuestionBank.desertRanks.getOrNull(1),
    val activeSession: QuizSessionState? = null,
    val latestSummary: SessionSummary? = null,
    val vaultQuestions: List<QuizQuestion> = emptyList(),
    val showVipDialog: Boolean = false,
    val vipError: String? = null
)

private data class TransientState(
    val selectedLevel: CefrLevel = CefrLevel.LEVEL_1,
    val selectedCategoryFilter: SkillCategory? = null,
    val subScreen: ArenaSubScreen = ArenaSubScreen.HUB,
    val activeSession: QuizSessionState? = null,
    val latestSummary: SessionSummary? = null,
    val showVipDialog: Boolean = false,
    val vipError: String? = null
)

class ArenaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProgressRepository(application.applicationContext)
    private val transientState = MutableStateFlow(TransientState())

    val uiState: StateFlow<ArenaUiState> = combine(
        repository.progressFlow,
        transientState
    ) { progress, transient ->
        val currentRank = QuestionBank.rankForXp(progress.totalXp)
        val nextRank = QuestionBank.nextRankForXp(progress.totalXp)
        val vault = progress.missedQuestionIds
            .mapNotNull { QuestionBank.questionById(it) }
            .sortedWith(compareBy({ it.level.ordinal }, { it.category.ordinal }))

        ArenaUiState(
            selectedLevel = transient.selectedLevel,
            selectedCategoryFilter = transient.selectedCategoryFilter,
            subScreen = transient.subScreen,
            userProgress = progress,
            currentRank = currentRank,
            nextRank = nextRank,
            activeSession = transient.activeSession,
            latestSummary = transient.latestSummary,
            vaultQuestions = vault,
            showVipDialog = transient.showVipDialog,
            vipError = transient.vipError
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ArenaUiState()
    )

    fun selectLevel(level: CefrLevel) {
        val vipUnlocked = uiState.value.userProgress.isVipUnlocked
        if (level.isVipRequired && !vipUnlocked) {
            openVipDialog()
            return
        }
        transientState.update { it.copy(selectedLevel = level) }
    }

    fun selectCategoryFilter(category: SkillCategory?) {
        val vipUnlocked = uiState.value.userProgress.isVipUnlocked
        if (category != null && category.isVipRequired && !vipUnlocked) {
            openVipDialog()
            return
        }
        transientState.update { it.copy(selectedCategoryFilter = category) }
    }

    fun openVipDialog() {
        transientState.update { it.copy(showVipDialog = true, vipError = null) }
    }

    fun dismissVipDialog() {
        transientState.update { it.copy(showVipDialog = false, vipError = null) }
    }

    fun submitVipCode(rawCode: String) {
        if (QuestionBank.isValidVipCode(rawCode)) {
            viewModelScope.launch {
                repository.unlockVip(rawCode)
            }
            transientState.update {
                it.copy(
                    showVipDialog = false,
                    vipError = null,
                    selectedLevel = CefrLevel.LEVEL_3
                )
            }
        } else {
            transientState.update {
                it.copy(vipError = "Invalid code. Try MARVEL-VIP, DESERT-FORCE, or CINEMA-2026.")
            }
        }
    }

    fun startMission(
        level: CefrLevel = transientState.value.selectedLevel,
        categoryFilter: SkillCategory? = transientState.value.selectedCategoryFilter
    ) {
        val vipUnlocked = uiState.value.userProgress.isVipUnlocked
        if ((level.isVipRequired || (categoryFilter?.isVipRequired == true)) && !vipUnlocked) {
            openVipDialog()
            return
        }

        val pool = QuestionBank.questionsForMission(level, categoryFilter)
        val effectivePool = if (pool.isEmpty()) QuestionBank.questionsForMission(level, null) else pool
        if (effectivePool.isEmpty()) return

        transientState.update {
            it.copy(
                selectedLevel = level,
                subScreen = ArenaSubScreen.QUIZ,
                latestSummary = null,
                activeSession = QuizSessionState(
                    level = level,
                    isVaultPractice = false,
                    questions = effectivePool.take(10),
                    liveStreak = uiState.value.userProgress.currentStreak
                )
            )
        }
    }

    fun startVaultPractice(levelFilter: CefrLevel? = null) {
        val vaultPool = uiState.value.vaultQuestions.filter {
            levelFilter == null || it.level == levelFilter
        }.shuffled()
        if (vaultPool.isEmpty()) return

        transientState.update {
            it.copy(
                subScreen = ArenaSubScreen.QUIZ,
                latestSummary = null,
                activeSession = QuizSessionState(
                    level = vaultPool.first().level,
                    isVaultPractice = true,
                    questions = vaultPool,
                    liveStreak = uiState.value.userProgress.currentStreak
                )
            )
        }
    }

    fun submitAnswer(optionIndex: Int) {
        val session = transientState.value.activeSession ?: return
        if (session.isRevealed) return
        val question = session.currentQuestion ?: return

        val isCorrect = optionIndex == question.correctIndex
        val newStreak = if (isCorrect) session.liveStreak + 1 else 0
        val newMaxStreak = maxOf(session.maxSessionStreak, newStreak)

        val multiplier = when {
            newStreak >= 6 -> 4
            newStreak >= 4 -> 3
            newStreak >= 2 -> 2
            else -> 1
        }
        val earnedXp = if (isCorrect) question.baseXp * multiplier else 0
        val record = SessionAnswerRecord(
            question = question,
            selectedIndex = optionIndex,
            isCorrect = isCorrect,
            earnedXp = earnedXp
        )

        transientState.update { state ->
            state.copy(
                activeSession = session.copy(
                    selectedOptionIndex = optionIndex,
                    isRevealed = true,
                    liveStreak = newStreak,
                    maxSessionStreak = newMaxStreak,
                    sessionScore = session.sessionScore + earnedXp,
                    records = session.records + record,
                    confettiBurstCounter = if (isCorrect) session.confettiBurstCounter + 1 else session.confettiBurstCounter
                )
            )
        }
    }

    fun nextQuestion() {
        val session = transientState.value.activeSession ?: return
        if (!session.isRevealed) return

        val nextIndex = session.currentIndex + 1
        if (nextIndex < session.questions.size) {
            transientState.update {
                it.copy(
                    activeSession = session.copy(
                        currentIndex = nextIndex,
                        selectedOptionIndex = null,
                        isRevealed = false
                    )
                )
            }
        } else {
            finishSession(session)
        }
    }

    private fun finishSession(session: QuizSessionState) {
        val previousProgress = uiState.value.userProgress
        val previousRank = QuestionBank.rankForXp(previousProgress.totalXp)

        val newlyMissed = session.records
            .filter { !it.isCorrect }
            .map { it.question.id }
            .toSet()

        val newlyMastered = session.records
            .filter { it.isCorrect }
            .map { it.question.id }
            .toSet()

        val catCorrect = mutableMapOf<SkillCategory, Int>()
        val catTotal = mutableMapOf<SkillCategory, Int>()
        session.records.forEach { rec ->
            val cat = rec.question.category
            catTotal[cat] = (catTotal[cat] ?: 0) + 1
            if (rec.isCorrect) {
                catCorrect[cat] = (catCorrect[cat] ?: 0) + 1
            }
        }

        val breakdowns = SkillCategory.entries.map { cat ->
            CategoryProgress(
                category = cat,
                correctCount = catCorrect[cat] ?: 0,
                totalAttempts = catTotal[cat] ?: 0
            )
        }.filter { it.totalAttempts > 0 }

        val updatedXp = previousProgress.totalXp + session.sessionScore
        val updatedRank = QuestionBank.rankForXp(updatedXp)
        val unlockedNewRank = if (updatedRank.minXp > previousRank.minXp) updatedRank else null

        val previousHigh = previousProgress.highScoreFor(session.level)
        val isNewHighScore = !session.isVaultPractice && session.sessionScore > previousHigh && session.sessionScore > 0

        val summary = SessionSummary(
            level = session.level,
            isVaultPractice = session.isVaultPractice,
            totalScore = session.sessionScore,
            correctCount = session.records.count { it.isCorrect },
            totalQuestions = session.questions.size,
            maxCombo = session.maxSessionStreak,
            isNewHighScore = isNewHighScore,
            newlyUnlockedRank = unlockedNewRank,
            records = session.records,
            categoryBreakdowns = breakdowns
        )

        transientState.update {
            it.copy(
                subScreen = ArenaSubScreen.SUMMARY,
                activeSession = null,
                latestSummary = summary
            )
        }

        viewModelScope.launch {
            repository.recordSessionResult(
                xpEarned = session.sessionScore,
                level1Score = if (!session.isVaultPractice && session.level == CefrLevel.LEVEL_1) session.sessionScore else null,
                level2Score = if (!session.isVaultPractice && session.level == CefrLevel.LEVEL_2) session.sessionScore else null,
                level3Score = if (!session.isVaultPractice && session.level == CefrLevel.LEVEL_3) session.sessionScore else null,
                endingStreak = session.liveStreak,
                sessionMaxStreak = session.maxSessionStreak,
                newlyMissedIds = newlyMissed,
                newlyMasteredIds = newlyMastered,
                sessionCategoryCorrect = catCorrect,
                sessionCategoryTotal = catTotal
            )
        }
    }

    fun exitQuizToHub() {
        transientState.update {
            it.copy(
                subScreen = ArenaSubScreen.HUB,
                activeSession = null,
                latestSummary = null
            )
        }
    }

    fun markVaultQuestionMastered(questionId: String) {
        viewModelScope.launch {
            repository.removeFromVault(questionId)
        }
    }

    fun selectTheme(themeId: String) {
        viewModelScope.launch {
            repository.setTheme(themeId)
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            repository.resetAllProgress()
            transientState.update {
                TransientState()
            }
        }
    }
}
