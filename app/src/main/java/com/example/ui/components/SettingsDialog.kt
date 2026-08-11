package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.VioletSecondary
import com.example.util.RootDeviceController
import com.example.util.SoundEffectsManager
import kotlinx.coroutines.launch

import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.util.RootAuditManager

import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.School

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    onRequestRootPermission: () -> Unit,
    onOpenGames: () -> Unit,
    onOpenOpenSourceCode: () -> Unit = {},
    onOpenLiveStreamingEngine: () -> Unit = {},
    onOpenTerminalLearning: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var soundEnabled by remember { mutableStateOf(true) }
    var autoCleanCache by remember { mutableStateOf(true) }
    var highPerformanceMode by remember { mutableStateOf(false) }
    var rootStatusText by remember { mutableStateOf(RootDeviceController.getActivePermissionState().message) }
    var terminalInput by remember { mutableStateOf("id") }
    var rawTerminalOutput by remember { mutableStateOf("جاهز لتنفيذ الأوامر عبر su...") }

    val auditLogs by RootAuditManager.auditLogs.collectAsStateWithLifecycle()
    val isMonitoringActive by RootAuditManager.isMonitoringActive.collectAsStateWithLifecycle()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.background
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
                            .size(38.dp)
                            .background(IndigoPrimary.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = IndigoPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "إعدادات التطبيق ونظام Root",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "إدارة صلاحيات Root، الأداء والذكاء الاصطناعي",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Root Status Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD32F2F).copy(alpha = 0.08f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = Color(0xFFD32F2F)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "حالة نظام Root / SuperSU",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFFD32F2F)
                            )
                        }

                        Text(
                            text = rootStatusText,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Button(
                            onClick = {
                                SoundEffectsManager.playRootCommandSound()
                                scope.launch {
                                    val state = RootDeviceController.requestRootPermission()
                                    rootStatusText = state.message
                                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("طلب تفعيل صلاحيات Root المباشرة والتحقق (su)")
                        }
                    }
                }

                // Interactive Live Root Terminal UI Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Terminal,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "شاشة الشل المباشر والتحكم بالروتس (Interactive Terminal)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF4CAF50)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = terminalInput,
                                onValueChange = { terminalInput = it },
                                placeholder = { Text("أدخل أمر شل هنا مثل id أو getprop...", color = Color.Gray, fontSize = 12.sp) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true
                            )

                            Button(
                                onClick = {
                                    SoundEffectsManager.playClick()
                                    scope.launch {
                                        rawTerminalOutput = "جاري تنفيذ الأمر عبر Root shell..."
                                        val res = RootDeviceController.executeCommand(terminalInput)
                                        rawTerminalOutput = "[Root Exec: ${if (res.isRealRoot) "su (UID 0)" else "sh"}]\nExit Code OK: ${res.success}\n\nRAW OUTPUT:\n${res.output}"
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Run", tint = Color.Black)
                            }
                        }

                        // Raw output display box
                        Surface(
                            color = Color.Black,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Text(
                                    text = rawTerminalOutput,
                                    color = Color(0xFF00FF66),
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Normal)
                                )
                            }
                        }
                    }
                }

                // Root Shell Continuous Transparency Monitor Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = IndigoPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "خدمة مراقبة شل الروت والشفافية 🛡️",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Surface(
                                color = if (isMonitoringActive) Color(0xFF388E3C).copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (isMonitoringActive) "المراقبة نشطة Live" else "متوقف",
                                    color = if (isMonitoringActive) Color(0xFF388E3C) else Color.Gray,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "تقوم هذه الخدمة بمراقبة جدار أوامر su في النظام وتسجيل كل عملية ينفذها الذكاء الاصطناعي بشفافية كاملة لمنع الأوامر غير المصرح بها.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Display last 5 audit logs
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "سجل الأوامر المباشر (${auditLogs.size} سجل):",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = IndigoPrimary
                            )

                            if (auditLogs.isEmpty()) {
                                Text(
                                    text = "لا توجد سجلات روت حالياً.",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                auditLogs.take(4).forEach { log ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = log.timeFormatted,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                            color = IndigoPrimary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = log.command,
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                                maxLines = 1
                                            )
                                            Text(
                                                text = "${log.caller} • ${log.details}",
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                }
                            }
                        }
                    }
                }

                // Mini-Games Launcher Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = IndigoPrimary.copy(alpha = 0.12f)),
                    onClick = onOpenGames
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = IndigoPrimary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "الألعاب المصغرة والمولد الذكي 🎮",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = IndigoPrimary
                                )
                                Text(
                                    text = "مكتبة ألعاب تفاعلية متنوعة ومولد ألعاب بالذكاء الاصطناعي",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Text(
                            text = "افتح الأن >",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = IndigoPrimary
                        )
                    }
                }

                // Open Source Code Button Card (أوبين سورس كود)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEC4899).copy(alpha = 0.15f)),
                    onClick = {
                        SoundEffectsManager.playClick()
                        onOpenOpenSourceCode()
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = "Open Source Code",
                                tint = Color(0xFFEC4899)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "أوبين سورس كود 🔓",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color(0xFFEC4899)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = Color(0xFFEC4899).copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "محمي بكود سر",
                                            color = Color(0xFFEC4899),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "عرض وتصدير الكود المفتوح المصدر للغة RedScript ومحرك الروت",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Text(
                            text = "دخول >",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFEC4899)
                        )
                    }
                }

                // Live Streaming & Page Engine Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEF4444).copy(alpha = 0.15f)),
                    onClick = {
                        SoundEffectsManager.playClick()
                        onOpenLiveStreamingEngine()
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = Icons.Default.LiveTv,
                                contentDescription = "Live Streaming Engine",
                                tint = Color(0xFFEF4444)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "محرك البث وتطوير الصفحات 📡",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFFEF4444)
                                )
                                Text(
                                    text = "بث مباشر للتطبيقات المصنوعة وإضافة وتعديل صفحات جديدة حياً",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Text(
                            text = "افتح البث >",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFEF4444)
                        )
                    }
                }

                // Terminal Language Learning Tool Card (أداة تعلم لغة الترمنال)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF10B981).copy(alpha = 0.15f)),
                    onClick = {
                        SoundEffectsManager.playClick()
                        onOpenTerminalLearning()
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = "Terminal Learning Academy",
                                tint = Color(0xFF10B981)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "أداة تعلم لغة الترمنال والسكربتات 🎓",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF10B981)
                                )
                                Text(
                                    text = "دروس تفاعلية، مختبر الأوامر الحية، قاموس السكربتات واختبارات قياس المهارة",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Text(
                            text = "ابدأ التعلم >",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF10B981)
                        )
                    }
                }

                // Settings Toggles
                SettingItemToggle(
                    icon = Icons.Default.VolumeUp,
                    title = "مؤثرات الصوت والتفاعل",
                    subtitle = "تشغيل الأصوات عند ضغط الأزرار وأوامر الـ Root",
                    checked = soundEnabled,
                    onCheckedChange = { soundEnabled = it }
                )

                SettingItemToggle(
                    icon = Icons.Default.CleaningServices,
                    title = "تنظيف كاش النظام التلقائي",
                    subtitle = "تنظيف الذاكرة المؤقتة دورياً عبر صلاحية الجذر",
                    checked = autoCleanCache,
                    onCheckedChange = { autoCleanCache = it }
                )

                SettingItemToggle(
                    icon = Icons.Default.Speed,
                    title = "وضع الأداء العالي للمعالج (CPU Boost)",
                    subtitle = "تفعيل أقصى سرعة لنواة المعالج باستخدام Root",
                    checked = highPerformanceMode,
                    onCheckedChange = {
                        highPerformanceMode = it
                        if (it) {
                            scope.launch {
                                RootDeviceController.enableCpuPerformanceMode()
                                Toast.makeText(context, "تم تفعيل وضع الأداء الفائق للمعالج!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )

                // Quick Root Tools Row
                Text(
                    text = "أدوات Root سريعة وأوامر التشغيل المباشرة",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickToolChip(
                        icon = Icons.Default.CleaningServices,
                        label = "تنظيف الكاش",
                        modifier = Modifier.weight(1f)
                    ) {
                        scope.launch {
                            val res = RootDeviceController.clearSystemCache()
                            Toast.makeText(context, res.output, Toast.LENGTH_SHORT).show()
                        }
                    }

                    QuickToolChip(
                        icon = Icons.Default.RestartAlt,
                        label = "إعادة التشغيل",
                        modifier = Modifier.weight(1f)
                    ) {
                        scope.launch {
                            val res = RootDeviceController.rebootNormal()
                            Toast.makeText(context, res.output, Toast.LENGTH_SHORT).show()
                        }
                    }

                    QuickToolChip(
                        icon = Icons.Default.RestartAlt,
                        label = "Bootloader",
                        modifier = Modifier.weight(1f)
                    ) {
                        scope.launch {
                            val res = RootDeviceController.rebootBootloader()
                            Toast.makeText(context, res.output, Toast.LENGTH_SHORT).show()
                        }
                    }

                    QuickToolChip(
                        icon = Icons.Default.RestartAlt,
                        label = "Recovery",
                        modifier = Modifier.weight(1f)
                    ) {
                        scope.launch {
                            val res = RootDeviceController.rebootRecovery()
                            Toast.makeText(context, res.output, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingItemToggle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(imageVector = icon, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = IndigoPrimary)
        )
    }
}

@Composable
fun QuickToolChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
        }
    }
}
