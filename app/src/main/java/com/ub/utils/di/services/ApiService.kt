package com.ub.utils.di.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.ub.utils.di.services.api.responses.PostResponse
import com.ub.utils.download
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

class ApiService {

    val api: Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(httpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService.Api::class.java)
    }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    suspend fun downloadImage(url: String): Bitmap? {
        return httpClient.download(url) {
            return@download BitmapFactory.decodeStream(it)
        }
    }

    interface Api {
        @GET("/posts")
        fun loadPosts() : Single<List<PostResponse>>
    }
}