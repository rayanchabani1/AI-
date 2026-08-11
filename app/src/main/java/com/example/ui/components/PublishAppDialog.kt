package com.example.ui.components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishAppDialog(
    onDismiss: () -> Unit,
    onPublish: (
        title: String,
        publisher: String,
        description: String,
        category: String,
        country: String,
        downloadUrl: String,
        version: String,
        size: String,
        iconRes: String
    ) -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var title by remember { mutableStateOf("") }
    var publisher by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("أدوات") }
    var country by remember { mutableStateOf("🇸🇦 السعودية") }
    var downloadUrl by remember { mutableStateOf("") }
    var version by remember { mutableStateOf("1.0.0") }
    var fileSize by remember { mutableStateOf("8.5 MB") }
    var selectedIconRes by remember { mutableStateOf("ic_calc") }

    var isLocalUpload by remember { mutableStateOf(false) }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    val categories = listOf("أدوات", "إنتاجية", "ألعاب", "وسائط", "خدمات")
    val countriesList = listOf(
        "🇸🇦 السعودية",
        "🇪🇬 مصر",
        "🇩🇿 الجزائر",
        "🇲🇦 المغرب",
        "🇦🇪 الإمارات",
        "🇮🇶 العراق",
        "🇸🇾 سوريا",
        "🇯🇴 الأردن",
        "🇵🇸 فلسطين",
        "🇹🇳 تونس",
        "🇶🇦 قطر",
        "🇰🇼 الكويت",
        "🇴🇲 عمان",
        "🇧🇭 البحرين",
        "🇱🇧 لبنان",
        "🌐 عالمي / جميع الدول"
    )
    val iconOptions = listOf(
        Pair("ic_calc", "حاسبة"),
        Pair("ic_notes", "ملاحظات"),
        Pair("ic_game", "لعبة"),
        Pair("ic_cleaner", "منظف"),
        Pair("ic_music", "صوتيات"),
        Pair("ic_weather", "طقس"),
        Pair("ic_code", "برمجة")
    )

    // File picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedFileUri = uri
            downloadUrl = uri.toString()
            Toast.makeText(context, "تم اختيار ملف الـ APK بنجاح!", Toast.LENGTH_SHORT).show()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("publish_app_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = "Upload",
                        tint = IndigoPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "نشر تطبيق جديد للمجتمع",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_publish_sheet")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Public visibility banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(com.example.ui.theme.SoftContainerBlue, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CloudUpload,
                    contentDescription = "Cloud Public",
                    tint = com.example.ui.theme.VibrantBluePrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "🌐 النشر سحابي ومفتوح: سيظهر تطبيقك فوراً لجميع مستخدمي المنصة المفتوحة!",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = com.example.ui.theme.VibrantBlueDark
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dual Mode Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (!isLocalUpload) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { isLocalUpload = false }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = "Link",
                            tint = if (!isLocalUpload) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "رابط تحميل مباشر",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (!isLocalUpload) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isLocalUpload) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { isLocalUpload = true }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FolderOpen,
                            contentDescription = "File",
                            tint = if (isLocalUpload) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "رفع ملف APK محلي",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isLocalUpload) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input Fields
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("اسم التطبيق") },
                placeholder = { Text("مثال: حاسبة ذكية، لعبة سباق...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("publish_title_input"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = publisher,
                onValueChange = { publisher = it },
                label = { Text("اسم المطور / الناشر") },
                placeholder = { Text("اسمك أو اسم قناتك التقنية") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("publish_publisher_input"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("وصف التطبيق ومميزاته") },
                placeholder = { Text("اشرح ماذا يقدم تطبيقك للمستخدمين...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .testTag("publish_description_input"),
                shape = RoundedCornerShape(12.dp),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Category Chips
            Text(
                text = "اختر فئة التطبيق:",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat) },
                        leadingIcon = if (category == cat) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Country Selector
            Text(
                text = "اختر الدولة أو المنطقة المصدر:",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(countriesList) { cnt ->
                    FilterChip(
                        selected = country == cnt,
                        onClick = { country = cnt },
                        label = { Text(cnt) },
                        leadingIcon = if (country == cnt) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Icon Selector
            Text(
                text = "اختر أيقونة بصرية مناسبة:",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(iconOptions) { (iconKey, label) ->
                    val isSelected = selectedIconRes == iconKey
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { selectedIconRes = iconKey }
                    ) {
                        Box(
                            modifier = Modifier
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) IndigoPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .padding(4.dp)
                        ) {
                            AppIconView(
                                iconUrl = null,
                                iconResName = iconKey,
                                category = category,
                                size = 46.dp
                            )
                        }
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) IndigoPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Download URL / File Selection Section
            if (isLocalUpload) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (selectedFileUri != null) "تم إرفاق ملف APK" else "اختر ملف APK من جهازك",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = selectedFileUri?.path ?: "اضغط أدناه للوصول لذاكرة الهاتف واختيار الملف",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { filePickerLauncher.launch("application/vnd.android.package-archive") },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.FolderOpen, contentDescription = "Pick APK")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("تصفح ملفات APK")
                        }
                    }
                }
            } else {
                OutlinedTextField(
                    value = downloadUrl,
                    onValueChange = { downloadUrl = it },
                    label = { Text("رابط التحميل المباشر (APK / MediaFire / Drive)") },
                    placeholder = { Text("https://example.com/app.apk") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("publish_url_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = version,
                    onValueChange = { version = it },
                    label = { Text("رقم الإصدار") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = fileSize,
                    onValueChange = { fileSize = it },
                    label = { Text("حجم الملف") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = {
                    onPublish(
                        title,
                        publisher,
                        description,
                        category,
                        country,
                        downloadUrl,
                        version,
                        fileSize,
                        selectedIconRes
                    )
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_publish_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Publish", modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "نشر التطبيق فوراً للجميع",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
