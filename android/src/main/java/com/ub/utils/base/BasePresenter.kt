package com.ub.utils.base

import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T : BaseView> : MvpPresenter<T>()  {

    protected val subscriptions = CompositeDisposable()

    override fun onDestroy() {
        subscriptions.clear()
    }
}