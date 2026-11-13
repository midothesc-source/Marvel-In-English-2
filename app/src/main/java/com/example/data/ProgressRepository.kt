package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.SkillCategory
import com.example.model.UserProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.arenaDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "marvel_arena_prefs"
)

class ProgressRepository(private val context: Context) {

    private object Keys {
        val TOTAL_XP = intPreferencesKey("total_xp")
        val HIGH_SCORE_L1 = intPreferencesKey("high_score_l1")
        val HIGH_SCORE_L2 = intPreferencesKey("high_score_l2")
        val HIGH_SCORE_L3 = intPreferencesKey("high_score_l3")
        val CURRENT_STREAK = intPreferencesKey("current_streak")
        val BEST_STREAK = intPreferencesKey("best_streak")
        val SESSIONS_COMPLETED = intPreferencesKey("sessions_completed")
        val SELECTED_THEME_ID = stringPreferencesKey("selected_theme_id")
        val IS_VIP_UNLOCKED = booleanPreferencesKey("is_vip_unlocked")
        val VIP_CODE_USED = stringPreferencesKey("vip_code_used")
        val UNLOCKED_RANKS = stringSetPreferencesKey("unlocked_ranks")
        val MISSED_QUESTIONS = stringSetPreferencesKey("missed_questions")
        val MASTERED_QUESTIONS = stringSetPreferencesKey("mastered_questions")
        val CATEGORY_CORRECT = stringPreferencesKey("category_correct")
        val CATEGORY_TOTAL = stringPreferencesKey("category_total")
    }

    val progressFlow: Flow<UserProgress> = context.arenaDataStore.data.map { prefs ->
        val totalXp = prefs[Keys.TOTAL_XP] ?: 0
        val unlocked = prefs[Keys.UNLOCKED_RANKS] ?: setOf("scout")
        UserProgress(
            totalXp = totalXp,
            highScoreLevel1 = prefs[Keys.HIGH_SCORE_L1] ?: 0,
            highScoreLevel2 = prefs[Keys.HIGH_SCORE_L2] ?: 0,
            highScoreLevel3 = prefs[Keys.HIGH_SCORE_L3] ?: 0,
            currentStreak = prefs[Keys.CURRENT_STREAK] ?: 0,
            bestStreak = prefs[Keys.BEST_STREAK] ?: 0,
            sessionsCompleted = prefs[Keys.SESSIONS_COMPLETED] ?: 0,
            selectedThemeId = prefs[Keys.SELECTED_THEME_ID] ?: "desert_force",
            isVipUnlocked = prefs[Keys.IS_VIP_UNLOCKED] ?: false,
            vipCodeUsed = prefs[Keys.VIP_CODE_USED],
            unlockedRankIds = unlocked,
            missedQuestionIds = prefs[Keys.MISSED_QUESTIONS] ?: emptySet(),
            masteredQuestionIds = prefs[Keys.MASTERED_QUESTIONS] ?: emptySet(),
            categoryCorrect = decodeCategoryMap(prefs[Keys.CATEGORY_CORRECT]),
            categoryTotal = decodeCategoryMap(prefs[Keys.CATEGORY_TOTAL])
        )
    }

