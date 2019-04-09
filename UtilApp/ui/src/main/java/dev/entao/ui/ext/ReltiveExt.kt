package dev.entao.ui.ext

import android.app.Fragment
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

//create

fun Context.relativeLayout(): RelativeLayout {
    val ll = RelativeLayout(this)
    return ll
}

fun Fragment.relativeLayout(): RelativeLayout {
    return this.activity.relativeLayout()
}

fun ViewGroup.relativeLayout(): RelativeLayout {
    return context.relativeLayout()
}


inline fun <reified T : RelativeLayout> T.addViewParam(view: View, noinline f: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): T {
    val lp = relativeParam(f)
    this.addView(view, lp)
    return this;
}

