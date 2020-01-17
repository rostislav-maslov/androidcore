package com.ub.utils.ui.main

import android.graphics.Bitmap
import com.ub.utils.containsIgnoreCase
import com.ub.utils.di.services.api.responses.PostResponse
import io.reactivex.Single
import java.util.*

class MainInteractor(private val repository: IMainRepository) {

    fun loadPosts(): Single<List<PostResponse>> {
        return repository.getPosts()
    }

    fun isEquals(): Single<Boolean> {
        return Single.just(arrayListOf("Test", "TEst", "TESt", "TEST"))
            .map {
                return@map it.containsIgnoreCase("test")
            }
    }

    fun generatePushContent(list: List<PostResponse>): Single<Pair<String, String>> {
        return Single.just(list)
            .map {
                val rnd = list[Random().nextInt(list.size)]
                return@map Pair(rnd.title, rnd.body)
            }
    }

    suspend fun loadImage(url: String): Bitmap {
        val image = repository.getImage(url) ?: throw IllegalStateException("Image is null")

        return image
    }
}