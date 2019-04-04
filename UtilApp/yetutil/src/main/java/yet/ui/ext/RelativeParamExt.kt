package yet.ui.ext

import android.view.View
import android.widget.RelativeLayout

/**
 * Created by entaoyang@163.com on 2016-07-21.
 */
val RParam: RelativeLayout.LayoutParams
    get() {
        return relativeParam()
    }

fun rParam(): RelativeLayout.LayoutParams {
    return relativeParam()
}

fun relativeParam(): RelativeLayout.LayoutParams {
    val ll =
        RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
    return ll
}

fun relativeParam(f: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): RelativeLayout.LayoutParams {
    val ll =
        RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
    ll.f()
    return ll
}

fun <T : RelativeLayout.LayoutParams> T.parentBottom(): T {
    addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
    return this
}

val <T : RelativeLayout.LayoutParams> T.ParentBottom: T
    get() {
        addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        return this
    }

fun <T : RelativeLayout.LayoutParams> T.parentLeft(): T {
    addRule(RelativeLayout.ALIGN_PARENT_LEFT)
    return this
}

val <T : RelativeLayout.LayoutParams> T.ParentLeft: T
    get() {
        addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        return this
    }

fun <T : RelativeLayout.LayoutParams> T.parentRight(): T {
    addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
    return this
}

val <T : RelativeLayout.LayoutParams> T.ParentRight: T
    get() {
        addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        return this
    }

fun <T : RelativeLayout.LayoutParams> T.parentTop(): T {
    addRule(RelativeLayout.ALIGN_PARENT_TOP)
    return this
}

val <T : RelativeLayout.LayoutParams> T.ParentTop: T
    get() {
        addRule(RelativeLayout.ALIGN_PARENT_TOP)
        return this
    }

fun <T : RelativeLayout.LayoutParams> T.centerInParent(): T {
    addRule(RelativeLayout.CENTER_IN_PARENT)
    return this
}

val <T : RelativeLayout.LayoutParams> T.CenterInParent: T
    get() {
        addRule(RelativeLayout.CENTER_IN_PARENT)
        return this
    }

fun <T : RelativeLayout.LayoutParams> T.centerHorizontal(): T {
    addRule(RelativeLayout.CENTER_HORIZONTAL)
    return this
}

val <T : RelativeLayout.LayoutParams> T.CenterHorizontal: T
    get() {
        addRule(RelativeLayout.CENTER_HORIZONTAL)
        return this
    }


fun <T : RelativeLayout.LayoutParams> T.centerVertical(): T {
    addRule(RelativeLayout.CENTER_VERTICAL)
    return this
}

val <T : RelativeLayout.LayoutParams> T.CenterVertical: T
    get() {
        addRule(RelativeLayout.CENTER_VERTICAL)
        return this
    }


fun <T : RelativeLayout.LayoutParams> T.above(anchor: Int): T {
    addRule(RelativeLayout.ABOVE, anchor)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.above(anchor: View): T {
    addRule(RelativeLayout.ABOVE, anchor.id)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.alignBaseline(anchor: Int): T {
    addRule(RelativeLayout.ALIGN_BASELINE, anchor)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.alignBaseline(anchor: View): T {
    addRule(RelativeLayout.ALIGN_BASELINE, anchor.id)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.alignBottom(anchor: Int): T {
    addRule(RelativeLayout.ALIGN_BOTTOM, anchor)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.alignBottom(anchor: View): T {
    addRule(RelativeLayout.ALIGN_BOTTOM, anchor.id)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.alignLeft(anchor: Int): T {
    addRule(RelativeLayout.ALIGN_LEFT, anchor)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.alignLeft(anchor: View): T {
    addRule(RelativeLayout.ALIGN_LEFT, anchor.id)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.alignRight(anchor: Int): T {
    addRule(RelativeLayout.ALIGN_RIGHT, anchor)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.alignRight(anchor: View): T {
    addRule(RelativeLayout.ALIGN_RIGHT, anchor.id)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.alignTop(anchor: Int): T {
    addRule(RelativeLayout.ALIGN_TOP, anchor)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.alignTop(anchor: View): T {
    addRule(RelativeLayout.ALIGN_TOP, anchor.id)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.below(anchor: Int): T {
    addRule(RelativeLayout.BELOW, anchor)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.below(anchor: View): T {
    addRule(RelativeLayout.BELOW, anchor.id)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.toLeftOf(anchor: Int): T {
    addRule(RelativeLayout.LEFT_OF, anchor)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.toLeftOf(anchor: View): T {
    addRule(RelativeLayout.LEFT_OF, anchor.id)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.toRightOf(anchor: Int): T {
    addRule(RelativeLayout.RIGHT_OF, anchor)
    return this
}

fun <T : RelativeLayout.LayoutParams> T.toRightOf(anchor: View): T {
    addRule(RelativeLayout.RIGHT_OF, anchor.id)
    return this
}