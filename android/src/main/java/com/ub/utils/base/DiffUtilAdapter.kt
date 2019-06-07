package com.ub.utils.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ub.utils.renew
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.launch


@Suppress("UNUSED")
abstract class DiffUtilAdapter<D : DiffComparable, VH : RecyclerView.ViewHolder> : BaseRVAdapter<VH>(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    protected var dataset: MutableList<D> = mutableListOf()
    private val diffCallback by lazy(LazyThreadSafetyMode.NONE) { DiffCallback() }
    @ObsoleteCoroutinesApi
    private val eventActor = actor<List<D>>(capacity = Channel.CONFLATED) {
        for (list in channel)
            internalUpdate(this@DiffUtilAdapter, list)
    }

    @ObsoleteCoroutinesApi
    fun update(list: List<D>) = eventActor.offer(list)

    fun updateSync(list: List<D>) {
        diffCallback.newList.renew(list)
        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(this)
        dataset.renew(list)
    }

    private suspend fun internalUpdate(scope: CoroutineScope, list: List<D>) {
        val result = DiffUtil.calculateDiff(diffCallback.apply {
            newList.renew(list)
        })
        scope.launch {
            dataset.renew(list)
            result.dispatchUpdatesTo(this@DiffUtilAdapter)
        }.join()
    }

    private inner class DiffCallback : DiffUtil.Callback() {
        internal var newList: MutableList<D> = mutableListOf()
        override fun getOldListSize() = dataset.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = dataset[oldItemPosition].getItemId() == newList[newItemPosition].getItemId()
        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = dataset[oldItemPosition] == newList[newItemPosition]
    }
}

interface DiffComparable {
    fun getItemId(): Int
}