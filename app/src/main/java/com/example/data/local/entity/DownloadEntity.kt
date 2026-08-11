package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "download_history")
data class DownloadEntity(
    @PrimaryKey
    val appId: Long,
    val appTitle: String,
    val downloadUrl: String,
    val downloadManagerId: Long = -1L,
    val localFilePath: String? = null,
    val status: String = "DOWNLOADING",
    val progress: Int = 0,
    val downloadedAt: Long = System.currentTimeMillis()
)
