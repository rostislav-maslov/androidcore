package com.ub.utils.ui.main.presenters

import com.arellomobile.mvp.InjectViewState
import com.ub.utils.BaseApplication
import com.ub.utils.LogUtils
import com.ub.utils.Notification
import com.ub.utils.base.BasePresenter
import com.ub.utils.ui.main.views.MainView
import com.ub.utils.containsIgnoreCase
import com.ub.utils.di.services.ApiService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class MainPresenter : BasePresenter<MainView>() {

    @Inject lateinit var api : ApiService

    init {
        BaseApplication.appComponent.inject(this)
    }

    fun load() {
        api.api.loadPosts()
            .subscribeOn(Schedulers.io())
            .delay(100, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ viewState.done() },
                { LogUtils.logError("POST", it.message ?: "Error", it) })
    }

    fun isEquals() {
        Observable.just(arrayListOf("Test", "TEst", "TESt", "TEST"))
            .map {
                return@map it.containsIgnoreCase("test")
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { viewState.isEquals(it) }
    }
}