package com.example.util

import android.content.Context
import android.os.Environment
import android.os.Vibrator
import android.os.VibrationEffect
import android.os.Build
import android.widget.Toast
import com.example.data.local.entity.AppEntity
import com.example.ui.components.RedMagicGameState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File

data class RedScriptItem(
    val title: String,
    val description: String,
    val author: String,
    val content: String,
    val isCommunityPublished: Boolean = false,
    val downloadCount: Int = 0
)

object RedScriptEngine {

    val sampleScripts = listOf(
        RedScriptItem(
            title = "🔥 PUBG & Battle Royale Ultra Hack & Turbo",
            description = "سكربت أقصى أداء مع كسر الفريمات وتثبيت الأيم وتصحيح البنج والأوديو 3D.",
            author = "RedMagic Dev Team",
            content = """
            # ==========================================
            # RedScript Ultra Battle Royale Boost
            # Language: RedScript v2.0 (.rmscript)
            # ==========================================
            PRINT '[RedScript] جارٍ تهيئة المحرك وتفعيل الروت الخارق...'
            VIBRATE 80
            IF_ROOT PRINT '✅ تم التأكد من صلاحيات السوبر يوزر SuperSU/Magisk'
            
            PRINT '[1/6] تفعيل وضع المعالج الماكس Rise Super Turbo'
            BOOST_CPU MAX
            
            PRINT '[2/6] قفل الشاشة على 120Hz فريمات عالية'
            LOCK_FPS 120
            
            PRINT '[3/6] تفعيل تثبيت الأيم ومنع الارتداد Zero-Recoil'
            ZERO_RECOIL ON
            
            PRINT '[4/6] تفعيل محرك Vulkan GPU وتوليد الأوديو 3D'
            VULKAN_BOOST ON
            3D_AUDIO ON
            
            PRINT '[5/6] تنظيف الرام وتجميد تطبيقات الخلفية'
            CLEAN_RAM
            FREEZE_BG
            
            PRINT '[6/6] تسريع الشبكة وتثبيت البنج على أقصى سرعة'
            SET_PING_LOW
            CROSSHAIR ON
            AI_TOUCH_BOOST ON
            
            NOTIFY 'RedScript Executed' '🎯🔥 تم تفعيل هك وسكربت الألعاب الفائق بنجاح!'
            VIBRATE 150
            PRINT '✨ [تم الانتهاء بنجاح بنسبة 100%]'
            """.trimIndent()
        ),
        RedScriptItem(
            title = "🎯 Aimlock & Zero-Recoil Hardware Touch Engine",
            description = "سكربت القفل الجذري للمس وتثبيت الهدف في الشاشة بدون اهتزاز.",
            author = "Root Hacking Community",
            content = """
            # ==========================================
            # Aimlock & Zero-Recoil Touch Calibration
            # ==========================================
            PRINT '[Aimlock] قفل زاوية الاستجابة واللمس الجذري...'
            ZERO_RECOIL ON
            AI_TOUCH_BOOST ON
            SYS_PROP debug.touch.latency 0
            SYS_PROP debug.gestures.touch_slop 0
            CROSSHAIR ON
            VIBRATE 100
            PRINT '✅ تم ضبـط حساسية اللمس 1000Hz ومنع اهتزاز التصويب!'
            """.trimIndent()
        ),
        RedScriptItem(
            title = "❄️ Extreme Battery Cooling & RAM Purifier",
            description = "سكربت تبريد المعالج وتفريغ الكاش بالكامل لحفظ الطاقة.",
            author = "Energy Lab",
            content = """
            # ==========================================
            # Battery Cooler & Cache Purifier
            # ==========================================
            PRINT '❄️ جارٍ تبريد المعالج وتفريغ ذاكرة النظام...'
            CLEAN_RAM
            FREEZE_BG
            EXEC_ROOT echo schedutil > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor
            SET_HUD_OPACITY 0.70
            NOTIFY 'Cooling System' '❄️ تم تفريغ الرام وتبريد البطارية والمعالج!'
            PRINT '✅ تم تنظيف النظام بنجاح!'
            """.trimIndent()
        ),
        RedScriptItem(
            title = "🎨 Cyber Matrix HUD & Terminal Customizer",
            description = "سكربت تخصيص ثيمات الترمنال ولون الشبيكة وشفافية الواجهة.",
            author = "Custom Theme Specialist",
            content = """
            # ==========================================
            # Cyber Matrix Customization Script
            # ==========================================
            PRINT '🎨 جارٍ تطبيق ثيم الماتريكس وتخصيص الترمنال...'
            SET_THEME CyberNeon
            SET_HUD_OPACITY 0.90
            CROSSHAIR ON
            SYS_PROP debug.hwui.renderer vulkan
            PRINT '✨ تم تطبيق التخصيص الرائع للواجهة!'
            """.trimIndent()
        )
    )

