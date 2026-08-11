package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.util.RedScriptEngine
import com.example.util.RootDeviceController
import com.example.util.SoundEffectsManager
import com.example.util.VoiceSpeechManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalLearningDialog(
    onDismiss: () -> Unit,
    onOpenFullTerminal: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color(0xFF0D1117)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color(0xFF10B981).copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Learning Tool",
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "أداة تعلم لغة الترمنال 🎓",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = Color(0xFF10B981).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "Terminal & RedScript Academy",
                                    color = Color(0xFF10B981),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "تعلم أوامر نظام Linux والسكربتات البرمجية بطريقة تفاعلية مبسطة",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = Color(0xFF8B949E)
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            Divider(color = Color(0xFF21262D))

            // Navigation Tabs
            val tabs = listOf(
                "📖 الدروس التفاعلية",
                "🧪 مختبر التجارب",
                "❓ اختبار الفهم",
                "📚 قاموس الأوامر"
            )

            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF0D1117),
                contentColor = Color(0xFF10B981),
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Color(0xFF10B981),
                            height = 3.dp
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            SoundEffectsManager.playClick()
                            selectedTab = index
                        },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) Color(0xFF10B981) else Color(0xFF8B949E)
                            )
                        }
                    )
                }
            }

            Divider(color = Color(0xFF21262D))

            // Tab Content
            when (selectedTab) {
                0 -> InteractiveLessonsView(
                    onRunCommand = { cmd ->
                        SoundEffectsManager.playClick()
                        Toast.makeText(context, "تم إرسال الأمر للترمنال: $cmd", Toast.LENGTH_SHORT).show()
                    }
                )
                1 -> CodePlaygroundView(onOpenFullTerminal = onOpenFullTerminal)
                2 -> InteractiveQuizView()
                else -> CommandDictionaryView()
            }
        }
    }
}

