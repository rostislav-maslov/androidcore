package com.ub.utils.base

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseRVAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {

    protected var listener: BaseClickListener? = null
}

interface BaseClickListener {
    fun onClick(view: View, position: Int)
}