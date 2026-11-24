package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.model.DesertRank
import com.example.model.SessionSummary

object WhatsAppHelper {

    const val B2B_WHATSAPP_DISPLAY = "+201126542477"
    const val B2B_WHATSAPP_DIGITS = "201126542477"

    fun buildScoreShareMessage(summary: SessionSummary, rank: DesertRank): String {
        val weakText = if (summary.weakSpots.isEmpty()) {
            "No weak spots — total Desert Force domination!"
        } else {
            "Training focus next: ${summary.weakSpots.joinToString { it.category.label }}"
        }
        return buildString {
            appendLine("🔥 *Marvel in English: The Ultimate Arena*")
            appendLine("Track: ${summary.level.shortTitle} — ${summary.level.title} (${summary.level.cefrRange})")
            appendLine("Score: ${summary.totalScore} XP (${summary.accuracyPercent}% accuracy)")
            appendLine("Best Streak Fire: ${summary.maxCombo}x")
            appendLine("Desert Force Rank: ${rank.title} (${rank.arabicTitle})")
            append(weakText)
        }
    }

    fun buildTutorInquiryMessage(summary: SessionSummary, rank: DesertRank): String {
        val weakTopics = if (summary.weakSpots.isEmpty()) {
            "Advanced idioms, proverbs & phrasal verbs"
        } else {
            summary.weakSpots.joinToString(", ") { "${it.category.label} (${it.accuracyPercent}%)" }
        }
        return buildString {
            appendLine("أهلاً مستر/فريق Marvel in English! 👋")
            appendLine("خلصت جولة في تطبيق Marvel in English (${summary.level.shortTitle}: ${summary.level.title}).")
            appendLine("نتيجتي: ${summary.correctCount}/${summary.totalQuestions} (${summary.accuracyPercent}%) - الرتبة: ${rank.title}.")
            append("حابب أتدرب أكتر أو أستفسر عن التفعيل الكامل لـ: $weakTopics.")
        }
    }

    fun buildVipActivationMessage(): String {
        return buildString {
            appendLine("أهلاً فريق Desert Force B2B 👋")
            appendLine("أنا بستخدم تطبيق *Marvel in English* (50% Free Edition).")
            append("عايز أفعل الـ 50% الباقية والحصول على كود التفعيل الكامل لجميع الأمثال والتعبيرات والأفعال الاصطلاحية.")
        }
    }

    fun openWhatsAppB2B(context: Context, message: String) {
        val encoded = Uri.encode(message)
        val waIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://wa.me/$B2B_WHATSAPP_DIGITS?text=$encoded")
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        launchWithFallback(context, waIntent, message)
    }

    fun shareToWhatsApp(context: Context, message: String) {
        val encoded = Uri.encode(message)
        val waIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/?text=$encoded")).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        launchWithFallback(context, waIntent, message)
    }

    private fun launchWithFallback(context: Context, waIntent: Intent, message: String) {
        val chooser = Intent.createChooser(
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            },
            "Open with"
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            val resolved = context.packageManager.resolveActivity(waIntent, 0)
            if (resolved != null) {
                context.startActivity(waIntent)
            } else {
                context.startActivity(chooser)
            }
        } catch (_: Exception) {
            runCatching { context.startActivity(chooser) }
        }
    }
}
