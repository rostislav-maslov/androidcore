package com.ub.utils

import io.reactivex.exceptions.CompositeException
import io.reactivex.functions.Consumer


abstract class ApiExceptionObservable : Consumer<Throwable> {


    override fun accept(t: Throwable?) {
        if (t is CompositeException) {

        } else {

        }
    }
}