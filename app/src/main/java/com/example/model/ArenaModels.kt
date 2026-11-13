package com.example.model

enum class CefrLevel(
    val shortTitle: String,
    val title: String,
    val badge: String,
    val subtitle: String,
    val cefrRange: String,
    val isVipRequired: Boolean
) {
    LEVEL_1(
        shortTitle = "Level 1",
        title = "Beginner & Essential Idioms",
        badge = "LVL 1 • A2–B1 Foundation",
        subtitle = "Foundational phrasal verbs & everyday expressions",
        cefrRange = "A2–B1",
        isVipRequired = false
    ),
    LEVEL_2(
        shortTitle = "Level 2",
        title = "Intermediate & Colloquial Power",
        badge = "LVL 2 • B1–B2 Power",
        subtitle = "B1–B2 idioms, proverbs & conversational collocations",
        cefrRange = "B1–B2",
        isVipRequired = false
    ),
    LEVEL_3(
        shortTitle = "Level 3",
        title = "Advanced & Cinematic Mastery",
        badge = "LVL 3 • C1–C2 VIP",
        subtitle = "High-level idioms, cinematic slang & advanced expressions",
        cefrRange = "C1–C2",
        isVipRequired = true
    )
}

enum class SkillCategory(
    val label: String,
    val arabicLabel: String,
    val isVipRequired: Boolean = false
) {
    PHRASAL_VERBS("Phrasal Verbs", "الأفعال الاصطلاحية", false),
    COLLOCATIONS("Collocations & Proverbs", "المتلازمات والأمثال", false),
    IDIOMATIC_POWER("Idioms & Expressions", "التعبيرات الاصطلاحية", false),
    CINEMATIC_SLANG("Cinematic & Slang", "لغة السينما والاحتراف", true)
}

data class QuizQuestion(
    val id: String,
    val level: CefrLevel,
    val category: SkillCategory,
    val focusTag: String,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanationAr: String,
    val proTipAr: String,
    val baseXp: Int = 25
)

data class DesertRank(
    val id: String,
    val title: String,
    val arabicTitle: String,
    val minXp: Int,
    val mottoEn: String,
    val mottoAr: String
)

data class CategoryProgress(
    val category: SkillCategory,
    val correctCount: Int,
    val totalAttempts: Int
) {
    val accuracyPercent: Int
        get() = if (totalAttempts == 0) 0 else ((correctCount.toFloat() / totalAttempts) * 100).toInt()
}

data class UserProgress(
    val totalXp: Int = 0,
    val highScoreLevel1: Int = 0,
    val highScoreLevel2: Int = 0,
    val highScoreLevel3: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val sessionsCompleted: Int = 0,
    val selectedThemeId: String = "desert_force",
    val isVipUnlocked: Boolean = false,
    val vipCodeUsed: String? = null,
    val unlockedRankIds: Set<String> = setOf("scout"),
    val missedQuestionIds: Set<String> = emptySet(),
    val masteredQuestionIds: Set<String> = emptySet(),
    val categoryCorrect: Map<SkillCategory, Int> = emptyMap(),
    val categoryTotal: Map<SkillCategory, Int> = emptyMap()
) {
    fun highScoreFor(level: CefrLevel): Int = when (level) {
        CefrLevel.LEVEL_1 -> highScoreLevel1
        CefrLevel.LEVEL_2 -> highScoreLevel2
        CefrLevel.LEVEL_3 -> highScoreLevel3
    }
}

data class SessionAnswerRecord(
    val question: QuizQuestion,
    val selectedIndex: Int,
    val isCorrect: Boolean,
    val earnedXp: Int
)

data class SessionSummary(
    val level: CefrLevel,
    val isVaultPractice: Boolean,
    val totalScore: Int,
    val correctCount: Int,
    val totalQuestions: Int,
    val maxCombo: Int,
    val isNewHighScore: Boolean,
    val newlyUnlockedRank: DesertRank?,
    val records: List<SessionAnswerRecord>,
    val categoryBreakdowns: List<CategoryProgress>
) {
    val accuracyPercent: Int
        get() = if (totalQuestions == 0) 0 else ((correctCount.toFloat() / totalQuestions) * 100).toInt()

    val weakSpots: List<CategoryProgress>
        get() = categoryBreakdowns.filter { it.totalAttempts > 0 && it.accuracyPercent < 70 }
            .sortedBy { it.accuracyPercent }
}
