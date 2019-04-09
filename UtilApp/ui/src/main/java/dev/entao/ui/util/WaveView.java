package dev.entao.ui.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import dev.entao.appbase.App;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by yangentao on 2015/11/22.
 * entaoyang@163.com
 */
public class WaveView extends View {
	private int maxVal = 0;
	private List<Integer> data = new ArrayList<>();
	private Paint paint = new Paint();

	public WaveView(Context context) {
		super(context);
		paint.setColor(Color.GRAY);
		paint.setStrokeWidth(App.INSTANCE.dp2px(2));
	}

	public void clearData(){
		data.clear();
		postInvalidate();
	}

	public WaveView setColor(int color) {
		paint.setColor(color);
		return this;
	}

	public WaveView setStrokeWidth(int dp) {
		paint.setStrokeWidth(App.INSTANCE.dp2px(dp));
		return this;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		ArrayList<Integer> ls;
		synchronized (this) {
			ls = new ArrayList<>(data);
		}
		int size = ls.size();
		if (size == 0) {
			return;
		}
		int w = getWidth();
		int h = getHeight();
		double scaleH = h * 1.0 / maxVal;
		for (int i = 0; i < ls.size(); ++i) {
			ls.set(i, (int) (ls.get(i) * scaleH));
		}

		double f = w * 1.0 / size;//0.5
		int left = 0;
		for (int i = 0; i < size; ++i) {
			int val = ls.get(i);
			left = (int) (i * f);
			canvas.drawLine(left, h - val, left, h, paint);
		}

	}

	public WaveView setMaxValue(int max) {
		this.maxVal = max;
		return this;
	}

	public WaveView setValue(List<Integer> ls) {
		synchronized (this) {
			data = ls;
		}
		this.postInvalidate();
		return this;
	}

	public void show() {
		setVisibility(View.VISIBLE);
	}

	public void hide() {
		setVisibility(View.GONE);
	}
}
