package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.AppEntity
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.util.RedRootDictionary
import com.example.util.RedScriptEngine
import com.example.util.RedScriptItem
import com.example.util.RootDeviceController
import com.example.util.SoundEffectsManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedScriptTerminalDialog(
    onDismiss: () -> Unit,
    onPublishScriptToStore: (AppEntity) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val clipboardManager = LocalClipboardManager.current

    var selectedTab by remember { mutableStateOf(0) } // 0 = Terminal & Editor, 1 = Library, 2 = 1000 Keyword Inspector
    var currentScriptText by remember { mutableStateOf(RedScriptEngine.sampleScripts[0].content) }
    var scriptTitle by remember { mutableStateOf("RedScript_Turbo_BattleRoyale") }
    var terminalLogs by remember { mutableStateOf(listOf("💻 [RedScript Console v2.0 READY]")) }
    var isRunning by remember { mutableStateOf(false) }

    // Keyword Inspector search state
    var keywordQuery by remember { mutableStateOf("") }

    // Publish script dialog state
    var showPublishDialog by remember { mutableStateOf(false) }
    var pubAuthor by remember { mutableStateOf("") }
    var pubDesc by remember { mutableStateOf("سكربت تهكير وتسريع الألعاب المتقدم ببرمجة RedScript المخصصة.") }

    val themeColor = when (CustomizationState.terminalThemeName) {
        "Cyber Neon" -> CyanAccent
        "RedMagic Crimson" -> Color(0xFFDC2626)
        "Sunset Amber" -> Color(0xFFF59E0B)
        "Vaporwave Purple" -> Color(0xFFA855F7)
        "Hacker Monospace" -> Color(0xFF22C55E)
        else -> EmeraldSuccess
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF030712),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(themeColor.copy(alpha = 0.2f), CircleShape)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Terminal, contentDescription = null, tint = themeColor)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "💻 ترمنال لغة RedScript المخصصة",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                            color = Color.White
                        )
                        Text(
                            text = "توليد، برمجة، تشغيل ونشر سكربتات التهكير والروت",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            color = Color.Gray
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Navigation Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("💻 المبرمج والترمنال", "📂 مكتبة السكربتات", "📚 معجم 1000 كلمة AI").forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = index },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) themeColor.copy(alpha = 0.25f) else Color(0xFF1F2937),
                        border = BorderStroke(1.dp, if (isSelected) themeColor else Color.Transparent)
                    ) {
                        Box(modifier = Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, fontSize = 10.sp),
                                color = if (isSelected) themeColor else Color.LightGray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Tab Content
            when (selectedTab) {
                0 -> {
                    // TAB 0: Code Editor & Execution Console
                    Column(modifier = Modifier.weight(1f)) {
                        // Title & Action Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = scriptTitle,
                                onValueChange = { scriptTitle = it },
                                label = { Text("اسم ملف السكربت (.rmscript)", fontSize = 10.sp) },
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp),
                                textStyle = MaterialTheme.typography.bodySmall.copy(color = Color.White, fontSize = 11.sp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = themeColor)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Button(
                                onClick = {
                                    if (isRunning) return@Button
                                    SoundEffectsManager.playRootCommandSound()
                                    isRunning = true
                                    terminalLogs = listOf("🚀 بدء التشغيل المباشر لـ $scriptTitle.rmscript...")
                                    scope.launch {
                                        RedScriptEngine.executeRedScript(context, currentScriptText) { log ->
                                            terminalLogs = terminalLogs + log
                                        }
                                        isRunning = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = themeColor),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(52.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("تشغيل", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Script Editor Input Box
                        OutlinedTextField(
                            value = currentScriptText,
                            onValueChange = { currentScriptText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            textStyle = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF86EFAC),
                                fontSize = CustomizationState.terminalFontSize.sp,
                                lineHeight = (CustomizationState.terminalFontSize + 4).sp
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF030712),
                                unfocusedContainerColor = Color(0xFF030712),
                                focusedBorderColor = themeColor
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Quick Code Tools Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Button(
                                    onClick = {
                                        val file = RedScriptEngine.saveScriptToFile(context, scriptTitle, currentScriptText)
                                        if (file != null) {
                                            Toast.makeText(context, "💾 تم حفظ ملف السكربت بنجاح في: ${file.name}", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Icon(Icons.Default.Save, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("حفظ ملف .rmscript", fontSize = 10.sp, color = Color.White)
                                }

                                Button(
                                    onClick = { showPublishDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Icon(Icons.Default.Publish, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("نشر للمجتمع 🌐", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }

                            IconButton(onClick = {
                                clipboardManager.setText(AnnotatedString(currentScriptText))
                                Toast.makeText(context, "تم نسخ كود السكربت للحافظة!", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "نسخ", tint = Color.Gray, modifier = Modifier.size(18.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Terminal Console Log Output Window
                        Text(
                            text = "📟 مخرجات وسجلات السكربت (Console Output):",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF000000)),
                            border = BorderStroke(1.dp, themeColor.copy(alpha = 0.4f))
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                items(terminalLogs) { log ->
                                    Text(
                                        text = log,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 10.sp,
                                            color = when {
                                                log.contains("✅") || log.contains("🎉") -> Color(0xFF4ADE80)
                                                log.contains("⚡") || log.contains("🚀") -> themeColor
                                                log.contains("⚠️") -> Color(0xFFFACC15)
                                                else -> Color(0xFFCBD5E1)
                                            }
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // TAB 1: Script Library (Pre-built + Community Published)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(RedScriptEngine.sampleScripts) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        currentScriptText = item.content
                                        scriptTitle = item.title.replace(" ", "_").take(25)
                                        selectedTab = 0
                                        Toast
                                            .makeText(context, "تم تحميل السكربت في المحرر!", Toast.LENGTH_SHORT)
                                            .show()
                                    },
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF111827)),
                                border = BorderStroke(1.dp, Color(0xFF1F2937))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp),
                                            color = themeColor
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = themeColor.copy(alpha = 0.15f)
                                        ) {
                                            Text(
                                                text = item.author,
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                                color = themeColor,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.description,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                        color = Color.LightGray
                                    )
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // TAB 2: 1000 AI Root Keyword Dictionary Inspector
                    Column(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = keywordQuery,
                            onValueChange = { keywordQuery = it },
                            placeholder = { Text("بحث في معجم الـ 1000 كلمة والروت...", fontSize = 11.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodySmall.copy(color = Color.White),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = themeColor)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val matchedKeywords = remember(keywordQuery) {
                            RedRootDictionary.searchKeywords(keywordQuery)
                        }

                        Text(
                            text = "نتائج البحث (${matchedKeywords.size} من 1000 كلمة):",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(matchedKeywords) { kw ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            currentScriptText += "\n${kw.rootCommand}"
                                            Toast
                                                .makeText(context, "تم إدراج الأمر: ${kw.keyword}", Toast.LENGTH_SHORT)
                                            .show()
                                        },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                                    border = BorderStroke(1.dp, Color(0xFF1E293B))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "#${kw.id} ${kw.arabicName}",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                                                    color = Color.White
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "[${kw.category}]",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                                    color = themeColor
                                                )
                                            }
                                            Text(
                                                text = "الأمر: ${kw.rootCommand}",
                                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace, fontSize = 9.sp),
                                                color = Color(0xFF86EFAC)
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                scope.launch {
                                                    RootDeviceController.executeCommand(kw.rootCommand)
                                                    Toast.makeText(context, "⚡ تم تنفيذ: ${kw.arabicName}", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = themeColor),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text("تنفيذ", fontSize = 9.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Publish Script Dialog
    if (showPublishDialog) {
        AlertDialog(
            onDismissRequest = { showPublishDialog = false },
            title = { Text("🌐 نشر السكربت للمتجر المجتمعي", fontWeight = FontWeight.Bold, fontSize = 14.sp) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("انشر السكربت المخصص ليستفيد منه جميع المستخدمين بالمتجر المفتوح:", fontSize = 11.sp)
                    OutlinedTextField(
                        value = pubAuthor,
                        onValueChange = { pubAuthor = it },
                        label = { Text("اسم الناشر/المطور") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = pubDesc,
                        onValueChange = { pubDesc = it },
                        label = { Text("وصف السكربت وما يقدمه") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val publishedEntity = RedScriptEngine.createCommunityPublishEntity(
                            title = scriptTitle,
                            author = pubAuthor,
                            description = pubDesc,
                            scriptContent = currentScriptText
                        )
                        onPublishScriptToStore(publishedEntity)
                        showPublishDialog = false
                        Toast.makeText(context, "🎉 تم نشر السكربت بنجاح للمتجر المجتمعي!", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = themeColor)
                ) {
                    Text("نشر الآن 🚀", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPublishDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }
}