@Composable
private fun InteractiveLessonsView(onRunCommand: (String) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedLessonIndex by remember { mutableIntStateOf(0) }
    var testConsoleOutput by remember { mutableStateOf("اضغط على 'اختبر الأمر ⚡' لتجربة الكود حياً") }

    val lessons = listOf(
        LessonItem(
            title = "الدرس 1: أساسيات أوامر Linux والترمنال",
            subtitle = "فهم المبادئ الأولى لإدخال الأوامر واستكشاف النظام",
            explanation = "الترمنال (Terminal) هو الواجهة النصية التي تسمح لك بالتحدث مباشرة مع نظام تشغيل أندرويد (الذي يعتمد في نواته على Linux). كل أمر تكتبه ينفذ مهمة محددة.",
            codeSnippet = "pwd\nls -la\necho \"مرحباً بك في عالم الترمنال!\"",
            tips = "• `pwd`: يعرض المسار الحالي الذي تواجد فيه.\n• `ls`: يعرض قائمة الملفات داخل المجلد.\n• `echo`: يطبع النص المكتوب في الشاشة."
        ),
        LessonItem(
            title = "الدرس 2: صلاحيات الروت الخارقة (su & Root Permissions)",
            subtitle = "كيفية الحصول على الصلاحيات الكاملة للتحكم في معالج الهاتف والذاكرة",
            explanation = "أمر `su` اختصار لـ (SuperUser) وهو الأمر الذي يمنحك صلاحيات المسؤول الجذر (Root) لإدارة كافة إعدادات النظام المتقدمة.",
            codeSnippet = "su -c \"id\"\nsu -c \"getprop ro.build.version.release\"",
            tips = "⚠️ تنبيه: لا تنفذ أوامر الروت إلا عندما تكون واثقاً من مصدر السكربت. صلاحيات su تتيح القراءة والتعديل على أعمق ملفات النظام."
        ),
        LessonItem(
            title = "الدرس 3: لغة السكربت المخصصة RedScript",
            subtitle = "كتابة سكربتات ذكية تجمع بين التحكم في النظام والذكاء الاصطناعي",
            explanation = "RedScript هي لغة برمجية بسيطة وسريعة مدمجة في هذا التطبيق للربط بين الأوامر البرمجية والواجهات والمساعد الصوتي.",
            codeSnippet = "app.title = \"تطبيق تسريع الألعاب الخارق\"\nroot.exec(\"sysctl -w vm.drop_caches=3\")\nvoice.speak(\"تم تنظيف ذاكرة العشوائية وتسريع النظام بنجاح\")\nui.addCard(\"🚀 نمط Turbo متوفر الآن\")",
            tips = "• `app.title`: يحدد عنوان التطبيق.\n• `root.exec()`: ينفذ أمر الروت.\n• `voice.speak()`: ينطق النص بصوت المساعد."
        ),
        LessonItem(
            title = "الدرس 4: بناء الصفحات والبث المباشر برمجيّاً",
            subtitle = "إنشاء صفحات تفاعلية داخل التطبيق بأسطر كود قليلة",
            explanation = "يمكنك استخدام دالات `page.*` و `stream.*` لإنشاء صفحات جديدة ديناميكية وبث واجهة التطبيق عبر خوادم البث المباشر.",
            codeSnippet = "page.create(\"🎮 صفحة أداء الألعاب\", \"ic_game\")\npage.addHeader(\"معدل الفريمات الحالي: 120 FPS\", \"#10B981\")\nstream.broadcastApp(1001, \"1080p60\")",
            tips = "• `page.create()`: ينشئ صفحة جديدة.\n• `stream.broadcastApp()`: يبدأ بث التطبيق حياً للمشاهدين."
        )
    )

    val currentLesson = lessons[selectedLessonIndex]

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Lesson Selector Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            lessons.forEachIndexed { idx, lesson ->
                Button(
                    onClick = {
                        SoundEffectsManager.playClick()
                        selectedLessonIndex = idx
                        testConsoleOutput = "جاهز لتجربة كود ${lesson.title.split(":")[0]}"
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedLessonIndex == idx) Color(0xFF10B981) else Color(0xFF161B22)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "درس ${idx + 1}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedLessonIndex == idx) Color.Black else Color.White
                    )
                }
            }
        }

        // Selected Lesson Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF30363D))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = currentLesson.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF10B981)
                )

                Text(
                    text = currentLesson.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = CyanAccent
                )

                Divider(color = Color(0xFF21262D))

                Text(
                    text = currentLesson.explanation,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = Color(0xFFC9D1D9)
                )

                // Code snippet view
                Text(
                    text = "💻 الكود البرمجي للدرس:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )

                Surface(
                    color = Color(0xFF0D1117),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF30363D)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = currentLesson.codeSnippet,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = Color(0xFF38BDF8),
                            lineHeight = 18.sp
                        )
                    }
                }

                // Tips surface
                Surface(
                    color = Color(0xFF1F2937),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = currentLesson.tips,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = Color(0xFFE5E7EB)
                        )
                    }
                }

                // Run & Test Command Button
                Button(
                    onClick = {
                        SoundEffectsManager.playRootCommandSound()
                        when (selectedLessonIndex) {
                            0 -> testConsoleOutput = "⚡ [Console Output]:\n/system/bin\n-rw-r--r-- 1 root root RedScript_Engine.so\nمرحباً بك في عالم الترمنال!"
                            1 -> {
                                scope.launch {
                                    val res = RootDeviceController.executeCommand("id")
                                    testConsoleOutput = "⚡ [Root Result]:\n${res.output}"
                                }
                            }
                            2 -> {
                                VoiceSpeechManager(context).speak("تم تشغيل تجربة كود RedScript بنجاح")
                                testConsoleOutput = "⚡ [RedScript Executed]:\n• تم ضبط العنوان: تطبيق تسريع الألعاب\n• تنظيف الذاكرة: Dropped Caches (RAM Clean)\n• الصوت: نطق التنبيه بنجاح."
                            }
                            else -> testConsoleOutput = "⚡ [Page Studio Executed]:\n• إنشاء صفحة: 🎮 صفحة أداء الألعاب\n• تم تفعيل خادم البث: 1080p@60FPS"
                        }
                        onRunCommand(currentLesson.codeSnippet)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("اختبر هذا الدرس في المحاكي ⚡", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                // Live Output Result Box
                Surface(
                    color = Color(0xFF030712),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = testConsoleOutput,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = Color(0xFF10B981)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CodePlaygroundView(onOpenFullTerminal: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var customScript by remember {
        mutableStateOf(
            "PRINT '[Playground] بدء تشغيل الكود في المختبر...'\n" +
                    "BOOST_CPU MAX\n" +
                    "LOCK_FPS 120\n" +
                    "ZERO_RECOIL ON\n" +
                    "CLEAN_RAM\n" +
                    "PRINT '✅ تم تنفيذ جميع تعليمات RedScript بنجاح!'"
        )
    }
    var consoleLogs by remember { mutableStateOf("جاهز لتنفيذ كود المختبر...") }
    var isExecuting by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🧪 مختبر التجربة والكتابة الحية (Interactive Playground):",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )

            Button(
                onClick = {
                    SoundEffectsManager.playClick()
                    onOpenFullTerminal()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F2937)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Terminal, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("افتح الترمنال الشامل", fontSize = 10.sp, color = Color.White)
            }
        }

        OutlinedTextField(
            value = customScript,
            onValueChange = { customScript = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                color = Color(0xFF38BDF8)
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF0D1117),
                unfocusedContainerColor = Color(0xFF0D1117),
                focusedBorderColor = Color(0xFF10B981),
                unfocusedBorderColor = Color(0xFF30363D)
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    SoundEffectsManager.playRootCommandSound()
                    isExecuting = true
                    consoleLogs = "🚀 جارٍ التنفيذ..."
                    scope.launch {
                        RedScriptEngine.executeRedScript(context, customScript) { log ->
                            consoleLogs += "\n$log"
                        }
                        isExecuting = false
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(6.dp))
                Text("تشغيل الكود الآن ⚡", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    customScript = "root.exec(\"pm list packages -e\")\nvoice.speak(\"تم استعراض قائمة التطبيقات المفعلة\")"
                    SoundEffectsManager.playClick()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF161B22)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = CyanAccent)
                Spacer(modifier = Modifier.width(4.dp))
                Text("مثال جاهز", color = CyanAccent, fontSize = 11.sp)
            }
        }

        // Console Output View
        Text(
            text = "📟 مخرجات الكونسول والـ Output Log:",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF8B949E)
        )

        Surface(
            color = Color(0xFF030712),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B)),
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(12.dp)
            ) {
                Text(
                    text = consoleLogs,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = Color(0xFF10B981),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun InteractiveQuizView() {
    val context = LocalContext.current
    var quizScore by remember { mutableIntStateOf(0) }
    var currentQuestionIdx by remember { mutableIntStateOf(0) }
    var selectedOptionIdx by remember { mutableIntStateOf(-1) }
    var answeredCorrectly by remember { mutableStateOf<Boolean?>(null) }

    val quizQuestions = listOf(
        QuizQuestion(
            question = "سؤال 1: ما هو الأمر الذي يمنحك صلاحيات المسؤول الجذر (Root) في الترمنال؟",
            options = listOf("a) cd", "b) su", "c) echo", "d) mkdir"),
            correctAnswerIdx = 1,
            explanation = "أمر `su` اختصار لـ SuperUser وهو المسئول عن منح صلاحيات الروت الكاملة."
        ),
        QuizQuestion(
            question = "سؤال 2: ما هي الدالة المسؤولة عن جعل المساعد ينطق النص في لغة RedScript؟",
            options = listOf("a) voice.speak()", "b) root.talk()", "c) system.sound()", "d) echo.voice()"),
            correctAnswerIdx = 0,
            explanation = "الدالة `voice.speak(\"نص\")` هي الدالة المعتمدة للنطق الصوتي التفاعلي."
        ),
        QuizQuestion(
            question = "سؤال 3: ما فائدة أمر `pwd` في Linux Terminal؟",
            options = listOf("a) إيقاف النظام", "b) عرض كلمة المرور", "c) طباعة مسار المجلد الحالي", "d) حذف الملفات"),
            correctAnswerIdx = 2,
            explanation = "أمر `pwd` تعني (Print Working Directory) لعرض مسار المجلد الحالي."
        )
    )

    val question = quizQuestions[currentQuestionIdx]

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Score Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Psychology, contentDescription = null, tint = Color(0xFFF59E0B))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "اختبار الفهم وتقييم المهارة 🎯",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }

            Surface(
                color = Color(0xFF10B981).copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "النقاط: $quizScore / ${quizQuestions.size * 10}",
                    color = Color(0xFF10B981),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // Question Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF30363D))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = question.question,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                    color = Color.White
                )

                Divider(color = Color(0xFF21262D))

                question.options.forEachIndexed { optIdx, optionText ->
                    val isSelected = selectedOptionIdx == optIdx
                    val btnColor = when {
                        answeredCorrectly != null && optIdx == question.correctAnswerIdx -> Color(0xFF10B981)
                        answeredCorrectly == false && isSelected -> Color(0xFFEF4444)
                        isSelected -> Color(0xFF38BDF8)
                        else -> Color(0xFF0D1117)
                    }

                    Surface(
                        color = btnColor.copy(alpha = if (isSelected || (answeredCorrectly != null && optIdx == question.correctAnswerIdx)) 0.25f else 1f),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, btnColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (answeredCorrectly == null) {
                                    selectedOptionIdx = optIdx
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = optionText,
                                color = if (isSelected || (answeredCorrectly != null && optIdx == question.correctAnswerIdx)) Color.White else Color(0xFFC9D1D9),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                if (answeredCorrectly != null) {
                    Surface(
                        color = if (answeredCorrectly == true) Color(0xFF10B981).copy(alpha = 0.2f) else Color(0xFFEF4444).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (answeredCorrectly == true) "🎉 إجابة صحيحة ممتاز!" else "❌ إجابة خاطئة!",
                                fontWeight = FontWeight.Bold,
                                color = if (answeredCorrectly == true) Color(0xFF10B981) else Color(0xFFEF4444),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = question.explanation,
                                color = Color.White,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        if (answeredCorrectly == null && selectedOptionIdx != -1) {
                            SoundEffectsManager.playClick()
                            val isRight = selectedOptionIdx == question.correctAnswerIdx
                            answeredCorrectly = isRight
                            if (isRight) {
                                quizScore += 10
                                SoundEffectsManager.playRootCommandSound()
                            }
                        } else if (answeredCorrectly != null) {
                            // Next Question
                            if (currentQuestionIdx < quizQuestions.size - 1) {
                                currentQuestionIdx += 1
                                selectedOptionIdx = -1
                                answeredCorrectly = null
                            } else {
                                // Completed
                                Toast.makeText(context, "🏆 تهانينا! أكملت اختبار الترمنال بنجاح!", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (answeredCorrectly == null) "تأكيد الإجابة 🎯" else "السؤال التالي ➡️",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun CommandDictionaryView() {
    var searchQuery by remember { mutableStateOf("") }

    val commandsList = listOf(
        CommandDoc("su", "Root Access", "الدخول لنمط المسؤول الجذر وتنفيذ الأوامر الخارقة.", "su -c \"id\""),
        CommandDoc("ls", "List Files", "عرض قائمة الملفات والمجلدات في المسار الحالي.", "ls -la /sdcard"),
        CommandDoc("pwd", "Current Directory", "عرض المسار الكامل للمجلد الحالي.", "pwd"),
        CommandDoc("chmod", "Change Permissions", "تغيير تصريحات وصلاحيات القراءة والكتابة للملفات.", "chmod 755 script.sh"),
        CommandDoc("app.title", "RedScript Title", "تحديد اسم وعنوان التطبيق البرمجي.", "app.title = \"تطبيقي\""),
        CommandDoc("root.exec", "RedScript Root", "تنفيذ أمر Linux بعموم صلاحيات الروت.", "root.exec(\"reboot\")"),
        CommandDoc("voice.speak", "Speech Assistant", "جعل المساعد ينطق عبارة نصية.", "voice.speak(\"أهلاً\")"),
        CommandDoc("page.create", "Page Studio", "إنشاء صفحة تفاعلية جديدة في التطبيق.", "page.create(\"الألعاب\", \"ic_game\")"),
        CommandDoc("stream.broadcastApp", "Live Stream", "بدء بث مباشر للتطبيق وجودة الفيديو.", "stream.broadcastApp(1001, \"1080p\")"),
        CommandDoc("sysctl", "Kernel Tuning", "التحكم في إعدادات النواة والذاكرة العشوائية RAM.", "sysctl -w vm.drop_caches=3")
    )

    val filtered = commandsList.filter {
        it.command.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true) ||
                it.description.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("بحث في أوامر ودالات الترمنال...", color = Color.Gray) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color(0xFF10B981)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF10B981),
                unfocusedBorderColor = Color(0xFF30363D),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        )

        filtered.forEach { cmd ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF30363D))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = cmd.command,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8),
                            fontSize = 13.sp
                        )

                        Surface(
                            color = Color(0xFF10B981).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = cmd.category,
                                color = Color(0xFF10B981),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = cmd.description,
                        color = Color(0xFFC9D1D9),
                        fontSize = 11.sp
                    )

                    Surface(
                        color = Color(0xFF0D1117),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "مثال: ${cmd.example}",
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF10B981),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

private data class LessonItem(
    val title: String,
    val subtitle: String,
    val explanation: String,
    val codeSnippet: String,
    val tips: String
)

private data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswerIdx: Int,
    val explanation: String
)

private data class CommandDoc(
    val command: String,
    val category: String,
    val description: String,
    val example: String
)
