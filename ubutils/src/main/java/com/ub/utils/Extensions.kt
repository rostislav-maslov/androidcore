package com.ub.utils

import android.animation.ObjectAnimator
import android.support.v7.app.AlertDialog
import android.util.Property
import android.util.TypedValue
import android.view.View

fun View.dpToPx(dp : Int): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), this.context.resources.displayMetrics)
}

inline val View.visible: View
    get() = apply { visibility = View.VISIBLE }

inline val View.invisible: View
    get() = apply { visibility = View.INVISIBLE }

inline val View.gone: View
    get() = apply { visibility = View.GONE }

inline fun AlertDialog.isNotShowing(): Boolean = !isShowing

fun Collection<String>.containsIgnoreCase(value: String): Boolean {
    return this
        .firstOrNull()
        ?.let { (it.contains(value, ignoreCase = true)) }
        ?: false
}

fun <T : View> T.animator(property: Property<T, Float>, vararg values: Float): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, property, *values)
}

fun <T : View> T.animator(property: String, vararg values: Float): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, property, *values)
}