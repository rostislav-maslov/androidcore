@file:Suppress("UNUSED")

package com.ub.utils

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

/**
 * Класс, обеспечивающий отрисовку стандартными средствами [RecyclerView.ItemDecoration] разделителей
 * с учётом возможности их кастомизации через методы [ItemDecoratable.onItemDecorate] и добавления
 * отступов с помощью [ItemDecoratable.onGetOffsets]
 *
 * Для работы необходимо подключить [RecyclerView.ItemDecoration] на экземпляр [RecyclerView] с помощью
 * ```
   rv_content.addItemDecoration(ViewHolderItemDecoration())
 * ```
 *
 * Теперь в адаптере, который связан с этим [RecyclerView] нужно в требуемых [RecyclerView.ViewHolder]'ах
 * реализовать интерфейс [ItemDecoratable], где будут описываться детали какие отступы будут добавляться
 * и как будет отрисовываться разделители формата [Drawable]
 *
 * Стандартными средствами можно нарисовать разделители с помощью [ItemDecoratable.drawBottomDecorator]
 * или [ItemDecoratable.drawTopDecorator], но есть поддержка [Canvas], которая позволяет рисовать вообще,
 * что угодно
 *
 * ```
  inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view), ItemDecoratable {

  override fun onGetOffsets(adapterPosition: Int): Rect = Rect(0, 0, 0, 0)

  override fun onItemDecorate(canvas: Canvas, previousViewHolder: RecyclerView.ViewHolder?) {
    if (getItem(adapterPosition + 1) is SomeBottomViewHolder) {
      canvas.drawTopDecorator(this, divider, height = itemView.dpToPx(8).toInt())
    } else if (getItem(adapterPosition + 1) is SomeAnotherViewHolder) {
      canvas.drawBottomDecorator(this, divider, height = itemView.dpToPx(8).toInt())
    }
  }
 * ```
 */
class ViewHolderItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val viewHolder = parent.findContainingViewHolder(view)
        (viewHolder as? ItemDecoratable)?.let { decoratable ->
            val rect = decoratable.onGetOffsets(viewHolder.adapterPosition)
            outRect.set(rect)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (i in 0 until parent.childCount) {
            val viewHolder = parent.findContainingViewHolder(parent.getChildAt(i))
            (viewHolder as? ItemDecoratable)?.let { decoratable ->
                val previousViewHolder =
                    parent.findViewHolderForAdapterPosition(viewHolder.adapterPosition.minus(1))
                decoratable.onItemDecorate(c, previousViewHolder)
            }
        }
    }
}

interface ItemDecoratable {
    fun onGetOffsets(adapterPosition: Int): Rect
    fun onItemDecorate(canvas: Canvas, previousViewHolder: RecyclerView.ViewHolder?)

    fun Canvas.drawBottomDecorator(
        viewHolder: RecyclerView.ViewHolder,
        drawable: Drawable,
        leftPadding: Int = 0,
        rightPadding: Int = 0,
        height: Int = drawable.intrinsicHeight
    ) {
        val bottom =
            viewHolder.itemView.bottom + height + viewHolder.itemView.translationY.roundToInt()
        drawable.setBounds(
            0 + leftPadding,
            viewHolder.itemView.bottom,
            width - rightPadding,
            bottom
        )
        drawable.draw(this)
    }

    fun Canvas.drawTopDecorator(
        viewHolder: RecyclerView.ViewHolder,
        drawable: Drawable,
        leftPadding: Int = 0,
        rightPadding: Int = 0,
        height: Int = drawable.intrinsicHeight
    ) {
        val topOffset =
            viewHolder.itemView.top - height - viewHolder.itemView.translationY.roundToInt()
        drawable.setBounds(
            0 + leftPadding,
            topOffset,
            width - rightPadding,
            viewHolder.itemView.top
        )
        drawable.draw(this)
    }
}