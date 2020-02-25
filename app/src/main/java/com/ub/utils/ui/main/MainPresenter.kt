package com.ub.utils.ui.main

import android.content.Context
import android.os.Build
import android.os.SystemClock
import com.ub.utils.*
import com.ub.utils.di.services.api.responses.PostResponse
import com.ub.security.AesGcmEncryption
import com.ub.security.AuthenticatedEncryption
import com.ub.security.toSecretKey
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
                val url = testAes(urlToLoad)
                val image = interactor.loadImage(url)
                viewState.showImage(image)
            } catch (e: Exception) {
                LogUtils.e("ImageDownload", e.message, e)
            }
        }
    }

    private fun testAes(textToTest: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LogUtils.d("AES/GCM", "Encryption test started")
            val startTime = SystemClock.uptimeMillis()
            val key = "test".toSecretKey()
            val encryption: AuthenticatedEncryption = AesGcmEncryption()
            val encrypted = encryption.encrypt(
                urlToLoad.toByteArray(charset("UTF-8")),
                key
            )
            val encryptTime = SystemClock.uptimeMillis()
            LogUtils.d("AES/GCM", "Time to encrypt is ${encryptTime - startTime}")
            val decryption: AuthenticatedEncryption = AesGcmEncryption()
            val decrypted = decryption.decrypt(
                encrypted,
                key
            )
            val decryptTime = SystemClock.uptimeMillis()
            LogUtils.d("AES/GCM", "Time to decrypt is ${decryptTime - encryptTime}")
            return String(decrypted)
        } else textToTest
    }
}