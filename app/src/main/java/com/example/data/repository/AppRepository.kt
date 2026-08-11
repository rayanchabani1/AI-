package com.example.data.repository

import com.example.data.local.dao.AppDao
import com.example.data.local.entity.AppEntity
import com.example.data.local.entity.DownloadEntity
import com.example.data.local.entity.ReviewEntity
import com.example.data.remote.CommunityCloudClient
import com.example.data.remote.RestfulAppObject
import com.example.data.remote.RestfulData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AppRepository(private val appDao: AppDao) {

    val allApps: Flow<List<AppEntity>> = appDao.getAllApps()
    val downloads: Flow<List<DownloadEntity>> = appDao.getDownloads()

    suspend fun syncWithCloud() = withContext(Dispatchers.IO) {
        try {
            val objects = CommunityCloudClient.restfulService.getObjects()
            objects.forEach { obj ->
                val data = obj.data
                val appTitle = data?.title ?: if (obj.name?.startsWith("CommunityApp_") == true) obj.name.removePrefix("CommunityApp_") else null
                if (!appTitle.isNullOrBlank()) {
                    val existing = appDao.findAppByTitle(appTitle)
                    val entity = if (existing != null) {
                        existing.copy(
                            publisher = data?.publisher ?: existing.publisher,
                            description = data?.description ?: existing.description,
                            category = data?.category ?: existing.category,
                            country = data?.country ?: existing.country,
                            downloadUrl = data?.downloadUrl ?: existing.downloadUrl,
                            versionName = data?.versionName ?: existing.versionName,
                            fileSize = data?.fileSize ?: existing.fileSize,
                            iconResName = data?.iconResName ?: existing.iconResName,
                            downloadsCount = (data?.downloadsCount ?: 1).coerceAtLeast(existing.downloadsCount),
                            likesCount = (data?.likesCount ?: 1).coerceAtLeast(existing.likesCount),
                            publishedAt = data?.publishedAt ?: existing.publishedAt
                        )
                    } else {
                        AppEntity(
                            id = 0,
                            title = appTitle,
                            publisher = data?.publisher ?: "مطور مجتمعي",
                            description = data?.description ?: "تطبيق مرفوع عبر المنصة المفتوحة",
                            category = data?.category ?: "عام",
                            country = data?.country ?: "🌐 جميع الدول",
                            downloadUrl = data?.downloadUrl ?: "https://fdroid.org/repo/org.simplemobiletools.calculator_78.apk",
                            versionName = data?.versionName ?: "1.0.0",
                            fileSize = data?.fileSize ?: "8.5 MB",
                            iconResName = data?.iconResName ?: "ic_default",
                            downloadsCount = data?.downloadsCount ?: 1,
                            likesCount = data?.likesCount ?: 1,
                            rating = data?.rating ?: 5.0f,
                            isVerified = data?.isVerified ?: true,
                            publishedAt = data?.publishedAt ?: System.currentTimeMillis()
                        )
                    }
                    appDao.insertApp(entity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun searchApps(query: String): Flow<List<AppEntity>> {
        return if (query.isBlank()) {
            appDao.getAllApps()
        } else {
            appDao.searchApps(query.trim())
        }
    }

    fun getAppsByCategory(category: String): Flow<List<AppEntity>> {
        return if (category.isEmpty() || category == "الكل" || category.equals("All", ignoreCase = true)) {
            appDao.getAllApps()
        } else {
            appDao.getAppsByCategory(category)
        }
    }

    fun getAppById(appId: Long): Flow<AppEntity?> = appDao.getAppById(appId)

    suspend fun insertApp(appEntity: AppEntity): Long = appDao.insertApp(appEntity)

    fun getReviewsForApp(appId: Long): Flow<List<ReviewEntity>> = appDao.getReviewsForApp(appId)

    suspend fun publishApp(
        title: String,
        publisher: String,
        description: String,
        category: String,
        country: String = "🌐 جميع الدول",
        downloadUrl: String,
        versionName: String,
        fileSize: String,
        iconUrl: String? = null,
        iconResName: String = "ic_default"
    ): Long {
        val newApp = AppEntity(
            title = title,
            publisher = publisher,
            description = description,
            category = category,
            country = country,
            downloadUrl = downloadUrl,
            versionName = versionName.ifBlank { "1.0.0" },
            fileSize = fileSize.ifBlank { "8.5 MB" },
            iconUrl = iconUrl,
            iconResName = iconResName,
            downloadsCount = 1,
            likesCount = 1,
            rating = 5.0f,
            isVerified = true,
            publishedAt = System.currentTimeMillis()
        )
        val localId = appDao.insertApp(newApp)

        // Asynchronously broadcast and publish to global cloud repository so everyone sees it!
        withContext(Dispatchers.IO) {
            try {
                val restfulObj = RestfulAppObject(
                    name = "CommunityApp_${title.trim()}",
                    data = RestfulData(
                        title = title,
                        publisher = publisher,
                        description = description,
                        category = category,
                        country = country,
                        downloadUrl = downloadUrl,
                        versionName = versionName.ifBlank { "1.0.0" },
                        fileSize = fileSize.ifBlank { "8.5 MB" },
                        iconResName = iconResName,
                        downloadsCount = 1,
                        likesCount = 1,
                        rating = 5.0f,
                        isVerified = true,
                        publishedAt = System.currentTimeMillis()
                    )
                )
                CommunityCloudClient.restfulService.createObject(restfulObj)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return localId
    }

    suspend fun likeApp(appId: Long) {
        appDao.incrementLikes(appId)
    }

    suspend fun incrementDownloads(appId: Long) {
        appDao.incrementDownloads(appId)
    }

    suspend fun addReview(appId: Long, author: String, rating: Int, comment: String) {
        val review = ReviewEntity(
            appId = appId,
            author = author.ifBlank { "مستخدم مجتمعي" },
            rating = rating,
            comment = comment
        )
        appDao.insertReview(review)
    }

    suspend fun updateDownloadStatus(download: DownloadEntity) {
        appDao.insertOrUpdateDownload(download)
    }

    fun getDownloadForApp(appId: Long): Flow<DownloadEntity?> = appDao.getDownloadByAppId(appId)

    suspend fun removeDownload(appId: Long) {
        appDao.deleteDownload(appId)
    }
}
