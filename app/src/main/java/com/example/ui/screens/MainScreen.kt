package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Widgets
import com.example.ui.components.AppCard
import com.example.ui.components.AppDetailSheet
import com.example.ui.components.DownloadsSheet
import com.example.ui.components.FeaturesHubDialog
import com.example.ui.components.MiniGamesDialog
import com.example.ui.components.PublishAppDialog
import com.example.ui.components.RootWarningDialog
import com.example.ui.components.SettingsDialog
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.VioletSecondary
import com.example.ui.viewmodel.CommunityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: CommunityViewModel
) {
    val context = LocalContext.current

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedCountry by viewModel.selectedCountry.collectAsStateWithLifecycle()
    val apps by viewModel.apps.collectAsStateWithLifecycle()
    val downloads by viewModel.downloads.collectAsStateWithLifecycle()

    val selectedAppForDetails by viewModel.selectedAppForDetails.collectAsStateWithLifecycle()
    val selectedAppReviews by viewModel.selectedAppReviews.collectAsStateWithLifecycle()

    val showPublishSheet by viewModel.showPublishSheet.collectAsStateWithLifecycle()
    val showDownloadsSheet by viewModel.showDownloadsSheet.collectAsStateWithLifecycle()
    val showAiAssistantSheet by viewModel.showAiAssistantSheet.collectAsStateWithLifecycle()
    val showRootWarningDialog by viewModel.showRootWarningDialog.collectAsStateWithLifecycle()
    val showSettingsSheet by viewModel.showSettingsSheet.collectAsStateWithLifecycle()
    val showGamesSheet by viewModel.showGamesSheet.collectAsStateWithLifecycle()
    val showFeaturesHubSheet by viewModel.showFeaturesHubSheet.collectAsStateWithLifecycle()
    val showMetaVoiceDialog by viewModel.showMetaVoiceDialog.collectAsStateWithLifecycle()
    val showTerminalDialog by viewModel.showTerminalDialog.collectAsStateWithLifecycle()
    val showTerminalLearningDialog by viewModel.showTerminalLearningDialog.collectAsStateWithLifecycle()
    val showCustomizationHubDialog by viewModel.showCustomizationHubDialog.collectAsStateWithLifecycle()
    val showOpenSourceCodeDialog by viewModel.showOpenSourceCodeDialog.collectAsStateWithLifecycle()
    val showLiveStreamingEngineDialog by viewModel.showLiveStreamingEngineDialog.collectAsStateWithLifecycle()
    val selectedAppForStreaming by viewModel.selectedAppForStreaming.collectAsStateWithLifecycle()
    val isUltraGamingModeActive by viewModel.isUltraGamingModeActive.collectAsStateWithLifecycle()
    val showGameHudDialog by viewModel.showGameHudDialog.collectAsStateWithLifecycle()
    val lastMetaVoiceResponse by viewModel.lastMetaVoiceResponse.collectAsStateWithLifecycle()
    val aiChatMessages by viewModel.aiChatMessages.collectAsStateWithLifecycle()
    val isAiThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()
    val isHighThinkingMode by viewModel.isHighThinkingMode.collectAsStateWithLifecycle()
    val isGeneratingVeoVideo by viewModel.isGeneratingVeoVideo.collectAsStateWithLifecycle()
    val veoVideoResult by viewModel.veoVideoResult.collectAsStateWithLifecycle()

    val categories = listOf("الكل", "أدوات", "إنتاجية", "ألعاب", "وسائط", "خدمات")
    val countriesFilter = listOf(
        "🌐 جميع الدول",
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
        "🇱🇧 لبنان"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.navigationBarsPadding()
            ) {
                FloatingActionButton(
                    onClick = { viewModel.toggleLiveStreamingEngineDialog(true) },
                    containerColor = Color(0xFFEF4444),
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.LiveTv,
                        contentDescription = "Live Streaming"
                    )
                }

                FloatingActionButton(
                    onClick = { viewModel.toggleTerminalDialog(true) },
                    containerColor = Color(0xFF10B981),
                    contentColor = Color.Black,
                    shape = CircleShape
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Terminal,
                            contentDescription = "RedScript Terminal"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "ترمنال 💻",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                FloatingActionButton(
                    onClick = { viewModel.toggleCustomizationHubDialog(true) },
                    containerColor = Color(0xFF7C3AED),
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.AutoAwesome,
                        contentDescription = "Customization"
                    )
                }

                FloatingActionButton(
                    onClick = { viewModel.toggleMetaVoiceDialog(true) },
                    containerColor = com.example.ui.theme.CyanAccent,
                    contentColor = Color(0xFF0F172A),
                    shape = CircleShape,
                    modifier = Modifier.testTag("meta_voice_fab")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.AutoAwesome,
                            contentDescription = "Meta Voice AI"
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ميتا AI 🎙️",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                FloatingActionButton(
                    onClick = { viewModel.togglePublishSheet(true) },
                    containerColor = IndigoPrimary,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.testTag("publish_fab")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Publish App"
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "نشر تطبيق",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                // Top Hero Banner Section
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_hero_banner_1786120241979),
                            contentDescription = "Hero Banner",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Dark Gradient Overlay for readability
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.4f),
                                            Color.Black.copy(alpha = 0.85f)
                                        )
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .statusBarsPadding()
                                .padding(horizontal = 20.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Top Bar inside Hero
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Storefront,
                                        contentDescription = "Logo",
                                        tint = CyanAccent,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "Community App Hub",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        )
                                        Text(
                                            text = "منصة التطبيقات المجتمعية المفتوحة",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color.White.copy(alpha = 0.8f)
                                            )
                                        )
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    IconButton(
                                        onClick = { viewModel.toggleGameHudDialog(true) },
                                        modifier = Modifier
                                            .background(
                                                Brush.linearGradient(
                                                    listOf(Color(0xFFEC4899), Color(0xFF8B5CF6))
                                                ),
                                                CircleShape
                                            )
                                            .testTag("ultra_game_mode_header_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.SportsEsports,
                                            contentDescription = "Ultra Game Turbo HUD",
                                            tint = Color.White
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.toggleFeaturesHubSheet(true) },
                                        modifier = Modifier
                                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                            .testTag("features_hub_header_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Widgets,
                                            contentDescription = "1000 Features Hub",
                                            tint = Color.White
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.toggleGamesSheet(true) },
                                        modifier = Modifier
                                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                            .testTag("games_header_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.SportsEsports,
                                            contentDescription = "Mini Games Hub",
                                            tint = Color.White
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.toggleSettingsSheet(true) },
                                        modifier = Modifier
                                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                            .testTag("settings_header_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Settings,
                                            contentDescription = "Settings and Root",
                                            tint = Color.White
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.toggleAiAssistantSheet(true) },
                                        modifier = Modifier
                                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                            .testTag("ai_assistant_header_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = "AI Assistant Web Search",
                                            tint = Color.White
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            viewModel.syncCloudFeed()
                                            Toast.makeText(context, "جاري تحديث ومزامنة المتجر المجتمعي...", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier
                                            .background(Color.White.copy(alpha = 0.15f), CircleShape)
                                            .testTag("sync_cloud_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CloudUpload,
                                            contentDescription = "Sync Cloud",
                                            tint = Color.White
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.toggleDownloadsSheet(true) },
                                        modifier = Modifier
                                            .background(Color.White.copy(alpha = 0.15f), CircleShape)
                                            .testTag("downloads_header_button")
                                    ) {
                                        BadgedBox(
                                            badge = {
                                                if (downloads.isNotEmpty()) {
                                                    Badge { Text("${downloads.size}") }
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.DownloadForOffline,
                                                contentDescription = "Downloads",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }
                            }

                            // Community Stats Bar inside Hero
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${apps.size}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = CyanAccent
                                        )
                                    )
                                    Text(
                                        text = "تطبيق مرفوع",
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.9f))
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(1.dp)
                                        .background(Color.White.copy(alpha = 0.2f))
                                )

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    val totalDownloads = apps.sumOf { it.downloadsCount }
                                    Text(
                                        text = "$totalDownloads+",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    )
                                    Text(
                                        text = "تحميل مجتمعي",
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.9f))
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(1.dp)
                                        .background(Color.White.copy(alpha = 0.2f))
                                )

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "مفتوح للجميع",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF10B981)
                                        )
                                    )
                                    Text(
                                        text = "رفع وتنزيل فوراً",
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.9f))
                                    )
                                }
                            }
                        }
                    }
                }

                // Search Bar Section
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.onSearchQueryChanged(it, context) },
                            placeholder = { Text("ابحث أو انطق 'ميتا' للفتح والصوت...") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            trailingIcon = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { viewModel.onSearchQueryChanged("", context) }) {
                                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search")
                                        }
                                    }
                                    IconButton(
                                        onClick = { viewModel.toggleMetaVoiceDialog(true) },
                                        modifier = Modifier.testTag("search_voice_icon")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Mic,
                                            contentDescription = "Meta Voice",
                                            tint = com.example.ui.theme.IndigoPrimary
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("search_input_field"),
                            shape = RoundedCornerShape(28.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = com.example.ui.theme.LightPillBackground,
                                unfocusedContainerColor = com.example.ui.theme.LightPillBackground,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // AI Web Assistant Banner Card
                        Card(
                            onClick = { viewModel.toggleAiAssistantSheet(true) },
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ai_assistant_banner")
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                IndigoPrimary.copy(alpha = 0.12f),
                                                VioletSecondary.copy(alpha = 0.18f)
                                            )
                                        )
                                    )
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .background(
                                                Brush.linearGradient(
                                                    listOf(IndigoPrimary, CyanAccent)
                                                ),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = "AI Assistant",
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "مساعد الذكاء الاصطناعي والتحكم بنظام Root ⚡",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = IndigoPrimary
                                        )
                                        Text(
                                            text = "بحث بالويب، توليد ردود صوتیة، والتحكم بالهاتف ونظام Root مباشرةً!",
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Surface(
                                        color = IndigoPrimary,
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = "اسأل الآن",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            ),
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Categories Horizontal Scroll
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(categories) { category ->
                                val isSelected = selectedCategory == category
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.onCategorySelected(category) },
                                    label = {
                                        Text(
                                            text = category,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    leadingIcon = if (isSelected) {
                                        {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Selected",
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    } else null,
                                    shape = RoundedCornerShape(20.dp),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = com.example.ui.theme.SoftContainerBlue,
                                        selectedLabelColor = com.example.ui.theme.VibrantBlueDark,
                                        selectedLeadingIconColor = com.example.ui.theme.VibrantBlueDark,
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = isSelected,
                                        borderColor = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Countries Horizontal Scroll
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(countriesFilter) { country ->
                                val isSelected = selectedCountry == country
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.onCountrySelected(country) },
                                    label = {
                                        Text(
                                            text = country,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 11.sp
                                            )
                                        )
                                    },
                                    leadingIcon = if (isSelected) {
                                        {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Selected",
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    } else null,
                                    shape = RoundedCornerShape(16.dp),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = IndigoPrimary.copy(alpha = 0.15f),
                                        selectedLabelColor = IndigoPrimary,
                                        selectedLeadingIconColor = IndigoPrimary,
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }

                // Community Apps Stream Heading
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedCategory == "الكل") "تطبيقات المجتمع المتاحة" else "فئة: $selectedCategory",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )

                        Text(
                            text = "${apps.size} تطبيق",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Apps List or Empty State
                if (apps.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Apps,
                                    contentDescription = "Empty",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(54.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "لم نجد نتائج مطابقة لبحثك",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "جرب تغيير كلمة البحث أو انشر تطبيقك الخاص ليصل لجميع مستخدمي المنصة!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(
                        items = apps,
                        key = { it.id }
                    ) { app ->
                        AppCard(
                            app = app,
                            onAppClick = { viewModel.selectApp(app) },
                            onDownloadClick = { viewModel.startDownload(context, app) },
                            onLikeClick = { viewModel.likeApp(app.id) },
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Sheets & Dialogs
            if (showPublishSheet) {
                PublishAppDialog(
                    onDismiss = { viewModel.togglePublishSheet(false) },
                    onPublish = { title, publisher, desc, cat, country, url, ver, sz, iconKey ->
                        viewModel.publishTitle.value = title
                        viewModel.publishPublisher.value = publisher
                        viewModel.publishDescription.value = desc
                        viewModel.publishCategory.value = cat
                        viewModel.publishCountry.value = country
                        viewModel.publishDownloadUrl.value = url
                        viewModel.publishVersion.value = ver
                        viewModel.publishSize.value = sz
                        viewModel.publishIconRes.value = iconKey

                        viewModel.publishApp(
                            onSuccess = {
                                Toast.makeText(context, "تم نشر تطبيقك بنجاح على المنصة!", Toast.LENGTH_LONG).show()
                            },
                            onError = { err ->
                                Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                )
            }

            selectedAppForDetails?.let { app ->
                AppDetailSheet(
                    app = app,
                    reviews = selectedAppReviews,
                    onDismiss = { viewModel.selectApp(null) },
                    onDownloadClick = { viewModel.startDownload(context, app) },
                    onLikeClick = { viewModel.likeApp(app.id) },
                    onSubmitReview = { rating, comment ->
                        viewModel.submitReview(app.id, "مستخدم مجتمعي", rating, comment)
                    }
                )
            }

            if (showDownloadsSheet) {
                DownloadsSheet(
                    downloads = downloads,
                    onDismiss = { viewModel.toggleDownloadsSheet(false) },
                    onInstallClick = { download ->
                        viewModel.installApp(context, download)
                    }
                )
            }

            if (showAiAssistantSheet) {
                com.example.ui.components.AiAssistantSheet(
                    messages = aiChatMessages,
                    isThinking = isAiThinking,
                    onSendMessage = { prompt -> viewModel.sendAiPrompt(prompt, context) },
                    onRequestRootPermission = { viewModel.toggleRootWarningDialog(true) },
                    onDismiss = { viewModel.toggleAiAssistantSheet(false) },
                    onDownloadApp = { app ->
                        viewModel.startDownload(context, app)
                        viewModel.toggleDownloadsSheet(true)
                    },
                    onOpenMetaVoice = { viewModel.toggleMetaVoiceDialog(true) },
                    onOpenTerminal = {
                        viewModel.toggleAiAssistantSheet(false)
                        viewModel.toggleTerminalDialog(true)
                    },
                    onOpenCustomization = {
                        viewModel.toggleAiAssistantSheet(false)
                        viewModel.toggleCustomizationHubDialog(true)
                    }
                )
            }

            if (showTerminalDialog) {
                com.example.ui.components.RedScriptTerminalDialog(
                    onDismiss = { viewModel.toggleTerminalDialog(false) },
                    onPublishScriptToStore = { scriptApp ->
                        viewModel.publishDirectApp(scriptApp)
                    }
                )
            }

            if (showCustomizationHubDialog) {
                com.example.ui.components.CustomizationHubDialog(
                    onDismiss = { viewModel.toggleCustomizationHubDialog(false) }
                )
            }

            if (showOpenSourceCodeDialog) {
                com.example.ui.components.OpenSourceCodeDialog(
                    onDismiss = { viewModel.toggleOpenSourceCodeDialog(false) }
                )
            }

            if (showLiveStreamingEngineDialog) {
                com.example.ui.components.LiveStreamingEngineDialog(
                    initialApp = selectedAppForStreaming,
                    availableApps = apps,
                    onDismiss = { viewModel.toggleLiveStreamingEngineDialog(false) },
                    onPublishUpdatedApp = { updatedApp ->
                        viewModel.publishDirectApp(updatedApp)
                    }
                )
            }

            if (showTerminalLearningDialog) {
                com.example.ui.components.TerminalLearningDialog(
                    onDismiss = { viewModel.toggleTerminalLearningDialog(false) },
                    onOpenFullTerminal = {
                        viewModel.toggleTerminalLearningDialog(false)
                        viewModel.toggleTerminalDialog(true)
                    }
                )
            }

            if (showMetaVoiceDialog) {
                com.example.ui.components.MetaVoiceDialog(
                    lastAiResponseText = lastMetaVoiceResponse,
                    isAiThinking = isAiThinking,
                    isHighThinkingMode = isHighThinkingMode,
                    onToggleHighThinking = { viewModel.toggleHighThinkingMode(it) },
                    onSendVoicePrompt = { prompt -> viewModel.sendMetaVoicePrompt(prompt, context) },
                    onGenerateVeoVideo = { prompt, ratio -> viewModel.generateVeoVideo(prompt, ratio) },
                    isGeneratingVeoVideo = isGeneratingVeoVideo,
                    veoVideoResult = veoVideoResult,
                    onTranscribeAudio = { bytes, mime -> viewModel.transcribeAudio(bytes, mime) },
                    onDismiss = { viewModel.toggleMetaVoiceDialog(false) }
                )
            }

            if (showSettingsSheet) {
                SettingsDialog(
                    onDismiss = { viewModel.toggleSettingsSheet(false) },
                    onRequestRootPermission = { viewModel.toggleRootWarningDialog(true) },
                    onOpenGames = {
                        viewModel.toggleSettingsSheet(false)
                        viewModel.toggleGamesSheet(true)
                    },
                    onOpenOpenSourceCode = {
                        viewModel.toggleSettingsSheet(false)
                        viewModel.toggleOpenSourceCodeDialog(true)
                    },
                    onOpenLiveStreamingEngine = {
                        viewModel.toggleSettingsSheet(false)
                        viewModel.toggleLiveStreamingEngineDialog(true)
                    },
                    onOpenTerminalLearning = {
                        viewModel.toggleSettingsSheet(false)
                        viewModel.toggleTerminalLearningDialog(true)
                    }
                )
            }

            if (showGamesSheet) {
                MiniGamesDialog(
                    onDismiss = { viewModel.toggleGamesSheet(false) }
                )
            }

            if (showFeaturesHubSheet) {
                FeaturesHubDialog(
                    onDismiss = { viewModel.toggleFeaturesHubSheet(false) },
                    onExecuteFeatureCommand = { cmdName ->
                        Toast.makeText(context, "تم تشغيل $cmdName بنجاح!", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            if (showRootWarningDialog) {
                RootWarningDialog(
                    onAccept = {
                        viewModel.toggleRootWarningDialog(false)
                        Toast.makeText(
                            context,
                            "تم تفعيل صلاحية SuperSU للذكاء الاصطناعي بنجاح",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    onDismiss = {
                        viewModel.toggleRootWarningDialog(false)
                        Toast.makeText(
                            context,
                            "تم الشغيل بالوضع الآمن بدون صلاحيات Root العالية",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }

            // Floating Ultra Gaming Bubble Overlay
            com.example.ui.components.UltraGameBubbleOverlay(
                isUltraGamingModeActive = isUltraGamingModeActive,
                currentFps = 120,
                onClickBubble = { viewModel.toggleGameHudDialog(true) }
            )

            // Ultra Gaming HUD Dialog
            if (showGameHudDialog) {
                com.example.ui.components.UltraGamingHudDialog(
                    isUltraGamingActive = isUltraGamingModeActive,
                    onToggleUltraGaming = { viewModel.toggleUltraGamingMode(it) },
                    onOpenMetaVoice = { viewModel.toggleMetaVoiceDialog(true) },
                    onDismiss = { viewModel.toggleGameHudDialog(false) }
                )
            }
        }
    }
}
