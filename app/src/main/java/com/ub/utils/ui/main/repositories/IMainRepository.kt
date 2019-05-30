package com.ub.utils.ui.main.repositories

import com.ub.utils.di.services.api.responses.PostResponse
import io.reactivex.Single

interface IMainRepository {

    fun getPosts(): Single<List<PostResponse>>
}