package com.ub.utils.base

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNUSED")
abstract class BaseListAdapter<D : DiffComparable, VH : RecyclerView.ViewHolder>
@JvmOverloads
constructor(differ: DiffUtil.ItemCallback<D> = DiffCallback()) : ListAdapter<D, VH>(differ) {

    var listener: BaseClickListener? = null
    var listListener: BaseListClickListener<D>? = null

    fun update(list: List<D>) {
        this.submitList(ArrayList(list))
    }

    @SuppressLint("DiffUtilEquals")
    private class DiffCallback<D : DiffComparable> : DiffUtil.ItemCallback<D>() {
        override fun areContentsTheSame(oldItem: D, newItem: D): Boolean = oldItem == newItem
        override fun areItemsTheSame(oldItem: D, newItem: D): Boolean = oldItem.getItemId() == newItem.getItemId()
    }

    interface BaseListClickListener<D> {
        fun onClick(view: View, item: D, position: Int)
    }
}