package com.ub.utils.ui.main.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.ub.utils.base.BaseRVAdapter

class MainRVAdapter : BaseRVAdapter<MainRVAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = View(parent.context)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int = 0

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        fun bind(position: Int) {
            //something
        }

        override fun onClick(v: View?) {
            listener?.onClick(itemView, adapterPosition)
        }
    }
}