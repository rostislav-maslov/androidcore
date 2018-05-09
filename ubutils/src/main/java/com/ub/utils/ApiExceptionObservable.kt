package com.ub.utils

import io.reactivex.exceptions.CompositeException
import io.reactivex.functions.Consumer

abstract class ApiExceptionObservable(private val tag: String, private vararg val exceptionPair: Pair<Exception, String>) : Consumer<Throwable> {

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
        return exceptionPair.filter { it.first == error }
            .map { it.second }[0]
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
}