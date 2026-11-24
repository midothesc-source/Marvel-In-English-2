package com.example.data

import com.example.model.CefrLevel
import com.example.model.DesertRank
import com.example.model.QuizQuestion
import com.example.model.SkillCategory

object QuestionBank {

    const val ACTIVATION_CODE = "Desert_Force"

    fun isValidVipCode(input: String): Boolean {
        return input.trim().equals(ACTIVATION_CODE, ignoreCase = true)
    }

    val desertRanks: List<DesertRank> = listOf(
        DesertRank(
            id = "scout",
            title = "Desert Scout",
            arabicTitle = "كشّاف الصحراء",
            minXp = 0,
            mottoEn = "Every legend begins with the courage to enter the arena.",
            mottoAr = "أول خطوة في الصحرا بتفرق بين المتفرج والبطل."
        ),
        DesertRank(
            id = "pathfinder",
            title = "Sand Pathfinder",
            arabicTitle = "دليل الرمال",
            minXp = 150,
            mottoEn = "You spot idioms and phrasal traps before the wind shifts.",
            mottoAr = "عينك بدأت تلقط التعبيرات والتريكات قبل ما تقع فيها."
        ),
        DesertRank(
            id = "vanguard",
            title = "Oasis Vanguard",
            arabicTitle = "طليعة الواحة",
            minXp = 350,
            mottoEn = "Calm under pressure, razor-sharp with everyday fluency.",
            mottoAr = "ثابت في الكلام، وكلمتك دايماً في الجون."
        ),
        DesertRank(
            id = "tactician",
            title = "Mirage Tactician",
            arabicTitle = "مخطط السراب",
            minXp = 650,
            mottoEn = "Collocations and proverbs flow naturally in your speech.",
            mottoAr = "الأمثال والمتلازمات اللفظية بقت بتطلع منك بسلاسة."
        ),
        DesertRank(
            id = "commander",
            title = "Falcon Commander",
            arabicTitle = "قائد الصقور",
            minXp = 1000,
            mottoEn = "Your English strikes with cinematic clarity and power.",
            mottoAr = "تركيزك عالي وإنجليزيتك بتتكلم زي نجوم السينما."
        ),
        DesertRank(
            id = "master",
            title = "Desert Force Master",
            arabicTitle = "سيد قوة الصحراء",
            minXp = 1500,
            mottoEn = "Supreme C1–C2 fluency. You own the Ultimate Arena.",
            mottoAr = "أسطورة الساحة.. قفلت اللعبة يا بطل!"
        )
    )

    fun rankForXp(xp: Int): DesertRank {
        return desertRanks.lastOrNull { xp >= it.minXp } ?: desertRanks.first()
    }

    fun nextRankForXp(xp: Int): DesertRank? {
        return desertRanks.firstOrNull { it.minXp > xp }
    }

    val allQuestions: List<QuizQuestion> =
        IdiomsCatalog.items +
            ProverbsCatalog.items +
            PhrasalVerbsCatalog.items +
            CollocationsCinematicCatalog.items

    val freeQuestions: List<QuizQuestion>
        get() = allQuestions.filter { !it.isVipOnly }

    val vipQuestions: List<QuizQuestion>
        get() = allQuestions.filter { it.isVipOnly }

    fun accessibleQuestions(isVipUnlocked: Boolean): List<QuizQuestion> {
        return if (isVipUnlocked) allQuestions else freeQuestions
    }

    fun questionsForMission(
        level: CefrLevel,
        categoryFilter: SkillCategory? = null,
        isVipUnlocked: Boolean = false
    ): List<QuizQuestion> {
        val unlockedPool = accessibleQuestions(isVipUnlocked)
        val exact = unlockedPool.filter {
            it.level == level && (categoryFilter == null || it.category == categoryFilter)
        }
        if (exact.isNotEmpty()) return exact.shuffled()

        // If a specific level+category combination has no free items, fall back to unlocked items for that category or level
        val byCategory = if (categoryFilter != null) {
            unlockedPool.filter { it.category == categoryFilter }
        } else emptyList()
        if (byCategory.isNotEmpty()) return byCategory.shuffled()

        return unlockedPool.filter { it.level == level }.shuffled()
    }

    fun questionById(id: String): QuizQuestion? = allQuestions.find { it.id == id }
}
