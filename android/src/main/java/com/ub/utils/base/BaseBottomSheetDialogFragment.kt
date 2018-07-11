package com.ub.utils.base

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import com.arellomobile.mvp.MvpDelegate

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private var mMvpDelegate: MvpDelegate<out BottomSheetDialogFragment>? = null

    /**
     * @return The [MvpDelegate] being used by this Activity.
     */
    val mvpDelegate: MvpDelegate<*>
        get() {
            if (mMvpDelegate == null) {
                mMvpDelegate = MvpDelegate(this)
            }
            return mMvpDelegate as MvpDelegate<*>
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mvpDelegate.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        mvpDelegate.onAttach()
    }

    override fun onResume() {
        super.onResume()

        mvpDelegate.onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        mvpDelegate.onSaveInstanceState(outState)
        mvpDelegate.onDetach()
    }

    override fun onStop() {
        super.onStop()

        mvpDelegate.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()

        mvpDelegate.onDestroyView()
        mvpDelegate.onDestroy()
    }
}