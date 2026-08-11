package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.AppEntity
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.util.SoundEffectsManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveStreamingEngineDialog(
    initialApp: AppEntity? = null,
    availableApps: List<AppEntity> = emptyList(),
    onDismiss: () -> Unit,
    onPublishUpdatedApp: (AppEntity) -> Unit = {}
) {
    val context = LocalContext.current
    var selectedApp by remember {
        mutableStateOf(
            initialApp ?: availableApps.firstOrNull() ?: AppEntity(
                id = System.currentTimeMillis(),
                title = "تطبيق البث المخصص (Custom Live App)",
                publisher = "المطور المباشر",
                description = "تطبيق تفاعلي مع محرك بث مباشر وتطوير صفحات بالذكاء الاصطناعي.",
                category = "أدوات",
                country = "🌐 جميع الدول",
                versionName = "1.0.0-Live",
                fileSize = "2.4 MB",
                downloadUrl = "https://community.store/live/app.apk",
                rating = 5.0f,
                isVerified = true
            )
        )
    }

    var isStreamingLive by remember { mutableStateOf(true) }
    var streamQuality by remember { mutableStateOf("1080p @ 60 FPS") }
    var micEnabled by remember { mutableStateOf(true) }
    var activeViewerCount by remember { mutableIntStateOf(1420) }
    var likesCount by remember { mutableIntStateOf(3850) }

    // Page Studio state
    var pagesList by remember {
        mutableStateOf(
            listOf(
                AppPageItem("1", "🏠 الرئيسية (Home Dashboard)", "صفحة العرض الرئيسية للتطبيق مع أزرار الإطلاق", true),
                AppPageItem("2", "📊 إحصائيات الأداء (Live Performance)", "مراقبة الفريمات الـ FPS واستهلاك المعالج حياً", false),
                AppPageItem("3", "💬 شات البث التفاعلي (Stream Chat)", "غرفة شات وتعليقات المتابعين في الوقت الفعلي", false),
                AppPageItem("4", "🤖 المساعد المدمج (AI Assistant Studio)", "مساعد ذكي للرد التلقائي على المتابعين", false)
            )
        )
    }

    var newPageTitle by remember { mutableStateOf("") }
    var newPageDesc by remember { mutableStateOf("") }
    var showAddPageBox by remember { mutableStateOf(false) }

    var chatMessages by remember {
        mutableStateOf(
            listOf(
                "أحمد: ما شاء الله، جودة البث والتطبيق خيالية 🔥",
                "سارة: الصفحة الجديدة شغال فيها الأداء عالي جداً 🚀",
                "علي: هل يمكن إضافة كود RedScript للتحكم بالصوت؟",
                "الذكاء الاصطناعي Bot: تم تحديث الصفحة وحفظ الأوامر بنجاح!"
            )
        )
    }
    var inputChatMessage by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color(0xFF090D16)
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
                            .size(40.dp)
                            .background(Color(0xFFEF4444).copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LiveTv,
                            contentDescription = "Live Stream Engine",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "محرك البث وتطوير الصفحات 📡",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = if (isStreamingLive) Color(0xFFEF4444) else Color.Gray,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (isStreamingLive) "LIVE 🔴" else "موقف",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "بث حي ومباشر للتطبيقات المصنوعة وتطوير صفحات جديدة حياً",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            Divider(color = Color(0xFF1E293B))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Live Stream Video Canvas Box
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFEF4444).copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Dashboard, contentDescription = null, tint = CyanAccent)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = selectedApp.title,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                            }

                            Surface(
                                color = Color(0xFF1E293B),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = streamQuality,
                                    color = CyanAccent,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Stream Player Simulation Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(170.dp)
                                .background(Color(0xFF030712), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = null,
                                    tint = if (isStreamingLive) Color(0xFF10B981) else Color.Gray,
                                    modifier = Modifier.size(42.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (isStreamingLive) "جاري بث واجهة التطبيق مباشرة (WebRTC / 60 FPS)" else "البث متوقف حالياً",
                                    color = if (isStreamingLive) Color(0xFF10B981) else Color.Gray,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "تفاعل المشاهدين: $activeViewerCount مشاهد حي • $likesCount إعجاب ❤️",
                                    color = Color(0xFF94A3B8),
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                                )
                            }

                            // Stream overlay stats
                            Row(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    color = Color.Black.copy(alpha = 0.7f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "FPS: 60.0",
                                        color = Color(0xFF38BDF8),
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Surface(
                                    color = Color.Black.copy(alpha = 0.7f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "Ping: 3ms",
                                        color = Color(0xFF10B981),
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // Stream Control Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    isStreamingLive = !isStreamingLive
                                    SoundEffectsManager.playClick()
                                    Toast.makeText(context, if (isStreamingLive) "🔴 تم تشغيل البث المباشر للتطبيق" else "⏸️ تم إيقاف البث", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isStreamingLive) Color(0xFFEF4444) else Color(0xFF10B981)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = if (isStreamingLive) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isStreamingLive) "إيقاف البث" else "بدء البث",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            IconButton(
                                onClick = {
                                    micEnabled = !micEnabled
                                    SoundEffectsManager.playClick()
                                }
                            ) {
                                Icon(
                                    imageVector = if (micEnabled) Icons.Default.Mic else Icons.Default.MicOff,
                                    contentDescription = "Mic",
                                    tint = if (micEnabled) Color(0xFF10B981) else Color.Gray
                                )
                            }

                            Button(
                                onClick = {
                                    likesCount += 1
                                    SoundEffectsManager.playClick()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEC4899).copy(alpha = 0.2f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFEC4899), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("$likesCount", color = Color(0xFFEC4899), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Page Building Studio Section
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Layers, contentDescription = null, tint = Color(0xFF8B5CF6))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "استوديو تطوير الصفحات 🛠️ (Page Studio)",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                            }

                            Button(
                                onClick = { showAddPageBox = !showAddPageBox },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("إضافة صفحة", fontSize = 11.sp, color = Color.White)
                            }
                        }

                        Text(
                            text = "يمكنك إضافة وتطوير صفحات جديدة وتضمينها فوراً داخل هذا التطبيق المصنوع:",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = Color(0xFF94A3B8)
                        )

                        AnimatedVisibility(visible = showAddPageBox) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF1E293B), RoundedCornerShape(10.dp))
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "إنشاء صفحة جديدة بالذكاء الاصطناعي و RedScript:",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF8B5CF6)
                                )

                                OutlinedTextField(
                                    value = newPageTitle,
                                    onValueChange = { newPageTitle = it },
                                    placeholder = { Text("اسم الصفحة (مثال: 🎮 صفحة الألعاب)", color = Color.Gray) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF8B5CF6),
                                        unfocusedBorderColor = Color(0xFF475569),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = newPageDesc,
                                    onValueChange = { newPageDesc = it },
                                    placeholder = { Text("وصف ومواصفات الصفحة المخصصة...", color = Color.Gray) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF8B5CF6),
                                        unfocusedBorderColor = Color(0xFF475569),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Button(
                                    onClick = {
                                        if (newPageTitle.isNotBlank()) {
                                            SoundEffectsManager.playRootCommandSound()
                                            val newP = AppPageItem(
                                                id = System.currentTimeMillis().toString(),
                                                title = newPageTitle,
                                                description = newPageDesc.ifBlank { "صفحة مخصصة متطورة حديثاً" },
                                                isActive = true
                                            )
                                            pagesList = pagesList + newP
                                            newPageTitle = ""
                                            newPageDesc = ""
                                            showAddPageBox = false
                                            Toast.makeText(context, "✅ تم إضافة الصفحة وتضمينها في التطبيق!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("بناء وتنسيق الصفحة الجديدة", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }

                        // Render pages list
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            pagesList.forEach { page ->
                                Surface(
                                    color = if (page.isActive) Color(0xFF1E293B) else Color(0xFF0F172A),
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (page.isActive) Color(0xFF8B5CF6) else Color(0xFF334155)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = page.title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                                            Text(text = page.description, color = Color(0xFF94A3B8), fontSize = 10.sp)
                                        }

                                        Button(
                                            onClick = {
                                                pagesList = pagesList.map { p ->
                                                    p.copy(isActive = p.id == page.id)
                                                }
                                                SoundEffectsManager.playClick()
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (page.isActive) Color(0xFF10B981) else Color(0xFF334155)
                                            ),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = if (page.isActive) "معروضة حياً" else "تفعيل",
                                                fontSize = 10.sp,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Interactive Stream Chat Box
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "💬 دردشة وتعليقات البث الحي للمشاهدين:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )

                        Surface(
                            color = Color(0xFF030712),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                chatMessages.forEach { msg ->
                                    Text(
                                        text = msg,
                                        color = Color(0xFFCBD5E1),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = inputChatMessage,
                                onValueChange = { inputChatMessage = it },
                                placeholder = { Text("أكتب تعليق للبث...", color = Color.Gray, fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFEF4444),
                                    unfocusedBorderColor = Color(0xFF334155),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )

                            Button(
                                onClick = {
                                    if (inputChatMessage.isNotBlank()) {
                                        SoundEffectsManager.playClick()
                                        chatMessages = chatMessages + "أنت: $inputChatMessage"
                                        inputChatMessage = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                            }
                        }
                    }
                }

                // Deploy Updated App Button
                Button(
                    onClick = {
                        SoundEffectsManager.playRootCommandSound()
                        val updatedApp = selectedApp.copy(
                            description = "${selectedApp.description}\n[تحديث المحرك الحي]: يتضمن ${pagesList.size} صفحات مطورة للبث المباشر."
                        )
                        onPublishUpdatedApp(updatedApp)
                        Toast.makeText(context, "🚀 تم حفظ ونشر الصفحات الجديدة للبث في متجر المنصة!", Toast.LENGTH_LONG).show()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تحديث ونشر التطبيق بالصفحات الجديدة في المتجر 🚀",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                }
            }
        }
    }
}

private data class AppPageItem(
    val id: String,
    val title: String,
    val description: String,
    val isActive: Boolean
)
