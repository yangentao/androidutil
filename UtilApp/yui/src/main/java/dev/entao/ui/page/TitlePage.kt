package dev.entao.ui.page

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import dev.entao.appbase.ex.Colors
import dev.entao.base.Progress
import dev.entao.ui.activities.TabBarActivity
import dev.entao.ui.ext.*
import dev.entao.ui.viewcreator.createLinearVertical
import dev.entao.ui.widget.BottomBar
import dev.entao.ui.widget.TitleBar
import dev.entao.ui.widget.TopProgressBar
import dev.entao.util.Task
import dev.entao.util.app.OS

open class TitlePage : BaseFragment(), Progress {
    lateinit var rootView: LinearLayout
        private set

    lateinit var titleBar: TitleBar
        private set

    lateinit var contentView: LinearLayout
        private set

    var hasBottomBar = false
    var bottomBar: BottomBar? = null
        private set

    var hasTopProgress = true
    var topProgress: TopProgressBar? = null

    var hasSnak = false
    var snack: Snack? = null

    var contentCreated = false
        private set

    var enableContentScroll = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        rootView = createLinearVertical()
        rootView.backColorWhite()
        if (hasTopProgress) {
            val b = TopProgressBar(activity).gone()
            rootView.addView(b, LParam.WidthFill.height(6))
            topProgress = b
        }
        titleBar = TitleBar(activity)
        rootView.addView(titleBar, LParam.WidthFill.height(TitleBar.HEIGHT))


        if (hasSnak) {
            val v = Snack(activity).gone()
            rootView.addView(v, LParam.WidthFill.HeightWrap.GravityCenterVertical)
            snack = v
        }

        contentView = createLinearVertical()
        if (enableContentScroll) {
//			val scrollView = createScroll()
            val scrollView = NestedScrollView(rootView.context).genId()
            rootView.addView(scrollView, LParam.WidthFill.height(0).weight(1))
            scrollView.addView(contentView, LParam.WidthFill.HeightWrap)
        } else {
            rootView.addView(contentView, LParam.WidthFill.height(0).weight(1))
        }
        if (hasBottomBar) {
            val bar = BottomBar(activity)
            rootView.addView(bar, LParam.WidthFill.height(BottomBar.HEIGHT))
            bottomBar = bar
        }

        if (this.activity !is TabBarActivity) {
            titleBar.showBack().onClick = {
                finish()
            }
        }

        onCreateContent(this.activity, contentView)
        titleBar.commit()
        bottomBar?.commit()
        if (OS.GE50) {
            val c = dev.entao.ui.MyColor(Colors.Theme)
            statusBarColor(c.multiRGB(0.7))
        }
        onContentCreated()
        contentCreated = true
        return rootView
    }


    fun titleBar(block: TitleBar.() -> Unit) {
        titleBar.block()
    }


    open fun onContentCreated() {

    }

    open fun onCreateContent(context: Context, contentView: LinearLayout) {

    }


    override fun onProgressStart(total: Int) {
        topProgress?.show(100)
    }

    override fun onProgress(current: Int, total: Int, percent: Int) {
        topProgress?.visiable()
        topProgress?.setProgress(percent)
    }

    override fun onProgressFinish() {
        val p = this.topProgress ?: return
        Task.foreDelay(1500) {
            p.hide()
        }
    }

}
