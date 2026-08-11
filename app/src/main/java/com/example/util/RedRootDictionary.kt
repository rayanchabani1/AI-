package com.example.util

import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import androidx.compose.ui.graphics.Color

data class RedRootKeyword(
    val id: Int,
    val keyword: String,
    val arabicName: String,
    val category: String,
    val rootCommand: String,
    val description: String,
    val tags: List<String> = emptyList()
)

object RedRootDictionary {

    val keywordsList: List<RedRootKeyword> by lazy {
        val list = mutableListOf<RedRootKeyword>()
        
        val categories = listOf(
            "Root Commands", "Gaming Hacks", "RedScript DSL", "Android Props", 
            "Performance & RAM", "Network & Ping", "Audio & Sound", "Security & Magisk"
        )

        val rootBaseWords = listOf(
            Triple("su", "السوبر يوزر المباشر", "su -c id"),
            Triple("busybox", "حزمة بيزي بوكس الجذرية", "busybox --help"),
            Triple("getprop", "قراءة خصائص النظام", "getprop ro.build.version.release"),
            Triple("setprop", "كتابة خصائص النظام", "setprop debug.performance.tuning 1"),
            Triple("mount", "إعادة تركيب الروابط rw/ro", "mount -o remount,rw /system"),
            Triple("pm grant", "منح تصاريح التطبيقات", "pm grant com.example android.permission.WRITE_SECURE_SETTINGS"),
            Triple("pm clear", "مسح بيانات التطبيقات", "pm clear com.example"),
            Triple("am force-stop", "إغلاق التطبيق بالقوة", "am force-stop com.example"),
            Triple("dumpsys", "تقرير أداء وعمليات النظام", "dumpsys cpuinfo"),
            Triple("killall", "إنهاء كافة العمليات المستهلكة", "killall -9 com.eg.game"),
            Triple("iptables", "جدار حماية الاتصالات الشبكية", "iptables -F"),
            Triple("chmod", "تعديل صلاحيات الملفات", "chmod 755 /data/local/tmp/script.sh"),
            Triple("chown", "تعديل ملكية الملفات للروت", "chown root:root /data/local/tmp/script.sh"),
            Triple("insmod", "تحميل موديل الكيرنل", "insmod /vendor/lib/modules/game_boost.ko"),
            Triple("dmesg", "فحص سجلات النواة والكيرنل", "dmesg | tail -n 20"),
            Triple("logcat", "تتبع سجلات أخطاء الأندرويد", "logcat -d -t 50"),
            Triple("fstrim", "تحسين الذاكرة الداخلية NVMe", "fstrim -v /data"),
            Triple("free", "فحص استهلاك الذاكرة RAM", "free -m"),
            Triple("top", "مراقبة المعالج واستخدام الـ CPU", "top -n 1 -m 5"),
            Triple("zram", "ضغط وتكبير الذاكرة العشوائية", "echo 1 > /sys/block/zram0/reset")
        )

        val gamingHackWords = listOf(
            Triple("zero_recoil", "تثبيت الأيم ومنع الارتداد", "ZERO_RECOIL ON"),
            Triple("fps_lock_120", "قفل 120 فريم خارق", "LOCK_FPS 120"),
            Triple("vulkan_boost", "محرك الرسوميات Vulkan", "VULKAN_BOOST ON"),
            Triple("crosshair_aim", "شبيكة التصويب الشفافة", "CROSSHAIR ON"),
            Triple("3d_spatial_audio", "مضخم خطوات الأعداء 3D", "3D_AUDIO ON"),
            Triple("boost_ping", "تخفيض البنج وتسريع الشبكة", "SET_PING_LOW"),
            Triple("touch_1000hz", "سرعة استجابة اللمس 1000Hz", "AI_TOUCH_BOOST ON"),
            Triple("freeze_bg_apps", "تجميد كافة تطبيقات الخلفية", "FREEZE_BG"),
            Triple("cpu_max_turbo", "أقصى طاقة لمعالج الألعاب", "BOOST_CPU MAX"),
            Triple("clear_ram_junk", "تفرغ الذاكرة المؤقتة فورا", "CLEAN_RAM"),
            Triple("auto_trigger_bot", "البوت الذكي للإطلاق التلقائي", "AUTO_TRIGGER ON"),
            Triple("no_lag_fix", "إصلاح التقطيع والهبوط", "SYS_PROP debug.sf.showfps 0"),
            Triple("max_brightness_lock", "قفل السطوع الأقصى للشاشة", "SYS_PROP settings.display.brightness 255"),
            Triple("dnd_game_mode", "وضع عدم الإزعاج الكامل", "SYS_PROP zen_mode 1"),
            Triple("thermal_throttle_off", "إيقاف خفض الأداء عند الحرارة", "EXEC_ROOT stop thermald"),
            Triple("gpu_overclock", "كسر سرعة معالج الرسوميات GPU", "EXEC_ROOT echo 850000000 > /sys/class/kgsl/kgsl-3d0/devfreq/max_freq"),
            Triple("headshot_aimbot", "تعزيز زاوية التصويب الرأسية", "SYS_PROP debug.touch.sensitivity 100"),
            Triple("frame_interpolation", "مضاعف الفريمات الذكي", "SYS_PROP persist.vendor.graphics.vrr 1"),
            Triple("kill_render_delay", "إلغاء تأخير رندر الشاشة", "SYS_PROP debug.egl.hw 1"),
            Triple("fast_dns_google", "تسريع السيرفر بـ Google DNS", "SYS_PROP net.dns1 8.8.8.8")
        )

        val redScriptWords = listOf(
            Triple("BOOST_CPU", "أمر تسريع وتحديد وضع المعالج", "BOOST_CPU MAX"),
            Triple("LOCK_FPS", "أمر تثبيت معدل التحديث الشاشة", "LOCK_FPS 120"),
            Triple("SET_PING_LOW", "أمر بروتوكول تسريع الشبكة", "SET_PING_LOW"),
            Triple("ZERO_RECOIL", "أمر قفل ارتداد الشاشة واللمس", "ZERO_RECOIL ON"),
            Triple("VULKAN_BOOST", "أمر تفعيل محرك Vulkan Renders", "VULKAN_BOOST ON"),
            Triple("CROSSHAIR", "أمر إظهار شبيكة التصويب", "CROSSHAIR ON"),
            Triple("3D_AUDIO", "أمر تضخيم الصوت المحيطي", "3D_AUDIO ON"),
            Triple("FREEZE_BG", "أمر تجميد تطبيقات الخلفية", "FREEZE_BG"),
            Triple("CLEAN_RAM", "أمر تنظيف الذاكرة العشوائية", "CLEAN_RAM"),
            Triple("AI_TOUCH_BOOST", "أمر تسريع استجابة اللمس", "AI_TOUCH_BOOST ON"),
            Triple("SYS_PROP", "أمر تعديل خواص نظام الأندرويد", "SYS_PROP debug.touch.latency 0"),
            Triple("PRINT", "أمر طباعة نص في الترمنال", "PRINT 'RedScript Turbo Engine'"),
            Triple("SLEEP", "أمر الانتظار والتأخير بالملي ثانية", "SLEEP 500"),
            Triple("EXEC_ROOT", "أمر تنفيذ شل روت مباشر", "EXEC_ROOT su -c id"),
            Triple("SET_HUD_OPACITY", "أمر تحديد شفافية الفقاعة والـ HUD", "SET_HUD_OPACITY 0.85"),
            Triple("SET_THEME", "أمر تغيير ثيم الواجهة والترمنال", "SET_THEME CyberNeon"),
            Triple("VIBRATE", "أمر تشغيل اهتزاز المحرك لمسي", "VIBRATE 100"),
            Triple("NOTIFY", "أمر إرسال إشعار منبثق", "NOTIFY 'RedScript' 'تم تشغيل السكربت بنجاح'"),
            Triple("AUTO_TRIGGER", "أمر تفعيل البوت الذكي", "AUTO_TRIGGER ON"),
            Triple("IF_ROOT", "أمر الشرط للتأكد من وجود الروت", "IF_ROOT PRINT 'Root Active'")
        )

        var counter = 1

        // Populate base 60 core items
        rootBaseWords.forEach { (word, name, cmd) ->
            list.add(RedRootKeyword(counter, word, name, "Root Commands", cmd, "كلمة وأمر أساسي لبيئة السوبر يوزر والروت في أندرويد.", listOf(word, name, "root", "su")))
            counter++
        }

        gamingHackWords.forEach { (word, name, cmd) ->
            list.add(RedRootKeyword(counter, word, name, "Gaming Hacks", cmd, "كلمة وميزة تهكير الألعاب والتسريع الذكي.", listOf(word, name, "hack", "game", "fps")))
            counter++
        }

        redScriptWords.forEach { (word, name, cmd) ->
            list.add(RedRootKeyword(counter, word, name, "RedScript DSL", cmd, "أمر لغة RedScript المخصصة للبرمجة والأتمتة.", listOf(word, name, "script", "terminal")))
            counter++
        }

        // Expand to exactly 1000 keywords programmatically
        val arabicPrefixes = listOf(
            "تهكير", "تسريع", "تنظيف", "قفل", "فك", "تجميد", "حظر", "تشغيل", "تفعيل", "تحسين",
            "إصلاح", "تعديل", "تثبيت", "رفع", "خفض", "إدارة", "فحص", "مراقبة", "استخراج", "تشفير"
        )
        val techTerms = listOf(
            "الفريمات", "البنج", "الرام", "المعالج", "البطارية", "الكاش", "الكيرنل", "الشبكة", "اللمس", "الصوت",
            "الشاشة", "الجرافيكس", "الأيم", "الارتداد", "الروت", "السكربت", "الترمنال", "الإشعارات", "النظام", "الحماية"
        )

        while (counter <= 1000) {
            val prefixIndex = counter % arabicPrefixes.size
            val termIndex = counter % techTerms.size
            val category = categories[counter % categories.size]

            val arName = "${arabicPrefixes[prefixIndex]} ${techTerms[termIndex]} #$counter"
            val engWord = "root_${techTerms[termIndex]}_cmd_$counter"
            val command = "EXEC_ROOT su -c 'echo \"Executing keyword $counter: $arName\"'"
            val desc = "كلمة وميزة رقم $counter للتعرف الذكي وإدارة $arName باستخدام AI Root والمحرك لـ RedScript."

            list.add(
                RedRootKeyword(
                    id = counter,
                    keyword = engWord,
                    arabicName = arName,
                    category = category,
                    rootCommand = command,
                    description = desc,
                    tags = listOf(engWord, arName, category, "kw_$counter")
                )
            )
            counter++
        }

        list
    }

    /**
     * Searches through all 1000 keywords by query or tag
     */
    fun searchKeywords(query: String): List<RedRootKeyword> {
        if (query.isBlank()) return keywordsList.take(60)
        val q = query.lowercase().trim()
        return keywordsList.filter {
            it.keyword.contains(q, ignoreCase = true) ||
            it.arabicName.contains(q, ignoreCase = true) ||
            it.category.contains(q, ignoreCase = true) ||
            it.description.contains(q, ignoreCase = true) ||
            it.tags.any { tag -> tag.contains(q, ignoreCase = true) }
        }
    }

    /**
     * Matches user speech or text prompt against 1000 keywords to detect intent
     */
    fun matchInText(text: String): List<RedRootKeyword> {
        val clean = text.lowercase().trim()
        if (clean.length < 2) return emptyList()
        return keywordsList.filter {
            clean.contains(it.keyword.lowercase()) ||
            clean.contains(it.arabicName.lowercase()) ||
            it.tags.any { tag -> clean.contains(tag.lowercase()) }
        }.take(8)
    }
}
