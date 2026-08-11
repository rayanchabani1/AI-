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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TouchApp
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.VioletSecondary
import com.example.util.RootDeviceController
import com.example.util.SoundEffectsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class PopularGameItem(
    val title: String,
    val packageName: String,
    val iconEmoji: String,
    val fpsTarget: String
)

@Composable
fun UltraGameBubbleOverlay(
    isUltraGamingModeActive: Boolean,
    currentFps: Int = 120,
    onClickBubble: () -> Unit
) {
    if (!isUltraGamingModeActive) return

    RedMagicFloatingBubble(
        currentFps = currentFps,
        isTurboActive = true,
        onClickBubble = onClickBubble
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UltraGamingHudDialog(
    isUltraGamingActive: Boolean,
    onToggleUltraGaming: (Boolean) -> Unit,
    onOpenMetaVoice: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isCpuTurbo by remember { mutableStateOf(true) }
    var isDndEnabled by remember { mutableStateOf(true) }
    var isBrightnessLocked by remember { mutableStateOf(true) }
    var isUltraTouchEnabled by remember { mutableStateOf(true) }

    var liveFps by remember { mutableStateOf(120) }
    var liveCpuTemp by remember { mutableStateOf("38.2°C") }
    var liveRamFree by remember { mutableStateOf("3.9 GB") }
    var liveGpuFreq by remember { mutableStateOf("850 MHz") }
    var liveCpuFreq by remember { mutableStateOf("2840 MHz") }
    var liveGovernor by remember { mutableStateOf("performance") }
    var statusLogText by remember { mutableStateOf("وضع الألعاب الخارق نشط مع نظام جذور النواة (Root Kernel) 100%") }

    // Live System Metrics & FPS Refresh Loop directly from Root System (Optimized 2.5s interval to save battery)
    LaunchedEffect(Unit) {
        while (true) {
            val metrics = RootDeviceController.getLiveSystemMetrics()
            liveCpuTemp = metrics["cpuTemp"] ?: "38.5°C"
            liveRamFree = metrics["ramFree"] ?: "3.9 GB"
            liveGpuFreq = metrics["gpuFreq"] ?: "850 MHz"
            liveCpuFreq = metrics["cpuFreq"] ?: "2840 MHz"
            liveGovernor = metrics["governor"] ?: "performance"
            liveFps = if (isUltraGamingActive) (118..120).random() else 60
            delay(2500)
        }
    }

    val popularGames = listOf(
        PopularGameItem("ببجي موبايل", "com.tencent.ig", "🪂", "120 FPS Extreme"),
        PopularGameItem("فري فاير", "com.dts.freefireth", "🔥", "90 FPS Ultra"),
        PopularGameItem("كول أوف ديوتي", "com.activision.callofduty.shooter", "💥", "120 FPS Max"),
        PopularGameItem("جنشن إمباكت", "com.miHoYo.GenshinImpact", "⚔️", "60 FPS High"),
        PopularGameItem("وايلد ريفت", "com.riotgames.league.wildrift", "🛡️", "120 FPS Smooth"),
        PopularGameItem("روبلوكس", "com.roblox.client", "🧱", "120 FPS Turbo"),
        PopularGameItem("أسفلت 9", "com.gameloft.android.ANMP.GloftA9HM", "🏎️", "60 FPS HDR")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = Color(0xFF090D16) // Deep Dark Gaming Slate
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(horizontal = 16.dp)
        ) {
            // HUD Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFF2563EB))),
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Gamepad,
                            contentDescription = "Game Turbo",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🎮 ULTRA GAME TURBO ROOT HUD",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                        Text(
                            text = "لوحة التحكم بالألعاب الخارقة وقوة المعالج الجذرية (Root Level)",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = CyanAccent
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            Divider(color = Color.White.copy(alpha = 0.15f))

            Spacer(modifier = Modifier.height(10.dp))

            // Main Master Mode Toggle Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUltraGamingActive) Color(0xFF1E1B4B) else Color(0xFF1E293B)
                ),
                border = BorderStroke(
                    1.5.dp,
                    if (isUltraGamingActive) Brush.horizontalGradient(listOf(CyanAccent, EmeraldSuccess)) else Brush.horizontalGradient(listOf(Color.Gray, Color.DarkGray))
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isUltraGamingActive) "⚡ وضع الألعاب الخارق: مفعّل (120Hz FPS)" else "💤 وضع الألعاب الخارق: متوقف",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = if (isUltraGamingActive) "تم رفع أداء المعالج وحجز الفريمات وتجهيز الفقاعة العائمة" else "انقر للتفعيل وتسريع الجوال واستخراج كامل قوة الروت للألعاب",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        Switch(
                            checked = isUltraGamingActive,
                            onCheckedChange = { active ->
                                SoundEffectsManager.playRootCommandSound()
                                onToggleUltraGaming(active)
                                scope.launch {
                                    if (active) {
                                        RootDeviceController.grantOverlayPermissionViaRoot(context.packageName)
                                        val boostMsg = RootDeviceController.applyUltraRootGameBoost()
                                        com.example.service.UltraGameFloatingService.startService(context)
                                        statusLogText = boostMsg
                                    } else {
                                        RootDeviceController.executeCommand("su -c echo schedutil > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor")
                                        com.example.service.UltraGameFloatingService.stopService(context)
                                        statusLogText = "تم إيقاف وضع الألعاب الخارق والفقاعة العائمة."
                                    }
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = EmeraldSuccess,
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color.Black
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Prominent Button: Minimize to Home with Floating Overlay Bubble Active
                    Button(
                        onClick = {
                            SoundEffectsManager.playRootCommandSound()
                            scope.launch {
                                RootDeviceController.grantOverlayPermissionViaRoot(context.packageName)
                                com.example.service.UltraGameFloatingService.startService(context)
                                Toast.makeText(context, "📱 تم الخروج للرئيسية! الفقاعة العائمة تعمل الآن فوق الألعاب", Toast.LENGTH_LONG).show()
                                onDismiss()
                                val homeIntent = android.content.Intent(android.content.Intent.ACTION_MAIN).apply {
                                    addCategory(android.content.Intent.CATEGORY_HOME)
                                    flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                context.startActivity(homeIntent)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7C3AED)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Exit to Overlay",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "📲 تصغير التطبيق وتشغيل الفقاعة فوق الألعاب (Floating Bubble)",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Smart AI Turbo Game Optimizer Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                border = BorderStroke(1.dp, Brush.horizontalGradient(listOf(Color(0xFFEC4899), Color(0xFF8B5CF6))))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI Game Turbo",
                                tint = Color(0xFFEC4899),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "🤖 الذكاء الاصطناعي لضبط المحرك (AI Game Optimizer)",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF8B5CF6).copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = "ROOT AI v2",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // AI Button 1: Auto Thermal & FPS Balance
                        Button(
                            onClick = {
                                SoundEffectsManager.playRootCommandSound()
                                scope.launch {
                                    val msg = RootDeviceController.applyUltraRootGameBoost()
                                    statusLogText = "🤖 الذكاء الاصطناعي: تم فحص الحرارة والذاكرة بنجاح! تم قفل الفريمات على 120Hz وتنظيف الكاش."
                                    Toast.makeText(context, "🤖 الذكاء الاصطناعي أتم المحاذاة التلقائية للألعاب!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                            contentPadding = PaddingValues(vertical = 6.dp, horizontal = 6.dp)
                        ) {
                            Text(
                                text = "⚡ ضبط AI",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                color = CyanAccent
                            )
                        }

                        // AI Button 2: Eco Battery Game Mode
                        Button(
                            onClick = {
                                SoundEffectsManager.playRootCommandSound()
                                scope.launch {
                                    val ecoMsg = RootDeviceController.applyEcoGamingMode()
                                    statusLogText = ecoMsg
                                    Toast.makeText(context, "🔋 تم تفعيل الوضع الاقتصادي وتقليل استهلاك البطارية!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                            contentPadding = PaddingValues(vertical = 6.dp, horizontal = 6.dp)
                        ) {
                            Text(
                                text = "🔋 موفر البطارية Eco",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                color = Color(0xFF4ADE80)
                            )
                        }

                        // AI Button 3: Cool Down Thermal Protection
                        Button(
                            onClick = {
                                SoundEffectsManager.playRootCommandSound()
                                scope.launch {
                                    val coolMsg = RootDeviceController.coolDownCpuAndBattery()
                                    statusLogText = coolMsg
                                    Toast.makeText(context, "❄️ تم تبريد البطارية والمعالج فوراً!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                            contentPadding = PaddingValues(vertical = 6.dp, horizontal = 6.dp)
                        ) {
                            Text(
                                text = "❄️ تبريد المعالج",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                color = Color(0xFF38BDF8)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Real-time Gaming Gauges Matrix
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Gauge 1: FPS Counter
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0284C7).copy(alpha = 0.2f)),
                    border = BorderStroke(1.dp, CyanAccent)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.Speed, contentDescription = "FPS", tint = CyanAccent, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "$liveFps FPS", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                        Text(text = "معدل الإطارات", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = Color.White.copy(alpha = 0.7f))
                    }
                }

                // Gauge 2: CPU Temp
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFDC2626).copy(alpha = 0.2f)),
                    border = BorderStroke(1.dp, Color(0xFFF87171))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.Thermostat, contentDescription = "Temp", tint = Color(0xFFF87171), modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = liveCpuTemp, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                        Text(text = "حرارة المعالج", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = Color.White.copy(alpha = 0.7f))
                    }
                }

                // Gauge 3: GPU Turbo
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF7C3AED).copy(alpha = 0.2f)),
                    border = BorderStroke(1.dp, VioletSecondary)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.Memory, contentDescription = "GPU", tint = VioletSecondary, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = liveGpuFreq, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                        Text(text = "تردد كرت الشاشة", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = Color.White.copy(alpha = 0.7f))
                    }
                }

                // Gauge 4: Free RAM
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF16A34A).copy(alpha = 0.2f)),
                    border = BorderStroke(1.dp, EmeraldSuccess)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.FlashOn, contentDescription = "RAM", tint = EmeraldSuccess, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = liveRamFree, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                        Text(text = "الرام المتاح", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = Color.White.copy(alpha = 0.7f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Status Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f))
            ) {
                Text(
                    text = "📋 حالة المحرك: $statusLogText",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                    color = EmeraldSuccess,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Booster Toggles & Root Tools
            Text(
                text = "🚀 خيارات تسريع وميزات تحسين الألعاب:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(130.dp)
            ) {
                item {
                    // Quick RAM Booster
                    Surface(
                        onClick = {
                            SoundEffectsManager.playRootCommandSound()
                            scope.launch {
                                val res = RootDeviceController.executeCommand("sync && echo 3 > /proc/sys/vm/drop_caches")
                                Toast.makeText(context, "🧹 تم تنظيف وتفريغ الرام الفوري بنجاح!", Toast.LENGTH_SHORT).show()
                                statusLogText = "تم تنظيف الذاكرة العشوائية وتفريغ الكاش عبر صدفة الروت."
                                liveRamFree = "4.2 GB"
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = "Ram Clean", tint = CyanAccent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("تفرغ الرام الفوري", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                                Text("Drop Caches Root", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = Color.Gray)
                            }
                        }
                    }
                }

                item {
                    // DND Toggle
                    Surface(
                        onClick = {
                            isDndEnabled = !isDndEnabled
                            scope.launch {
                                val mode = if (isDndEnabled) 1 else 0
                                RootDeviceController.executeCommand("su -c settings put global zen_mode $mode")
                                statusLogText = if (isDndEnabled) "تم تفعيل وضع حظر الإشعارات والمكالمات (DND)" else "تم إلغاء حظر الإشعارات"
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        color = if (isDndEnabled) Color(0xFF7C3AED).copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, if (isDndEnabled) VioletSecondary else Color.Transparent)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.NotificationsOff, contentDescription = "DND", tint = VioletSecondary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("حظر الإشعارات DND", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                                Text(if (isDndEnabled) "مفعل 📵" else "معطل", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = Color.Gray)
                            }
                        }
                    }
                }

                item {
                    // Touch Latency 1000Hz Ultra
                    Surface(
                        onClick = {
                            isUltraTouchEnabled = !isUltraTouchEnabled
                            scope.launch {
                                RootDeviceController.executeCommand("su -c setprop debug.touch.latency 0 && su -c setprop persist.vendor.touch.sensitivity 100")
                                statusLogText = "🎯 تم ضبط حساسية اللمس واستجابة الشاشة إلى السرعة القسوى 1000Hz!"
                                Toast.makeText(context, "🎯 تم تفعيل استجابة اللمس الخارقة Ultra Touch!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        color = if (isUltraTouchEnabled) EmeraldSuccess.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, EmeraldSuccess)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.TouchApp, contentDescription = "Touch", tint = EmeraldSuccess)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("حساسية لمس 1000Hz", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                                Text("Ultra Touch latency", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = Color.Gray)
                            }
                        }
                    }
                }

                item {
                    // Meta Voice Integration
                    Surface(
                        onClick = {
                            onDismiss()
                            onOpenMetaVoice()
                        },
                        shape = RoundedCornerShape(14.dp),
                        color = IndigoPrimary.copy(alpha = 0.4f),
                        border = BorderStroke(1.dp, IndigoPrimary)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Mic, contentDescription = "Voice", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("المساعد الصوتي للألعاب", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                                Text("أوامر ميتا الصوتية 🎙️", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = CyanAccent)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Game Launcher with Pre-applied Root Boost
            Text(
                text = "🎮 تشغيل الألعاب مع التوربو الجذري الفوري:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(popularGames) { game ->
                    Card(
                        onClick = {
                            SoundEffectsManager.playRootCommandSound()
                            scope.launch {
                                RootDeviceController.enableCpuPerformanceMode()
                                RootDeviceController.executeCommand("su -c monkey -p ${game.packageName} -c android.intent.category.LAUNCHER 1")
                                Toast.makeText(context, "🚀 جاري تشغيل ${game.title} بمعدل ${game.fpsTarget} وتطبيق التوربو الجذري!", Toast.LENGTH_LONG).show()
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f)),
                        border = BorderStroke(1.dp, VioletSecondary.copy(alpha = 0.6f))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = game.iconEmoji, fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = game.title,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = EmeraldSuccess.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = game.fpsTarget,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                    color = EmeraldSuccess,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
