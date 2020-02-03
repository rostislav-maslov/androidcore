package com.ub.utils.ui.main

import android.content.Context
import com.ub.utils.BaseApplication
import com.ub.utils.LogUtils
import com.ub.utils.cNetwork
import com.ub.utils.di.services.api.responses.PostResponse
import com.ub.utils.renew
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpPresenter
import moxy.presenterScope
import java.util.*
import javax.inject.Inject

class MainPresenter(private val urlToLoad: String) : MvpPresenter<MainView>() {

    @Inject
    lateinit var interactor: MainInteractor
    private val list = ArrayList<PostResponse>()
    private val subscriptions = CompositeDisposable()

    init {
        BaseApplication.getMainSubcomponent().inject(this)
    }

    override fun onDestroy() {
        subscriptions.clear()

        BaseApplication.clearMainSubcomponent()
    }

    fun load() {
        presenterScope.launch {
            try {
                val posts = withContext(Dispatchers.IO) {
                    interactor.loadPosts()
                }

                list.renew(posts)

                delay(100)

                viewState.done()
            } catch (e: Exception) {
                LogUtils.e("POST", e.message ?: "Error", e)
            }
        }
    }

    fun generatePushContent() {
        val pushTask = interactor.generatePushContent(list)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer { viewState.showPush(it) })
        subscriptions.add(pushTask)
    }

    fun networkTest(context: Context) {
        presenterScope.launch {
            try {
                val network = context.cNetwork

                network.startListener().collect {
                    viewState.onConnectivityChange(it)
                }
            } catch (e: Exception) {
                LogUtils.e("NetworkTest", e.message, e)
            }
        }
    }

    fun isEquals() {
        val equalsTask = interactor.isEquals()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer { viewState.isEquals(it) })
        subscriptions.add(equalsTask)
    }

    fun loadImage() {
        presenterScope.launch {
            try {
                val image = interactor.loadImage(urlToLoad)
                viewState.showImage(image)
            } catch (e: Exception) {
                LogUtils.e("ImageDownload", e.message, e)
            }
        }
    }
}