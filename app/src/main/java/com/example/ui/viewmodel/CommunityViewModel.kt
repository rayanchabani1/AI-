package com.example.ui.viewmodel

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.AppEntity
import com.example.data.local.entity.DownloadEntity
import com.example.data.local.entity.ReviewEntity
import com.example.data.repository.AppRepository
import com.example.util.DownloadInstaller
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File

class CommunityViewModel(private val repository: AppRepository) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("الكل")
    val selectedCountry = MutableStateFlow("🌐 جميع الدول")
    val selectedAppForDetails = MutableStateFlow<AppEntity?>(null)

    val showPublishSheet = MutableStateFlow(false)
    val showDownloadsSheet = MutableStateFlow(false)
    val showAiAssistantSheet = MutableStateFlow(false)
    val showRootWarningDialog = MutableStateFlow(true)
    val showSettingsSheet = MutableStateFlow(false)
    val showGamesSheet = MutableStateFlow(false)
    val showFeaturesHubSheet = MutableStateFlow(false)

    val showMetaVoiceDialog = MutableStateFlow(false)
    val showTerminalDialog = MutableStateFlow(false)
    val showTerminalLearningDialog = MutableStateFlow(false)
    val showCustomizationHubDialog = MutableStateFlow(false)
    val showOpenSourceCodeDialog = MutableStateFlow(false)
    val showLiveStreamingEngineDialog = MutableStateFlow(false)
    val selectedAppForStreaming = MutableStateFlow<AppEntity?>(null)
    val lastMetaVoiceResponse = MutableStateFlow("")
    val isHighThinkingMode = MutableStateFlow(false)
    val isGeneratingVeoVideo = MutableStateFlow(false)
    val veoVideoResult = MutableStateFlow<com.example.data.remote.GeminiAssistantClient.VeoVideoResult?>(null)

    val isUltraGamingModeActive = MutableStateFlow(true)
    val showGameHudDialog = MutableStateFlow(false)

    fun toggleUltraGamingMode(active: Boolean) {
        isUltraGamingModeActive.value = active
    }

    fun toggleGameHudDialog(show: Boolean) {
        showGameHudDialog.value = show
    }

    fun toggleMetaVoiceDialog(show: Boolean) {
        showMetaVoiceDialog.value = show
    }

    fun toggleHighThinkingMode(enabled: Boolean) {
        isHighThinkingMode.value = enabled
    }

    fun generateVeoVideo(prompt: String, aspectRatio: String = "16:9", context: android.content.Context? = null) {
        if (prompt.isBlank()) return
        isGeneratingVeoVideo.value = true
        viewModelScope.launch {
            val result = com.example.data.remote.GeminiAssistantClient.generateVeoVideo(prompt, aspectRatio, context)
            veoVideoResult.value = result
            isGeneratingVeoVideo.value = false

            // Also post message to AI Chat history
            val videoMsg = com.example.data.remote.AiChatMessage(
                sender = com.example.data.remote.MessageSender.AI,
                text = result.message
            )
            aiChatMessages.value = aiChatMessages.value + videoMsg
        }
    }

    fun transcribeAudio(audioBytes: ByteArray, mimeType: String = "audio/wav", context: android.content.Context? = null, onResult: (String) -> Unit = {}) {
        isAiThinking.value = true
        viewModelScope.launch {
            val transcribedText = com.example.data.remote.GeminiAssistantClient.transcribeAudioWithGemini(
                audioBytes = audioBytes,
                mimeType = mimeType,
                context = context
            )
            isAiThinking.value = false
            onResult(transcribedText)
        }
    }

    fun onSearchQueryChanged(newQuery: String, context: android.content.Context? = null) {
        searchQuery.value = newQuery
        val lower = newQuery.lowercase().trim()
        if (lower.contains("ميتا") || lower.contains("meta")) {
            showMetaVoiceDialog.value = true
            if (lower.replace("ميتا", "").replace("meta", "").trim().isNotBlank()) {
                sendMetaVoicePrompt(newQuery, context)
            }
        }
    }

    fun sendMetaVoicePrompt(prompt: String, context: android.content.Context? = null) {
        if (prompt.isBlank()) return
        val cleanPrompt = prompt.replace("ميتا", "", ignoreCase = true).replace("meta", "", ignoreCase = true).trim().ifBlank { prompt }

        // Also add to main AI chat history
        sendAiPrompt(cleanPrompt, context)

        // Check if gaming mode trigger in prompt
        val lower = cleanPrompt.lowercase()
        if (lower.contains("العاب") || lower.contains("ألعاب") || lower.contains("وضع الالعاب") || lower.contains("فريمات") || lower.contains("fps") || lower.contains("توربو") || lower.contains("ببجي") || lower.contains("game")) {
            isUltraGamingModeActive.value = true
            showGameHudDialog.value = true
        }

        isAiThinking.value = true
        viewModelScope.launch {
            // Check Root Intent
            val rootResult = com.example.util.RootDeviceController.processConversationalRootIntent(cleanPrompt)
            if (rootResult != null) {
                com.example.util.SoundEffectsManager.playRootCommandSound()
                val responseText = "⚡ [ميتا AI - تحكم بالنظام والألعاب]\n${rootResult.output}"
                lastMetaVoiceResponse.value = responseText
                isAiThinking.value = false
            } else {
                val currentApps = apps.value
                val aiResponse = com.example.data.remote.GeminiAssistantClient.askGeminiWithSearch(
                    context = context,
                    prompt = cleanPrompt,
                    history = aiChatMessages.value,
                    availableStoreApps = currentApps,
                    isHighThinkingMode = isHighThinkingMode.value
                )
                lastMetaVoiceResponse.value = aiResponse.text
                isAiThinking.value = false
            }
        }
    }

    fun toggleRootWarningDialog(show: Boolean) {
        showRootWarningDialog.value = show
    }

    fun toggleSettingsSheet(show: Boolean) {
        showSettingsSheet.value = show
    }

    fun toggleGamesSheet(show: Boolean) {
        showGamesSheet.value = show
    }

    fun toggleFeaturesHubSheet(show: Boolean) {
        showFeaturesHubSheet.value = show
    }

    val aiChatMessages = MutableStateFlow<List<com.example.data.remote.AiChatMessage>>(
        listOf(
            com.example.data.remote.AiChatMessage(
                sender = com.example.data.remote.MessageSender.AI,
                text = "مرحباً بك! أنا مساعدك الذكي في المتجر المجتمعي ومعالج SuperSU للتحكم بالجهاز. أستطيع البحث في الويب للوصول للتطبيقات وتنفيذ أوامر نظام SuperSU وRoot بأمان!"
            )
        )
    )
    val isAiThinking = MutableStateFlow(false)

    fun toggleAiAssistantSheet(show: Boolean) {
        showAiAssistantSheet.value = show
    }

    fun sendAiPrompt(prompt: String, context: android.content.Context? = null) {
        if (prompt.isBlank()) return
        val currentList = aiChatMessages.value
        val userMsg = com.example.data.remote.AiChatMessage(
            sender = com.example.data.remote.MessageSender.USER,
            text = prompt
        )
        aiChatMessages.value = currentList + userMsg
        isAiThinking.value = true

        val lowerPrompt = prompt.lowercase().trim()

        viewModelScope.launch {
            // Check matching in 1000 keyword catalog
            val matchedKeywords = com.example.util.RedRootDictionary.matchInText(prompt)

            // First check if the prompt corresponds to a natural language Root control intent
            val rootIntentResult = com.example.util.RootDeviceController.processConversationalRootIntent(prompt)

            if (rootIntentResult != null) {
                com.example.util.SoundEffectsManager.playRootCommandSound()
                val kwInfo = if (matchedKeywords.isNotEmpty()) {
                    "\n\n📚 **[تم التعرف على ${matchedKeywords.size} كلمة من معجم الـ 1000 كلمة AI]:**\n" +
                            matchedKeywords.joinToString("\n") { "• #${it.id} ${it.arabicName} (${it.keyword}) -> `${it.rootCommand}`" }
                } else ""

                val aiRootResponse = com.example.data.remote.AiChatMessage(
                    sender = com.example.data.remote.MessageSender.AI,
                    text = "⚡ **[وحدة فهم الحديث والتحكم بالنظام Root AI Controller]**\n${rootIntentResult.output}$kwInfo"
                )
                aiChatMessages.value = aiChatMessages.value + aiRootResponse
                isAiThinking.value = false
            } else {
                val currentApps = apps.value
                var responseMsg = com.example.data.remote.GeminiAssistantClient.askGeminiWithSearch(
                    context = context,
                    prompt = prompt,
                    history = currentList,
                    availableStoreApps = currentApps,
                    isHighThinkingMode = isHighThinkingMode.value
                )

                if (matchedKeywords.isNotEmpty()) {
                    val kwInfo = "\n\n📚 **[تم التعرف التلقائي على كلمات من معجم الـ 1000 كلمة AI]:**\n" +
                            matchedKeywords.joinToString("\n") { "• #${it.id} ${it.arabicName} [${it.category}] -> أمر السكربت: `${it.rootCommand}`" }
                    responseMsg = responseMsg.copy(text = responseMsg.text + kwInfo)
                }

                aiChatMessages.value = aiChatMessages.value + responseMsg
                isAiThinking.value = false
            }
        }
    }

    fun clearAiChat() {
        aiChatMessages.value = listOf(
            com.example.data.remote.AiChatMessage(
                sender = com.example.data.remote.MessageSender.AI,
                text = "مرحباً بك! تم مسح المحادثة السابقة. كيف يمكنني مساعدتك في البحث أو الشرح اليوم؟"
            )
        )
    }

    // Form states for publishing
    var publishTitle = MutableStateFlow("")
    var publishPublisher = MutableStateFlow("")
    var publishDescription = MutableStateFlow("")
    var publishCategory = MutableStateFlow("أدوات")
    var publishCountry = MutableStateFlow("🇸🇦 السعودية")
    var publishDownloadUrl = MutableStateFlow("")
    var publishVersion = MutableStateFlow("1.0.0")
    var publishSize = MutableStateFlow("10.0 MB")
    var publishIconRes = MutableStateFlow("ic_default")

    init {
        syncCloudFeed()
        startPeriodicSync()
    }

    private fun startPeriodicSync() {
        viewModelScope.launch {
            while (isActive) {
                delay(10000) // 10 seconds auto-refresh
                repository.syncWithCloud()
            }
        }
    }

    fun syncCloudFeed() {
        viewModelScope.launch {
            repository.syncWithCloud()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val apps: StateFlow<List<AppEntity>> = combine(
        searchQuery,
        selectedCategory,
        selectedCountry,
        repository.allApps
    ) { query, category, country, allList ->
        var result = allList

        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            result = result.filter { app ->
                app.title.lowercase().contains(q) ||
                app.publisher.lowercase().contains(q) ||
                app.description.lowercase().contains(q) ||
                app.category.lowercase().contains(q) ||
                app.country.lowercase().contains(q)
            }
        }

        if (category.isNotBlank() && category != "الكل" && !category.equals("All", ignoreCase = true)) {
            result = result.filter { it.category == category }
        }

        if (country.isNotBlank() && country != "🌐 جميع الدول") {
            result = result.filter { it.country == country }
        }

        result
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val downloads: StateFlow<List<DownloadEntity>> = repository.downloads.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedAppReviews: StateFlow<List<ReviewEntity>> = selectedAppForDetails.flatMapLatest { app ->
        if (app != null) {
            repository.getReviewsForApp(app.id)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
    }

    fun onCategorySelected(category: String) {
        selectedCategory.value = category
    }

    fun onCountrySelected(country: String) {
        selectedCountry.value = country
    }

    fun selectApp(app: AppEntity?) {
        selectedAppForDetails.value = app
    }

    fun togglePublishSheet(show: Boolean) {
        showPublishSheet.value = show
    }

    fun toggleTerminalDialog(show: Boolean) {
        showTerminalDialog.value = show
    }

    fun toggleTerminalLearningDialog(show: Boolean) {
        showTerminalLearningDialog.value = show
    }

    fun toggleCustomizationHubDialog(show: Boolean) {
        showCustomizationHubDialog.value = show
    }

    fun toggleOpenSourceCodeDialog(show: Boolean) {
        showOpenSourceCodeDialog.value = show
    }

    fun toggleLiveStreamingEngineDialog(show: Boolean, app: AppEntity? = null) {
        selectedAppForStreaming.value = app
        showLiveStreamingEngineDialog.value = show
    }

    fun toggleDownloadsSheet(show: Boolean) {
        showDownloadsSheet.value = show
    }

    fun publishDirectApp(appEntity: AppEntity) {
        viewModelScope.launch {
            repository.insertApp(appEntity)
            repository.syncWithCloud()
        }
    }

    fun publishApp(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val title = publishTitle.value.trim()
        val publisher = publishPublisher.value.trim()
        val description = publishDescription.value.trim()
        val category = publishCategory.value
        val country = publishCountry.value
        val url = publishDownloadUrl.value.trim()

        if (title.isBlank()) {
            onError("يرجى إدخال اسم التطبيق")
            return
        }
        if (publisher.isBlank()) {
            onError("يرجى إدخال اسم الناشر أو المطور")
            return
        }
        if (description.isBlank()) {
            onError("يرجى إدخال وصف للتطبيق")
            return
        }
        if (url.isBlank()) {
            onError("يرجى إدخال رابط التحميل أو اختيار ملف الـ APK")
            return
        }

        viewModelScope.launch {
            try {
                repository.publishApp(
                    title = title,
                    publisher = publisher,
                    description = description,
                    category = category,
                    country = country,
                    downloadUrl = url,
                    versionName = publishVersion.value.ifBlank { "1.0.0" },
                    fileSize = publishSize.value.ifBlank { "12.0 MB" },
                    iconResName = publishIconRes.value
                )

                // Reset inputs
                publishTitle.value = ""
                publishPublisher.value = ""
                publishDescription.value = ""
                publishDownloadUrl.value = ""

                showPublishSheet.value = false
                syncCloudFeed()
                onSuccess()
            } catch (e: Exception) {
                onError("حدث خطأ أثناء النشر: ${e.localizedMessage}")
            }
        }
    }

    fun likeApp(appId: Long) {
        viewModelScope.launch {
            repository.likeApp(appId)
            // refresh selected app if currently viewing details
            if (selectedAppForDetails.value?.id == appId) {
                selectedAppForDetails.value = selectedAppForDetails.value?.copy(
                    likesCount = (selectedAppForDetails.value?.likesCount ?: 0) + 1
                )
            }
        }
    }

    fun submitReview(appId: Long, author: String, rating: Int, comment: String) {
        if (comment.isBlank()) return
        viewModelScope.launch {
            repository.addReview(appId, author, rating, comment)
        }
    }

    fun startDownload(context: Context, app: AppEntity) {
        viewModelScope.launch {
            repository.incrementDownloads(app.id)

            // Open downloads sheet so user sees live progress
            showDownloadsSheet.value = true

            val initialDownloadEntity = DownloadEntity(
                appId = app.id,
                appTitle = app.title,
                downloadUrl = app.downloadUrl,
                downloadManagerId = System.currentTimeMillis(),
                localFilePath = null,
                status = "DOWNLOADING",
                progress = 5,
                downloadedAt = System.currentTimeMillis()
            )

            repository.updateDownloadStatus(initialDownloadEntity)

            Toast.makeText(context, "جاري بدء تحميل: ${app.title}", Toast.LENGTH_SHORT).show()

            // Also enqueue with system DownloadManager in parallel for system notification bar
            DownloadInstaller.enqueueDownload(
                context = context,
                url = app.downloadUrl,
                title = app.title
            )

            // Execute direct streaming download with real-time progress
            val downloadedFile = DownloadInstaller.downloadFileWithProgress(
                context = context,
                urlStr = app.downloadUrl,
                title = app.title,
                onProgress = { currentProgress ->
                    viewModelScope.launch {
                        repository.updateDownloadStatus(
                            initialDownloadEntity.copy(
                                progress = currentProgress,
                                status = if (currentProgress >= 100) "COMPLETED" else "DOWNLOADING"
                            )
                        )
                    }
                }
            )

            if (downloadedFile != null && downloadedFile.exists()) {
                val completedEntity = initialDownloadEntity.copy(
                    localFilePath = downloadedFile.absolutePath,
                    status = "COMPLETED",
                    progress = 100
                )
                repository.updateDownloadStatus(completedEntity)

                Toast.makeText(context, "اكتمل تحميل: ${app.title}! جاهز للتثبيت", Toast.LENGTH_LONG).show()

                // Offer automatic installation trigger
                DownloadInstaller.installApk(context, downloadedFile, app.downloadUrl)
            } else {
                Toast.makeText(context, "حدث خطأ أثناء تحميل الحزمة", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun installApp(context: Context, download: DownloadEntity) {
        viewModelScope.launch {
            val apkFile = if (download.localFilePath != null) File(download.localFilePath) else null

            if (apkFile != null && apkFile.exists()) {
                val success = DownloadInstaller.installApk(context, apkFile, download.downloadUrl)
                if (!success) {
                    // Handled gracefully in installApk
                }
            } else {
                Toast.makeText(context, "الملف غير موجود، جاري إعادة التحميل...", Toast.LENGTH_SHORT).show()
                // Re-download
                startDownload(
                    context = context,
                    app = AppEntity(
                        id = download.appId,
                        title = download.appTitle,
                        publisher = "مجتمعي",
                        description = "",
                        category = "عام",
                        downloadUrl = download.downloadUrl
                    )
                )
            }
        }
    }

    class Factory(private val repository: AppRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CommunityViewModel::class.java)) {
                return CommunityViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
