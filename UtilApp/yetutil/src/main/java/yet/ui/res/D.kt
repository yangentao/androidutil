package yet.ui.res

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.support.annotation.DrawableRes
import net.yet.R
import yet.ext.RGB
import yet.theme.Colors
import yet.theme.IconSize
import yet.theme.ViewSize
import yet.ui.ext.dp
import yet.ui.widget.EditTextX
import yet.util.app.App

/**
 * Created by entaoyang@163.com on 2016-07-23.
 */

object D {
	val CheckBox: Drawable get() = checked(R.drawable.yet_checkbox, R.drawable.yet_checkbox_checked)
	val EditClear: Drawable get() = D.sized(R.drawable.yet_edit_clear, EditTextX.IMAGE_WIDTH)
	val Back: Drawable get() = D.res(R.drawable.yet_back).sized(IconSize.Normal)
	val ArrowRight: Drawable get() = D.res(R.drawable.yet_arrow_right).sized(IconSize.Tiny)
	val RedPoint: Drawable
		get() = Shapes.oval {
			size(10)
			fillColor = RGB(255, 128, 0)
		}
	val Input: Drawable
		get() {
			val corner: Int = ViewSize.EditCorner
			val normal = RectDraw(Colors.WHITE).corner(corner).stroke(1, Colors.GRAY).value
			val focused = RectDraw(Colors.WHITE).corner(corner).stroke(1, Colors.EditFocus).value
			return focused(normal, focused)
		}
	val InputSearch: Drawable
		get() {
			val corner: Int = ViewSize.EditHeightSearch / 2
			val normal = RectDraw(Colors.WHITE).corner(corner).stroke(1, Colors.GRAY).value
			val focused = RectDraw(Colors.WHITE).corner(corner).stroke(1, Colors.EditFocus).value
			return focused(normal, focused)
		}

	val InputRect: Drawable
		get() {
			val normal = RectDraw(Colors.WHITE).corner(2).stroke(1, Colors.GRAY).value
			val focused = RectDraw(Colors.WHITE).corner(2).stroke(1, Colors.EditFocus).value
			return focused(normal, focused)
		}

	fun buttonGray(corner: Int = ViewSize.ButtonCorner): Drawable {
		return buttonColor(Colors.GrayMajor, corner)
	}
	fun buttonGreen(corner: Int = ViewSize.ButtonCorner): Drawable {
		return buttonColor(Colors.Safe, corner)
	}

	fun buttonRed(corner: Int = ViewSize.ButtonCorner): Drawable {
		return buttonColor(Colors.RedMajor, corner)
	}

	fun buttonWhite(corner: Int = ViewSize.ButtonCorner): Drawable {
		return buttonColor(Color.rgb(230, 230, 230), corner)
	}

	fun buttonColor(color: Int, corner: Int = ViewSize.ButtonCorner): Drawable {
		val normal = RectDraw(color).corner(corner).value
		val pressed = RectDraw(Colors.Fade).corner(corner).value
		val enableFalse = RectDraw(Colors.Disabled).corner(corner).value
		return ImageStated(normal).pressed(pressed).enabled(enableFalse, false).value
	}

	fun panelBorder(color: Int = Colors.LightGray, corner: Int = ViewSize.ButtonCorner): Drawable {
		return RectDraw(Color.WHITE).corner(corner).stroke(1, color).value
	}

	@Suppress("DEPRECATION")
	fun res(@DrawableRes resId: Int): Drawable {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			App.resource.getDrawable(resId, App.app.theme)
		} else {
			App.resource.getDrawable(resId)
		}
	}

	fun sized(@DrawableRes resId: Int, size: Int): Drawable {
		return res(resId).sized(size, size)
	}

	fun limited(@DrawableRes resId: Int, edge: Int): Drawable {
		return res(resId).limited(edge)
	}

	fun tinted(@DrawableRes res: Int, color: Int): Drawable {
		return D.res(res).tinted(color)
	}

	fun tintTheme(@DrawableRes res: Int): StateListDrawable {
		return Bmp.tintTheme(res)
	}

	fun tintLight(@DrawableRes res: Int, normalColor: Int, lightColor: Int): StateListDrawable {
		return Bmp.tintLight(res, normalColor, lightColor)
	}

	fun color(color: Int): ColorDrawable {
		return ColorDrawable(color)
	}

	fun listColor(normal: Int, pressed: Int): ColorStateList {
		return ColorList(normal).pressed(pressed).selected(pressed).focused(pressed).get()
	}

	fun light(normal: Drawable, pressed: Drawable): StateListDrawable {
		return ImageStated(normal).pressed(pressed).selected(pressed).focused(pressed).value
	}

	fun light(@DrawableRes normal: Int, @DrawableRes light: Int): StateListDrawable {
		return ImageStated(normal).pressed(light).selected(light).focused(light).value
	}

	fun lightColor(normalColor: Int, pressedColor: Int): StateListDrawable {
		return ColorStated(normalColor).pressed(pressedColor).selected(pressedColor).focused(pressedColor).value
	}

	fun checked(@DrawableRes normalId: Int, @DrawableRes checkedId: Int): StateListDrawable {
		return ImageStated(D.res(normalId)).checked(D.res(checkedId)).value
	}

	fun focused(@DrawableRes normalId: Int, @DrawableRes checkedId: Int): StateListDrawable {
		return ImageStated(D.res(normalId)).focused(D.res(checkedId)).value
	}

	fun focused(normal: Drawable, focusedImage: Drawable): StateListDrawable {
		return ImageStated(normal).focused(focusedImage).value
	}

	fun layerOval(resId: Int, tintColor: Int, ovalColor: Int, inset: Int): LayerDrawable {
		return layerOval(Res.drawable(resId).tinted(tintColor), ovalColor, inset)
	}

	fun layerOval(resId: Int, ovalColor: Int, inset: Int): LayerDrawable {
		return layerOval(Res.drawable(resId), ovalColor, inset)
	}

	fun layerOval(drawable: Drawable, ovalColor: Int, inset: Int): LayerDrawable {
		val bg = Shapes.oval {
			fillColor = ovalColor
		}
		val ld = LayerDrawable(arrayOf(bg, drawable))
		val n = dp(inset)
		ld.setLayerInset(1, n, n, n, n)
		return ld
	}
}