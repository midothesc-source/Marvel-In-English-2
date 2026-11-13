package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.QuestionBank
import com.example.model.CefrLevel
import com.example.ui.theme.ArenaThemes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `app name matches Marvel in English`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Marvel in English", appName)
    }

    @Test
    fun `question bank covers all 3 CEFR levels with Egyptian Arabic secret sauce`() {
        val l1 = QuestionBank.allQuestions.filter { it.level == CefrLevel.LEVEL_1 }
        val l2 = QuestionBank.allQuestions.filter { it.level == CefrLevel.LEVEL_2 }
        val l3 = QuestionBank.allQuestions.filter { it.level == CefrLevel.LEVEL_3 }

        assertTrue(l1.size >= 9)
        assertTrue(l2.size >= 9)
        assertTrue(l3.size >= 9)

        QuestionBank.allQuestions.forEach { q ->
            assertTrue("Question ${q.id} must have 4 options", q.options.size == 4)
            assertTrue("Question ${q.id} valid index", q.correctIndex in 0..3)
            assertTrue("Question ${q.id} explanation non-blank", q.explanationAr.isNotBlank())
            assertTrue("Question ${q.id} pro-tip non-blank", q.proTipAr.contains("حركة فنان"))
        }
    }

    @Test
    fun `vip code validation works case-insensitively`() {
        assertTrue(QuestionBank.isValidVipCode("marvel-vip"))
        assertTrue(QuestionBank.isValidVipCode(" DESERT-FORCE "))
        assertTrue(QuestionBank.isValidVipCode("CINEMA-2026"))
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
