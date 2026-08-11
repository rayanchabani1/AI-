package com.example.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CenterFocusWeak
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.RootDeviceController
import com.example.util.SoundEffectsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// REDMAGIC Color Palette
val RedMagicCrimson = Color(0xFFFF0033)
val RedMagicDarkMetal = Color(0xFF0B0C12)
val RedMagicCardBg = Color(0xFF141622)
val RedMagicCyan = Color(0xFF00F0FF)
val RedMagicEmerald = Color(0xFF10B981)

/**
 * Global persistent state holder for REDMAGIC Game Space features.
 * Preserves all active toggles and statuses when minimizing, dragging, or reopening the overlay.
 */
object RedMagicGameState {
    var isRiseModeActive by mutableStateOf(true)
    var isFanTurbofanActive by mutableStateOf(true)
    var is120HzLock by mutableStateOf(true)
    var isAiTouchBoost by mutableStateOf(true)
    var isRecordingScreen by mutableStateOf(false)
    var isCrosshairEnabled by mutableStateOf(false)
    var isAutoTriggerEnabled by mutableStateOf(false)
    var isZeroRecoilActive by mutableStateOf(false)
    var isVulkanGpuActive by mutableStateOf(true)
    var is3dFootstepAudioActive by mutableStateOf(true)
    var isDndEnabled by mutableStateOf(true)
    var isMaxBrightnessEnabled by mutableStateOf(false)
    var hudOpacity by mutableStateOf(0.92f)
    var statusText by mutableStateOf("🔥 REDMAGIC Game Space Rise Mode Active")
}

/**
 * Super compact REDMAGIC Floating Bubble Badge
 */
@Composable
fun RedMagicFloatingBubble(
    currentFps: Int = 120,
    pingMs: Int = 28,
    isTurboActive: Boolean = true,
    onClickBubble: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val pingColor = when {
        pingMs < 45 -> RedMagicEmerald
        pingMs < 85 -> Color(0xFFF59E0B)
        else -> RedMagicCrimson
    }

    Box(
        modifier = Modifier
            .scale(pulseGlow)
            .alpha(RedMagicGameState.hudOpacity)
            .clickable {
                SoundEffectsManager.playClick()
                onClickBubble()
            }
            .testTag("redmagic_floating_bubble"),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = RedMagicDarkMetal,
            border = BorderStroke(1.2.dp, Brush.horizontalGradient(listOf(RedMagicCrimson, RedMagicCyan))),
            shadowElevation = 6.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // REDMAGIC Crimson Glowing Dot
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(if (isTurboActive) RedMagicCrimson else Color.Gray, CircleShape)
                )

                Text(
                    text = "$currentFps FPS",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold, fontSize = 10.sp),
                    color = Color.White
                )

                // Real-Time Ping Latency Monitor Display
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = pingColor.copy(alpha = 0.2f),
                    border = BorderStroke(0.8.dp, pingColor)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(pingColor, CircleShape)
                        )
                        Text(
                            text = "${pingMs}ms",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold, fontSize = 8.sp),
                            color = pingColor
                        )
                    }
                }

                if (RedMagicGameState.isCrosshairEnabled) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .border(1.dp, RedMagicCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.size(2.dp).background(RedMagicCrimson, CircleShape))
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = RedMagicCrimson
                ) {
                    Text(
                        text = "REDMAGIC",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 7.sp),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                    )
                }
            }
        }
    }
}

/**
 * REDMAGIC Game Space Overlay HUD Panel (Appears floating right over the game screen when bubble is clicked!)
 */
