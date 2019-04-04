package yet.ui

import android.view.View
import android.view.animation.*

//AnimUtil.rotate(editText, 1000, 0, 360);
//AnimUtil.alpha(editText, 1000, 1.0f, 0.2f);
//AnimUtil.translateY(editText, 1000, 0, 100);
//AnimUtil.scale(editText, 1000, 1, 0.5f, 1, 0.5f);
//AnimUtil au = new AnimUtil();
//au.alpha(1, 0.2f).scale(1, 0.2f, 1, 1).start(editText, 2000);
class AnimUtil {

	private val animSet = AnimationSet(true)

	fun trans(fromXDelta: Float, toXDelta: Float, fromYDelta: Float, toYDelta: Float): AnimUtil {
		val anim = TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta)
		animSet.addAnimation(anim)
		return this
	}

	fun alpha(fromAlpha: Float, toAlpha: Float): AnimUtil {
		animSet.addAnimation(AlphaAnimation(fromAlpha, toAlpha))
		return this
	}

	fun scale(fromX: Float, toX: Float, fromY: Float, toY: Float): AnimUtil {
		animSet.addAnimation(ScaleAnimation(fromX, toX, fromY, toY))
		return this
	}

	fun rotate(fromDegrees: Float, toDegrees: Float): AnimUtil {
		animSet.addAnimation(RotateAnimation(fromDegrees, toDegrees))
		return this
	}

	fun get(): AnimationSet {
		return animSet
	}

	fun start(view: View, duration: Int) {
		animSet.duration = duration.toLong()
		view.startAnimation(animSet)
	}

	companion object {

		fun translateX(view: View, durationMillis: Int, fromXDelta: Float, toXDelta: Float) {
			val anim = TranslateAnimation(fromXDelta, toXDelta, 0f, 0f)
			anim.duration = durationMillis.toLong()
			view.startAnimation(anim)
		}

		fun translateY(view: View, durationMillis: Int, fromYDelta: Float, toYDelta: Float): TranslateAnimation {
			val anim = TranslateAnimation(0f, 0f, fromYDelta, toYDelta)
			anim.duration = durationMillis.toLong()
			view.startAnimation(anim)
			return anim
		}

		fun alpha(view: View, durationMillis: Int, fromAlpha: Float, toAlpha: Float) {
			val aa = AlphaAnimation(fromAlpha, toAlpha)
			aa.duration = durationMillis.toLong()
			view.startAnimation(aa)
		}

		//fromX, toX, fromY, toY是百分比,  0.5f
		//相对于自己的中点
		fun scale(view: View, durationMillis: Int, fromX: Float, toX: Float, fromY: Float,
		          toY: Float): ScaleAnimation {
			val sa = ScaleAnimation(fromX, toX, fromY, toY,
					ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f)
			sa.duration = durationMillis.toLong()
			view.startAnimation(sa)
			return sa
		}

		//相对于自己的中点,  fromDegrees, toDegrees 是度, 如 180
		fun rotate(view: View, durationMillis: Int, fromDegrees: Float, toDegrees: Float) {
			val ra = RotateAnimation(fromDegrees, toDegrees,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)
			ra.duration = durationMillis.toLong()
			view.startAnimation(ra)
		}
	}
}
