package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.AppDao
import com.example.data.local.entity.AppEntity
import com.example.data.local.entity.DownloadEntity
import com.example.data.local.entity.ReviewEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [AppEntity::class, ReviewEntity::class, DownloadEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, coroutineScope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "community_app_hub.db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(AppDatabaseCallback(coroutineScope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.appDao())
                }
            }
        }

        private suspend fun populateInitialData(appDao: AppDao) {
            val sampleApps = listOf(
                AppEntity(
                    id = 1,
                    title = "Pixel Calc Pro - حاسبة متقدمة",
                    packageName = "com.community.pixelcalc",
                    publisher = "أحمد المطور - DevAhmed",
                    description = "تطبيق حاسبة علمية متقدمة خفيفة الوزن بتصميم أنيق يدعم الرسم البياني وحساب المعادلات المعقدة بدون إعلانات.",
                    category = "أدوات",
                    country = "🇸🇦 السعودية",
                    versionName = "2.4.1",
                    fileSize = "4.2 MB",
                    downloadUrl = "https://fdroid.org/repo/org.simplemobiletools.calculator_78.apk",
                    iconResName = "ic_calc",
                    downloadsCount = 1420,
                    likesCount = 380,
                    rating = 4.9f,
                    isVerified = true
                ),
                AppEntity(
                    id = 2,
                    title = "Zen Notes - مفكرة زين السريعة",
                    packageName = "com.community.zennotes",
                    publisher = "سارة تقني - SarahTech",
                    description = "ملاحظات وتذكيرات سريعة مشفرة بالكامل على هاتفك لحفظ أفكارك وقوائم المهام مع مزامنة محلية بدون إنترنت.",
                    category = "إنتاجية",
                    country = "🇪🇬 مصر",
                    versionName = "1.8.0",
                    fileSize = "6.8 MB",
                    downloadUrl = "https://fdroid.org/repo/org.simplemobiletools.notes.pro_72.apk",
                    iconResName = "ic_notes",
                    downloadsCount = 2150,
                    likesCount = 590,
                    rating = 4.8f,
                    isVerified = true
                ),
                AppEntity(
                    id = 3,
                    title = "Retro Racer 2D - لعبة سباق مجتمعية",
                    packageName = "com.community.retroracer",
                    publisher = "فريق الألعاب العربي - ArabGameDev",
                    description = "لعبة سباق سيارات كلاسيكية بنظام 2D خفيفة الحجم وتعمل على جميع الهواتف الضعيفة مع إمكانية تصميم مضمارك الخاص.",
                    category = "ألعاب",
                    country = "🇩🇿 الجزائر",
                    versionName = "3.1.0",
                    fileSize = "18.5 MB",
                    downloadUrl = "https://fdroid.org/repo/com.termux_118.apk",
                    iconResName = "ic_game",
                    downloadsCount = 3400,
                    likesCount = 890,
                    rating = 4.7f,
                    isVerified = true
                ),
                AppEntity(
                    id = 4,
                    title = "Clear Cleaner - منظف الذاكرة الذكي",
                    packageName = "com.community.clearcleaner",
                    publisher = "عمر سيستم - OmarSys",
                    description = "أداة مجتمعية مفيدة لتنظيف الملفات المؤقتة وبقايا التطبيقات وتفريغ الذاكرة العشوائية لسرعة أداء مضاعفة.",
                    category = "أدوات",
                    country = "🇲🇦 المغرب",
                    versionName = "1.0.5",
                    fileSize = "3.1 MB",
                    downloadUrl = "https://fdroid.org/repo/org.fdroid.fdroid_1020050.apk",
                    iconResName = "ic_cleaner",
                    downloadsCount = 980,
                    likesCount = 210,
                    rating = 4.6f,
                    isVerified = true
                ),
                AppEntity(
                    id = 5,
                    title = "Vibe Music Player - مشغل صوتيات احترافي",
                    packageName = "com.community.vibemusic",
                    publisher = "كودر ميديا - CoderMedia",
                    description = "مشغل أغاني وصوتيات يدعم الترددات الصوتية Equalizer، مؤقت النوم، والكلمات المزامنة بدعم الصوت عالي الدقة.",
                    category = "وسائط",
                    country = "🇦🇪 الإمارات",
                    versionName = "2.0.1",
                    fileSize = "12.0 MB",
                    downloadUrl = "https://fdroid.org/repo/com.vlcforandroid.client_13050200.apk",
                    iconResName = "ic_music",
                    downloadsCount = 1850,
                    likesCount = 420,
                    rating = 4.9f,
                    isVerified = true
                ),
                AppEntity(
                    id = 6,
                    title = "Aura Weather - طقس هادئ ومباشر",
                    packageName = "com.community.auraweather",
                    publisher = "مختبرات السحاب - CloudLabs",
                    description = "توقعات الطقس ودرجات الحرارة بدقة عالية للشرق الأوسط مع تنبيهات الأمطار وتأثيرات بصرية حية للجو.",
                    category = "خدمات",
                    country = "🇮🇶 العراق",
                    versionName = "1.2.0",
                    fileSize = "8.4 MB",
                    downloadUrl = "https://fdroid.org/repo/org.simplemobiletools.gallery.pro_385.apk",
                    iconResName = "ic_weather",
                    downloadsCount = 760,
                    likesCount = 195,
                    rating = 4.5f,
                    isVerified = true
                )
            )

            sampleApps.forEach { appDao.insertApp(it) }

            // Initial reviews
            val sampleReviews = listOf(
                ReviewEntity(appId = 1, author = "خالد العتيبي", rating = 5, comment = "تطبيق ممتاز ورائع جدًا خفيف على الجهاز بدون إعلانات مزعجة!"),
                ReviewEntity(appId = 1, author = "مريم علي", rating = 5, comment = "استخدمته في دراستي للجامعة، دقيق للغاية وشكله عصري."),
                ReviewEntity(appId = 2, author = "محمد ياسين", rating = 5, comment = "أفضل تطبيق ملاحظات مجتمعي جربته حتى الآن."),
                ReviewEntity(appId = 3, author = "سيف الدين", rating = 4, comment = "لعبة ممتعة جداً تعيد الذكريات الجملة.")
            )

            sampleReviews.forEach { appDao.insertReview(it) }
        }
    }
}
