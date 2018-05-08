package com.ub.utils

import android.support.v7.app.AlertDialog
import android.util.TypedValue
import android.view.View

fun View.dpToPx(dp : Int) : Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
}

inline val View.visible: View
    get() = apply { visibility = View.VISIBLE }

inline val View.invisible: View
    get() = apply { visibility = View.INVISIBLE }

inline val View.gone: View
    get() = apply { visibility = View.GONE }

inline fun AlertDialog.isNotShowing(): Boolean = !isShowing

fun Collection<String>.containsIgnoreCase(value: String) : Boolean {
    return this
        .firstOrNull()
        ?.let { (it.contains(value, ignoreCase = true)) }
        ?: false
}