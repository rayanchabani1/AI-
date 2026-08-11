package com.example.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.VioletSecondary
import com.example.util.VoiceSpeechManager
import kotlinx.coroutines.launch

@Composable
fun MetaVoiceDialog(
    lastAiResponseText: String,
    isAiThinking: Boolean,
    isHighThinkingMode: Boolean = false,
    onToggleHighThinking: (Boolean) -> Unit = {},
    onSendVoicePrompt: (String) -> Unit,
    onGenerateVeoVideo: (prompt: String, aspectRatio: String) -> Unit = { _, _ -> },
    isGeneratingVeoVideo: Boolean = false,
    veoVideoResult: com.example.data.remote.GeminiAssistantClient.VeoVideoResult? = null,
    onTranscribeAudio: (ByteArray, String) -> Unit = { _, _ -> },
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedTab by remember { mutableStateOf(0) } // 0: Voice Chat, 1: Veo Video, 2: Audio Transcribe
    var veoPrompt by remember { mutableStateOf("") }
    var selectedAspectRatio by remember { mutableStateOf("16:9") } // "16:9" or "9:16"
    var transcribedTextResult by remember { mutableStateOf("") }
    var isTranscribingAudio by remember { mutableStateOf(false) }

    val speechManager = remember { VoiceSpeechManager(context) }

    var isListening by remember { mutableStateOf(false) }
    var isSpeakingByTts by remember { mutableStateOf(false) }
    var recognizedSpeechText by remember { mutableStateOf("") }
    var audioLevel by remember { mutableStateOf(0f) }
    var manualInputText by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("اضغط الميكروفون وتحدث مع ميتا AI بصوتك 🎙️") }

    // Read AI response when it arrives
    LaunchedEffect(lastAiResponseText) {
        if (lastAiResponseText.isNotBlank()) {
            statusMessage = "ميتا AI يتحدث معك الآن 🔊"
            isSpeakingByTts = true
            speechManager.speak(lastAiResponseText) {
                isSpeakingByTts = false
                statusMessage = "انتهى التحدث. اضغط الميكروفون للتحدث مجدداً."
            }
        }
    }

    // Mic Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            isListening = true
            statusMessage = "ميتا AI يستمع إليك الآن... تحدث بوضوح 🎙️"
            speechManager.startListening(
                onResult = { result ->
                    recognizedSpeechText = result
                    manualInputText = result
                    isListening = false
                    statusMessage = "تم التقاط صوتك: '$result'"
                    speechManager.stopListening()
                    onSendVoicePrompt(result)
                },
                onError = { err ->
                    isListening = false
                    statusMessage = err
                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                },
                onRmsChanged = { rms ->
                    audioLevel = (rms.coerceIn(0f, 10f) / 10f)
                }
            )
        } else {
            Toast.makeText(context, "يجب الموافقة على إذن الميكروفون لتنفيذ التحدث الصوتي مع ميتا AI", Toast.LENGTH_LONG).show()
        }
    }

    fun toggleListening() {
        if (isListening) {
            speechManager.stopListening()
            isListening = false
            statusMessage = "تم إيقاف الاستماع الصوتي."
        } else {
            speechManager.stopSpeaking()
            isSpeakingByTts = false
            val hasPermission = ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                isListening = true
                statusMessage = "ميتا AI يستمع إليك الآن... تحدث بوضوح 🎙️"
                speechManager.startListening(
                    onResult = { result ->
                        recognizedSpeechText = result
                        manualInputText = result
                        isListening = false
                        statusMessage = "تم استقبال صوتك: '$result'"
                        speechManager.stopListening()
                        onSendVoicePrompt(result)
                    },
                    onError = { err ->
                        isListening = false
                        statusMessage = err
                    },
                    onRmsChanged = { rms ->
                        audioLevel = (rms.coerceIn(0f, 10f) / 10f)
                    }
                )
            } else {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            speechManager.shutdown()
        }
    }

    // Infinite breathing animation for orb
    val infiniteTransition = rememberInfiniteTransition(label = "MetaOrb")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.22f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "OrbPulse"
    )

    val quickVoiceSuggestions = listOf(
        "ميتا ادخل يتيوب وابحث مستر بيست",
        "ميتا افتح الواتساب",
        "ميتا نظف ذاكرة الجهاز والبطارية",
        "ميتا ما هو الطقس وأحدث الأخبار اليوم",
        "ميتا شغل زر الروت وتسريع الهاتف",
        "ميتا من أنت وما هي مميزاتك"
    )

    Dialog(
        onDismissRequest = {
            speechManager.shutdown()
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(28.dp)),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0F172A),
                                Color(0xFF1E1B4B),
                                Color(0xFF0F172A)
                            )
                        )
                    )
            ) {
                // Top Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    Brush.linearGradient(listOf(IndigoPrimary, CyanAccent)),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Meta AI Logo",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "المساعد الصوتي ميتا AI ✨",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Meta Voice Command & Control Center",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            speechManager.shutdown()
                            onDismiss()
                        },
                        modifier = Modifier.testTag("close_meta_voice_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Dialog",
                            tint = Color.White
                        )
                    }
                }

                // High Thinking Toggle & Feature Mode Tabs
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    // High Thinking Mode Badge/Switch
                    Surface(
                        onClick = { onToggleHighThinking(!isHighThinkingMode) },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isHighThinkingMode) Color(0xFF7C3AED) else Color.White.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, if (isHighThinkingMode) Color(0xFFA78BFA) else Color.White.copy(alpha = 0.2f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isHighThinkingMode) "🧠 وضع التفكير الراقي (HIGH THINKING) مفعل" else "💡 تفعيل التفكير الراقي (gemini-3.1-pro)",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp),
                                    color = Color.White
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = if (isHighThinkingMode) EmeraldSuccess else Color.Gray.copy(alpha = 0.4f)
                            ) {
                                Text(
                                    text = if (isHighThinkingMode) "HIGH ⚡" else "تأكيد",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    // Feature Navigation Tabs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val tabs = listOf("🎙️ المحادثة", "🎬 فيديو Veo 3", "📝 تفريغ الصوت")
                        tabs.forEachIndexed { index, title ->
                            Surface(
                                onClick = { selectedTab = index },
                                shape = RoundedCornerShape(20.dp),
                                color = if (selectedTab == index) IndigoPrimary else Color.White.copy(alpha = 0.12f),
                                border = BorderStroke(1.dp, if (selectedTab == index) CyanAccent else Color.Transparent),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 11.sp
                                    ),
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    if (selectedTab == 0) {
                        // TAB 0: VOICE CHAT
                        // Animated Glowing Orb
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(160.dp)
                                .padding(8.dp)
                        ) {
                            val orbColor1 = when {
                                isListening -> CyanAccent
                                isSpeakingByTts -> EmeraldSuccess
                                isAiThinking -> VioletSecondary
                                else -> IndigoPrimary
                            }

                            val orbColor2 = when {
                                isListening -> Color(0xFF00E5FF)
                                isSpeakingByTts -> Color(0xFF34D399)
                                isAiThinking -> Color(0xFFC084FC)
                                else -> VioletSecondary
                            }

                            // Outer Pulse Glow
                            Box(
                                modifier = Modifier
                                    .size(140.dp)
                                    .scale(if (isListening || isSpeakingByTts) pulseScale else 1f)
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                orbColor1.copy(alpha = 0.45f),
                                                orbColor2.copy(alpha = 0.15f),
                                                Color.Transparent
                                            )
                                        ),
                                        CircleShape
                                    )
                            )

                            // Core Interactive Orb
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(
                                        Brush.linearGradient(listOf(orbColor1, orbColor2)),
                                        CircleShape
                                    )
                                    .border(
                                        2.dp,
                                        Color.White.copy(alpha = 0.6f),
                                        CircleShape
                                    )
                                    .clickable { toggleListening() }
                                    .testTag("meta_orb_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isAiThinking) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(36.dp),
                                        strokeWidth = 3.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = if (isListening) Icons.Default.GraphicEq else if (isSpeakingByTts) Icons.Default.VolumeUp else Icons.Default.Mic,
                                        contentDescription = "Orb Mic Icon",
                                        tint = Color.White,
                                        modifier = Modifier.size(42.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Live Status Text
                        Text(
                            text = statusMessage,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = if (isListening) CyanAccent else if (isSpeakingByTts) EmeraldSuccess else Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Recognized User Voice Text Box
                        if (recognizedSpeechText.isNotBlank()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White.copy(alpha = 0.1f)
                                )
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "🎙️ نص صوتك الملتقط:",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = CyanAccent
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "\"$recognizedSpeechText\"",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // AI Voice Response Box
                        if (lastAiResponseText.isNotBlank()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF1E293B)
                                ),
                                border = BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.AutoAwesome,
                                                contentDescription = "Response",
                                                tint = EmeraldSuccess,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "إجابة ميتا AI الصوتية المباشرة:",
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                color = EmeraldSuccess
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                if (isSpeakingByTts) {
                                                    speechManager.stopSpeaking()
                                                    isSpeakingByTts = false
                                                    statusMessage = "تم كتم الصوت."
                                                } else {
                                                    isSpeakingByTts = true
                                                    speechManager.speak(lastAiResponseText) {
                                                        isSpeakingByTts = false
                                                    }
                                                    statusMessage = "جاري إعادة قراءة الإجابة بصوت ميتا AI..."
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if (isSpeakingByTts) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                                contentDescription = "Toggle TTS",
                                                tint = Color.White
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = lastAiResponseText,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = 14.sp,
                                            lineHeight = 22.sp
                                        ),
                                        color = Color.White
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Quick Voice Actions Scroll
                        Text(
                            text = "💡 اختصارات وأوامر صوتية سريعة مع ميتا:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 8.dp)
                        ) {
                            items(quickVoiceSuggestions) { suggestion ->
                                Surface(
                                    onClick = {
                                        recognizedSpeechText = suggestion
                                        manualInputText = suggestion
                                        statusMessage = "جاري تنفيذ الأمر الصوتي: '$suggestion'"
                                        onSendVoicePrompt(suggestion)
                                    },
                                    shape = RoundedCornerShape(20.dp),
                                    color = Color.White.copy(alpha = 0.12f),
                                    border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.3f))
                                ) {
                                    Text(
                                        text = suggestion,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Text Input Bar for manual typing fallback
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = manualInputText,
                                onValueChange = { manualInputText = it },
                                placeholder = {
                                    Text(
                                        text = "اكتب أو انطق 'ميتا' للتحكم بالجوال...",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("meta_voice_text_input"),
                                shape = RoundedCornerShape(24.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = IndigoPrimary,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                    focusedContainerColor = Color.White.copy(alpha = 0.08f),
                                    unfocusedContainerColor = Color.White.copy(alpha = 0.08f)
                                ),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    if (manualInputText.isNotBlank()) {
                                        val textToSend = manualInputText
                                        recognizedSpeechText = textToSend
                                        manualInputText = ""
                                        statusMessage = "جاري إرسال الطلب لميتا AI..."
                                        onSendVoicePrompt(textToSend)
                                    }
                                },
                                enabled = manualInputText.isNotBlank() && !isAiThinking,
                                shape = CircleShape,
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("meta_send_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = IndigoPrimary,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Send",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    } else if (selectedTab == 1) {
                        // TAB 1: VEO 3 VIDEO GENERATOR
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B)),
                            border = BorderStroke(1.dp, VioletSecondary)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "🎬 انشاء فيديو باستخدام Veo 3 (veo-3.1-fast-generate-preview)",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "قم بكتابة وصف المشهد وسوف يقدم لك نموذج Veo 3 مقطع فيديو عالي الدقة بحجم أبعاد 16:9 أو 9:16.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                OutlinedTextField(
                                    value = veoPrompt,
                                    onValueChange = { veoPrompt = it },
                                    placeholder = {
                                        Text(
                                            text = "اكتب وصف الفيديو هنا (مثال: أسد ذهبي يركض في غابة بنمط سينمائي)",
                                            color = Color.Gray,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = CyanAccent,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                                    ),
                                    minLines = 3
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "اختر أبعاد الفيديو المطلوبة:",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = { selectedAspectRatio = "16:9" },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (selectedAspectRatio == "16:9") CyanAccent else Color.White.copy(alpha = 0.15f),
                                            contentColor = if (selectedAspectRatio == "16:9") Color.Black else Color.White
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("🖥️ 16:9 (أفقي)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }

                                    Button(
                                        onClick = { selectedAspectRatio = "9:16" },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (selectedAspectRatio == "9:16") CyanAccent else Color.White.copy(alpha = 0.15f),
                                            contentColor = if (selectedAspectRatio == "9:16") Color.Black else Color.White
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("📱 9:16 (عمودي)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = {
                                        if (veoPrompt.isNotBlank()) {
                                            onGenerateVeoVideo(veoPrompt, selectedAspectRatio)
                                        }
                                    },
                                    enabled = veoPrompt.isNotBlank() && !isGeneratingVeoVideo,
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                                ) {
                                    if (isGeneratingVeoVideo) {
                                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("جاري معالجة فيديو Veo 3...", color = Color.White)
                                    } else {
                                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "Veo Generate", tint = Color.White)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("انشاء فيديو Veo 3 🎬", fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }

                                if (veoVideoResult != null) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                                        border = BorderStroke(1.dp, EmeraldSuccess)
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Text(
                                                text = "🎉 نتيجة انشاء الفيديو عبر Veo 3:",
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                color = EmeraldSuccess
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = veoVideoResult.message,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else if (selectedTab == 2) {
                        // TAB 2: AUDIO TRANSCRIBE
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                            border = BorderStroke(1.dp, CyanAccent)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "📝 نسخ وتفريغ الصوت بالذكاء الاصطناعي (gemini-3.5-flash)",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "تحدث بالميكروفون وسيقوم نموذج gemini-3.5-flash بنسخ وتفريغ كلامك الصوتي حرفياً بدقة عالية.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = { toggleListening() },
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .size(70.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isListening) CyanAccent else IndigoPrimary
                                    )
                                ) {
                                    Icon(
                                        imageVector = if (isListening) Icons.Default.GraphicEq else Icons.Default.Mic,
                                        contentDescription = "Transcribe Mic",
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = if (isListening) "جاري الاستماع للنسخ والتفريغ..." else "اضغط الميكروفون للبدء بالنسخ والتفريغ",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )

                                if (recognizedSpeechText.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text("📄 النص الصوتي المنسوخ:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = CyanAccent)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = recognizedSpeechText, style = MaterialTheme.typography.bodyMedium, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