    suspend fun recordSessionResult(
        xpEarned: Int,
        level1Score: Int?,
        level2Score: Int?,
        level3Score: Int?,
        endingStreak: Int,
        sessionMaxStreak: Int,
        newlyMissedIds: Set<String>,
        newlyMasteredIds: Set<String>,
        sessionCategoryCorrect: Map<SkillCategory, Int>,
        sessionCategoryTotal: Map<SkillCategory, Int>
    ) {
        context.arenaDataStore.edit { prefs ->
            val updatedXp = (prefs[Keys.TOTAL_XP] ?: 0) + xpEarned
            prefs[Keys.TOTAL_XP] = updatedXp

            if (level1Score != null) {
                val prev = prefs[Keys.HIGH_SCORE_L1] ?: 0
                if (level1Score > prev) prefs[Keys.HIGH_SCORE_L1] = level1Score
            }
            if (level2Score != null) {
                val prev = prefs[Keys.HIGH_SCORE_L2] ?: 0
                if (level2Score > prev) prefs[Keys.HIGH_SCORE_L2] = level2Score
            }
            if (level3Score != null) {
                val prev = prefs[Keys.HIGH_SCORE_L3] ?: 0
                if (level3Score > prev) prefs[Keys.HIGH_SCORE_L3] = level3Score
            }

            prefs[Keys.CURRENT_STREAK] = endingStreak
            val prevBest = prefs[Keys.BEST_STREAK] ?: 0
            prefs[Keys.BEST_STREAK] = maxOf(prevBest, sessionMaxStreak, endingStreak)
            prefs[Keys.SESSIONS_COMPLETED] = (prefs[Keys.SESSIONS_COMPLETED] ?: 0) + 1

            val unlocked = QuestionBank.desertRanks
                .filter { updatedXp >= it.minXp }
                .map { it.id }
                .toSet()
            prefs[Keys.UNLOCKED_RANKS] = (prefs[Keys.UNLOCKED_RANKS] ?: emptySet()) + unlocked

            val currentMissed = (prefs[Keys.MISSED_QUESTIONS] ?: emptySet()).toMutableSet()
            currentMissed.addAll(newlyMissedIds)
            currentMissed.removeAll(newlyMasteredIds)
            prefs[Keys.MISSED_QUESTIONS] = currentMissed

            val currentMastered = (prefs[Keys.MASTERED_QUESTIONS] ?: emptySet()).toMutableSet()
            currentMastered.addAll(newlyMasteredIds)
            currentMastered.removeAll(newlyMissedIds)
            prefs[Keys.MASTERED_QUESTIONS] = currentMastered

            val prevCorrect = decodeCategoryMap(prefs[Keys.CATEGORY_CORRECT]).toMutableMap()
            sessionCategoryCorrect.forEach { (cat, delta) ->
                prevCorrect[cat] = (prevCorrect[cat] ?: 0) + delta
            }
            prefs[Keys.CATEGORY_CORRECT] = encodeCategoryMap(prevCorrect)

            val prevTotal = decodeCategoryMap(prefs[Keys.CATEGORY_TOTAL]).toMutableMap()
            sessionCategoryTotal.forEach { (cat, delta) ->
                prevTotal[cat] = (prevTotal[cat] ?: 0) + delta
            }
            prefs[Keys.CATEGORY_TOTAL] = encodeCategoryMap(prevTotal)
        }
    }

    suspend fun removeFromVault(questionId: String) {
        context.arenaDataStore.edit { prefs ->
            val currentMissed = (prefs[Keys.MISSED_QUESTIONS] ?: emptySet()).toMutableSet()
            currentMissed.remove(questionId)
            prefs[Keys.MISSED_QUESTIONS] = currentMissed

            val currentMastered = (prefs[Keys.MASTERED_QUESTIONS] ?: emptySet()).toMutableSet()
            currentMastered.add(questionId)
            prefs[Keys.MASTERED_QUESTIONS] = currentMastered
        }
    }

    suspend fun setTheme(themeId: String) {
        context.arenaDataStore.edit { prefs ->
            prefs[Keys.SELECTED_THEME_ID] = themeId
        }
    }

    suspend fun unlockVip(code: String) {
        context.arenaDataStore.edit { prefs ->
            prefs[Keys.IS_VIP_UNLOCKED] = true
            prefs[Keys.VIP_CODE_USED] = code.trim().uppercase()
        }
    }

    suspend fun resetAllProgress() {
        context.arenaDataStore.edit { prefs ->
            prefs.clear()
        }
    }

    private fun encodeCategoryMap(map: Map<SkillCategory, Int>): String {
        return SkillCategory.entries.joinToString(",") { cat ->
            "${cat.name}:${map[cat] ?: 0}"
        }
    }

    private fun decodeCategoryMap(raw: String?): Map<SkillCategory, Int> {
        if (raw.isNullOrBlank()) return emptyMap()
        val result = mutableMapOf<SkillCategory, Int>()
        raw.split(",").forEach { pair ->
            val parts = pair.split(":")
            if (parts.size == 2) {
                val category = SkillCategory.entries.find { it.name == parts[0] }
                val count = parts[1].toIntOrNull()
                if (category != null && count != null) {
                    result[category] = count
                }
            }
        }
        return result
    }
}
