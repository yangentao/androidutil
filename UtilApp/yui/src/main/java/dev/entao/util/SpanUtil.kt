package dev.entao.util

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.*
import dev.entao.appbase.App

class SpanUtil(s: String) {
	var ss: SpannableString = SpannableString(s)


	//字体颜色
	fun foreColor(color: Int, start: Int, end: Int): SpanUtil {
		ss.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
		return this
	}

	//背景色
	fun backColor(color: Int, start: Int, end: Int): SpanUtil {
		ss.setSpan(BackgroundColorSpan(color), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
		return this
	}

	//缩放字体
	fun scaleX(scale: Float, start: Int, end: Int): SpanUtil {
		ss.setSpan(ScaleXSpan(scale), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
		return this
	}

	//设置字体大小
	fun size(sizeSp: Int, start: Int, end: Int): SpanUtil {
		ss.setSpan(AbsoluteSizeSpan(App.sp2px(sizeSp.toFloat())), start, end,
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
		return this
	}

	//设置字体"sans"
	fun typeface(family: String, start: Int, end: Int): SpanUtil {
		ss.setSpan(TypefaceSpan(family), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
		return this
	}

	//设置字体粗体
	fun bold(start: Int, end: Int): SpanUtil {
		ss.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
		return this
	}

	//设置斜体
	fun italic(start: Int, end: Int): SpanUtil {
		ss.setSpan(StyleSpan(Typeface.ITALIC), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
		return this
	}

	//设置粗斜体
	fun boldItalic(start: Int, end: Int): SpanUtil {
		ss.setSpan(StyleSpan(Typeface.BOLD_ITALIC), start, end,
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
		return this
	}

	//删除线
	fun strikethrough(start: Int, end: Int): SpanUtil {
		ss.setSpan(StrikethroughSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
		return this
	}

	//下划线
	fun underline(start: Int, end: Int): SpanUtil {
		ss.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
		return this
	}

	//下标
	fun subscript(start: Int, end: Int): SpanUtil {
		ss.setSpan(SubscriptSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
		return this
	}

	//上标
	fun superscript(start: Int, end: Int): SpanUtil {
		ss.setSpan(SuperscriptSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
		return this
	}

	//url
	fun url(url: String, start: Int, end: Int): SpanUtil {
		ss.setSpan(URLSpan(url), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
		return this
	}

	//image
	fun image(d: Drawable, start: Int, end: Int): SpanUtil {
		ss.setSpan(ImageSpan(d), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
		return this
	}

	//image
	fun imageBaseline(d: Drawable, start: Int, end: Int): SpanUtil {
		ss.setSpan(ImageSpan(d, ImageSpan.ALIGN_BASELINE), start, end,
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
		return this
	}

	fun roundBackground(backColor: Int, textColor: Int, radius: Int, start: Int, end: Int): SpanUtil {
		ss.setSpan(
            dev.entao.ui.util.RoundBackgroundSpan(backColor, textColor, radius), start, end,
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
		return this
	}

//	//url
//	fun clickable(url: String, start: Int, end: Int,
//	              clickListener: View.OnClickListener?): SpanUtil {
//		val sp = object : ClickableSpan() {
//
//			override fun onClick(widget: View) {
//				clickListener?.onClick(widget)
//			}
//		}
//		ss.setSpan(sp, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
//		return this
//	}
}
