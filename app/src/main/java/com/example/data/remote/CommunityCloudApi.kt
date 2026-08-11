package com.example.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

data class RestfulData(
    val title: String? = null,
    val publisher: String? = null,
    val description: String? = null,
    val category: String? = null,
    val country: String? = null,
    val downloadUrl: String? = null,
    val versionName: String? = null,
    val fileSize: String? = null,
    val iconResName: String? = null,
    val downloadsCount: Int? = null,
    val likesCount: Int? = null,
    val rating: Float? = null,
    val isVerified: Boolean? = null,
    val publishedAt: Long? = null
)

data class RestfulAppObject(
    val id: String? = null,
    val name: String? = null,
    val data: RestfulData? = null
)

interface RestfulApiService {

    @GET("objects")
    suspend fun getObjects(): List<RestfulAppObject>

    @POST("objects")
    suspend fun createObject(
        @Body body: RestfulAppObject
    ): RestfulAppObject
}

object CommunityCloudClient {

    private const val BASE_URL = "https://api.restful-api.dev/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    val restfulService: RestfulApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(RestfulApiService::class.java)
    }
}
