package com.ub.utils

import com.arellomobile.mvp.MvpView

interface MainView : MvpView {
    fun done()
    fun isEquals(equals: Boolean)
}