package com.ub.utils

import android.animation.ObjectAnimator
import android.content.Context
import androidx.appcompat.app.AlertDialog
import android.util.Property
import android.util.TypedValue
import android.view.View
import io.reactivex.disposables.Disposable

fun View.dpToPx(dp : Int): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), this.context.resources.displayMetrics)
}

fun Context.dpToPx(dp : Int): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), this.resources.displayMetrics)
}

inline val View.visible: View
    get() = apply { visibility = View.VISIBLE }

inline val View.invisible: View
    get() = apply { visibility = View.INVISIBLE }

inline val View.gone: View
    get() = apply { visibility = View.GONE }

fun AlertDialog.isNotShowing(): Boolean = !isShowing

fun Disposable.isNotDisposed(): Boolean = !isDisposed

fun <T>MutableList<T>.renew(list: Collection<T>): MutableList<T> {
    clear()
    addAll(list)
    return this
}

fun <K, V>MutableMap<K, V>.renew(map: Map<K, V>): MutableMap<K, V> {
    clear()
    putAll(map)
    return this
}

fun Collection<String>.containsIgnoreCase(value: String): Boolean {
    return this
        .firstOrNull()
        ?.let { (it.contains(value, ignoreCase = true)) }
        ?: false
}

fun <T : View> T.animator(property: Property<T, Float>, vararg values: Float): ObjectAnimator = ObjectAnimator.ofFloat(this, property, *values)

fun <T : View> T.animator(property: String, vararg values: Float): ObjectAnimator = ObjectAnimator.ofFloat(this, property, *values)

fun <T : View> T.animator(property: Property<T, Int>, vararg values: Int): ObjectAnimator = ObjectAnimator.ofInt(this, property, *values)

fun <T : View> T.animator(property: String, vararg values: Int): ObjectAnimator = ObjectAnimator.ofInt(this, property, *values)