package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "community_apps")
data class AppEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val packageName: String = "com.community.app",
    val publisher: String,
    val description: String,
    val category: String,
    val country: String = "🌐 جميع الدول",
    val versionName: String = "1.0.0",
    val fileSize: String = "10.0 MB",
    val downloadUrl: String,
    val iconUrl: String? = null,
    val iconResName: String = "ic_default",
    val downloadsCount: Int = 120,
    val likesCount: Int = 45,
    val rating: Float = 4.8f,
    val isVerified: Boolean = true,
    val publishedAt: Long = System.currentTimeMillis()
)
