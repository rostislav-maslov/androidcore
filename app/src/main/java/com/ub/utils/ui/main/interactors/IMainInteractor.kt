package com.ub.utils.ui.main.interactors

import com.ub.utils.di.services.api.responses.PostResponse
import io.reactivex.Single

interface IMainInteractor {

    fun loadPosts(): Single<List<PostResponse>>
    fun isEquals(): Single<Boolean>
    fun generatePushContent(list: List<PostResponse>): Single<Pair<String, String>>
}