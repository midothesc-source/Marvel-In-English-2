package com.example

import com.example.data.QuestionBank
import com.example.model.CefrLevel
import com.example.model.SkillCategory
import com.example.ui.theme.ArenaThemes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun `question bank covers idioms, proverbs, phrasal verbs and collocations with exact 50 percent free split`() {
        val all = QuestionBank.allQuestions
        val free = QuestionBank.freeQuestions
        val vip = QuestionBank.vipQuestions

        assertEquals(160, all.size)
        assertEquals(80, free.size)
        assertEquals(80, vip.size)
        assertEquals(all.size / 2, free.size)

        CefrLevel.entries.forEach { level ->
            assertTrue("Level $level must have free questions", free.any { it.level == level })
        }

        assertEquals(50, all.count { it.category == SkillCategory.IDIOMATIC_POWER })
        assertEquals(40, all.count { it.category == SkillCategory.PROVERBS })
        assertEquals(50, all.count { it.category == SkillCategory.PHRASAL_VERBS })
        assertEquals(25, free.count { it.category == SkillCategory.IDIOMATIC_POWER })
        assertEquals(20, free.count { it.category == SkillCategory.PROVERBS })
        assertEquals(25, free.count { it.category == SkillCategory.PHRASAL_VERBS })

        all.forEach { q ->
            assertTrue("Question ${q.id} must have 4 options", q.options.size == 4)
            assertTrue("Question ${q.id} valid index", q.correctIndex in 0..3)
            assertTrue("Question ${q.id} explanation non-blank", q.explanationAr.isNotBlank())
            assertTrue("Question ${q.id} pro-tip non-blank", q.proTipAr.contains("حركة فنان"))
        }
    }

    @Test
    fun `single vip activation code Desert_Force works cleanly`() {
        assertTrue(QuestionBank.isValidVipCode("Desert_Force"))
        assertTrue(QuestionBank.isValidVipCode(" desert_force "))
        assertFalse(QuestionBank.isValidVipCode("MARVEL-VIP"))
        assertFalse(QuestionBank.isValidVipCode("CINEMA-2026"))
        assertFalse(QuestionBank.isValidVipCode("WRONG-CODE"))
    }

    @Test
    fun `desert rank progression resolves correctly`() {
        assertEquals("scout", QuestionBank.rankForXp(0).id)
        assertEquals("pathfinder", QuestionBank.rankForXp(150).id)
        assertEquals("master", QuestionBank.rankForXp(1600).id)
    }

    @Test
    fun `arena provides 10 distinct colorful themes`() {
        assertEquals(10, ArenaThemes.all.size)
        val ids = ArenaThemes.all.map { it.id }.toSet()
        assertEquals(10, ids.size)
        ArenaThemes.all.forEach { theme ->
            assertTrue(theme.name.isNotBlank())
            assertTrue(theme.arabicName.isNotBlank())
            assertEquals(4, theme.previewSwatches.size)
            assertEquals(theme, ArenaThemes.byId(theme.id))
        }
        assertEquals(ArenaThemes.DesertForce, ArenaThemes.byId("unknown_id"))
    }
}