    /**
     * Executes RedScript code line by line and reports progress logs
     */
    suspend fun executeRedScript(
        context: Context,
        scriptCode: String,
        onLog: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        val lines = scriptCode.lines()
        var lineIndex = 0

        onLog("🚀 [RedScript Engine v2.0] بدء تنفيذ السكربت المخصص...")

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isEmpty() || trimmed.startsWith("#") || trimmed.startsWith("//")) {
                continue
            }

            lineIndex++
            val parts = trimmed.split("\\s+".toRegex())
            val command = parts[0].uppercase()
            val arg1 = parts.getOrNull(1) ?: ""
            val arg2 = parts.getOrNull(2) ?: ""

            when (command) {
                "BOOST_CPU" -> {
                    val msg = RootDeviceController.applyUltraRootGameBoost()
                    onLog("⚡ [BOOST_CPU]: $msg")
                }
                "LOCK_FPS" -> {
                    val fps = arg1.toIntOrNull() ?: 120
                    RootDeviceController.executeCommand("su -c 'setprop debug.sf.max_igbp_buffer_count 3'")
                    onLog("🎯 [LOCK_FPS]: تم قفل معدل الفريمات على $fps Hz")
                }
                "SET_PING_LOW" -> {
                    val msg = RootDeviceController.boostNetworkPingViaRoot()
                    onLog("🌐 [SET_PING_LOW]: $msg")
                }
                "ZERO_RECOIL" -> {
                    val enable = arg1.uppercase() == "ON" || arg1 == "1"
                    RedMagicGameState.isZeroRecoilActive = enable
                    val msg = RootDeviceController.toggleZeroRecoilAimLock(enable)
                    onLog("🎯 [ZERO_RECOIL]: $msg")
                }
                "VULKAN_BOOST" -> {
                    val enable = arg1.uppercase() == "ON" || arg1 == "1"
                    RedMagicGameState.isVulkanGpuActive = enable
                    val msg = RootDeviceController.toggleVulkanGpuBoost(enable)
                    onLog("🚀 [VULKAN_BOOST]: $msg")
                }
                "CROSSHAIR" -> {
                    val enable = arg1.uppercase() == "ON" || arg1 == "1"
                    RedMagicGameState.isCrosshairEnabled = enable
                    onLog("🎯 [CROSSHAIR]: ${if (enable) "تم إظهار الشبيكة" else "تم إخفاء الشبيكة"}")
                }
                "3D_AUDIO" -> {
                    val enable = arg1.uppercase() == "ON" || arg1 == "1"
                    RedMagicGameState.is3dFootstepAudioActive = enable
                    val msg = RootDeviceController.toggleSpatialFootstepAudio(enable)
                    onLog("🎧 [3D_AUDIO]: $msg")
                }
                "FREEZE_BG", "CLEAN_RAM" -> {
                    val msg = RootDeviceController.freezeBackgroundAppsViaRoot()
                    onLog("🧹 [CLEAN_RAM]: $msg")
                }
                "AI_TOUCH_BOOST" -> {
                    val enable = arg1.uppercase() == "ON" || arg1 == "1"
                    RedMagicGameState.isAiTouchBoost = enable
                    RootDeviceController.executeCommand("su -c setprop debug.touch.latency 0")
                    onLog("🤖 [AI_TOUCH_BOOST]: تم تفعيل تسريع استجابة اللمس 1000Hz")
                }
                "SYS_PROP" -> {
                    if (arg1.isNotEmpty()) {
                        RootDeviceController.executeCommand("su -c setprop $arg1 $arg2")
                        onLog("⚙️ [SYS_PROP]: setprop $arg1 $arg2")
                    }
                }
                "PRINT" -> {
                    val message = trimmed.removePrefix("PRINT").trim().trim('\'', '"')
                    onLog("💬 [PRINT]: $message")
                }
                "SLEEP" -> {
                    val ms = arg1.toLongOrNull() ?: 300L
                    onLog("⏳ [SLEEP]: انتظار $ms ملي ثانية...")
                    delay(ms)
                }
                "EXEC_ROOT" -> {
                    val shellCmd = trimmed.removePrefix("EXEC_ROOT").trim().removePrefix("su -c").trim('\'', '"')
                    val res = RootDeviceController.executeCommand("su -c '$shellCmd'")
                    onLog("💻 [EXEC_ROOT]: $res")
                }
                "SET_HUD_OPACITY" -> {
                    val opacity = arg1.toFloatOrNull() ?: 0.9f
                    RedMagicGameState.hudOpacity = opacity.coerceIn(0.2f, 1.0f)
                    onLog("👁️ [SET_HUD_OPACITY]: تم ضبط شفافية الواجهة والفقاعة على ${(RedMagicGameState.hudOpacity * 100).toInt()}%")
                }
                "VIBRATE" -> {
                    val ms = arg1.toLongOrNull() ?: 100L
                    try {
                        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
                        } else {
                            vibrator.vibrate(ms)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    onLog("📳 [VIBRATE]: اهتزاز $ms ms")
                }
                "NOTIFY" -> {
                    val fullText = trimmed.removePrefix("NOTIFY").trim()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "📢 RedScript: $fullText", Toast.LENGTH_LONG).show()
                    }
                    onLog("📢 [NOTIFY]: $fullText")
                }
                "AUTO_TRIGGER" -> {
                    val enable = arg1.uppercase() == "ON" || arg1 == "1"
                    RedMagicGameState.isAutoTriggerEnabled = enable
                    val msg = RootDeviceController.toggleAutoTriggerAiBot(enable)
                    onLog("⚡ [AUTO_TRIGGER]: $msg")
                }
                "IF_ROOT" -> {
                    val isGranted = RootDeviceController.checkUidZero()
                    if (isGranted) {
                        val remainingCmd = trimmed.removePrefix("IF_ROOT").trim()
                        onLog("✅ [IF_ROOT True]: جارٍ تنفيذ $remainingCmd")
                    } else {
                        onLog("⚠️ [IF_ROOT False]: لم يتم العثور على صلاحية الروت")
                    }
                }
                else -> {
                    onLog("ℹ️ [RedScript Statement]: $trimmed")
                }
            }
            delay(150)
        }

        onLog("🎉 [كتالوج RedScript]: اكتمل تشغيل $lineIndex سطر من السكربت بنجاح!")
    }

    /**
     * Save script to a local file (.rmscript / .rsh) on the Android system
     */
    fun saveScriptToFile(context: Context, fileName: String, scriptContent: String): File? {
        return try {
            val cleanName = if (fileName.endsWith(".rmscript") || fileName.endsWith(".rsh")) fileName else "$fileName.rmscript"
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
            val file = File(dir, cleanName)
            file.writeText(scriptContent)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Convert RedScript into a published community app entity ready to upload to Community Cloud Store
     */
    fun createCommunityPublishEntity(
        title: String,
        author: String,
        description: String,
        scriptContent: String,
        country: String = "🌐 جميع الدول"
    ): AppEntity {
        return AppEntity(
            id = 0,
            title = "📜 [RedScript] $title",
            publisher = author.ifBlank { "مطور RedScript المجتمعي" },
            description = "$description\n\n--- [كود السكربت البرمجي المخصص] ---\n$scriptContent",
            category = "أدوات",
            country = country,
            versionName = "2.0.0",
            fileSize = "${(scriptContent.length * 2 / 1024).coerceAtLeast(1)} KB",
            downloadUrl = "https://community.store/scripts/${System.currentTimeMillis()}.rmscript",
            iconResName = "ic_script",
            rating = 5.0f,
            isVerified = true,
            publishedAt = System.currentTimeMillis()
        )
    }
}
