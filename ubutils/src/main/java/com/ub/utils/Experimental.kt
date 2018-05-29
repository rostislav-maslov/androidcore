package com.ub.utils

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import retrofit2.HttpException

fun <T : Any>refreshTokenLaunch(block: suspend () -> Unit, updateToken: suspend () -> T): Job {
    return launch {
        try {
            block.invoke()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                updateToken.invoke()
                block.invoke()
            } else {
                LogUtils.e("Update token", e.message(), e)
            }
        }
    }
}