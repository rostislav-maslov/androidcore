package com.ub.utils.ui.main.views

import com.ub.utils.base.BaseView

interface MainView : BaseView {
    fun done()
    fun isEquals(equals: Boolean)
    fun showPush(content: Pair<String, String>)
}