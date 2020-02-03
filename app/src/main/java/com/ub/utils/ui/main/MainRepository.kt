package com.ub.utils.ui.main

import android.graphics.Bitmap
import com.ub.utils.di.services.ApiService
import com.ub.utils.di.services.api.responses.PostResponse
import io.reactivex.Single

interface IMainRepository {

    suspend fun getPosts(): List<PostResponse>
    suspend fun getImage(url: String): Bitmap?
}

class MainRepository(private val api : ApiService) : IMainRepository {

    override suspend fun getPosts(): List<PostResponse> {
        return api.api.loadPosts()
    }

    override suspend fun getImage(url: String): Bitmap? {
        return api.downloadImage(url)
    }
}