package com.ub.utils.ui.main.activities

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.ub.utils.*
import com.ub.utils.base.BaseActivity
import com.ub.utils.ui.main.presenters.MainPresenter
import com.ub.utils.ui.main.views.MainView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainView {

    @InjectPresenter lateinit var presenter : MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.load()

        Notification.Builder(this)
            .fromLocal("title", "message very long for two string")

    }

    override fun done() {
        showMessage(message = "16 dp is ${btn_text_action.dpToPx(16)} in pixels", actionText = "Ok", action =  { presenter.isEquals() } )
    }

    override fun isEquals(equals: Boolean) {
        showError(message = "${UbUtils.getString(R.string.app_name)}. Equals $equals")
    }

    fun hideTest(v : View) {
        if (tv_text.visibility == View.GONE) {
            tv_text.visible
            btn_text_action.text = "HIDE TEXT"
        } else {
            tv_text.gone
            btn_text_action.text = "SHOW TEXT"
        }
    }
}
