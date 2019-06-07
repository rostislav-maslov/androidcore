package com.ub.utils.ui.main.presenters

import com.ub.utils.LogUtils
import com.ub.utils.base.BasePresenter
import com.ub.utils.ui.main.views.MainView
import com.ub.utils.di.services.api.responses.PostResponse
import com.ub.utils.renew
import com.ub.utils.ui.main.interactors.MainInteractor
import com.ub.utils.ui.main.repositories.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import java.util.*
import java.util.concurrent.TimeUnit

@InjectViewState
class MainPresenter : BasePresenter<MainView>() {

    private val interactor = MainInteractor(MainRepository())
    private val list = ArrayList<PostResponse>()

    fun load() {
        interactor.loadPosts()
            .map {
                list.renew(it)
            }
            .subscribeOn(Schedulers.io())
            .delay(100, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ viewState.done() },
                { LogUtils.e("POST", it.message ?: "Error", it) })
    }

    fun generatePushContent() {
        interactor.generatePushContent(list)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer { viewState.showPush(it) })
    }

    fun isEquals() {
        interactor.isEquals()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer { viewState.isEquals(it) })
    }
}