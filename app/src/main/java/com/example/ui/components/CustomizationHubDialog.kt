package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Terminal
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.VioletSecondary
import com.example.util.SoundEffectsManager
import com.example.util.VoiceSpeechManager

data class AppCustomTheme(
    val id: String,
    val name: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val bgDarkColor: Color
)

object CustomizationState {
    var selectedTheme by mutableStateOf("RedMagic Crimson")
    var isSoundEffectsEnabled by mutableStateOf(true)
    var isVoiceEnabled by mutableStateOf(true)
    var voicePitch by mutableFloatStateOf(1.0f)
    var voiceSpeed by mutableFloatStateOf(1.0f)
    var terminalFontSize by mutableFloatStateOf(14f)
    var isTerminalMatrixRain by mutableStateOf(true)
    var terminalThemeName by mutableStateOf("Emerald Matrix")
    var isAutoExecuteKeywords by mutableStateOf(true)
    var crosshairColorHex by mutableStateOf("#00E5FF")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizationHubDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val themes = listOf(
        AppCustomTheme("redmagic", "RedMagic Crimson", Color(0xFFDC2626), Color(0xFF991B1B), Color(0xFF0F172A)),
        AppCustomTheme("cyber", "Cyber Neon", Color(0xFF00E5FF), Color(0xFF7C3AED), Color(0xFF090D16)),
        AppCustomTheme("matrix", "Emerald Matrix", Color(0xFF10B981), Color(0xFF059669), Color(0xFF022C22)),
        AppCustomTheme("gold", "Gold Sovereign", Color(0xFFF59E0B), Color(0xFFD97706), Color(0xFF1C1917)),
        AppCustomTheme("sunset", "Sunset Flame", Color(0xFFF97316), Color(0xFFEA580C), Color(0xFF18181B)),
        AppCustomTheme("amoled", "AMOLED Deep Black", Color(0xFF6366F1), Color(0xFF4F46E5), Color(0xFF000000))
    )

