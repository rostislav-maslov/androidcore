package com.ub.utils.base

import android.support.v7.app.AlertDialog
import com.arellomobile.mvp.MvpAppCompatActivity
import com.ub.utils.isNotShowing

abstract class BaseActivity : MvpAppCompatActivity() {

    var alertDialog: AlertDialog? = null

    fun showError(title: String? = null, message: String) {
        if (alertDialog?.isNotShowing() != false) {
            alertDialog = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .show()
        }
    }

    fun showMessage(title: String? = null, message: String, actionText: String, action : () -> Unit) {
        if (alertDialog?.isNotShowing() != false) {
            alertDialog = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(actionText, { _, _ -> action() })
                .show()
        }
    }
}