package com.ub.utils.ui.main.views

import android.graphics.Bitmap
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface MainView : MvpView {
    fun done()
    fun isEquals(equals: Boolean)
    fun showPush(content: Pair<String, String>)
    fun showImage(image: Bitmap)
}