package com.ub.utils.api

import com.ub.utils.LogUtils
import io.reactivex.exceptions.CompositeException
import io.reactivex.functions.Consumer

abstract class ApiExceptionObservable(private val tag: String, private vararg val exceptionPair: Pair<Throwable, String>) : Consumer<Throwable> {

    private val defaultMessage = "Undefined error"

    override fun accept(t: Throwable?) {
        if (t is CompositeException) {
            val message = buildMessage(t.exceptions[0])
            logError(t.exceptions[0])

            call(message)
        } else {

            logError(t)
        }
    }

    /**
     * Формирует сообщение об ошибке
     * Текст сообщения определяется с помощью предустановленых [exceptionPair] значений
     * Если значение среди переданных в параметрах не найдено, тогда показывается стандартное сообщение об ошибке
     * @param error - ошибка для построения сообщения об ошибке
     */
    private fun buildMessage(error : Throwable?) : String {
        var message = ""

        //TODO generating message

        return message
    }

    /**
     * Логгирует сообщение об ошибке
     * Если ошибка == null, тогда выводится стандартное сообщение (должен быть редкий случай)
     * @param error - ошибка для отображения
     */
    private fun logError(error : Throwable?) {
        if (error == null) {
            LogUtils.logError(tag, defaultMessage)
        } else {
            LogUtils.logError(tag, error.message ?: defaultMessage, error)
        }
    }

    abstract fun call(message: String)
    abstract fun unauthorized()

    companion object {
        private const val TAG = "ApiExceptionObservable"

        private const val PHONE_KEY = "phone"
        private const val CODE_KEY = "code"
        private const val REFRESH_KEY = "refreshToken"
        private const val INVALID = "error.invalid"
        private const val NOT_FOUND = "error.notFound"
        private const val USER_BLOCKED = "error.user.blocked"
        private const val USER_EXISTS = "error.userExists"
        private const val USER_NOT_EXISTS = "error.userNotExists"
        private const val SMS_TIMEOUT = "error.sms.timeout"
        private const val LIMIT_REACHED = "error.limitReached"
        private const val TOKEN_EXPIRED = "error.token.expired"
        private const val REQUEST_ERROR = "error.request.error"
        private const val REQUEST_MISSING_PARAM = "request.missingRequestParam"
    }
}