package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.AppEntity
import com.example.data.local.entity.DownloadEntity
import com.example.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM community_apps ORDER BY publishedAt DESC")
    fun getAllApps(): Flow<List<AppEntity>>

    @Query("SELECT * FROM community_apps WHERE title LIKE '%' || :query || '%' OR publisher LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY publishedAt DESC")
    fun searchApps(query: String): Flow<List<AppEntity>>

    @Query("SELECT * FROM community_apps WHERE category = :category ORDER BY publishedAt DESC")
    fun getAppsByCategory(category: String): Flow<List<AppEntity>>

    @Query("SELECT * FROM community_apps WHERE country = :country ORDER BY publishedAt DESC")
    fun getAppsByCountry(country: String): Flow<List<AppEntity>>

    @Query("SELECT * FROM community_apps WHERE id = :appId")
    fun getAppById(appId: Long): Flow<AppEntity?>

    @Query("SELECT * FROM community_apps WHERE LOWER(title) = LOWER(:title) LIMIT 1")
    suspend fun findAppByTitle(title: String): AppEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApp(app: AppEntity): Long

    @Query("UPDATE community_apps SET downloadsCount = downloadsCount + 1 WHERE id = :appId")
    suspend fun incrementDownloads(appId: Long)

    @Query("UPDATE community_apps SET likesCount = likesCount + 1 WHERE id = :appId")
    suspend fun incrementLikes(appId: Long)

    @Query("SELECT * FROM app_reviews WHERE appId = :appId ORDER BY createdAt DESC")
    fun getReviewsForApp(appId: Long): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Query("SELECT * FROM download_history ORDER BY downloadedAt DESC")
    fun getDownloads(): Flow<List<DownloadEntity>>

    @Query("SELECT * FROM download_history WHERE appId = :appId")
    fun getDownloadByAppId(appId: Long): Flow<DownloadEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDownload(download: DownloadEntity)

    @Query("DELETE FROM download_history WHERE appId = :appId")
    suspend fun deleteDownload(appId: Long)
}
