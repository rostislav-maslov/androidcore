package com.ub.utils.ui.main

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.ub.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.presenter.InjectPresenter
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.util.*

@StateStrategyType(OneExecutionStateStrategy::class)
interface MainView : MvpView {
    fun done()
    fun isEquals(equals: Boolean)
    fun showPush(content: Pair<String, String>)
    fun showImage(image: Bitmap)
    fun onConnectivityChange(state: String)
}

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter : MainPresenter

    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.load()

        presenter.loadImage("https://upload.wikimedia.org/wikipedia/commons/thumb/7/74/Kotlin-logo.svg/1200px-Kotlin-logo.svg.png")

        presenter.networkTest(this)
    }

    override fun done() {
        AlertDialog.Builder(this)
            .setMessage(spannableBuilder {
                append("20sp text") {
                    size(20F)
                }
                appendSpace("with")
                appendSpace("underline") {
                    underline()
                }
                appendSpace("and")
                appendSpace("strikethrough") {
                    strikethrough()
                }
                appendLn("Bold text") {
                    typeface(Typeface.DEFAULT_BOLD)
                }
                appendSpace("blue yeti") {
                    color(android.R.color.holo_blue_light)
                    size(10F)
                }
            })
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

    override fun onConnectivityChange(state: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = when (state) {
                CNetwork.State.ACTIVE -> ContextCompat.getColor(this@MainActivity, R.color.colorPrimaryDark)
                CNetwork.State.DISABLE -> Color.RED
                CNetwork.State.CAPTIVE -> Color.GREEN
                else -> Color.YELLOW
            }
        }
        supportActionBar?.setBackgroundDrawable(
            GradientDrawable().apply {
                setColor(when (state) {
                    CNetwork.State.ACTIVE -> ContextCompat.getColor(this@MainActivity, R.color.colorPrimary)
                    CNetwork.State.DISABLE -> Color.RED
                    CNetwork.State.CAPTIVE -> Color.GREEN
                    else -> Color.YELLOW
                })
            }
        )
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
