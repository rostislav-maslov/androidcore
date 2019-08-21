package com.ub.utils.ui.main.activities

import android.graphics.Bitmap
import android.os.Bundle
import androidx.core.app.NotificationCompat
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.ub.utils.*
import com.ub.utils.ui.main.presenters.MainPresenter
import com.ub.utils.ui.main.views.MainView
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import java.util.*

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter : MainPresenter

    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.load()

        presenter.loadImage("https://upload.wikimedia.org/wikipedia/commons/thumb/7/74/Kotlin-logo.svg/1200px-Kotlin-logo.svg.png")
    }

    override fun done() {
        AlertDialog.Builder(this)
            .setMessage("16 dp is ${btn_text_action.dpToPx(16)} in pixels")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                presenter.isEquals()
            }
            .show()
    }

    override fun isEquals(equals: Boolean) {
        AlertDialog.Builder(this)
            .setMessage("${UbUtils.getString(R.string.app_name)}. Equals $equals")
            .show()
    }

    override fun showPush(content: Pair<String, String>) {
        UbNotify
            .create(this, android.R.drawable.ic_dialog_alert, content.first, content.second)
            .setChannelParams(content.first, content.second, null)
            .setParams {
                setAutoCancel(true)
                setStyle(NotificationCompat.BigTextStyle().bigText(content.second))
            }
            .show(id = random.nextInt())
    }

    override fun showImage(image: Bitmap) {
        iv_image.setImageBitmap(image)
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
