package com.ub.utils.base

import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter<T : BaseView> : MvpPresenter<T>(), CoroutineScope  {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    protected val subscriptions = CompositeDisposable()

    override fun onDestroy() {
        subscriptions.clear()
    }
}