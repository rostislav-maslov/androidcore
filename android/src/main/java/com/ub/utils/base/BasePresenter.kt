package com.ub.utils.base

import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import moxy.MvpPresenter
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter<T : BaseView> : MvpPresenter<T>(), CoroutineScope  {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    protected val subscriptions = CompositeDisposable()

    override fun onDestroy() {
        subscriptions.clear()
    }
}