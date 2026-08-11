package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.AiChatMessage
import com.example.data.remote.MessageSender
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SoftContainerBlue
import com.example.ui.theme.VibrantBluePrimary
import com.example.ui.theme.VioletSecondary
import com.example.util.DownloadInstaller

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.style.TextOverflow
import com.example.data.local.entity.AppEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantSheet(
    messages: List<AiChatMessage>,
    isThinking: Boolean,
    onSendMessage: (String) -> Unit,
    onRequestRootPermission: () -> Unit = {},
    onClearChat: () -> Unit = {},
    onDismiss: () -> Unit,
    onDownloadApp: (AppEntity) -> Unit = {},
    onOpenMetaVoice: () -> Unit = {},
    onOpenTerminal: () -> Unit = {},
    onOpenCustomization: () -> Unit = {}
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }
    var showKeyDialog by remember { mutableStateOf(false) }
    var customKeyInput by remember {
        mutableStateOf(com.example.data.remote.GeminiAssistantClient.getActiveApiKey(context))
    }

    LaunchedEffect(messages.size, isThinking) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val samplePrompts = listOf(
        "⚡ [Root] تنظيف كاش النظام وحذف المخلفات",
        "⚡ [Root] فحص الذاكرة العشوائية RAM والأداء",
        "⚡ [Root] تفعيل وضع الأداء الفائق للمعالج",
        "⚡ [Root] إعادة تشغيل الجوال بالوضع الآمن",
        "🌐 [ويب] ابحث بالويب عن أفضل تطبيقات 2026",
        "🎁 [تطبيقات] اقتراح هدايا وتطبيقات علمية",
        "🛡️ [أمان] فحص ثغرات النظام وتصريحات التطبيقات",
        "⚙️ [سكربت] توليد سكربت شل bash أوتوماتيكي",
        "📱 [تجميد] تجميد التطبيقات المستهلكة للبطارية",
        "🚀 [تسريع] تسريع وتحسين أداء الألعاب الفائق",
        "🎨 [تصميم] توليد أفكار أيقونات وواجهات متطورة",
        "🔊 [صوت] تشغيل نغمات تنبيهات Root والمساعد",
        "📶 [شبكة] فحص جودة الاتصال بالإنترنت والـ Ping",
        "💬 [ترجمة] ترجمة نصوص وتقارير النظام الفنية",
        "📄 [ملخص] تلخيص المقالات والملفات البرمجية",
        "🎮 [ألعاب] توليد لعبة تفاعلية بالذكاء الاصطناعي",
        "🛠️ [مطور] فحص خواص أندرويد system.prop",
        "🔋 [بطارية] تحسين استهلاك الطاقة وإغلاق العمليات",
        "🧩 [حلول] حل مشكلات أخطاء التطبيقات والـ Crashes",
        "✨ [Gemini] محادثة مفتوحة وإجابة عن جميع الأسئلة"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(IndigoPrimary, VioletSecondary)
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Assistant",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "مساعد Gemini و SuperSU ⚡",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = EmeraldSuccess,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Public,
                                        contentDescription = "Live Web",
                                        tint = Color.White,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "متصل ✨",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Text(
                            text = "بحث ويب حي والتحكم بنظام Root للجوال",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }

                    // Root Trigger Button
                    Surface(
                        onClick = {
                            com.example.util.SoundEffectsManager.playRootCommandSound()
                            onRequestRootPermission()
                        },
                        color = Color(0xFFD32F2F),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Root Activation Request",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "طلب تفعيل Root",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                color = Color.White
                            )
                        }
                    }

                    IconButton(
                        onClick = { onOpenTerminal() }
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Terminal,
                            contentDescription = "RedScript Terminal",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = { onOpenCustomization() }
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.AutoAwesome,
                            contentDescription = "Customization Hub",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = { showKeyDialog = true },
                        modifier = Modifier.testTag("gemini_key_settings")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = "Gemini API Key",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_ai_sheet")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }
            }

            // Messages Container
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    val isUser = msg.sender == MessageSender.USER

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                    ) {
                        if (!isUser) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(IndigoPrimary.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SmartToy,
                                    contentDescription = "AI",
                                    tint = IndigoPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Column(
                            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
                            modifier = Modifier.fillMaxWidth(0.82f)
                        ) {
                            Surface(
                                color = if (isUser) IndigoPrimary else SoftContainerBlue,
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (isUser) 16.dp else 4.dp,
                                    bottomEnd = if (isUser) 4.dp else 16.dp
                                ),
                                shadowElevation = 1.dp
                            ) {
                                Text(
                                    text = msg.text,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp
                                    ),
                                    color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            // Search Queries Performed
                            if (msg.searchQueries.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "🔍 عمليات البحث المنفذة في المتصفح والإنترنت:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    ),
                                    color = IndigoPrimary,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    contentPadding = PaddingValues(top = 4.dp)
                                ) {
                                    items(msg.searchQueries) { query ->
                                        Surface(
                                            color = IndigoPrimary.copy(alpha = 0.08f),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Search,
                                                    contentDescription = "Search Query",
                                                    tint = IndigoPrimary,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = query,
                                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                    color = IndigoPrimary
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Web Citations
                            if (msg.webSources.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "🌐 المصادر والمواقع المستخرجة من الويب:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    ),
                                    color = VibrantBluePrimary,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    contentPadding = PaddingValues(top = 4.dp)
                                ) {
                                    items(msg.webSources) { citation ->
                                        AssistChip(
                                            onClick = {
                                                DownloadInstaller.openInBrowser(context, citation.url)
                                            },
                                            label = {
                                                Text(
                                                    text = citation.title.take(24),
                                                    style = MaterialTheme.typography.labelSmall
                                                )
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Default.Language,
                                                    contentDescription = "Web Link",
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            },
                                            trailingIcon = {
                                                Icon(
                                                    imageVector = Icons.Default.OpenInNew,
                                                    contentDescription = "Open",
                                                    modifier = Modifier.size(12.dp)
                                                )
                                            },
                                             colors = AssistChipDefaults.assistChipColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                                labelColor = VibrantBluePrimary
                                            )
                                        )
                                    }
                                }
                            }

                            // Suggested & Gifted Apps (1-Click Download)
                            if (msg.suggestedApps.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "🎁 هدية وتوصية AI - تطبيقات متاح تنزيلها فوراً:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    ),
                                    color = EmeraldSuccess,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                msg.suggestedApps.forEach { app ->
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        color = MaterialTheme.colorScheme.surface,
                                        shadowElevation = 2.dp,
                                        border = BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.4f))
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                AppIconView(
                                                    iconUrl = app.iconUrl,
                                                    iconResName = app.iconResName,
                                                    category = app.category,
                                                    size = 46.dp
                                                )

                                                Spacer(modifier = Modifier.width(10.dp))

                                                Column(modifier = Modifier.weight(1f)) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = app.title,
                                                            style = MaterialTheme.typography.titleSmall.copy(
                                                                fontWeight = FontWeight.Bold,
                                                                fontSize = 14.sp
                                                            ),
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis,
                                                            modifier = Modifier.weight(1f, fill = false)
                                                        )
                                                        if (app.isVerified) {
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Icon(
                                                                imageVector = Icons.Default.Verified,
                                                                contentDescription = "Verified",
                                                                tint = EmeraldSuccess,
                                                                modifier = Modifier.size(14.dp)
                                                            )
                                                        }
                                                    }

                                                    Spacer(modifier = Modifier.height(2.dp))

                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                    ) {
                                                        Surface(
                                                            color = EmeraldSuccess.copy(alpha = 0.12f),
                                                            shape = RoundedCornerShape(6.dp)
                                                        ) {
                                                            Text(
                                                                text = app.category,
                                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                                color = EmeraldSuccess,
                                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                            )
                                                        }

                                                        Text(
                                                            text = "• ${app.fileSize}",
                                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )

                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Icon(
                                                                imageVector = Icons.Default.Star,
                                                                contentDescription = "Rating",
                                                                tint = Color(0xFFFFB300),
                                                                modifier = Modifier.size(12.dp)
                                                            )
                                                            Spacer(modifier = Modifier.width(2.dp))
                                                            Text(
                                                                text = "${app.rating}",
                                                                style = MaterialTheme.typography.labelSmall.copy(
                                                                    fontWeight = FontWeight.Bold,
                                                                    fontSize = 10.sp
                                                                )
                                                            )
                                                        }
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = app.description,
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Spacer(modifier = Modifier.height(10.dp))

                                            Button(
                                                onClick = { onDownloadApp(app) },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(38.dp),
                                                shape = RoundedCornerShape(10.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = EmeraldSuccess,
                                                    contentColor = Color.White
                                                )
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Download,
                                                        contentDescription = "Download",
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = "تنزيل وتثبيت بضغطة زر 📥",
                                                        style = MaterialTheme.typography.labelMedium.copy(
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 12.sp
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (isThinking) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(SoftContainerBlue, RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = IndigoPrimary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "جاري البحث في الويب والمتصفح واستخراج الإجابة...",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = IndigoPrimary
                            )
                        }
                    }
                }
            }

            // Quick Prompt Suggestions
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(samplePrompts) { prompt ->
                    Surface(
                        onClick = {
                            onSendMessage(prompt.substringAfter(" "))
                        },
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    ) {
                        Text(
                            text = prompt,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Input Row
            Surface(
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text(
                                text = "اسأل عن أي تطبيق، شرح أو خبر تقني...",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("ai_input_field"),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = IndigoPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            onDismiss()
                            onOpenMetaVoice()
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                Brush.linearGradient(listOf(IndigoPrimary, CyanAccent)),
                                CircleShape
                            )
                            .testTag("ai_voice_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Meta Voice",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank() && !isThinking) {
                                com.example.util.SoundEffectsManager.playAiChime()
                                val textToSend = inputText
                                inputText = ""
                                onSendMessage(textToSend)
                            }
                        },
                        enabled = inputText.isNotBlank() && !isThinking,
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                if (inputText.isNotBlank() && !isThinking) IndigoPrimary else Color.LightGray.copy(alpha = 0.5f),
                                CircleShape
                            )
                            .testTag("ai_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }

    if (showKeyDialog) {
        AlertDialog(
            onDismissRequest = { showKeyDialog = false },
            title = {
                Text(
                    text = "مفتاح Google Gemini API 🔑",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column {
                    Text(
                        text = "أدخل مفتاح Google Gemini API الخاص بك لتشغيل النموذج المباشر والاستجابة المباشرة لجميع الأسئلة والبحث من الويب:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = customKeyInput,
                        onValueChange = { customKeyInput = it },
                        placeholder = { Text("AIzaSy...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        com.example.data.remote.GeminiAssistantClient.saveCustomApiKey(context, customKeyInput)
                        showKeyDialog = false
                    }
                ) {
                    Text("حفظ المفتاح ✨", fontWeight = FontWeight.Bold, color = IndigoPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showKeyDialog = false }) {
                    Text("إلغاء", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}
