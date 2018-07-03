package com.ub.utils.ui.main.activities

import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.ub.utils.*
import com.ub.utils.base.BaseActivity
import com.ub.utils.ui.main.presenters.MainPresenter
import com.ub.utils.ui.main.views.MainView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : BaseActivity(), MainView {

    @InjectPresenter lateinit var presenter : MainPresenter

    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.load()
    }

    override fun done() {
        showMessage(message = "16 dp is ${btn_text_action.dpToPx(16)} in pixels", actionText = "Ok", action =  { presenter.isEquals() } )
    }

    override fun isEquals(equals: Boolean) {
        showError(message = "${UbUtils.getString(R.string.app_name)}. Equals $equals")
    }

    override fun showPush(content: Pair<String, String>) {
        UbNotify.Builder(this)
            .fromLocal(android.R.drawable.ic_dialog_alert, content.first, content.second)
            .setChannelParams(content.first, content.second, null)
            .setParams {
                setAutoCancel(true)
                setStyle(NotificationCompat.BigTextStyle().bigText(content.second))
            }
            .show(random.nextInt())
    }

    fun showPush(v : View) {
        presenter.generatePushContent()
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