@Composable
fun RedMagicGameSpaceOverlay(
    onMinimizeToBubble: () -> Unit,
    onOpenMainApp: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var liveFps by remember { mutableStateOf(120) }
    var livePingMs by remember { mutableStateOf(28) }
    var liveCpuTemp by remember { mutableStateOf("38.2°C") }
    var liveRamFree by remember { mutableStateOf("3.9 GB") }
    var liveGpuFreq by remember { mutableStateOf("850 MHz") }
    var liveCpuFreq by remember { mutableStateOf("2840 MHz") }

    // Live Metrics Update Loop
    LaunchedEffect(Unit) {
        while (true) {
            val metrics = RootDeviceController.getLiveSystemMetrics()
            liveCpuTemp = metrics["cpuTemp"] ?: "38.5°C"
            liveRamFree = metrics["ramFree"] ?: "3.9 GB"
            liveGpuFreq = metrics["gpuFreq"] ?: "850 MHz"
            liveCpuFreq = metrics["cpuFreq"] ?: "2840 MHz"
            liveFps = RootDeviceController.getKernelRealtimeFps()
            livePingMs = RootDeviceController.getRealtimeNetworkPingMs()
            delay(1500)
        }
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.alpha(RedMagicGameState.hudOpacity)
    ) {
        Card(
            modifier = Modifier
                .width(320.dp)
                .padding(4.dp)
                .testTag("redmagic_overlay_hud"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = RedMagicDarkMetal),
            border = BorderStroke(1.5.dp, Brush.linearGradient(listOf(RedMagicCrimson, RedMagicCyan))),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Drag Handle Bar (مقبض تحريك وسحب الواجهة)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(4.dp)
                            .background(RedMagicCyan.copy(alpha = 0.8f), RoundedCornerShape(2.dp))
                    )
                }

                // REDMAGIC Top Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(RedMagicCrimson, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Gamepad,
                                contentDescription = "REDMAGIC",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "REDMAGIC GAME SPACE",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold, fontSize = 12.sp),
                                color = Color.White
                            )
                            Text(
                                text = "👆 اسحب الواجهة من أي مكان لتحريكها",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, fontWeight = FontWeight.Bold),
                                color = RedMagicCyan
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            SoundEffectsManager.playClick()
                            onMinimizeToBubble()
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Minimize",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Real-time Cyber Stats Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Stat 1: FPS
                    RedMagicStatChip(
                        title = "FPS",
                        value = "$liveFps",
                        accentColor = RedMagicEmerald,
                        modifier = Modifier.weight(1f)
                    )
                    // Stat 2: PING
                    RedMagicStatChip(
                        title = "PING",
                        value = "${livePingMs}ms",
                        accentColor = when {
                            livePingMs < 45 -> RedMagicEmerald
                            livePingMs < 85 -> Color(0xFFF59E0B)
                            else -> RedMagicCrimson
                        },
                        modifier = Modifier.weight(1f)
                    )
                    // Stat 3: CPU Temp
                    RedMagicStatChip(
                        title = "TEMP",
                        value = liveCpuTemp,
                        accentColor = RedMagicCrimson,
                        modifier = Modifier.weight(1f)
                    )
                    // Stat 4: RAM FREE
                    RedMagicStatChip(
                        title = "RAM",
                        value = liveRamFree,
                        accentColor = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Screen Recorder & Screenshot Action Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Button 1: Instant Screenshot
                    Button(
                        onClick = {
                            SoundEffectsManager.playRootCommandSound()
                            scope.launch {
                                val result = RootDeviceController.takeScreenshotViaRoot(context)
                                RedMagicGameState.statusText = result
                                Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                        contentPadding = PaddingValues(vertical = 6.dp, horizontal = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Screenshot",
                            tint = RedMagicCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "لقطة شاشة",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                            color = Color.White
                        )
                    }

                    // Button 2: Screen Record Toggle
                    Button(
                        onClick = {
                            SoundEffectsManager.playRootCommandSound()
                            scope.launch {
                                val (recording, msg) = RootDeviceController.toggleScreenRecordViaRoot(context)
                                RedMagicGameState.isRecordingScreen = recording
                                RedMagicGameState.statusText = msg
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (RedMagicGameState.isRecordingScreen) RedMagicCrimson else Color(0xFF1E293B)
                        ),
                        contentPadding = PaddingValues(vertical = 6.dp, horizontal = 4.dp)
                    ) {
                        Icon(
                            imageVector = if (RedMagicGameState.isRecordingScreen) Icons.Default.Stop else Icons.Default.Videocam,
                            contentDescription = "Screen Record",
                            tint = if (RedMagicGameState.isRecordingScreen) Color.White else RedMagicCrimson,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (RedMagicGameState.isRecordingScreen) "إيقاف التسجيل" else "تسجيل الشاشة",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                            color = Color.White
                        )
                    }
                }

                // Status Log Banner
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = RedMagicCardBg,
                    border = BorderStroke(1.dp, RedMagicCrimson.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = RedMagicGameState.statusText,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        textAlign = TextAlign.Center
                    )
                }

                // REDMAGIC Core Turbo Switches
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(RedMagicCardBg, RoundedCornerShape(12.dp))
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Toggle 1: Rise Mode Turbo
                    RedMagicToggleRow(
                        title = "⚡ Rise Mode (Super Turbo)",
                        subtitle = "تفعيل الماكس لأعلى أداء مع حجز 120Hz",
                        isChecked = RedMagicGameState.isRiseModeActive,
                        activeColor = RedMagicCrimson,
                        onCheckedChange = { active ->
                            SoundEffectsManager.playRootCommandSound()
                            RedMagicGameState.isRiseModeActive = active
                            scope.launch {
                                if (active) {
                                    RedMagicGameState.statusText = RootDeviceController.applyUltraRootGameBoost()
                                } else {
                                    RootDeviceController.executeCommand("su -c echo schedutil > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor")
                                    RedMagicGameState.statusText = "تم خفض الأداء للوضع الطبيعي."
                                }
                            }
                        }
                    )

                    // Toggle 2: ICE Turbofan Cooling
                    RedMagicToggleRow(
                        title = "🌀 مروحة التبريد ICE (20,000 RPM)",
                        subtitle = "تبريد المعالج والبطارية لتفادي الهبوط",
                        isChecked = RedMagicGameState.isFanTurbofanActive,
                        activeColor = RedMagicCyan,
                        onCheckedChange = { active ->
                            SoundEffectsManager.playRootCommandSound()
                            RedMagicGameState.isFanTurbofanActive = active
                            scope.launch {
                                if (active) {
                                    RedMagicGameState.statusText = RootDeviceController.coolDownCpuAndBattery()
                                } else {
                                    RedMagicGameState.statusText = "تم ضبط التبريد التلقائي."
                                }
                            }
                        }
                    )

                    // Toggle 3: Ping Stabilizer Boost
                    RedMagicToggleRow(
                        title = "📶 تسريع وتثبيت البنج (Ping Stabilizer)",
                        subtitle = "تحسين بروتوكولات TCP وسيرفرات الألعاب",
                        isChecked = true,
                        activeColor = RedMagicEmerald,
                        onCheckedChange = { _ ->
                            SoundEffectsManager.playRootCommandSound()
                            scope.launch {
                                RedMagicGameState.statusText = RootDeviceController.boostNetworkPingViaRoot()
                                Toast.makeText(context, "🚀 تم تثبيت البنج وتفعيل تسريع الشبكة!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )

                    // Toggle 4: Crosshair Aim Target (شبيكة تصويب الألعاب)
                    RedMagicToggleRow(
                        title = "🎯 شبيكة التصويب الذكية (Crosshair Aim)",
                        subtitle = "إظهار نقطة تصويب دقيقة لألعاب الباتل رويال",
                        isChecked = RedMagicGameState.isCrosshairEnabled,
                        activeColor = RedMagicCrimson,
                        onCheckedChange = { active ->
                            SoundEffectsManager.playRootCommandSound()
                            RedMagicGameState.isCrosshairEnabled = active
                            RedMagicGameState.statusText = if (active) "🎯 تم تفعيل شبيكة التصويب الخارقة!" else "تم إيقاف شبيكة التصويب."
                        }
                    )

                    // Toggle 5: AI Auto-Trigger Shoot Bot via Root Shell
                    RedMagicToggleRow(
                        title = "⚡🎯 البوت الذكي للإطلاق التلقائي (AI Auto-Trigger)",
                        subtitle = "إطلاق النار الفوري تلقائياً فور التعرف على العدو بالروت",
                        isChecked = RedMagicGameState.isAutoTriggerEnabled,
                        activeColor = RedMagicEmerald,
                        onCheckedChange = { active ->
                            SoundEffectsManager.playRootCommandSound()
                            RedMagicGameState.isAutoTriggerEnabled = active
                            scope.launch {
                                RedMagicGameState.statusText = RootDeviceController.toggleAutoTriggerAiBot(active)
                            }
                        }
                    )

                    // Toggle 6: Zero-Recoil Hardware Touch Lock
                    RedMagicToggleRow(
                        title = "🎯🔥 تثبيت الأيم الجذري (Zero-Recoil Aim)",
                        subtitle = "قفل الارتداد وتثبيت التصويب عبر تعريف Touch Kernel",
                        isChecked = RedMagicGameState.isZeroRecoilActive,
                        activeColor = RedMagicCrimson,
                        onCheckedChange = { active ->
                            SoundEffectsManager.playRootCommandSound()
                            RedMagicGameState.isZeroRecoilActive = active
                            scope.launch {
                                RedMagicGameState.statusText = RootDeviceController.toggleZeroRecoilAimLock(active)
                            }
                        }
                    )

                    // Toggle 7: GPU Vulkan Pipeline Engine
                    RedMagicToggleRow(
                        title = "🚀🎮 محرك الرسوميات الخارق (Vulkan Engine)",
                        subtitle = "إجبار بروتوكول Vulkan للوصول لأعلى فريمات واستجابة",
                        isChecked = RedMagicGameState.isVulkanGpuActive,
                        activeColor = RedMagicCyan,
                        onCheckedChange = { active ->
                            SoundEffectsManager.playRootCommandSound()
                            RedMagicGameState.isVulkanGpuActive = active
                            scope.launch {
                                RedMagicGameState.statusText = RootDeviceController.toggleVulkanGpuBoost(active)
                            }
                        }
                    )

                    // Toggle 8: 3D Spatial Audio & Enemy Footstep Enhancer
                    RedMagicToggleRow(
                        title = "🎧🔊 مضخم خطوات الأعداء (3D Spatial Audio)",
                        subtitle = "تضخيم ترددات المشي والأصوات المحيطية بالقتال",
                        isChecked = RedMagicGameState.is3dFootstepAudioActive,
                        activeColor = Color(0xFFF59E0B),
                        onCheckedChange = { active ->
                            SoundEffectsManager.playRootCommandSound()
                            RedMagicGameState.is3dFootstepAudioActive = active
                            scope.launch {
                                RedMagicGameState.statusText = RootDeviceController.toggleSpatialFootstepAudio(active)
                            }
                        }
                    )

                    // Toggle 6: Do Not Disturb Block Notifications
                    RedMagicToggleRow(
                        title = "🔕 حظر الإشعارات والمكالمات (DND Mode)",
                        subtitle = "منع الإزعاج المنبثق أثناء القتال",
                        isChecked = RedMagicGameState.isDndEnabled,
                        activeColor = Color(0xFFA855F7),
                        onCheckedChange = { active ->
                            SoundEffectsManager.playRootCommandSound()
                            RedMagicGameState.isDndEnabled = active
                            scope.launch {
                                RedMagicGameState.statusText = RootDeviceController.toggleDoNotDisturbViaRoot(active)
                            }
                        }
                    )

                    // Toggle 7: Max Brightness Lock
                    RedMagicToggleRow(
                        title = "☀️ قفل السطوع الأقصى (Max Brightness)",
                        subtitle = "تثبيت أعلى إضاءة ومنع التعتيم التلقائي",
                        isChecked = RedMagicGameState.isMaxBrightnessEnabled,
                        activeColor = Color(0xFFF59E0B),
                        onCheckedChange = { active ->
                            SoundEffectsManager.playRootCommandSound()
                            RedMagicGameState.isMaxBrightnessEnabled = active
                            scope.launch {
                                RedMagicGameState.statusText = RootDeviceController.setMaxBrightnessViaRoot(active)
                            }
                        }
                    )

                    // Toggle 8: AI Touch 1000Hz Boost
                    RedMagicToggleRow(
                        title = "🤖 الذكاء الاصطناعي للاستجابة (Touch 1000Hz)",
                        subtitle = "استجابة فائقة السرعة للمس في اللعب",
                        isChecked = RedMagicGameState.isAiTouchBoost,
                        activeColor = RedMagicEmerald,
                        onCheckedChange = { active ->
                            SoundEffectsManager.playRootCommandSound()
                            RedMagicGameState.isAiTouchBoost = active
                            scope.launch {
                                RootDeviceController.executeCommand("su -c setprop debug.touch.latency 0")
                                RedMagicGameState.statusText = "🤖 تم ضبط اللمس الفائق 1000Hz عبر AI!"
                            }
                        }
                    )

                    // Transparency / Opacity Slider Control
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "👁️ شفافية الواجهة والفقاعة",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                color = Color.White
                            )
                            Text(
                                text = "${(RedMagicGameState.hudOpacity * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold, fontSize = 10.sp),
                                color = RedMagicCyan
                            )
                        }
                        Slider(
                            value = RedMagicGameState.hudOpacity,
                            onValueChange = { RedMagicGameState.hudOpacity = it },
                            valueRange = 0.25f..1.0f,
                            colors = SliderDefaults.colors(
                                thumbColor = RedMagicCyan,
                                activeTrackColor = RedMagicCyan,
                                inactiveTrackColor = Color(0xFF334155)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(22.dp)
                        )
                    }
                }

                // Quick Action Buttons Footer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Clean RAM
                    Button(
                        onClick = {
                            SoundEffectsManager.playRootCommandSound()
                            scope.launch {
                                val msg = RootDeviceController.freezeBackgroundAppsViaRoot()
                                RedMagicGameState.statusText = msg
                                Toast.makeText(context, "🧹 تم تنظيف الذاكرة!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CleaningServices,
                            contentDescription = "Clean RAM",
                            tint = RedMagicCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "تنظيف الذاكرة",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp),
                            color = Color.White
                        )
                    }

                    // Open Main App
                    Button(
                        onClick = {
                            SoundEffectsManager.playClick()
                            onOpenMainApp()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RedMagicCrimson),
                        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Launch,
                            contentDescription = "Open App",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "فتح التطبيق",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp),
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Crosshair HUD Target Overlay if enabled
        if (RedMagicGameState.isCrosshairEnabled) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center)
                    .border(1.5.dp, RedMagicCrimson, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(RedMagicCyan, CircleShape)
                )
            }
        }
    }
}

@Composable
private fun RedMagicStatChip(
    title: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = RedMagicCardBg,
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.sp, fontWeight = FontWeight.Bold),
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.ExtraBold),
                color = accentColor
            )
        }
    }
}

@Composable
private fun RedMagicToggleRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    activeColor: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                color = Color.White
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                color = Color.Gray
            )
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = activeColor,
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.Black
            ),
            modifier = Modifier.scale(0.7f)
        )
    }
}
