package yet.ui.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import yet.util.app.App;

public class RoundBackgroundSpan extends ReplacementSpan {
	private int backColor = Color.GRAY;
	private int textColor = Color.argb(255, 30, 30, 30);
	private int radius = App.INSTANCE.dp2px(4);

	public RoundBackgroundSpan() {

	}

	public RoundBackgroundSpan(int backColor, int textColor, int radiusDp) {
		setBackColor(backColor);
		setTextColor(textColor);
		setRadius(radiusDp);
	}

	public void setBackColor(int backColor) {
		this.backColor = backColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public void setRadius(int dp) {
		this.radius = App.INSTANCE.dp2px(dp);
	}

	public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y,
			int bottom, Paint paint) {
		RectF rect = new RectF(x, top, x + measureText(paint, text, start, end), bottom);
		paint.setColor(backColor);
		canvas.drawRoundRect(rect, radius, radius, paint);
		paint.setColor(textColor);
		canvas.drawText(text, start, end, x, y, paint);
	}

	public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
		return Math.round(measureText(paint, text, start, end));
	}

	private float measureText(Paint paint, CharSequence text, int start, int end) {
		return paint.measureText(text, start, end);
	}
}