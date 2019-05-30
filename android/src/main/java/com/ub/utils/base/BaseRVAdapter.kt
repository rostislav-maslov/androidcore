package com.ub.utils.base

import androidx.recyclerview.widget.RecyclerView
import android.view.View

abstract class BaseRVAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {

    var listener: BaseClickListener? = null
}

interface BaseClickListener {
    fun onClick(view: View, position: Int)
}