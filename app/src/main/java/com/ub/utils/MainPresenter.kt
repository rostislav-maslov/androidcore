package com.ub.utils

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    fun load() {
        Observable.timer(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { viewState.done() }
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