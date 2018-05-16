package com.ub.utils.ui.main.repositories

import com.ub.utils.BaseApplication
import com.ub.utils.di.services.ApiService
import com.ub.utils.di.services.api.responses.PostResponse
import io.reactivex.Single
import javax.inject.Inject

class MainRepository : IMainRepository {

    @Inject lateinit var api : ApiService

    init {
        BaseApplication.appComponent.inject(this)
    }

    override fun getPosts(): Single<List<PostResponse>> {
        return api.api.loadPosts()
    }
}