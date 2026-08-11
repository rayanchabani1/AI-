package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.util.SoundEffectsManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenSourceCodeDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var inputPasscode by remember { mutableStateOf("") }
    var isUnlocked by remember { mutableStateOf(false) }
    var passcodeError by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }

    val requiredCode = "MTK-APD-AMOLD"

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color(0xFF0F172A)
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
                            .background(
                                if (isUnlocked) Color(0xFF10B981).copy(alpha = 0.2f) else Color(0xFFEC4899).copy(alpha = 0.2f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                            contentDescription = "Open Source Code",
                            tint = if (isUnlocked) Color(0xFF10B981) else Color(0xFFEC4899),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "أوبين سورس كود 🔓 (Open Source Code)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = if (isUnlocked) "كود مصدر لغة RedScript ومحرك الروت مفتوح بالكامل" else "منطقة المطورين المحمية بكود السر",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            Divider(color = Color(0xFF334155))

            if (!isUnlocked) {
                // Passcode Entry Screen
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color(0xFF1E293B), CircleShape)
                            .border(2.dp, Color(0xFFEC4899), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = "Key Passcode",
                            tint = Color(0xFFEC4899),
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Text(
                        text = "منطقة الكود المفتوح المصدر لـ RedScript Engine",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "لإلغاء قفل وعرض سورس كود اللغة ومحرك الروت ومكتبة Standard Library الكاملة، أدخل كود الدخول المخصص:",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                        color = Color(0xFFCBD5E1),
                        textAlign = TextAlign.Center
                    )

                    OutlinedTextField(
                        value = inputPasscode,
                        onValueChange = {
                            inputPasscode = it
                            passcodeError = ""
                        },
                        placeholder = { Text("أدخل كود السر المخصص...", color = Color.Gray) },
                        singleLine = true,
                        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Color(0xFFEC4899)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFEC4899),
                            unfocusedBorderColor = Color(0xFF475569),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (passcodeError.isNotEmpty()) {
                        Text(
                            text = passcodeError,
                            color = Color(0xFFEF4444),
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Button(
                        onClick = {
                            if (inputPasscode.trim() == requiredCode) {
                                SoundEffectsManager.playRootCommandSound()
                                isUnlocked = true
                                passcodeError = ""
                                Toast.makeText(context, "✅ تم إلغاء القفل بنجاح! مرحباً بك في الكود المفتوح المصدر.", Toast.LENGTH_SHORT).show()
                            } else {
                                SoundEffectsManager.playClick()
                                passcodeError = "❌ كود الدخول غير صحيح!"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEC4899)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Icon(imageVector = Icons.Default.LockOpen, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "فتح سورس كود اللغة (Unlock Source)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    Surface(
                        color = Color(0xFF1E293B),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "منطقة محمية - يلزم توفر كود الدخول الخاص بالمطورين",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = CyanAccent
                            )
                        }
                    }
                }
            } else {
                // Unlocked Open Source Code Hub View
                val tabs = listOf(
                    "📜 RedScript Interpreter",
                    "🛡️ Root Native Engine",
                    "📚 Standard Library v2.0",
                    "📄 Open Source Specs"
                )

                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFF0F172A),
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
                                    color = if (selectedTab == index) Color(0xFF10B981) else Color(0xFF94A3B8)
                                )
                            }
                        )
                    }
                }

                Divider(color = Color(0xFF334155))

                val codeContent = when (selectedTab) {
                    0 -> REDSCRIPT_INTERPRETER_SOURCE
                    1 -> ROOT_NATIVE_ENGINE_SOURCE
                    2 -> STDLIB_SOURCE
                    else -> REDSCRIPT_SPEC_DOCS
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "مفتوح المصدر تحت رخصة MIT • v2.0-release",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF10B981)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("RedScript Source Code", codeContent)
                                    clipboard.setPrimaryClip(clip)
                                    SoundEffectsManager.playClick()
                                    Toast.makeText(context, "📋 تم نسخ كود المصدر إلى الحافظة!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("نسخ", fontSize = 11.sp, color = Color.White)
                            }
                        }
                    }

                    // Code Viewer Box
                    Surface(
                        color = Color(0xFF030712),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(14.dp)
                        ) {
                            Text(
                                text = codeContent,
                                color = Color(0xFF38BDF8),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                SoundEffectsManager.playRootCommandSound()
                                Toast.makeText(context, "⚡ تم تجهيز حزمة SDK المفتوحة المصدر للتنزيل!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("تصدير SDK المفتوح", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

private const val REDSCRIPT_INTERPRETER_SOURCE = """// =========================================================
// RedScript Programming Language v2.0 - Open Source Core Engine
// License: MIT License (C) 2026 RedScript Core Team
// =========================================================

package com.example.util

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * RedScript Interpreter & AST Exec Engine
 */
class RedScriptEngine(private val context: Context) {

    data class ExecutionResult(
        val success: Boolean,
        val output: String,
        val executionTimeMs: Long,
        val generatedAppTitle: String = ""
    )

    suspend fun executeScript(script: String): ExecutionResult = withContext(Dispatchers.Default) {
        val startTime = System.currentTimeMillis()
        val logs = StringBuilder()
        
        val lines = script.lines()
        var appTitle = "تطبيق RedScript المخصص"
        
        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isEmpty() || trimmed.startsWith("//")) continue
            
            when {
                trimmed.startsWith("app.title =") -> {
                    appTitle = trimmed.substringAfter("=").trim().removeSurrounding("\"")
                    logs.append("📌 عنوان التطبيق: ${'$'}appTitle\n")
                }
                trimmed.startsWith("root.exec(") -> {
                    val cmd = trimmed.substringAfter("root.exec(").substringBeforeLast(")").removeSurrounding("\"")
                    val res = RootDeviceController.executeCommand(cmd)
                    logs.append("⚡ [Root Exec]: ${'$'}cmd -> ${'$'}{res.output}\n")
                }
                trimmed.startsWith("ui.addCard(") -> {
                    val cardText = trimmed.substringAfter("(").substringBeforeLast(")").removeSurrounding("\"")
                    logs.append("🎨 [UI Element Added]: ${'$'}cardText\n")
                }
                trimmed.startsWith("voice.speak(") -> {
                    val speech = trimmed.substringAfter("(").substringBeforeLast(")").removeSurrounding("\"")
                    VoiceSpeechManager(context).speak(speech)
                    logs.append("🔊 [Voice Output]: ${'$'}speech\n")
                }
            }
        }
        
        val duration = System.currentTimeMillis() - startTime
        ExecutionResult(true, logs.toString(), duration, appTitle)
    }
}
"""

private const val ROOT_NATIVE_ENGINE_SOURCE = """// =========================================================
// RedScript Root Native Engine & System Call Hooks
// Direct 'su' invocation and CPU Governor Controller
// =========================================================

package com.example.util

import java.io.DataOutputStream

object RootNativeEngine {

    data class RootExecResult(val isRealRoot: Boolean, val exitCode: Int, val output: String)

    fun execSu(command: String): RootExecResult {
        return try {
            val process = Runtime.getRuntime().exec("su")
            val os = DataOutputStream(process.outputStream)
            os.writeBytes(command + "\n")
            os.writeBytes("exit\n")
            os.flush()
            val output = process.inputStream.bufferedReader().readText()
            val exitCode = process.waitFor()
            RootExecResult(true, exitCode, output.ifEmpty { "تم تنفيذ أمر الجذر بنجاح بدون مخرجات نصية." })
        } catch (e: Exception) {
            RootExecResult(false, -1, "فشل تنفيذ الروت: ${'$'}{e.localizedMessage}")
        }
    }
}
"""

private const val STDLIB_SOURCE = """// =========================================================
// RedScript Standard Library v2.0 (RedScript StdLib)
// =========================================================

// 1. Root & Hardware Control Module
fun root.exec(cmd: String): String
fun root.setCpuGovernor(mode: String) // "performance", "powersave", "schedutil"
fun root.cleanMemoryRam(): String
fun root.setFPSLimit(fps: Int) // 60, 90, 120, 144

// 2. UI & Live Page Building Module
fun page.create(title: String, icon: String): PageHandle
fun page.addHeader(text: String, color: String)
fun page.addButton(label: String, onClickScript: String)
fun page.addLiveStreamBox(appId: Long)

// 3. AI & Speech Assistant Module
fun ai.ask(prompt: String): String
fun voice.speak(text: String)
fun gemini.searchWeb(query: String): String

// 4. Stream & Network Module
fun stream.broadcastApp(appId: Long, quality: String) // "1080p60", "4K"
fun network.ping(host: String): Int
"""

private const val REDSCRIPT_SPEC_DOCS = """=========================================================
REDSCRIPT LANGUAGE & RUNTIME SPECIFICATION (OPEN SOURCE v2.0)
Status: Unlocked Developer Session
=========================================================

1. OVERVIEW:
RedScript is a lightweight, domain-specific programming language designed for Android power users, root system tweaks, AI automation, live streaming apps, and visual page development.

2. SYNTAX RULES:
- Statements do not require trailing semicolons.
- Line comments start with '//'.
- Built-in modules: root.*, page.*, stream.*, ai.*, voice.*, db.*

3. COMPILER & RUNTIME BINDINGS:
- Target Platform: Android ART / Dalvik Virtual Machine
- Native Bridges: Java Native Interface (JNI) & Interactive 'su' Linux Shell
- License: Open Source (MIT License) - Free for community modification and distribution.
"""
