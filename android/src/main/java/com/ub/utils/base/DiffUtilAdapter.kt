package com.ub.utils.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.launch

abstract class DiffUtilAdapter<D : DiffViewHolder, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    var listener: BaseClickListener? = null
    protected var dataset: MutableList<D> = mutableListOf()
    private val diffCallback by lazy(LazyThreadSafetyMode.NONE) { DiffCallback() }
    private val eventActor = actor<List<D>>(capacity = Channel.CONFLATED) { for (list in channel) internalUpdate(list) }

    fun update (list: List<D>) = eventActor.offer(list)

    private suspend fun internalUpdate(list: List<D>) {
        val result = DiffUtil.calculateDiff(diffCallback.apply {
            newList.clear()
            newList.addAll(list)
        })
        launch(UI) {
            dataset.clear()
            dataset.addAll(list)
            result.dispatchUpdatesTo(this@DiffUtilAdapter)
        }.join()
    }

    private inner class DiffCallback : DiffUtil.Callback() {
        internal var newList: MutableList<D> = mutableListOf()
        override fun getOldListSize() = dataset.size
        override fun getNewListSize() = newList.size
        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = dataset[oldItemPosition].getDiffId() == newList[newItemPosition].getDiffId()
        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = dataset[oldItemPosition] == newList[newItemPosition]
    }
}

interface DiffViewHolder {
    fun getDiffId(): Int
}