    val terminalThemes = listOf(
        "Emerald Matrix", "Cyber Neon", "RedMagic Crimson", "Sunset Amber", "Vaporwave Purple", "Hacker Monospace"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF0B0F19),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .padding(16.dp)
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
                            .size(38.dp)
                            .background(
                                Brush.linearGradient(listOf(Color(0xFF00E5FF), Color(0xFF7C3AED))),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "🎨 مركز التخصيص الشامل",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                            color = Color.White
                        )
                        Text(
                            text = "تخصيص الواجهات، ثيمات الترمنال، الصوت، والفقاعة",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = Color.Gray
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = Color.White.copy(alpha = 0.1f))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section 1: App & HUD Global Theme Selection
                item {
                    Text(
                        text = "1️⃣ ثيم التطبيق والفقاعة العائمة (Global Theme)",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                        color = CyanAccent
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(themes) { theme ->
                            val isSelected = CustomizationState.selectedTheme == theme.name
                            Card(
                                modifier = Modifier
                                    .width(125.dp)
                                    .clickable {
                                        SoundEffectsManager.playClick()
                                        CustomizationState.selectedTheme = theme.name
                                        Toast
                                            .makeText(context, "تم اختيار ثيم: ${theme.name}", Toast.LENGTH_SHORT)
                                            .show()
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = theme.bgDarkColor),
                                border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) theme.primaryColor else Color.White.copy(alpha = 0.15f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(
                                                Brush.linearGradient(listOf(theme.primaryColor, theme.secondaryColor)),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = theme.name,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                // Section 2: HUD Overlay & Crosshair Customization
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2E)),
                        border = BorderStroke(1.dp, Color(0xFF1E293B))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "2️⃣ تخصيص الفقاعة العائمة وشفافية الـ HUD",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                                color = Color(0xFF00E5FF)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "شفافية الواجهة والفقاعة", style = MaterialTheme.typography.bodySmall, color = Color.White)
                                Text(text = "${(RedMagicGameState.hudOpacity * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = CyanAccent)
                            }
                            Slider(
                                value = RedMagicGameState.hudOpacity,
                                onValueChange = { RedMagicGameState.hudOpacity = it },
                                valueRange = 0.25f..1.0f,
                                colors = SliderDefaults.colors(thumbColor = CyanAccent, activeTrackColor = CyanAccent)
                            )

                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "مؤثرات الصوت الميكانيكية المباشرة", style = MaterialTheme.typography.bodySmall, color = Color.White)
                                Switch(
                                    checked = CustomizationState.isSoundEffectsEnabled,
                                    onCheckedChange = { CustomizationState.isSoundEffectsEnabled = it }
                                )
                            }
                        }
                    }
                }

                // Section 3: Custom Scripting Language Terminal Theme & Font
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2E)),
                        border = BorderStroke(1.dp, Color(0xFF1E293B))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "3️⃣ تخصيص ترمنال لغة RedScript",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                                color = EmeraldSuccess
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "اختر ثيم الترمنال المفضل:", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(terminalThemes) { tName ->
                                    val sel = CustomizationState.terminalThemeName == tName
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (sel) EmeraldSuccess.copy(alpha = 0.2f) else Color(0xFF1E293B),
                                        border = BorderStroke(1.dp, if (sel) EmeraldSuccess else Color.Transparent),
                                        modifier = Modifier.clickable { CustomizationState.terminalThemeName = tName }
                                    ) {
                                        Text(
                                            text = tName,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = if (sel) EmeraldSuccess else Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "حجم خط الأكواد في الترمنال", style = MaterialTheme.typography.bodySmall, color = Color.White)
                                Text(text = "${CustomizationState.terminalFontSize.toInt()} sp", style = MaterialTheme.typography.labelSmall, color = EmeraldSuccess)
                            }
                            Slider(
                                value = CustomizationState.terminalFontSize,
                                onValueChange = { CustomizationState.terminalFontSize = it },
                                valueRange = 10f..22f,
                                colors = SliderDefaults.colors(thumbColor = EmeraldSuccess, activeTrackColor = EmeraldSuccess)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "تفعيل انيميشن خلفية Matrix Rain", style = MaterialTheme.typography.bodySmall, color = Color.White)
                                Switch(
                                    checked = CustomizationState.isTerminalMatrixRain,
                                    onCheckedChange = { CustomizationState.isTerminalMatrixRain = it }
                                )
                            }
                        }
                    }
                }

                // Section 4: AI Voice Assistant & 1000 Keyword Recognition Sensitivity
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2E)),
                        border = BorderStroke(1.dp, Color(0xFF1E293B))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "4️⃣ تخصيص المساعد الصوتي والتعرف على 1000 كلمة",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                                color = VioletSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "سرعة نطق المساعد الصوتي", style = MaterialTheme.typography.bodySmall, color = Color.White)
                                Text(text = "x${"%.1f".format(CustomizationState.voiceSpeed)}", style = MaterialTheme.typography.labelSmall, color = VioletSecondary)
                            }
                            Slider(
                                value = CustomizationState.voiceSpeed,
                                onValueChange = { CustomizationState.voiceSpeed = it },
                                valueRange = 0.5f..2.0f,
                                colors = SliderDefaults.colors(thumbColor = VioletSecondary, activeTrackColor = VioletSecondary)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "تحديات التعرف الآلي وتنفيد الأوامر فور رصد 1000 كلمة", style = MaterialTheme.typography.bodySmall, color = Color.White)
                                Switch(
                                    checked = CustomizationState.isAutoExecuteKeywords,
                                    onCheckedChange = { CustomizationState.isAutoExecuteKeywords = it }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    SoundEffectsManager.playRootCommandSound()
                    VoiceSpeechManager(context).speak("تم حفظ كافة إعدادات وتفضيلات التخصيص المخصصة بنجاح!")
                    Toast.makeText(context, "✅ تم حفظ تفضيلات التخصيص بنجاح!", Toast.LENGTH_SHORT).show()
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent)
            ) {
                Text(text = "💾 حفظ واغلاق التخصيص", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold), color = Color.Black)
            }
        }
    }
}
