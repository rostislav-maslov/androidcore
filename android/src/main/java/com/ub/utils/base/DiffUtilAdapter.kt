package com.ub.utils.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@Suppress("UNUSED")
abstract class DiffUtilAdapter<D : DiffViewHolder, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    var listener: BaseClickListener? = null
    protected var dataset: MutableList<D> = mutableListOf()
    private val diffCallback by lazy(LazyThreadSafetyMode.NONE) { DiffCallback() }
    private val eventActor = actor<List<D>>(capacity = Channel.CONFLATED) { for (list in channel) internalUpdate(list) }

    fun update(list: List<D>) = eventActor.offer(list)

    fun updateSync(list: List<D>) {
        diffCallback.newList.clear()
        diffCallback.newList.addAll(list)
        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(this)
        dataset.clear()
        dataset.addAll(list)
    }

    private suspend fun internalUpdate(list: List<D>) {
        val result = DiffUtil.calculateDiff(diffCallback.apply {
            newList.clear()
            newList.addAll(list)
        })
        launch {
            dataset.clear()
            dataset.addAll(list)
            result.dispatchUpdatesTo(this@DiffUtilAdapter)
        }.join()
    }

    private inner class DiffCallback : DiffUtil.Callback() {
        internal var newList: MutableList<D> = mutableListOf()
        override fun getOldListSize() = dataset.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = dataset[oldItemPosition].getDiffId() == newList[newItemPosition].getDiffId()
        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = dataset[oldItemPosition] == newList[newItemPosition]
    }
}

interface DiffViewHolder {
    fun getDiffId(): Int
}