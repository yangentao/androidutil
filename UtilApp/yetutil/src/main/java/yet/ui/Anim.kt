package yet.ui

import android.view.animation.Interpolator
import android.view.animation.RotateAnimation

/**
 * Created by entaoyang@163.com on 16/5/13.
 */

object Anim {
	////相对于自己的中点,  fromDegrees, toDegrees 是度, 如 180
	fun rotate(durationMillis: Int, fromDegrees: Float, toDegrees: Float, repeat: Int = 0): RotateAnimation {
		val ra = RotateAnimation(fromDegrees, toDegrees,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)
		ra.duration = durationMillis.toLong()
		ra.repeatCount = repeat
		ra.interpolator = Interpolator { input -> input }
		return ra
	}
}